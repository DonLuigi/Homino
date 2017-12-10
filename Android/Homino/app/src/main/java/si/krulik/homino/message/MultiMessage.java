package si.krulik.homino.message;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;


public class MultiMessage
{
    @Getter private List<Message> messages = new ArrayList ();


    public MultiMessage (String peerId, String messagesAsString)
    {
        String[] messagesAsStrings = messagesAsString.split (messageDelimiter);
        for (String messageAsString : messagesAsStrings)
        {
            messages.add (new Message (peerId, messageAsString));
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


    private final static String messageDelimiter = ";";
}
