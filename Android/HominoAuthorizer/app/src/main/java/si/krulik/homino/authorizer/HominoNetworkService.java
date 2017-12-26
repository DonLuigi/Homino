package si.krulik.homino.authorizer;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import si.krulik.homino.authorizer.configuration.Configuration;
import si.krulik.homino.authorizer.configuration.ConfigurationItem;
import si.krulik.homino.common.logger.CustomLogger;


public class HominoNetworkService extends IntentService
{
    public HominoNetworkService ()
    {
        super ("BLAH");
    }


    public HominoNetworkService (String name)
    {
        super (name);
    }


    @Override protected void onHandleIntent (Intent intent)
    {
        // configuration
        Configuration configuration = new Configuration (this);


        // ssid
        WifiManager wifiManager = (WifiManager) getApplicationContext ().getSystemService (Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo ();
        String ssid = wifiInfo.getSSID ().replace ("\"", "");


        //
        for (ConfigurationItem configurationItem : configuration.getConfigurationItems ())
        {
            if (configurationItem.getSsid () == null || configurationItem.getSsid ().equals (ssid))
            {
                Socket socket = null;
                try
                {
                    socket = new Socket (configurationItem.getHostname (), configurationItem.getPort ());

                    DataInputStream socketDataInputStream = new DataInputStream (socket.getInputStream ());
                    DataOutputStream socketDataOutputStream = new DataOutputStream (socket.getOutputStream ());

                    socketDataOutputStream.write (composeRawMessage ("IDENT," + configuration.getDeviceControlNodeId () + ";"));

                    socketDataOutputStream.write (composeRawMessage ("LudvikPhoneAutorizer,AUTH," + configurationItem.getAuthorization () + "," + configuration.getValiditySec () + ";"));

                    socketDataOutputStream.flush ();
                }
                catch (Exception e)
                {
                    Toast.makeText (this, "ERROR " + e.getMessage (), Toast.LENGTH_LONG).show ();
                }
                finally
                {
                    if (socket != null)
                    {
                        try
                        {
                            socket.close ();
                        }
                        catch (Exception e)
                        {
                            Toast.makeText (this, "ERROR " + e.getMessage (), Toast.LENGTH_LONG).show ();
                        }
                    }
                }
            }
        }
    }


    private byte[] composeRawMessage (String messagePayload)
    {
        final byte[] messageMagicCookie = {'M', 'G', 'C', 'K'};
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


    private static final CustomLogger logger = CustomLogger.getLogger ("NETWORK_SERVICE");
}