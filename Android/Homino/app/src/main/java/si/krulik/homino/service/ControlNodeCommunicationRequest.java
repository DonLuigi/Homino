package si.krulik.homino.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.io.Serializable;

public class ControlNodeCommunicationRequest implements Serializable
{
    static public final String communicationRequestIntentAction = ControlNodeCommunicationRequest.class.getName () + "Broadcast";


    public ControlNodeCommunicationRequest (String controlNodeNetoworkAddress, String message)
    {
        this.controlNodeNetworkAddress = controlNodeNetoworkAddress;
        this.message = message;
    }


    public String controlNodeNetworkAddress;
    public String message;


    public String toString ()
    {
        return (ControlNodeCommunicationRequest.class.getSimpleName () + " [controlNodeNetworkAddress=" + controlNodeNetworkAddress + ",message=" + message + "]");
    }
}
