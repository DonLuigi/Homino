package si.krulik.homino.support;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import si.krulik.homino.configuration.Configuration;
import si.krulik.homino.configuration.devices.Device;

public class ControlNodeMultiMessage
{
    public ControlNodeMultiMessage (Configuration configuration)
    {
        this.configuration = configuration;
    }


    public void addMessage (Device device, String message)
    {
        String messages = commandsPerControlNode.get (device.controlNodeName);
        if (messages != null)
        {
            messages += (messages.endsWith (";") ? message : ";" + message);
        }
        else
        {
            messages = message;
        }
        commandsPerControlNode.put (device.controlNodeName, messages);
    }


    public void flushMessages ()
    {
        for (Map.Entry<String, String> entries : commandsPerControlNode.entrySet ())
        {
            logger.info ("Flushing to " + entries.getKey () + ": " + entries.getValue ());
            configuration.sendRequestToControlNode (entries.getKey (), entries.getValue ());
        }

        commandsPerControlNode.clear ();
    }


    private final static Logger logger = Logger.getLogger (ControlNodeMultiMessage.class.getName ());
    private Map<String, String> commandsPerControlNode = new HashMap<String, String> ();
    private Configuration configuration;
}