package si.krulik.homino.configuration.device;

import java.util.Date;

import lombok.Getter;
import lombok.ToString;
import si.krulik.homino.common.logger.CustomLogger;
import si.krulik.homino.common.validate.Validate;
import si.krulik.homino.configuration.command.Action;
import si.krulik.homino.configuration.device.common.Device;
import si.krulik.homino.message.Message;


@ToString (includeFieldNames = true, callSuper = true) public class NetworkAuthenticationDevice extends Device
{
    public NetworkAuthenticationDevice (String id, String controlNodeName, long logEveryMillis, long pingEveryMillis)
    {
        super (id, controlNodeName, logEveryMillis);
        this.pingEveryMillis = pingEveryMillis;
    }


    public void handleMessage (Message message)
    {
        logger.fine ("Handling message ", message);

        if (message.getParts ().length == 3 && message.getParts ()[1].equalsIgnoreCase ("STATUS"))
        {
            reachable = Boolean.parseBoolean (message.getParts ()[2]);
            statusUpdated = true;
        }
        else
        {
            configuration.error (controlNodeNameId, "Unknown message: " + message.toString ());
        }
    }


    public Message handleAction (Action action)
    {
        switch (action)
        {
            case Status:
                return (new Message (controlNodeNameId, id, "PING"));
            default:
                Validate.fail ("Unknown device command ", action);
                return (null);
        }
    }


    @Override public String log (Date now)
    {
        return (log (now, reachable));
    }


    public Message ping (Date now)
    {
        if (pingEveryMillis > 0 && now.getTime () - lastPingMillis > pingEveryMillis)
        {
            lastPingMillis = now.getTime ();
            return (handleAction (Action.Status));
        }
        return (null);
    }


    @Getter private boolean reachable = false;


    private long pingEveryMillis;


    protected long lastPingMillis = 0;


    static final CustomLogger logger = CustomLogger.getLogger ("SHELL_DEVICE");
}