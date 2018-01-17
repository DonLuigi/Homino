package si.krulik.homino.message;

import java.io.Serializable;

import lombok.Getter;
import lombok.ToString;
import si.krulik.homino.devices.base.DeviceControlNode;


@ToString (includeFieldNames = true, of = {"deviceControlNodeId", "deviceId", "parts"}) public class Message implements Serializable
{
    @Getter private DeviceControlNode deviceControlNode;


    @Getter private String deviceId;


    @Getter private String[] parts;


    public Message (DeviceControlNode deviceControlNode, String messageAsString)
    {
        this.deviceControlNode = deviceControlNode;
        deviceControlNodeId = deviceControlNode.getId ();
        parts = messageAsString.split (fieldDelimiter);
        deviceId = (parts.length > 0 ? parts[0] : null);
    }


    public Message (DeviceControlNode deviceControlNode, String... parts)
    {
        this.deviceControlNode = deviceControlNode;
        this.parts = parts;
        deviceId = (this.parts.length > 0 ? this.parts[0] : null);
    }


    public String getContentsAsString ()
    {
        String string = "";

        for (String part : parts)
        {
            string += (!string.isEmpty () ? fieldDelimiter : "");
            string += part;
        }
        return (string);
    }


    private String deviceControlNodeId; // for toString only


    private final static String fieldDelimiter = ",";
}