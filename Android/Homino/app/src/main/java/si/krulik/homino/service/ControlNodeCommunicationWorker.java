package si.krulik.homino.service;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Logger;

import si.krulik.homino.common.validate.Validate;

public class ControlNodeCommunicationWorker implements Runnable
{
    public ControlNodeCommunicationWorker (String peerAddressAndPort, Context context)
    {
        String[] split = peerAddressAndPort.split (":");
        Validate.isTrue (split.length == 2, "Invalid internet address format: ", peerAddressAndPort);

        this.peerAddressAndPort = peerAddressAndPort;
        this.context = context;

        peerAddress = split[0];
        peerPort = Integer.parseInt (split[1]);
    }


    public void run ()
    {
        try
        {
            logger.info ("Read thread started");
            boolean force = false;
            while (!requestReadThreadTermination)
            {
                String received = null;
                if (connect (force))
                {
                    received = read ();
                    force = (received == null);
                }
                else
                {
                    Thread.sleep (100);
                }

                if (received != null && !received.isEmpty ())
                {
                    ControlNodeCommunicationRequest request = new ControlNodeCommunicationRequest (peerAddressAndPort, received);

                    logger.info ("Broadcasting " + request);
                    Intent localIntent = new Intent (ControlNodeCommunicationRequest.communicationRequestIntentAction).putExtra (ControlNodeCommunicationRequest.class.getSimpleName (), request);
                    LocalBroadcastManager.getInstance (context).sendBroadcast (localIntent);
                }
            }
            logger.info ("Read thread terminated");
        }
        catch (InterruptedException e1)
        {
        }
    }


    synchronized public void write (String payload) throws IOException
    {
        logger.info ("writing to socket: begin");
        try
        {
            writeEx (payload);
        }
        catch (IOException e)
        {
            logger.severe ("IOException in write");
            close ();
        }
        logger.info ("writing to socket: end");
    }


    synchronized public void close ()
    {
        logger.info ("closing socket");
        try
        {
            socket.close ();
        }
        catch (IOException e)
        {
            logger.severe ("IOException in close");
        }
        socket = null;
        logger.info ("done");
    }


    private static final Logger logger = Logger.getLogger (ControlNodeCommunicationWorker.class.getName ());
    private Context context;
    private String peerAddress;
    private int peerPort;
    private String peerAddressAndPort;

    private Socket socket;
    boolean requestReadThreadTermination = false;

    DataInputStream socketDataInputStream;
    DataOutputStream socketDataOutputStream;
    private InetAddress socketInetAddress;

    private static final byte[] messageMagicCookie = {'M', 'G', 'C', 'K'};


    public boolean connect (boolean force)
    {
        synchronized (logger) // due to dumb bug - synchronized (this) doesn't work!!!
        {
            String id = peerAddressAndPort + "/" + this + "/" + Thread.currentThread ().getId ();
            try
            {
                if (socket == null)
                {
                    logger.info (id + ": connecting");
                    socket = new Socket (peerAddress, peerPort);
                    logger.info (id + ": connected");
                    socketDataInputStream = new DataInputStream (socket.getInputStream ());
                    socketDataOutputStream = new DataOutputStream (socket.getOutputStream ());
                }
                else if (force)
                {
                    socket.close ();
                    logger.info (id + ": closed");
                    logger.info (id + ": re-connecting");
                    socket = new Socket (peerAddress, peerPort);
                    logger.info (id + ": connected");
                    socketDataInputStream = new DataInputStream (socket.getInputStream ());
                    socketDataOutputStream = new DataOutputStream (socket.getOutputStream ());
                }

                try
                {
                    Thread.sleep (100); // TODO: add settle to configuration
                }
                catch (Throwable t)
                {
                }

                return (true);
            }
            catch (IOException e)
            {
                logger.info (id + ": failed to connect");
                socket = null;
                return (false);
            }
        }

    }


    private String read ()
    {
        // magic cookie
        try
        {
            logger.info (peerAddressAndPort + ": reading");
            byte[] receivedMagicCookie = new byte[messageMagicCookie.length];
            socketDataInputStream.readFully (receivedMagicCookie);

            logger.info (peerAddressAndPort + ": read magic");
            for (int i = 0; i < receivedMagicCookie.length; i++)
            {
                if (receivedMagicCookie[i] != messageMagicCookie[i])
                {
                    throw new IOException (peerAddressAndPort + ": invalid message cookie");
                }
            }


            byte[] sizeRaw = new byte[2];
            socketDataInputStream.readFully (sizeRaw);
            int size = (((int) sizeRaw[0] & 0xFF) << 8) + (sizeRaw[1] & 0xFF);
            logger.info (peerAddressAndPort + ": payload size raw: " + (int) sizeRaw[0] + ", " + (int) sizeRaw[1] + ", size: " + size);

            byte[] payload = new byte[size];
            socketDataInputStream.readFully (payload);

            String payloadAsString = new String (payload);
            logger.info (peerAddressAndPort + ": " + payloadAsString);
            return (payloadAsString);
        }
        catch (IOException e)
        {
            return (null);
        }
    }


    private void writeEx (String payload) throws IOException
    {
        logger.info ("writeEx begin: " + payload);
        // connect if not connected
        connect (false);

        // log
        logger.info (peerAddressAndPort + ": writing: " + payload);
        byte[] buffer = new byte[messageMagicCookie.length + 2 + payload.length ()];


        // magic cookie
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

        logger.info (peerAddressAndPort + ": buffer length: " + buffer.length);
        socketDataOutputStream.write (buffer);

        // flush
        logger.info (peerAddressAndPort + ": flushing");
        socketDataOutputStream.flush ();
        logger.info (peerAddressAndPort + ": flushed");
    }
}
