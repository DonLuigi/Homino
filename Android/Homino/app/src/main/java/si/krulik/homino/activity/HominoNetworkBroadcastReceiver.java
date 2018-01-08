package si.krulik.homino.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.Date;

import si.krulik.homino.common.logger.CustomLogger;
import si.krulik.homino.runtime.Runtime;
import si.krulik.homino.devices.base.Device;
import si.krulik.homino.message.Message;
import si.krulik.homino.message.MultiMessage;
import si.krulik.homino.plates.base.Plate;
import si.krulik.homino.runtime.Error;



public class HominoNetworkBroadcastReceiver extends BroadcastReceiver
{
    static public final String RECEIVE_MULTIMESSAGE_INTENT = "RECEIVE_MULTIMESSAGE";
    static public final String RECEIVE_ERROR_INTENT = "RECEIVE_ERROR";

    static public final IntentFilter intentFilter = new IntentFilter (RECEIVE_MULTIMESSAGE_INTENT);
    {
        intentFilter.addAction (RECEIVE_ERROR_INTENT);
    }


    @Override public void onReceive (Context context, Intent intent)
    {
        try
        {
            logger.info ("onReceive: begin");

            if (intent.getAction ().equals (RECEIVE_MULTIMESSAGE_INTENT))
            {
                MultiMessage multiMessage = (MultiMessage) intent.getSerializableExtra (MultiMessage.class.getSimpleName ());
                logger.info ("Received ", multiMessage);

                for (Message message : multiMessage.getMessages ())
                {
                    Device device = Runtime.devices.getDevice (message.getDeviceId ());
                    if (device == null)
                    {
                        Runtime.logError (new Error ("Unknown device " + message.getDeviceId (), new Date (), message.getDeviceControlNode ().getId ()));
                        continue;
                    }
                    device.handleMessage (message);

                    Plate plate = Runtime.platesAndPages.getPlatesById ().get (device.getId ());
                    if (plate != null)
                    {
                        plate.refresh ();
                    }
                }
            }


            else if (intent.getAction ().equals (RECEIVE_ERROR_INTENT))
            {
                Error error = (Error) intent.getSerializableExtra (Error.class.getSimpleName ());
                logger.info ("Received ", error);
                Runtime.logError (error);
            }


            logger.info ("onReceive: end");
        }
        catch (Exception e)
        {
            logger.severe (e, "Exception occured");
            Runtime.logError (new Error ("Exception occured", new Date (), null));
        }
    }


    private static final CustomLogger logger = CustomLogger.getLogger ("BROADCAST_RECEIVER");
}
