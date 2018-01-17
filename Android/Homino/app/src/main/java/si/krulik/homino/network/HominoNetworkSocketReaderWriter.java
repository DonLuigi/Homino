package si.krulik.homino.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import lombok.Setter;
import lombok.ToString;
import si.krulik.homino.common.logger.CustomLogger;
import si.krulik.homino.common.validate.Validate;
import si.krulik.homino.devices.base.DeviceControlNode;
import si.krulik.homino.message.Message;
import si.krulik.homino.message.MultiMessage;

import static si.krulik.homino.Constants.*;
import static si.krulik.homino.runtime.Runtime.*;


@ToString (includeFieldNames = true, of = {"deviceControlNode", "doConnect", "requestTermination", "lastSocketConnectMillis"}) public class HominoNetworkSocketReaderWriter
{
    // constructor, reader and writer
    public HominoNetworkSocketReaderWriter (final DeviceControlNode deviceControlNode, final Socket socket, IHominoNetworkSocketReaderWriterCallback callback)
    {
        logger = CustomLogger.getLogger ("HOMINO_SOCKET_RW[" + deviceControlNode.getNetworkAddress () + "]");
        this.deviceControlNode = deviceControlNode;
        this.socket = socket;
        this.callback = callback;


        doConnect = (socket == null);
        logger.fine ("Set up ", this);


        logger.fine ("Starting reader thread");
        new Thread (new Runnable ()
        {
            public void run ()
            {
                logger.fine ("Read thread started");
                boolean forceConnect = false;
                while (!requestTermination)
                {
                    try
                    {
                        // connectInternal
                        connectInternal (forceConnect, READER);
                        forceConnect = false;


                        // read
                        MultiMessage receivedMultiMessage = readInternal ();
                        logger.fine ("Received: ", receivedMultiMessage);


                        // process and filter network messages
                        for (Iterator<Message> iterator = receivedMultiMessage.getMessages ().iterator (); iterator.hasNext (); )
                        {
                            Message message = iterator.next ();
                            if (!message.getDeviceId ().equalsIgnoreCase (NETWORK))
                            {
                                continue;
                            }

                            String[] parts = message.getParts ();


                            // CONNECT
                            if (parts.length == 3 && parts[1].equals (CONNECT))
                            {
                                if (deviceControlNode == null)
                                {
                                    String deviceControlNodeId = parts[2];
                                    logger.fine ("DeviceControlNodeId ", deviceControlNodeId);

                                    HominoNetworkSocketReaderWriter.this.deviceControlNode = devices.getDeviceControlNodesById ().get (deviceControlNodeId);
                                    Validate.notNull (HominoNetworkSocketReaderWriter.this.deviceControlNode, "Unknown deviceControlNodeId ", deviceControlNodeId);

                                    HominoNetworkSocketReaderWriter.this.callback.registerHominoNetworkSocketReaderWriter (HominoNetworkSocketReaderWriter.this.deviceControlNode, HominoNetworkSocketReaderWriter.this);
                                    logger.fine ("Connection identified as ", deviceControlNodeId);
                                }

                                iterator.remove ();
                            }


                            // DISCONNECT
                            if (parts.length == 2 && parts[1].equals (DISCONNECT))
                            {
                                logger.fine ("Received ", DISCONNECT);
                                requestTermination = true;
                                iterator.remove ();
                            }
                        }


                        // callback
                        Validate.notNull (deviceControlNode, "Missing device control node connection id on ", deviceControlNode);
                        HominoNetworkSocketReaderWriter.this.callback.receive (receivedMultiMessage);
                    }
                    catch (Exception e)
                    {
                        logger.severe (e, "Connect and/or read exception");

                        close ();

                        if (doConnect)
                        {
                            forceConnect = true;
                            if (!requestTermination)
                            {
                                HominoNetworkSocketReaderWriter.this.callback.error (HominoNetworkSocketReaderWriter.this.deviceControlNode, e, "Exception during connectInternal/read");
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
        }).start ();


        logger.fine ("Starting writer thread");
        new Thread (new Runnable ()
        {
            public void run ()
            {
                logger.fine ("Write thread started");
                boolean forceConnect = false;
                while (!requestTermination)
                {
                    try
                    {
                        MultiMessage multiMessage = writerMessageQueue.poll (1000, TimeUnit.MILLISECONDS);
                        if (multiMessage == null)
                        {
                            continue;
                        }

                        connectInternal (forceConnect, "WRITER");
                        writeInternal (multiMessage);
                    }
                    catch (Exception e)
                    {
                        logger.severe (e, "Writer exception");

                        forceConnect = true;
                        HominoNetworkSocketReaderWriter.this.callback.error (HominoNetworkSocketReaderWriter.this.deviceControlNode, e, "Exception during write");
                    }
                }
                logger.info ("Write thread terminated");
            }
        }).start ();
    }


    public void write (MultiMessage multiMessage)
    {
        try
        {
            writerMessageQueue.put (multiMessage);
        }
        catch (InterruptedException e)
        {
            logger.severe (e, "Write queue overflow");
            HominoNetworkSocketReaderWriter.this.callback.error (HominoNetworkSocketReaderWriter.this.deviceControlNode, e, "Write queue overflow");
        }
    }


    synchronized public void close ()
    {
        logger.fine ("Closing socket");
        try
        {
            if (socket != null)
            {
                socket.close ();
            }
        }
        catch (Exception e)
        {
            logger.severe (e, "Socket closed with exception");
        }
        socket = null;
    }


    synchronized private void connectInternal (boolean force, String threadName) throws IOException, InterruptedException
    {
        if (!doConnect)
        {
            socketDataInputStream = new DataInputStream (socket.getInputStream ());
            socketDataOutputStream = new DataOutputStream (socket.getOutputStream ());
        }

        if (socket == null)
        {
            long nowMillis = new Date ().getTime ();
            if (nowMillis - lastSocketConnectMillis < 1000)
            {
                Thread.sleep (1000); // prevent cpu load on caller
                logger.fine ("Too soon for reconnect on thread ", threadName);
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
            socket = new Socket (deviceControlNode.getNetworkHost (), deviceControlNode.getNetworkPort ());
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
            socket = new Socket (deviceControlNode.getNetworkHost (), deviceControlNode.getNetworkPort ());
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
    }


    private MultiMessage readInternal () throws IOException
    {
        // magic cookie
        logger.fine ("Reading...");
        byte[] receivedMagicCookie = new byte[messageMagicCookie.length];
        socketDataInputStream.readFully (receivedMagicCookie);

        for (int i = 0; i < receivedMagicCookie.length; i++)
        {
            if (receivedMagicCookie[i] != messageMagicCookie[i])
            {
                throw new IOException ("Invalid message cookie from " + deviceControlNode.getId ());
            }
        }
        logger.fine ("Magic ok");


        byte[] sizeRaw = new byte[2];
        socketDataInputStream.readFully (sizeRaw);
        int size = (((int) sizeRaw[0] & 0xFF) << 8) + (sizeRaw[1] & 0xFF);
        logger.fine (String.format ("Payload size %d (0x%02x 0x%02x)", size, sizeRaw[0], sizeRaw[1]));

        byte[] payload = new byte[size];
        socketDataInputStream.readFully (payload);

        String payloadAsString = new String (payload);
        logger.fine ("Payload content '", payloadAsString, "'");

        return (new MultiMessage (HominoNetworkSocketReaderWriter.this.deviceControlNode, payloadAsString));
    }


    private void writeInternal (MultiMessage multiMessage) throws IOException
    {
        String contentsAsString = multiMessage.getContentsAsString ();
        logger.fine ("Write to ", deviceControlNode.getNetworkAddress (), ": ", contentsAsString);

        byte[] buffer = composeRawMessage (contentsAsString);


        //logger.fine ("Buffer length ", buffer.length);
        socketDataOutputStream.write (buffer);

        // flush
        //logger.fine ("Flushing");
        socketDataOutputStream.flush ();
        //logger.fine ("Flushed");
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


    private BlockingQueue<MultiMessage> writerMessageQueue = new ArrayBlockingQueue (5);
    private DeviceControlNode deviceControlNode;
    private boolean doConnect;
    private Socket socket;
    private DataInputStream socketDataInputStream;
    private DataOutputStream socketDataOutputStream;
    private CustomLogger logger;
    private IHominoNetworkSocketReaderWriterCallback callback;
    private long lastSocketConnectMillis = 0;


    private final byte[] messageMagicCookie = {'M', 'G', 'C', 'K'};
}