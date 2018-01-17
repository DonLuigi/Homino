package si.krulik.homino.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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

import si.krulik.homino.common.logger.CustomLogger;
import si.krulik.homino.runtime.Runtime;
import si.krulik.homino.devices.ShellDevice;
import si.krulik.homino.message.MultiMessage;
import si.krulik.homino.network.HominoNetworkService;

import static si.krulik.homino.runtime.Runtime.*;


public class MainActivity extends AppCompatActivity
{
    @Override protected void onCreate (Bundle savedInstanceState)
    {
        logger.info ("onCreate begin");

        super.onCreate (savedInstanceState);


        LocalBroadcastManager.getInstance (this).registerReceiver (new HominoNetworkBroadcastReceiver (), HominoNetworkBroadcastReceiver.intentFilter);


        // parse configuration
        Runtime.setup (getApplicationContext (), this);


        // pager view
        LinearLayout root = new LinearLayout (this);
        root.setLayoutParams (new LinearLayout.LayoutParams (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));


        viewPager = new ViewPager (this);
        viewPager.setLayoutParams (new ViewPager.LayoutParams ());

        viewPager.setAdapter (new PlatesPageAdapter ());

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

        logger.info ("Binding service ", HominoNetworkService.class.getSimpleName (), "...");
        Intent intent = new Intent (this, HominoNetworkService.class);
        bindService (intent, hominoNetworkServiceConnection, Context.BIND_AUTO_CREATE);
        logger.info ("Done");

        logger.info ("onStart end");
    }


    @Override protected void onStop ()
    {
        logger.info ("onStop begin");

        super.onStop ();

        if (Runtime.hominoNetworkService != null)
        {
            logger.info ("Binding service ", HominoNetworkService.class.getSimpleName (), "...");
            unbindService (hominoNetworkServiceConnection);
            Runtime.hominoNetworkService = null;
            logger.info ("Done");
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
            logger.fine ("Canceled pulse timer");
        }

        logger.info ("onPause end");
    }


    @Override public void onUserInteraction ()
    {
        lastUserInteractionSeconds = System.currentTimeMillis () / 1000;
    }


    private void pulse ()
    {
        logger.fine ("Pulsing...");
        Date now = new Date ();


        // ping
        MultiMessage multiMessage = new MultiMessage ();
        for (ShellDevice shellDevice : devices.getShellDevices ())
        {
            multiMessage.add (shellDevice.ping (now));
        }
        Runtime.flushMessages (multiMessage);


        // pulse
        Runtime.pulseHandler.pulse (now);


        // default plates page
        if (lastUserInteractionSeconds > 0 && now.getTime () / 1000 - lastUserInteractionSeconds > switchToDefaultPlatesPageTimeoutSeconds)
        {
            int defaultPlatePage = settings.getDefaultPlatePage ();
            if (defaultPlatePage >= 0)
            {
                viewPager.setCurrentItem (defaultPlatePage);
            }
            lastUserInteractionSeconds = 0;
        }

        Runtime.log (now);
    }


    private ServiceConnection hominoNetworkServiceConnection = new ServiceConnection ()
    {
        @Override public void onServiceConnected (ComponentName name, IBinder service)
        {
            logger.info ("onServiceConnected begin");
            Runtime.hominoNetworkService = ((HominoNetworkService.LocalBinder) service).getService ();

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

            Runtime.hominoNetworkService = null;

            // disable pulse
            if (pulseTimer != null)
            {
                pulseTimer.cancel ();
                pulseTimer = null;
            }

            logger.info ("onServiceDisconnected end");
        }
    };


    private final static CustomLogger logger = CustomLogger.getLogger (MainActivity.class.getName ());
    private Timer pulseTimer;
    private long lastUserInteractionSeconds;
    private ViewPager viewPager;
}