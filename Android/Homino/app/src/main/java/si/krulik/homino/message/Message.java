package si.krulik.homino.message;

import java.io.Serializable;

import lombok.Getter;
import si.krulik.homino.devices.base.DeviceControlNode;


public class Message implements Serializable
{
    @Getter private DeviceControlNode deviceControlNode;


    @Getter private String deviceId;


    @Getter private String[] parts;


    public Message (DeviceControlNode deviceControlNode, String messageAsString)
    {
        this.deviceControlNode = deviceControlNode;
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
        string += messageDelimiter;
        return (string);
    }


    private final static String fieldDelimiter = ",";
    private final static String messageDelimiter = ";";
}