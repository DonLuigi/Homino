package si.krulik.homino.devices;

import java.util.Date;

import lombok.Getter;
import lombok.ToString;
import si.krulik.homino.common.logger.CustomLogger;
import si.krulik.homino.common.validate.Validate;
import si.krulik.homino.runtime.Runtime;
import si.krulik.homino.devices.base.Device;
import si.krulik.homino.devices.base.DeviceControlNode;
import si.krulik.homino.message.Message;
import si.krulik.homino.runtime.Error;


@ToString (includeFieldNames = true, callSuper = true) public class ShellDevice extends Device
{
    public ShellDevice (String id, DeviceControlNode deviceControlNode, long logEveryMillis, long pingEveryMillis)
    {
        super (id, deviceControlNode, logEveryMillis);
        this.pingEveryMillis = pingEveryMillis;
    }


    public void handleMessage (Message message)
    {
        logger.fine ("Handling message ", message);

        if (message.getParts ().length == 3 && message.getParts ()[1].equalsIgnoreCase ("STATUS"))
        {
            uptimeSeconds = Long.parseLong (message.getParts ()[2]);
            statusUpdated = true;
        }
        else
        {
            Runtime.logError (new Error ( "Unknown message: " + message, deviceControlNode));
        }
    }


    public Message handleAction (Runtime.Action action)
    {
        switch (action)
        {
            case Status:
                return (new Message (deviceControlNode, id, "STATUS"));
            default:
                Validate.fail ("Unknown device command ", action);
                return (null);
        }
    }


    @Override public String log (Date now)
    {
        return (log (now, uptimeSeconds));
    }


    public Message ping (Date now)
    {
        long pingDeltaMillis = now.getTime () - lastPingMillis;
        if (pingEveryMillis > 0 &&  pingDeltaMillis > pingEveryMillis)
        {
            lastPingMillis = now.getTime ();
            logger.fine ("Ping ", getId (), " after ", pingDeltaMillis, " millis (every ", pingEveryMillis, " millis)");
            return (handleAction (Runtime.Action.Status));
        }
        return (null);
    }


    @Getter private long uptimeSeconds;


    private long pingEveryMillis;


    protected long lastPingMillis = 0;


    static final CustomLogger logger = CustomLogger.getLogger ("SHELL_DEVICE");
}