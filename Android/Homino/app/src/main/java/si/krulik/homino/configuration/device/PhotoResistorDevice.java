package si.krulik.homino.configuration.device;

import java.util.Date;

import lombok.Getter;
import lombok.ToString;
import si.krulik.homino.configuration.device.common.Device;
import si.krulik.homino.message.Message;


@ToString (includeFieldNames = true, callSuper = true) public class PhotoResistorDevice extends Device
{
    public PhotoResistorDevice (String id, String controlNodeName, long logEveryMillis)
    {
        super (id, controlNodeName, logEveryMillis);
    }


    public void handleMessage (Message message)
    {
        if (message.getParts ().length == 3 && message.getParts ()[1].equalsIgnoreCase ("STATUS"))
        {
            lightValue = Integer.parseInt (message.getParts ()[2]);
            statusUpdated = true;
        }
    }


    @Override public String log (Date now)
    {
        return (log (now, lightValue));
    }


    @Getter private int lightValue = -1;
}
