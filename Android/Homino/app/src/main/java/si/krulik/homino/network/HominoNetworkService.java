package si.krulik.homino.network;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import si.krulik.homino.activity.HominoNetworkBroadcastReceiver;
import si.krulik.homino.common.logger.CustomLogger;
import si.krulik.homino.runtime.Runtime;
import si.krulik.homino.devices.base.Device;
import si.krulik.homino.devices.base.DeviceControlNode;
import si.krulik.homino.message.Message;
import si.krulik.homino.message.MultiMessage;
import si.krulik.homino.plates.base.Plate;
import si.krulik.homino.runtime.Error;

import static si.krulik.homino.runtime.Runtime.*;


public class HominoNetworkService extends Service
{
    public void write (MultiMessage multiMessage)
    {
        logger.fine ("Write: ", multiMessage);


        // locate reader/writer
        for (Map.Entry<DeviceControlNode, MultiMessage> entry : multiMessage.getMessagesPerDeviceControlNodeId ().entrySet ())
        {
            DeviceControlNode deviceControlNode = entry.getKey ();
            MultiMessage multiMessagePerDeviceControlId = entry.getValue ();

            HominoNetworkSocketReaderWriter hominoNetworkSocketReaderWriter = getHominoNetworkSocketReaderWriter (deviceControlNode);
            if (hominoNetworkSocketReaderWriter == null)
            {
                // ignore messages to non connected control nodes
                continue;
            }

            hominoNetworkSocketReaderWriter.write (multiMessagePerDeviceControlId);
        }
    }


    public synchronized HominoNetworkSocketReaderWriter getHominoNetworkSocketReaderWriter (DeviceControlNode deviceControlNode)
    {
        // existing
        HominoNetworkSocketReaderWriter hominoNetworkSocketReaderWriter = hominoNetworkSocketReaderWritersByDeviceControlNodeId.get (deviceControlNode.getId ());
        if (hominoNetworkSocketReaderWriter != null)
        {
            logger.info ("Existing ", hominoNetworkSocketReaderWriter);
            return (hominoNetworkSocketReaderWriter);
        }


        // localhost listener
        if (deviceControlNode.isIncommingConnection ())
        {
            HominoNetworkSocketListener hominoNetworkSocketListener = hominoNetworkSocketListenerByPort.get (deviceControlNode.getNetworkPort ());
            if (hominoNetworkSocketListener == null)
            {
                hominoNetworkSocketListener = new HominoNetworkSocketListener (deviceControlNode.getNetworkPort (), this);
                hominoNetworkSocketListenerByPort.put (deviceControlNode.getNetworkPort (), hominoNetworkSocketListener);
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
            hominoNetworkSocketReaderWriter = new HominoNetworkSocketReaderWriter (deviceControlNode, null, callback);
            hominoNetworkSocketReaderWritersByDeviceControlNodeId.put (deviceControlNode.getId (), hominoNetworkSocketReaderWriter);
            logger.info ("New ", hominoNetworkSocketReaderWriter);
            return (hominoNetworkSocketReaderWriter);
        }
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


    @Getter private IHominoNetworkSocketReaderWriterCallback callback = new IHominoNetworkSocketReaderWriterCallback ()
    {
        public void registerHominoNetworkSocketReaderWriter (DeviceControlNode deviceControlNode, HominoNetworkSocketReaderWriter hominoNetworkSocketReaderWriter)
        {
            hominoNetworkSocketReaderWritersByDeviceControlNodeId.put (deviceControlNode.getId (), hominoNetworkSocketReaderWriter);
        }


        public void receive (MultiMessage multiMessage)
        {
            logger.fine ("Broadcasting ", multiMessage);
            Intent intent = new Intent (HominoNetworkBroadcastReceiver.RECEIVE_MULTIMESSAGE_INTENT).putExtra (MultiMessage.class.getSimpleName (), multiMessage);
             LocalBroadcastManager.getInstance (HominoNetworkService.this).sendBroadcast (intent);
//            startService(intent);
        }


        public void error (DeviceControlNode deviceControlNode, Exception e, String message)
        {
            Error error = new Error (message, new Date (), deviceControlNode.getId ());
            logger.fine ("Broadcasting ", error);
            Intent intent = new Intent (HominoNetworkBroadcastReceiver.RECEIVE_ERROR_INTENT).putExtra (Error.class.getSimpleName (), error);
            LocalBroadcastManager.getInstance (HominoNetworkService.this).sendBroadcast (intent);
        }
    };


    private Map<String, HominoNetworkSocketReaderWriter> hominoNetworkSocketReaderWritersByDeviceControlNodeId = new HashMap ();
    private Map<Integer, HominoNetworkSocketListener> hominoNetworkSocketListenerByPort = new HashMap ();

    private LocalBinder localBinder = new LocalBinder ();
    private static final CustomLogger logger = CustomLogger.getLogger ("NETWORK_SERVICE");
}