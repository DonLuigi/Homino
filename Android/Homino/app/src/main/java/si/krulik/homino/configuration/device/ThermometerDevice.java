package si.krulik.homino.configuration.device;

import java.util.Date;

import lombok.Getter;
import lombok.ToString;
import si.krulik.homino.common.logger.CustomLogger;
import si.krulik.homino.configuration.device.common.Device;
import si.krulik.homino.message.Message;


@ToString (includeFieldNames = true, callSuper = true) public class ThermometerDevice extends Device
{
    public ThermometerDevice (String id, String controlNodeName, long logEveryMillis)
    {
        super (id, controlNodeName, logEveryMillis);
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
            configuration.error (controlNodeNameId, "Unknown message: " + message.toString ());
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
