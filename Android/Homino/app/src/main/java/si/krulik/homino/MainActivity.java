package si.krulik.homino;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import si.krulik.homino.common.logger.CustomLogger;
import si.krulik.homino.common.validate.Validate;
import si.krulik.homino.configuration.Configuration;
import si.krulik.homino.configuration.device.ShellDevice;
import si.krulik.homino.configuration.device.common.Device;
import si.krulik.homino.configuration.device.common.DeviceControlNode;
import si.krulik.homino.configuration.plate.common.Plate;
import si.krulik.homino.message.Message;
import si.krulik.homino.message.MultiMessage;
import si.krulik.homino.network.HominoNetworkMessage;
import si.krulik.homino.network.HominoNetworkService;


public class MainActivity extends AppCompatActivity
{
    @Override protected void onCreate (Bundle savedInstanceState)
    {
        logger.info ("onCreate begin");

        super.onCreate (savedInstanceState);


        // parse configuration
        configuration = null;
        try
        {
            configuration = new Configuration (getApplicationContext (), this);
        }
        catch (Throwable t)
        {
            t.printStackTrace ();
            throw (new RuntimeException ("Failed to configure application", t));
        }


        // setup background communication service
        LocalBroadcastManager.getInstance (this).registerReceiver (new HominoNetworkBroadcastReceiver (configuration), new IntentFilter (HominoNetworkMessage.communicationRequestIntentAction));


        // pager view
        LinearLayout root = new LinearLayout (this);
        root.setLayoutParams (new LinearLayout.LayoutParams (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));


        ViewPager viewPager = new ViewPager (this);
        viewPager.setLayoutParams (new ViewPager.LayoutParams ());

        viewPager.setAdapter (new PlatesPageAdapter (configuration, this.getBaseContext ()));

        // create pager title strip
        PagerTitleStrip pagerTitleStrip = new PagerTitleStrip (this);
        pagerTitleStrip.setBackgroundColor (Color.BLACK);
        pagerTitleStrip.setTextSize (TypedValue.COMPLEX_UNIT_DIP, 24);

        ViewPager.LayoutParams layoutParams = new ViewPager.LayoutParams ();
        layoutParams.height = ViewPager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = ViewPager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.TOP;

        viewPager.addView (pagerTitleStrip, layoutParams);

        root.addView (viewPager);
        setContentView (root);


        logger.info ("onCreate end");
    }


    @Override protected void onStart ()
    {
        logger.info ("onStart begin");

        super.onStart ();
        Intent intent = new Intent (this, HominoNetworkService.class);
        bindService (intent, hominoNetworkServiceConnection, Context.BIND_AUTO_CREATE);

        logger.info ("onStart end");
    }


    @Override protected void onStop ()
    {
        logger.info ("onStop begin");

        super.onStop ();
        // Unbind from the service
        if (configuration != null && configuration.getHominoNetworkService () != null)
        {
            logger.info ("unbinding service");
            unbindService (hominoNetworkServiceConnection);
            configuration.setHominoNetworkService (null);
            logger.info ("done");
        }

        logger.info ("onStop end");
    }


    @Override public void onPause ()
    {
        logger.info ("onPause begin");

        super.onPause ();

        if (pulseTimer != null)
        {
            pulseTimer.cancel ();
        }

        logger.info ("onPause end");
    }


    private void pulse ()
    {
        if (configuration != null)
        {
            Date now = new Date ();


            // ping
            MultiMessage multiMessage = new MultiMessage ();
            for (ShellDevice shellDevice : configuration.getDevices ().getShellDevices ())
            {
                multiMessage.add (shellDevice.ping (now));
            }
            configuration.flushMessages (multiMessage);


            // pulse
            configuration.getPulseHandler ().pulse (configuration, now);

            configuration.log (now);
        }
    }


    private ServiceConnection hominoNetworkServiceConnection = new ServiceConnection ()
    {
        @Override public void onServiceConnected (ComponentName name, IBinder service)
        {
            logger.info ("onServiceConnected begin");
            configuration.setHominoNetworkService (((HominoNetworkService.LocalBinder) service).getService ());

            // pulse
            pulse ();

            // schedule next pulse
            pulseTimer = new Timer ();
            pulseTimer.schedule (new TimerTask ()
            {
                public void run ()
                {
                    runOnUiThread (new Runnable ()
                    {
                        public void run ()
                        {
                            pulse ();
                        }
                    });
                }
            }, 0, 1000); // updates each second


            logger.info ("onServiceConnected end");
        }


        @Override public void onServiceDisconnected (ComponentName name)
        {
            logger.info ("onServiceDisconnected begin");

            configuration.setHominoNetworkService (null);

            // disable pulse
            if (pulseTimer != null)
            {
                pulseTimer.cancel ();
                pulseTimer = null;
            }

            logger.info ("onServiceDisconnected end");
        }
    };


    private final static Logger logger = Logger.getLogger (MainActivity.class.getName ());
    private Timer pulseTimer;
    private Configuration configuration;
}


class HominoNetworkBroadcastReceiver extends BroadcastReceiver
{
    public HominoNetworkBroadcastReceiver (Configuration configuration)
    {
        this.configuration = configuration;
    }


    @Override public void onReceive (Context context, Intent intent)
    {
        try
        {
            logger.info ("onReceive: begin");

            // deserialize
            HominoNetworkMessage hominoNetworkMessage = (HominoNetworkMessage) intent.getSerializableExtra (HominoNetworkMessage.class.getSimpleName ());
            logger.info ("Received ", hominoNetworkMessage);


            // locate control device
            DeviceControlNode deviceControlNode = configuration.getDevices ().getDeviceControlNodesById ().get (hominoNetworkMessage.getDeviceControlNodeId ());


            // report error
            if (hominoNetworkMessage.isError ())
            {
                configuration.error (deviceControlNode != null ? deviceControlNode.getId () : null, hominoNetworkMessage.getMessage ());
            }


            // handle message
            else
            {
                Validate.notNull (deviceControlNode, "Missing device control node ", hominoNetworkMessage.getDeviceControlNodeId ());
                MultiMessage multiMessage = new MultiMessage (deviceControlNode.getId (), hominoNetworkMessage.getMessage ());

                for (Message message : multiMessage.getMessages ())
                {
                    Device device = configuration.getDevices ().getDevice (message.getDeviceId ());
                    if (device == null)
                    {
                        configuration.error (deviceControlNode.getId (), "Unknown device " + message.getDeviceId ());
                        continue;
                    }
                    device.handleMessage (message);

                    Plate plate = configuration.getPlatesAndPages ().getPlatesById ().get (device.getId ());
                    if (plate != null)
                    {
                        plate.refresh ();
                    }
                }
            }

            logger.info ("onReceive: end");
        }
        catch (Exception e)
        {
            logger.severe (e, "Exception occured");
            configuration.error (null, e.getMessage ());
        }
    }


    private static final CustomLogger logger = CustomLogger.getLogger ("BROADCAST_RECEIVER");
    private final Configuration configuration;
}