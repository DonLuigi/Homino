package si.krulik.homino.customization;

import java.util.logging.Logger;

import si.krulik.homino.common.validate.Validate;
import si.krulik.homino.configuration.Configuration;
import si.krulik.homino.configuration.IMessageHandler;
import si.krulik.homino.configuration.Plate;
import si.krulik.homino.configuration.devices.Device;
import si.krulik.homino.configuration.plates.TimedRelayPlate;
import si.krulik.homino.configuration.plates.WindowLouvreShutterPlate;
import si.krulik.homino.configuration.plates.WindowRollingShutterPlate;

public class ControlNodesMessageHandler implements IMessageHandler
{
    public void handle (String source, String message, Configuration configuration)
    {
        // prologue
        logger.info ("Handling communication message from " + source + ": " + message);
        long now = System.currentTimeMillis ();

        // split message into parts
        String[] parts = message.split (Customization.messageDelimiter);
        for (String part : parts)
        {
            // split part into fields
            String[] fields = part.split (Customization.fieldDelimiter);
            Validate.isTrue (fields.length > 0, "Empty message received");

            String deviceId = fields[0];
            Device device = configuration.devicesById.get (fields[0]);
            Validate.notNull (device, "Device ", deviceId, " not found");

            // find plate
            Plate plate = configuration.platesById.get (deviceId);
            logger.info ("Looked up plate with eventName " + deviceId + ": " + plate);

            // window rolling shutter
            if (plate != null)
            {
                if (plate instanceof WindowRollingShutterPlate && fields.length == 4 && fields[1].equalsIgnoreCase ("STATUS"))
                {
                    logger.info ("Window rolling shutter");
                    WindowRollingShutterPlate windowRollingShutterPlate = (WindowRollingShutterPlate) plate;
                    windowRollingShutterPlate.device.motionDirection = Device.MotionDirection.valueOf (fields[2]);
                    windowRollingShutterPlate.device.verticalPercent = Integer.parseInt (fields[3]);
                }

                // window louvre shutter
                if (plate instanceof WindowLouvreShutterPlate && fields.length == 5 && fields[1].equalsIgnoreCase ("STATUS"))
                {
                    logger.info ("Window louvre shutter");
                    WindowLouvreShutterPlate windowLouvreShutterPlate = (WindowLouvreShutterPlate) plate;
                    windowLouvreShutterPlate.device.motionDirection = Device.MotionDirection.valueOf (fields[2]);
                    windowLouvreShutterPlate.device.verticalPercent = Integer.parseInt (fields[3]);
                    windowLouvreShutterPlate.device.rotationPercent = Integer.parseInt (fields[4]);
                }

                // timed relay
                if (plate instanceof TimedRelayPlate && fields.length == 4 && fields[1].equalsIgnoreCase ("STATUS"))
                {
                    TimedRelayPlate timedRelayPlate = (TimedRelayPlate) plate;
                    timedRelayPlate.device.percent = Integer.parseInt (fields[2]);
                    timedRelayPlate.device.locked = fields[3].equals ("1");
                    logger.info ("Timed relay: " + timedRelayPlate.device.percent);
                }

                plate.refresh ();
                device.lastRefreshInMillis = now;
                logger.info ("Refreshed device: " + device.id);
            }
        }
    }


    private final static Logger logger = Logger.getLogger (ControlNodesMessageHandler.class.getName ());
}
