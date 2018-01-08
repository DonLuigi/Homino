package si.krulik.homino.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import si.krulik.homino.runtime.Runtime;
import si.krulik.homino.network.HominoNetworkService;

import static si.krulik.homino.Constants.*;


public class Alarms extends BroadcastReceiver
{
    @Override public void onReceive (Context context, Intent intent)
    {
        // configuration
        boolean enabled = Runtime.settings.getKeepConnectionToDeviceControlNodeIds () != null && !Runtime.settings.getKeepConnectionToDeviceControlNodeIds ().equals (NONE);
        if (!enabled)
        {
            return;
        }


        // boot
        if ("android.intent.action.BOOT_COMPLETED".equals (intent.getAction ()) && !Runtime.settings.getKeepConnectionAsDeviceControlNodeId ().isEmpty ())
        {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService (Context.ALARM_SERVICE);
            alarmManager.setInexactRepeating (AlarmManager.ELAPSED_REALTIME_WAKEUP, Runtime.keepConnectionToDeviceControlNodesEverySec * 1000, Runtime.keepConnectionToDeviceControlNodesEverySec,
                PendingIntent.getBroadcast (context, 133, new Intent (INTENT_START).setClass (context, Alarms.class), 0));
        }


        // delivery

        Intent startServiceIntent = new Intent (context, HominoNetworkService.class);
        context.startService (startServiceIntent);
    }


    static public void enableAlarms ()
    {
        boolean enabled = Runtime.settings != null && Runtime.settings.getKeepConnectionToDeviceControlNodeIds () != null && !Runtime.settings.getKeepConnectionToDeviceControlNodeIds ().equals (NONE);
        if (enabled)
        {
            AlarmManager alarmManager = (AlarmManager) Runtime.context.getSystemService (Context.ALARM_SERVICE);
            alarmManager.setInexactRepeating ( //
                AlarmManager.ELAPSED_REALTIME_WAKEUP, //
                Runtime.keepConnectionToDeviceControlNodesEverySec * 1000, //
                Runtime.keepConnectionToDeviceControlNodesEverySec * 1000, //
                PendingIntent.getService (Runtime.activity, 123, new Intent (INTENT_START).setClass (Runtime.activity, AlarmsService.class), 0));
        }
    }
}