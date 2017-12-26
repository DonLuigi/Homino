package si.krulik.homino.network;

import static si.krulik.homino.Constants.*;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import java.util.HashMap;
import java.util.Map;

import si.krulik.homino.common.logger.CustomLogger;


public class HominoNetworkService extends Service
{
    public void write (HominoNetworkMessage message)
    {
        logger.fine ("Write: ", message);


        // locate reader/writer
        HominoNetworkSocketReaderWriter hominoNetworkSocketReaderWriter = getHominoNetworkSocketReaderWriter (message);
        if (hominoNetworkSocketReaderWriter != null)
        {
            // located
            hominoNetworkSocketReaderWriter.write (message.getMessage ());
        }
        else
        {
            // not located, waiting on reader/writer for inbound connection
        }
    }


    public synchronized HominoNetworkSocketReaderWriter getHominoNetworkSocketReaderWriter (HominoNetworkMessage message)
    {
        // existing
        String deviceControlNodeId = message.getDeviceControlNodeId ();
        HominoNetworkSocketReaderWriter hominoNetworkSocketReaderWriter = hominoNetworkSocketReaderWritersByDeviceControlNodeId.get (deviceControlNodeId);
        if (hominoNetworkSocketReaderWriter != null)
        {
            logger.info ("Existing ", hominoNetworkSocketReaderWriter);
            return (hominoNetworkSocketReaderWriter);
        }


        // new
        String deviceControlNodeNetworkHost = message.getDeviceControlNodeNetworkHost ();
        int deviceControlNodeNetworkPort = message.getDeviceControlNodeNetworkPort ();

        // localhost listener
        if (deviceControlNodeNetworkHost.equals (LOCALHOST))
        {
            HominoNetworkSocketListener hominoNetworkSocketListener = hominoNetworkSocketListenerByPort.get (deviceControlNodeNetworkPort);
            if (hominoNetworkSocketListener == null)
            {
                hominoNetworkSocketListener = new HominoNetworkSocketListener (deviceControlNodeNetworkPort, this);
                hominoNetworkSocketListenerByPort.put (deviceControlNodeNetworkPort, hominoNetworkSocketListener);
                logger.fine ("New ", hominoNetworkSocketListener);
            }
            else
            {
                logger.fine ("Existig ", hominoNetworkSocketListener, ", waiting for connection");
            }

            return (null);
        }


        // remote connect
        else
        {
            hominoNetworkSocketReaderWriter = new HominoNetworkSocketReaderWriter (deviceControlNodeId, message.getDeviceControlNodeNetworkAddress (), null, this);
            hominoNetworkSocketReaderWritersByDeviceControlNodeId.put (deviceControlNodeId, hominoNetworkSocketReaderWriter);
            logger.info ("New ", hominoNetworkSocketReaderWriter);
            return (hominoNetworkSocketReaderWriter);
        }
    }


    public synchronized void putHominoNetworkSocketReaderWriter (String deviceControlNodeId, HominoNetworkSocketReaderWriter hominoNetworkSocketReaderWriter)
    {
        hominoNetworkSocketReaderWritersByDeviceControlNodeId.put (deviceControlNodeId, hominoNetworkSocketReaderWriter);
    }


    public synchronized void broadcast (HominoNetworkMessage hominoNetworkMessage)
    {
        logger.fine ("Broadcasting", hominoNetworkMessage);
        Intent localIntent = new Intent (HominoNetworkMessage.communicationRequestIntentAction).putExtra (HominoNetworkMessage.class.getSimpleName (), hominoNetworkMessage);
        LocalBroadcastManager.getInstance (this).sendBroadcast (localIntent);
    }


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
        return (START_STICKY);
    }


    @Override public void onDestroy ()
    {
        logger.info ("DESTROY");
        super.onDestroy ();


        // close listeners
        for (HominoNetworkSocketListener hominoNetworkSocketListener : hominoNetworkSocketListenerByPort.values ())
        {
            hominoNetworkSocketListener.setRequestTermination (true);
            hominoNetworkSocketListener.close ();
        }


        // close open connections
        for (HominoNetworkSocketReaderWriter hominoNetworkSocketReaderWriter : hominoNetworkSocketReaderWritersByDeviceControlNodeId.values ())
        {
            hominoNetworkSocketReaderWriter.setRequestTermination (true);
            hominoNetworkSocketReaderWriter.close ();
        }
    }


    private Map<String, HominoNetworkSocketReaderWriter> hominoNetworkSocketReaderWritersByDeviceControlNodeId = new HashMap ();
    private Map<Integer, HominoNetworkSocketListener> hominoNetworkSocketListenerByPort = new HashMap ();

    private LocalBinder localBinder = new LocalBinder ();
    private static final CustomLogger logger = CustomLogger.getLogger ("NETWORK_SERVICE");
}