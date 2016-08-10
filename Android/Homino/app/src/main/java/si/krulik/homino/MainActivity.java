package si.krulik.homino;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import si.krulik.homino.configuration.Configuration;
import si.krulik.homino.customization.Customization;
import si.krulik.homino.service.CommunicationService;
import si.krulik.homino.service.ControlNodeCommunicationRequest;


public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        logger.info ("onCreate begin");

        super.onCreate (savedInstanceState);


        // parse configuration
        configuration = null;
        try
        {
            configuration = new Customization ().compose (getApplicationContext (), this);
        }
        catch (Throwable t)
        {
            t.printStackTrace ();
            throw (new RuntimeException ("Failed to configure application", t));
        }


        // setup background communication service
        LocalBroadcastManager.getInstance (this).registerReceiver (new CommunicationReceiver (configuration), new IntentFilter (ControlNodeCommunicationRequest.communicationRequestIntentAction));


        // pager view
        LinearLayout root = new LinearLayout (this);
        root.setLayoutParams (new LinearLayout.LayoutParams (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));


        ViewPager viewPager = new ViewPager (this);
        viewPager.setLayoutParams (new ViewPager.LayoutParams ());

        viewPager.setAdapter (new PlatesPageAdapter (configuration, this.getBaseContext ()));

        root.addView (viewPager);
        setContentView (root);


        logger.info ("onCreate end");
    }


    @Override
    protected void onStart ()
    {
        logger.info ("onStart begin");

        super.onStart ();
        Intent intent = new Intent (this, CommunicationService.class);
        bindService (intent, communcationServiceConnection, Context.BIND_AUTO_CREATE);

        logger.info ("onStart end");
    }


    @Override
    protected void onStop ()
    {
        logger.info ("onStop begin");

        super.onStop ();
        // Unbind from the service
        if (configuration != null && configuration.communicationService != null)
        {
            logger.info ("unbinding service");
            unbindService (communcationServiceConnection);
            configuration.communicationService = null;
            logger.info ("done");
        }

        logger.info ("onStop end");
    }



    @Override
    public void onPause ()
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
            configuration.pulseHandler.pulse (configuration, new Date ());
        }
    }


    private ServiceConnection communcationServiceConnection = new ServiceConnection ()
    {
        @Override
        public void onServiceConnected (ComponentName name, IBinder service)
        {
            logger.info ("onServiceConnected begin");
            configuration.communicationService = ((CommunicationService.LocalBinder) service).getService ();

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


        @Override
        public void onServiceDisconnected (ComponentName name)
        {
            logger.info ("onServiceDisconnected begin");

            configuration.communicationService = null;

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


class CommunicationReceiver extends BroadcastReceiver
{
    public CommunicationReceiver (Configuration configuration)
    {
        this.configuration = configuration;
    }


    @Override
    public void onReceive (Context context, Intent intent)
    {
        logger.info ("onReceive: begin");

        ControlNodeCommunicationRequest controlNodeCommunicationRequest = (ControlNodeCommunicationRequest) intent.getSerializableExtra (ControlNodeCommunicationRequest.class.getSimpleName ());
        logger.info ("Main received communication request: " + controlNodeCommunicationRequest);

        if (controlNodeCommunicationRequest.message == null || controlNodeCommunicationRequest.message.equals ("ERROR"))
        {
            Toast toast = Toast.makeText (context, "Error communicating with control node " + controlNodeCommunicationRequest.controlNodeNetworkAddress, Toast.LENGTH_LONG);
        }
        else
        {
            try
            {
                configuration.communicationMessageHandler.handle (controlNodeCommunicationRequest.controlNodeNetworkAddress, controlNodeCommunicationRequest.message, configuration);
            }
            catch (Throwable t)
            {
                logger.log (Level.SEVERE, "Exception occured while handling event message " + controlNodeCommunicationRequest.message, t);
                Toast toast = Toast.makeText (context, t.getMessage (), Toast.LENGTH_LONG);
                toast.show ();
            }
        }
        logger.info ("onReceive: end");
    }


    private static final Logger logger = Logger.getLogger (CommunicationReceiver.class.getName ());
    private final Configuration configuration;
}