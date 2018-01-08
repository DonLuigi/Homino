package si.krulik.homino.devices;

import java.util.Date;

import lombok.Getter;
import lombok.ToString;
import si.krulik.homino.common.logger.CustomLogger;
import si.krulik.homino.devices.base.Device;
import si.krulik.homino.devices.base.DeviceControlNode;
import si.krulik.homino.message.Message;
import si.krulik.homino.runtime.Error;
import si.krulik.homino.runtime.Runtime;


@ToString (includeFieldNames = true, callSuper = true) public class ThermometerDevice extends Device
{
    public ThermometerDevice (String id, DeviceControlNode deviceControlNode, long logEveryMillis)
    {
        super (id, deviceControlNode, logEveryMillis);
    }


    public void handleMessage (Message message)
    {
        logger.fine ("Handling message ", message);

        if ((message.getParts ().length == 3 || message.getParts ().length == 4) && message.getParts ()[1].equalsIgnoreCase ("STATUS"))
        {
            temperature = Double.parseDouble (message.getParts ()[2]);

            if (message.getParts ().length == 4)
            {
                humidity = Double.parseDouble (message.getParts ()[3]);
            }

            statusUpdated = true;
        }
        else
        {
            Runtime.logError (new Error ("Unknown message: " + message, deviceControlNode));
        }
    }


    @Override public String log (Date now)
    {
        return (log (now, temperature, humidity));
    }


    @Getter private Double temperature = null;


    @Getter private Double humidity = null;


    static final CustomLogger logger = CustomLogger.getLogger ("THERMOMETER_DEVICE");
}
