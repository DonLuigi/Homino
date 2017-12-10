package si.krulik.homino.network;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.HashMap;
import java.util.Map;

import si.krulik.homino.common.logger.CustomLogger;


public class HominoNetworkService extends Service
{
    public class LocalBinder extends Binder
    {
        public HominoNetworkService getService ()
        {
            return HominoNetworkService.this;
        }
    }


    @Override public IBinder onBind (Intent workIntent)
    {
        logger.info ("BIND");
        return (localBinder);
    }


    @Override public int onStartCommand (Intent intent, int flags, int startId)
    {
        logger.info ("START");
        super.onStartCommand (intent, flags, startId);
        return START_STICKY;
    }


    @Override public void onDestroy ()
    {
        logger.info ("DESTROY");
        super.onDestroy ();

        for (HominoNetworkNodeWorker hominoNetworkNodeWorker : workersByIp.values ())
        {
            hominoNetworkNodeWorker.requestReadThreadTermination ();
            hominoNetworkNodeWorker.close ();
        }
    }


    public void write (HominoNetworkMessage message)
    {
        logger.fine ("Write: ", message);


        // locate worker
        HominoNetworkNodeWorker hominoNetworkNodeWorker = workersByIp.get (message.getControlNodeIpPort ());
        if (hominoNetworkNodeWorker == null)
        {
            hominoNetworkNodeWorker = new HominoNetworkNodeWorker (message.getControlNodeIpPort (), this);
            workersByIp.put (message.getControlNodeIpPort (), hominoNetworkNodeWorker);
            logger.info ("New ", hominoNetworkNodeWorker);
        }
        else
        {
            logger.info ("Existing ", hominoNetworkNodeWorker);
        }


        // write
        try
        {
            hominoNetworkNodeWorker.write (message.getMessage ());
        }
        catch (Exception e)
        {
            logger.severe (e, "Exception during write");
        }
    }


    private LocalBinder localBinder = new LocalBinder ();
    private Map<String, HominoNetworkNodeWorker> workersByIp = new HashMap<String, HominoNetworkNodeWorker> ();
    private static final CustomLogger logger = CustomLogger.getLogger ("NETWORK_SERVICE");
}