package si.krulik.homino.network;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import si.krulik.homino.common.validate.Validate;


@AllArgsConstructor @ToString (includeFieldNames = true) public class HominoNetworkMessage implements Serializable
{
    @Getter private String deviceControlNodeId;


    @Getter private String deviceControlNodeNetworkAddress;


    @Getter private String message;


    @Getter private boolean error;


    public String getDeviceControlNodeNetworkHost ()
    {
        String[] split = deviceControlNodeNetworkAddress.split (":");
        Validate.equals (2, split.length, "Invalid device control network address ", deviceControlNodeNetworkAddress);
        return (split[0]);
    }


    public int getDeviceControlNodeNetworkPort ()
    {
        String[] split = deviceControlNodeNetworkAddress.split (":");
        Validate.equals (2, split.length, "Invalid device control network address ", deviceControlNodeNetworkAddress);
        return (Integer.parseInt (split[1]));
    }


    static public final String communicationRequestIntentAction = HominoNetworkMessage.class.getName () + "Broadcast";
}
