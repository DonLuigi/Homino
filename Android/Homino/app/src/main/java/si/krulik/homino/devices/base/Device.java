package si.krulik.homino.devices.base;


import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import si.krulik.homino.runtime.Runtime;
import si.krulik.homino.message.Message;


@ToString (includeFieldNames = true) abstract public class Device
{
    public Device (String id, DeviceControlNode deviceControlNode, long logEveryMillis)
    {
        this.id = id;
        this.deviceControlNode = deviceControlNode;
        this.logEveryMillis = logEveryMillis;
    }


    public Message handleAction (Runtime.Action action)
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


    @Getter protected DeviceControlNode deviceControlNode;




    protected boolean statusUpdated = false;


    protected long logEveryMillis = 0;


    protected long lastLogMillis = 0;


    static final private SimpleDateFormat logDateFormat = new SimpleDateFormat ("MM/dd/yyyy HH:mm:ss");
}