package si.krulik.homino.alarms;

import android.app.IntentService;
import android.content.Intent;

import si.krulik.homino.common.logger.CustomLogger;


public class AlarmsService extends IntentService
{
    public AlarmsService ()
    {
        super (AlarmsService.class.getSimpleName ());
    }


    public AlarmsService (String name)
    {
        super (name);
    }


    @Override protected void onHandleIntent (Intent intent)
    {
        // configuration
//        String authorizationId = configuration.getSettings ().getKeepConnectionToDeviceControlNodeIds ();
//        boolean enabled = authorizationId != null && !authorizationId.equals (NONE);
//
//        if (enabled)
//        {
//            // ssid
//            WifiManager wifiManager = (WifiManager) getApplicationContext ().getSystemService (Context.WIFI_SERVICE);
//            WifiInfo wifiInfo = wifiManager.getConnectionInfo ();
//            String ssid = wifiInfo.getSSID ().replace ("\"", "");
//
//
//            Authorization authorization = configuration.getAuthorizations ().getAuthorizationsById ().get (keepConnectionToDeviceControlNodeIds);
//            for (DeviceControlNode deviceControlNode : authorization.getDeviceControlNodes ())
//            {
//                HominoNetworkSocketReaderWriter hominoNetworkSocketReaderWriter = new HominoNetworkSocketReaderWriter (deviceControlNode.getId (), deviceControlNode.getNetworkAddress (), null, null);
//                hominoNetworkSocketReaderWriter
//                    .write ("IDENT," + configuration.getSettings ().getKeepConnectionAsDeviceControlNodeId () + ";" + authorization.getAuthorizationDeviceId () + "AUTH," + authorization.getId () + "," + configuration.getAuthorizations ().getValiditySec () + ";");
//            }
//        }
    }


    private static final CustomLogger logger = CustomLogger.getLogger ("NETWORK_SERVICE");
}
