package si.krulik.homino.configuration.device;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.ToString;
import si.krulik.homino.common.logger.CustomLogger;
import si.krulik.homino.configuration.device.common.Device;
import si.krulik.homino.message.Message;


@ToString (includeFieldNames = true, callSuper = true) public class AuthorizerDevice extends Device
{
    public AuthorizerDevice (String id, String controlNodeName, long logEveryMillis)
    {
        super (id, controlNodeName, logEveryMillis);
    }


    public void handleMessage (Message message)
    {
        logger.fine ("Handling message ", message);
        lastSeen = new Date ();

        if (message.getParts ().length > 2 && message.getParts ()[1].equalsIgnoreCase ("AUTH"))
        {
            for (int i = 2; i < message.getParts ().length; i += 2)
            {
                String authorization = message.getParts ()[i];
                Long validityMillis = Long.parseLong (message.getParts ()[i + 1]);
                logger.fine ("Added authorization: ", authorization, ", validity ", validityMillis, " millis");

                authorizationsAndValidity.put (authorization, new Date (lastSeen.getTime () + validityMillis));
            }
        }
        else
        {
            configuration.error (controlNodeNameId, "Unknown message: " + message.toString ());
        }
    }


    @Override public String log (Date now)
    {
        return (log (now, "TODO"));
    }


    @Getter private Date lastSeen;


    Map<String, Date> authorizationsAndValidity = new HashMap ();


    static final CustomLogger logger = CustomLogger.getLogger ("AUTHORIZATION_DEVICE");
}