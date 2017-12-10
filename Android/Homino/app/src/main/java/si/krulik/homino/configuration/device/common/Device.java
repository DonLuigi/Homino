package si.krulik.homino.configuration.device.common;


import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import si.krulik.homino.configuration.Configuration;
import si.krulik.homino.configuration.command.Action;
import si.krulik.homino.message.Message;


@ToString (includeFieldNames = true) abstract public class Device
{
    public Device (String id, String controlNodeId, long logEveryMillis)
    {
        this.id = id;
        this.controlNodeNameId = controlNodeId;
        this.configuration = configuration;
        this.logEveryMillis = logEveryMillis;
    }


    public Message handleAction (Action action)
    {
        return (null);
    }


    public String log (Date now)
    {
        return (null);
    }


    abstract public void handleMessage (Message message);


    protected String log (Date now, Object... objects)
    {
        long nowMillis = now.getTime ();
        if (logEveryMillis > 0 && nowMillis - lastLogMillis > logEveryMillis && objects.length > 0 && statusUpdated)
        {
            String logEntry = logDateFormat.format (now) + "," + id;

            for (Object object : objects)
            {
                logEntry += "," + object;
                lastLogMillis = nowMillis;
            }

            return (logEntry);
        }
        return (null);
    }


    @Getter protected String id;


    @Getter protected String controlNodeNameId;


    @Setter protected Configuration configuration;


    protected boolean statusUpdated = false;


    protected long logEveryMillis = 0;


    protected long lastLogMillis = 0;


    static final private SimpleDateFormat logDateFormat = new SimpleDateFormat ("MM/dd/yyyy HH:mm:ss");
}
