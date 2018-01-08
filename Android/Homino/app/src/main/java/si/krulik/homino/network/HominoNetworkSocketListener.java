package si.krulik.homino.network;


import java.net.ServerSocket;
import java.net.Socket;

import lombok.Setter;
import lombok.ToString;
import si.krulik.homino.common.logger.CustomLogger;


@ToString (includeFieldNames = true, of = {"port"}) public class HominoNetworkSocketListener
{
    public HominoNetworkSocketListener (final int port, final HominoNetworkService hominoNetworkService)
    {
        logger = CustomLogger.getLogger ("HOMINO_SOCKET_LISTENER[" + port + "]");
        this.port = port;
        this.hominoNetworkService = hominoNetworkService;


        // listen thread
        Thread controlNodeCommunicationReaderThread = new Thread (new Runnable ()
        {
            public void run ()
            {
                logger.fine ("Listening thread started");
                while (!requestTermination)
                {
                    try
                    {
                        if (serverSocket == null)
                        {
                            serverSocket = new ServerSocket (port);
                        }
                        Socket socket = serverSocket.accept ();
                        String peerIpPort = socket.getRemoteSocketAddress ().toString ();

                        // create new reader writer, that will put itself into hominoNetworkService.workersByDeviceControlNodeId as soon as it received indentification
                        new HominoNetworkSocketReaderWriter (null /* needs identification */, socket, hominoNetworkService.getCallback ());
                    }
                    catch (Exception e)
                    {
                        close ();

                        if (!requestTermination)
                        {
                            hominoNetworkService.getCallback ().error (null, e, "Exception on port " + port + " during listen");
                            try
                            {
                                Thread.sleep (500);
                            }
                            catch (InterruptedException e1)
                            {
                            }
                        }
                    }
                }
                logger.info ("Listening thread terminated");
            }
        });
        controlNodeCommunicationReaderThread.start ();
    }


    public void close ()
    {
        try
        {
            if (serverSocket != null)
            {
                serverSocket.close ();
            }
        }
        catch (Exception e)
        {
        }
        serverSocket = null;
    }


    @Setter private boolean requestTermination = false;


    private int port;
    private ServerSocket serverSocket;
    private HominoNetworkService hominoNetworkService;
    private CustomLogger logger;
}
