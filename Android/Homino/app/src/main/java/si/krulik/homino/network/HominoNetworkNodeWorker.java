package si.krulik.homino.network;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import lombok.ToString;
import si.krulik.homino.common.logger.CustomLogger;
import si.krulik.homino.common.validate.Validate;


@ToString (includeFieldNames = true, of = {"peerIp", "peerPort", "requestReadThreadTermination"}) public class HominoNetworkNodeWorker
{
    public void write (String message)
    {
        try
        {
            writerMessageQueue.put (message);
        }
        catch (InterruptedException e)
        {
            logger.severe (e, "Write queue overflow");
            broadcast (new HominoNetworkMessage (peerIpPort, "Write queue overflow", true));
        }
    }


    public void requestReadThreadTermination ()
    {
        requestReadThreadTermination = true;
    }


    synchronized public void close ()
    {
        logger.fine ("Closing socket");
        try
        {
            socket.close ();
        }
        catch (Exception e)
        {
            logger.severe (e, "Socket closed with exception");
        }
        socket = null;
    }


    // constructor, reader and writer
    public HominoNetworkNodeWorker (final String peerIpPort, final Context context)
    {
        logger = CustomLogger.getLogger ("HOMINO_COMM[" + peerIpPort + "]");

        logger.fine ("Setting up ", HominoNetworkNodeWorker.class.getSimpleName ());
        String[] split = peerIpPort.split (":");
        Validate.isTrue (split.length == 2, "Invalid internet address format: ", peerIpPort);

        this.peerIpPort = peerIpPort;
        this.context = context;

        peerIp = split[0];
        peerPort = Integer.parseInt (split[1]);
        logger.fine (this);


        // start reader thread
        Thread controlNodeCommunicationReaderThread = new Thread (new Runnable ()
        {
            public void run ()
            {
                logger.fine ("Read thread started");
                boolean forceConnect = false;
                while (!requestReadThreadTermination)
                {
                    try
                    {
                        if (connect (forceConnect, "READER"))
                        {
                            String received = readInternal ();
                            forceConnect = false;
                            broadcast (new HominoNetworkMessage (peerIpPort, received, false));
                        }
                    }
                    catch (Exception e)
                    {
                        forceConnect = true;
                        if (!requestReadThreadTermination)
                        {
                            broadcast (new HominoNetworkMessage (peerIpPort, e.getMessage () + " during read ", true));
                        }
                    }
                }
                logger.info ("Read thread terminated");
            }
        });
        controlNodeCommunicationReaderThread.start ();


        // start reader thread
        Thread controlNodeCommunicationWriterThread = new Thread (new Runnable ()
        {
            public void run ()
            {
                logger.fine ("Write thread started");
                boolean forceConnect = false;
                while (!requestReadThreadTermination)
                {
                    try
                    {
                        String message = writerMessageQueue.poll (1000, TimeUnit.MILLISECONDS);
                        if (message == null)
                        {
                            continue;
                        }

                        if (connect (forceConnect, "WRITER"))
                        {
                            writeInternal (message);
                        }
                    }
                    catch (Exception e)
                    {
                        forceConnect = true;
                        broadcast (new HominoNetworkMessage (peerIpPort, e.getMessage () + " during write ", true));
                    }
                }
                logger.info ("Write thread terminated");
            }
        });
        controlNodeCommunicationWriterThread.start ();
    }


    private BlockingQueue<String> writerMessageQueue = new ArrayBlockingQueue (5);


    synchronized private boolean connect (boolean force, String threadName)
    {
        try
        {
            if (socket == null)
            {
                long nowMillis = new Date ().getTime ();
                if (nowMillis - lastSocketConnectMillis < 1000)
                {
                    Thread.sleep (1000); // prevent cpu load on caller
                    logger.fine ("Too soon for reconnect on thread ", threadName);
                    return (false);
                }
                else
                {
                    lastSocketConnectMillis = nowMillis;
                }
            }

            boolean settle = false;
            if (socket == null)
            {
                logger.fine ("Connecting on thread ", threadName);
                socket = new Socket (peerIp, peerPort);
                // TODO: socket.setSoTimeout ();
                logger.fine ("Connected");
                socketDataInputStream = new DataInputStream (socket.getInputStream ());
                socketDataOutputStream = new DataOutputStream (socket.getOutputStream ());
                logger.fine ("Streams prepared");
                settle = true;
            }
            else if (force)
            {
                logger.fine ("Connecting on thread ", threadName);

                // close socket
                close ();
                logger.fine ("Re-connecting");
                socket = new Socket (peerIp, peerPort);
                logger.fine ("Connected");
                socketDataInputStream = new DataInputStream (socket.getInputStream ());
                socketDataOutputStream = new DataOutputStream (socket.getOutputStream ());
                logger.fine ("Streams prepared");
                settle = true;
            }

            if (settle)
            {
                try
                {
                    logger.fine ("Settling");
                    Thread.sleep (100); // TODO: add settle to configuration
                    logger.fine ("Done connecting on thread ", threadName);
                }
                catch (Throwable t)
                {
                    logger.severe (t, "Exception during sleep");
                }
            }

            return (true);
        }
        catch (Exception e)
        {
            logger.severe (e, "Failed to connect on thread ", threadName);
            socket = null;
            logger.fine ("Connect returning false");
            broadcast (new HominoNetworkMessage (peerIpPort, "Failed to connect to control node ", true));
            return (false);
        }
    }


    private String readInternal () throws IOException
    {
        // magic cookie
        logger.fine ("Reading");
        byte[] receivedMagicCookie = new byte[messageMagicCookie.length];
        socketDataInputStream.readFully (receivedMagicCookie);

        logger.fine ("Read magic");
        for (int i = 0; i < receivedMagicCookie.length; i++)
        {
            if (receivedMagicCookie[i] != messageMagicCookie[i])
            {
                throw new IOException ("Invalid message cookie from " + peerIpPort);
            }
        }


        byte[] sizeRaw = new byte[2];
        socketDataInputStream.readFully (sizeRaw);
        int size = (((int) sizeRaw[0] & 0xFF) << 8) + (sizeRaw[1] & 0xFF);
        logger.fine ("Payload size raw: ", (int) sizeRaw[0], ", ", (int) sizeRaw[1], ", size: ", size);

        byte[] payload = new byte[size];
        socketDataInputStream.readFully (payload);

        String payloadAsString = new String (payload);
        logger.fine ("Received payload: '", payloadAsString, "'");
        return (payloadAsString);
    }


    private void writeInternal (String payload) throws IOException
    {
        logger.fine ("Write: '", payload, "'");


        // magic cookie
        byte[] buffer = new byte[messageMagicCookie.length + 2 + payload.length ()];


        int i;
        for (i = 0; i < messageMagicCookie.length; i++)
        {
            buffer[i] = messageMagicCookie[i];
        }

        // size
        buffer[i++] = (byte) (payload.length () >> 8);
        buffer[i++] = (byte) (payload.length () & 0xFF);

        // payload
        for (byte b : payload.getBytes ())
        {
            buffer[i] = b;
            i++;
        }

        logger.fine ("Buffer length ", buffer.length);
        socketDataOutputStream.write (buffer);

        // flush
        logger.fine ("Flushing");
        socketDataOutputStream.flush ();
        logger.fine ("Flushed");
    }


    private void broadcast (HominoNetworkMessage hominoNetworkMessage)
    {
        logger.fine ("Broadcasting", hominoNetworkMessage);
        Intent localIntent = new Intent (HominoNetworkMessage.communicationRequestIntentAction).putExtra (HominoNetworkMessage.class.getSimpleName (), hominoNetworkMessage);
        LocalBroadcastManager.getInstance (context).sendBroadcast (localIntent);
    }


    private Context context;
    private Socket socket;
    private DataInputStream socketDataInputStream;
    private DataOutputStream socketDataOutputStream;
    private InetAddress socketInetAddress;

    private CustomLogger logger;
    private String peerIp;
    private int peerPort;
    private String peerIpPort;
    private boolean requestReadThreadTermination = false;
    private long lastSocketConnectMillis = 0;

    private static final byte[] messageMagicCookie = {'M', 'G', 'C', 'K'};
}