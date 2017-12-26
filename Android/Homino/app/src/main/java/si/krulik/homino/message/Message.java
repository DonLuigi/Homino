package si.krulik.homino.message;

import lombok.Getter;


public class Message
{
    @Getter private String deviceControlNodeId;


    @Getter private String deviceId;


    @Getter private String[] parts;


    public Message (String deviceControlNodeId, String messageAsString)
    {
        this.deviceControlNodeId = deviceControlNodeId;
        parts = messageAsString.split (fieldDelimiter);
        deviceId = (parts.length > 0 ? parts[0] : null);
    }


    public Message (String deviceControlNodeId, String... part)
    {
        this.deviceControlNodeId = deviceControlNodeId;
        parts = part;
        deviceId = (parts.length > 0 ? parts[0] : null);
    }


    @Override public String toString ()
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