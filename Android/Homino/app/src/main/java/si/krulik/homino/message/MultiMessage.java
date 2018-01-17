package si.krulik.homino.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.ToString;
import si.krulik.homino.devices.base.DeviceControlNode;

@ToString (includeFieldNames = true, of = {"messages"})
public class MultiMessage implements Serializable
{
    @Getter private List<Message> messages = new ArrayList ();


    public Map<DeviceControlNode, MultiMessage> getMessagesPerDeviceControlNodeId ()
    {
        Map<DeviceControlNode, MultiMessage> messagesPerDeviceControlNodeId = new HashMap ();

        for (Message message : this.messages)
        {
            MultiMessage multiMessage = messagesPerDeviceControlNodeId.get (message.getDeviceControlNode ());

            if (multiMessage == null)
            {
                messagesPerDeviceControlNodeId.put (message.getDeviceControlNode (), multiMessage = new MultiMessage ());
            }
            multiMessage.add (message);
        }

        return (messagesPerDeviceControlNodeId);
    }


    public MultiMessage (DeviceControlNode deviceControlNode, String messagesAsString)
    {
        String[] messagesAsStrings = messagesAsString.split (messageDelimiter);
        for (String messageAsString : messagesAsStrings)
        {
            messages.add (new Message (deviceControlNode, messageAsString));
        }
    }


    public MultiMessage ()
    {
    }


    public void add (Message message)
    {
        if (message != null)
        {
            messages.add (message);
        }
    }


    public String getContentsAsString ()
    {
        String contents = "";
        for (Message message : this.messages)
        {
            contents += message.getContentsAsString () + messageDelimiter;
        }

        return (contents);
    }



    private final static String messageDelimiter = ";";
}