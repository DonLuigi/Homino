package si.krulik.homino.message;

import lombok.Getter;


public class Message
{
    @Getter private String peerId;


    @Getter private String[] parts;


    @Getter private String deviceId;


    public Message (String peerId, String messageAsString)
    {
        this.peerId = peerId;
        parts = messageAsString.split (fieldDelimiter);
        deviceId = (parts.length > 0 ? parts[0] : null);
    }


    public Message (String peerId, String... part)
    {
        this.peerId = peerId;
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