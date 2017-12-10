package si.krulik.homino.configuration.device;

import lombok.Getter;
import lombok.ToString;
import si.krulik.homino.common.logger.CustomLogger;
import si.krulik.homino.common.validate.Validate;
import si.krulik.homino.configuration.command.Action;
import si.krulik.homino.configuration.device.common.Device;
import si.krulik.homino.message.Message;


@ToString (includeFieldNames = true) public class TimedRelayDevice extends Device
{
    public TimedRelayDevice (String id, String controlNodeName, long durationMillis)
    {
        super (id, controlNodeName, 0);
        this.durationMillis = durationMillis;
    }


    public void handleMessage (Message message)
    {
        if (message.getParts ().length == 4 && message.getParts ()[1].equalsIgnoreCase ("STATUS"))
        {
            percent = Integer.parseInt (message.getParts ()[2]);
            locked = message.getParts ()[3].equals ("1");
            statusUpdated = true;
        }
    }


    public Message handleAction (Action action)
    {
        logger.fine ("Handling action ",action);
        switch (action)
        {
            case Start:
            case StartStop:
            case Stop:
                if (action == Action.Start || (action == Action.StartStop && percent < 0))
                {
                    return (new Message (controlNodeNameId, id, "START", (durationMillis > 0 ? Long.toString (durationMillis) : "-1"), "FORCE"));
                }
                else
                {
                    return (new Message (controlNodeNameId, id, "STOP"));
                }

            case LockUnlock:
                return (new Message (controlNodeNameId, id, (locked ? "UNLOCK" : "LOCK")));

            default:
                Validate.fail ("Unknown device command ", action);
                return (null);
        }
    }


    @Getter private int percent = -1;


    @Getter private boolean locked = false;


    private long durationMillis;


    private static final CustomLogger logger = CustomLogger.getLogger ("TIMED_RELAY_DEV");
}
