package si.krulik.homino.authorizer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.List;

import si.krulik.homino.authorizer.configuration.Configuration;
import si.krulik.homino.authorizer.configuration.ConfigurationItem;

import static si.krulik.homino.authorizer.Constants.*;


public class AlarmBroadcastReceiver extends BroadcastReceiver
{
    @Override public void onReceive (Context context, Intent intent)
    {
        // configuration
        Configuration configuration = new Configuration (context);


        // boot
        if ("android.intent.action.BOOT_COMPLETED".equals (intent.getAction ()) && configuration.isRunning ())
        {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService (Context.ALARM_SERVICE);
            alarmManager.setInexactRepeating (AlarmManager.ELAPSED_REALTIME_WAKEUP, configuration.getDeliverySec () * 1000, configuration.getDeliverySec () * 1000,
                PendingIntent.getBroadcast (context, 133, new Intent (INTENT_START).setClass (context, AlarmBroadcastReceiver.class), 0));
        }


        // delivery
        if (configuration.isRunning () && configuration.getDeliverySec () > 0)
        {
            // start network service
            Intent startServiceIntent = new Intent (context, HominoNetworkService.class);
            context.startService (startServiceIntent);
        }
    }


    private static final byte[] messageMagicCookie = {'M', 'G', 'C', 'K'};
}
