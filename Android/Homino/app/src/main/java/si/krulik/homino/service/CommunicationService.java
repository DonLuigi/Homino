package si.krulik.homino.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class CommunicationService extends Service
{
    public class LocalBinder extends Binder
    {
        public CommunicationService getService ()
        {
            return CommunicationService.this;
        }
    }


    @Override
    public IBinder onBind (Intent workIntent)
    {
        return (localBinder);
    }


    @Override
    public int onStartCommand (Intent intent, int flags, int startId)
    {
        super.onStartCommand (intent, flags, startId);
        return START_STICKY;
    }


    @Override
    public void onDestroy ()
    {
        super.onDestroy ();

        for (ControlNodeCommunicationWorker controlNodeCommunicationWorker : controlNodeCommunicationWorkers.values ())
        {
            controlNodeCommunicationWorker.requestReadThreadTermination = true;
            controlNodeCommunicationWorker.close ();
        }
    }


    public void write (ControlNodeCommunicationRequest request)
    {
        logger.info ("Received work: " + request);

        // look up socket
        ControlNodeCommunicationWorker controlNodeCommunicationWorker = controlNodeCommunicationWorkers.get (request.controlNodeNetworkAddress);
        logger.info ("Control node communication worker: " + controlNodeCommunicationWorker);

        if (controlNodeCommunicationWorker == null)
        {
            controlNodeCommunicationWorker = new ControlNodeCommunicationWorker (request.controlNodeNetworkAddress, this);
            logger.info ("New control communication worker: " + controlNodeCommunicationWorker);

            controlNodeCommunicationWorkers.put (request.controlNodeNetworkAddress, controlNodeCommunicationWorker);
            Thread controlNodeCommunicationReaderThread = new Thread (controlNodeCommunicationWorker);
            controlNodeCommunicationReaderThread.start ();
        }

        try
        {
            // write
            controlNodeCommunicationWorker.write (request.message);
        }
        catch (Throwable t)
        {
            Intent localIntent = new Intent (ControlNodeCommunicationRequest.communicationRequestIntentAction).putExtra (ControlNodeCommunicationRequest.class.getSimpleName (), new
                ControlNodeCommunicationRequest (request.controlNodeNetworkAddress, "ERROR"));
            LocalBroadcastManager.getInstance (this).sendBroadcast (localIntent);
        }
    }


    private static final Logger logger = Logger.getLogger (CommunicationService.class.getName ());
    private LocalBinder localBinder = new LocalBinder ();
    private Map<String, ControlNodeCommunicationWorker> controlNodeCommunicationWorkers = new HashMap<String, ControlNodeCommunicationWorker> ();
}