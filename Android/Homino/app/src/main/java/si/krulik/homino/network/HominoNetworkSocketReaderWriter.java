package si.krulik.homino.network;

import static si.krulik.homino.Constants.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import lombok.Setter;
import lombok.ToString;
import si.krulik.homino.common.logger.CustomLogger;
import si.krulik.homino.common.validate.Validate;
import si.krulik.homino.message.Message;
import si.krulik.homino.message.MultiMessage;


@ToString (includeFieldNames = true, of = {"deviceControlNodeIdArg", "deviceControlNodeNetworkAddressArg", "doConnect", "requestTermination"}) public class HominoNetworkSocketReaderWriter
{
    // constructor, reader and writer
    public HominoNetworkSocketReaderWriter (String deviceControlNodeIdArg, String deviceControlNodeNetworkAddressArg, Socket socketArg, HominoNetworkService hominoNetworkServiceArg)
    {
        logger = CustomLogger.getLogger ("HOMINO_SOCKET_RW[" + deviceControlNodeNetworkAddress + "]");

        deviceControlNodeNetworkAddress = deviceControlNodeNetworkAddressArg;
        deviceControlNodeId = deviceControlNodeIdArg;
        socket = socketArg;
        hominoNetworkService = hominoNetworkServiceArg;


        String[] split = deviceControlNodeNetworkAddress.split (":");
        Validate.isTrue (split.length == 2, "Invalid internet address format ", deviceControlNodeNetworkAddress);
        deviceControlNodeNetworkHost = split[0];
        deviceControlNodePort = Integer.parseInt (split[1]);

        doConnect = (socketArg == null);
        logger.fine ("Set up ", this);


        logger.fine ("Starting reader thread");
        Thread controlNodeCommunicationReaderThread = new Thread (new Runnable ()
        {
            public void run ()
            {
                logger.fine ("Read thread started");
                boolean forceConnect = false;
                while (!requestTermination)
                {
                    try
                    {
                        if (connect (forceConnect, READER))
                        {
                            String received = readInternal ();
                            logger.fine ("Read internal returned");
                            forceConnect = false;
                            if (deviceControlNodeId == null)
                            {
                                String[] parts = new MultiMessage (null, received).getMessages ().get (0).getParts ();
                                if (parts.length == 2 && parts[0].equals (IDENT))
                                {
                                    deviceControlNodeId = parts[1];
                                    logger.fine (deviceControlNodeNetworkAddress, " identified as ", deviceControlNodeId);

                                    hominoNetworkService.putHominoNetworkSocketReaderWriter (deviceControlNodeId, HominoNetworkSocketReaderWriter.this);
                                }
                                else
                                {
                                    Validate.fail ("Missing identification for device control node ", deviceControlNodeNetworkAddress);
                                }
                            }
                            else
                            {
                                HominoNetworkSocketReaderWriter.this.hominoNetworkService.broadcast (new HominoNetworkMessage (deviceControlNodeId, deviceControlNodeNetworkAddress, received, false));
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        close ();

                        logger.severe (e, "Connect and/or read exception");
                        if (doConnect)
                        {
                            forceConnect = true;
                            if (!requestTermination)
                            {
                                HominoNetworkSocketReaderWriter.this.hominoNetworkService.broadcast (new HominoNetworkMessage (deviceControlNodeId, deviceControlNodeNetworkAddress, e.getMessage () + " during read ", true));
                            }
                        }
                        else
                        {
                            requestTermination = true;
                        }
                    }
                }
                logger.info ("Read thread terminated");
            }
        });
        controlNodeCommunicationReaderThread.start ();


        logger.fine ("Starting writer thread");
        Thread controlNodeCommunicationWriterThread = new Thread (new Runnable ()
        {
            public void run ()
            {
                logger.fine ("Write thread started");
                boolean forceConnect = false;
                while (!requestTermination)
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
                        logger.severe (e, "Writer exception");

                        forceConnect = true;
                        HominoNetworkSocketReaderWriter.this.hominoNetworkService.broadcast (new HominoNetworkMessage (deviceControlNodeId, deviceControlNodeNetworkAddress, e.getMessage () + " during write ", true));
                    }
                }
                logger.info ("Write thread terminated");
            }
        });
        controlNodeCommunicationWriterThread.start ();
    }


    public void write (String message)
    {
        try
        {
            writerMessageQueue.put (message);
        }
        catch (InterruptedException e)
        {
            logger.severe (e, "Write queue overflow");
            HominoNetworkSocketReaderWriter.this.hominoNetworkService.broadcast (new HominoNetworkMessage (deviceControlNodeId, deviceControlNodeNetworkAddress, "Write queue overflow", true));
        }
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


    private BlockingQueue<String> writerMessageQueue = new ArrayBlockingQueue (5);


    synchronized private boolean connect (boolean force, String threadName)
    {
        if (!doConnect)
        {
            try
            {
                socketDataInputStream = new DataInputStream (socket.getInputStream ());
                socketDataOutputStream = new DataOutputStream (socket.getOutputStream ());
                return (true);
            }
            catch (Exception e)
            {
                hominoNetworkService.broadcast (new HominoNetworkMessage (deviceControlNodeId, deviceControlNodeNetworkAddress, "Failed to create data streams on provided socket", true));
                return (false);
            }
        }

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
                socket = new Socket (deviceControlNodeNetworkHost, deviceControlNodePort);
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
                socket = new Socket (deviceControlNodeNetworkHost, deviceControlNodePort);
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
            hominoNetworkService.broadcast (new HominoNetworkMessage (deviceControlNodeId, deviceControlNodeNetworkAddress, "Failed to connect to control node ", true));
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
                throw new IOException ("Invalid message cookie from " + deviceControlNodeNetworkAddress);
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


    private void writeInternal (String messagePayload) throws IOException
    {
        logger.fine ("Write: '", messagePayload, "'");


        byte[] buffer = composeRawMessage (messagePayload);


        logger.fine ("Buffer length ", buffer.length);
        socketDataOutputStream.write (buffer);

        // flush
        logger.fine ("Flushing");
        socketDataOutputStream.flush ();
        logger.fine ("Flushed");
    }


    private byte[] composeRawMessage (String messagePayload)
    {
        byte[] buffer = new byte[messageMagicCookie.length + 2 + messagePayload.length ()];
        int index;
        for (index = 0; index < messageMagicCookie.length; index++)
        {
            buffer[index] = messageMagicCookie[index];
        }

        // size
        buffer[index++] = (byte) (messagePayload.length () >> 8);
        buffer[index++] = (byte) (messagePayload.length () & 0xFF);

        // payload
        for (byte b : messagePayload.getBytes ())
        {
            buffer[index] = b;
            index++;
        }
        return (buffer);
    }


    @Setter private boolean requestTermination = false;


    private String deviceControlNodeId;
    private String deviceControlNodeNetworkAddress;
    private String deviceControlNodeNetworkHost;
    private int deviceControlNodePort;

    private boolean doConnect;
    private Socket socket;
    private DataInputStream socketDataInputStream;
    private DataOutputStream socketDataOutputStream;

    private CustomLogger logger;
    private HominoNetworkService hominoNetworkService;
    private long lastSocketConnectMillis = 0;

    final byte[] messageMagicCookie = {'M', 'G', 'C', 'K'};
}