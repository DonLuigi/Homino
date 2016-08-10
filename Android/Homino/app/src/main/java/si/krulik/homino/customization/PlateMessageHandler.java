package si.krulik.homino.customization;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import si.krulik.homino.common.validate.Validate;
import si.krulik.homino.configuration.Configuration;
import si.krulik.homino.configuration.IMessageHandler;
import si.krulik.homino.configuration.Plate;
import si.krulik.homino.configuration.devices.Device;
import si.krulik.homino.configuration.devices.WindowLouvreShutterDevice;
import si.krulik.homino.configuration.devices.WindowRollingShutterDevice;
import si.krulik.homino.configuration.plates.ActionPlate;
import si.krulik.homino.configuration.plates.TimedRelayPlate;
import si.krulik.homino.configuration.plates.WindowLouvreShutterPlate;
import si.krulik.homino.configuration.plates.WindowRollingShutterPlate;
import si.krulik.homino.support.ControlNodeMultiMessage;

public class PlateMessageHandler implements IMessageHandler
{
    public void handle (String source, String message, Configuration configuration)
    {
        // prologue
        logger.info ("Handling plate message " + message);
        String[] fields = message.split (Customization.fieldDelimiter);
        String plateId = fields[0];
        Plate plate = configuration.platesById.get (plateId);
        Validate.notNull (plate, "Plate ", plateId, " not found");
        ControlNodeMultiMessage controlNodeMultiMessage = new ControlNodeMultiMessage (configuration);


        // dispatch
        List<Device> windowShutterDevices = new ArrayList<Device> ();


        // action plate
        if (plate instanceof ActionPlate)
        {
            // multiple devices
            if (plateId.equals (Customization.windowShuttersKitchen))
            {
                windowShutterDevices.add (configuration.devicesById.get (Customization.windowRollingShutterKitchenE));
                windowShutterDevices.add (configuration.devicesById.get (Customization.windowRollingShutterKitchenS));
            }

            if (plateId.equals (Customization.windowShuttersLivingRoom))
            {
                windowShutterDevices.add (configuration.devicesById.get (Customization.windowLouvreShutterLivingRoomS));
                windowShutterDevices.add (configuration.devicesById.get (Customization.windowRollingShutterLivingRoomW));
            }

            if (plateId.equals (Customization.windowShuttersLowerFloor))
            {
                windowShutterDevices.add (configuration.devicesById.get (Customization.windowLouvreShutterLivingRoomS));
                windowShutterDevices.add (configuration.devicesById.get (Customization.windowShuttersKitchen));
                windowShutterDevices.add (configuration.devicesById.get (Customization.windowRollingShutterKitchenE));
                windowShutterDevices.add (configuration.devicesById.get (Customization.windowRollingShutterKitchenS));
                windowShutterDevices.add (configuration.devicesById.get (Customization.windowLouvreShutterDiningRoomS));
                windowShutterDevices.add (configuration.devicesById.get (Customization.windowShuttersLivingRoom));
                windowShutterDevices.add (configuration.devicesById.get (Customization.windowLouvreShutterLivingRoomS));
                windowShutterDevices.add (configuration.devicesById.get (Customization.windowRollingShutterLivingRoomW));
                windowShutterDevices.add (configuration.devicesById.get (Customization.windowRollingShutterStairs));
                windowShutterDevices.add (configuration.devicesById.get (Customization.windowRollingShutterBathroom0));
            }
        }


        // window shutter plate
        if (plate instanceof WindowLouvreShutterPlate || plate instanceof WindowRollingShutterPlate)
        {
            Device device = configuration.devicesById.get (plateId);
            Validate.notNull (device, "Device ", plateId, " not found");
            windowShutterDevices.add (device);
        }


        // handle shutters
        if (!windowShutterDevices.isEmpty ())
        {
            for (Device windowShutterDevice : windowShutterDevices)
            {
                if (windowShutterDevice == null)
                {
                    continue;
                }

                String command = null;


                // STOP button
                if (fields[1].equals (Customization.stop))
                {
                    command = windowShutterDevice.id + ",STOP;";
                }


                // UP button
                if (fields[1].equals (Customization.up))
                {
                    command = windowShutterDevice.id + ",MV,0;";
                }


                // DOWN button
                if (fields[1].equals (Customization.down))
                {
                    command = windowShutterDevice.id + ",MV,100;";
                }


                // HALF button
                if (fields[1].equals (Customization.half))
                {
                    command = windowShutterDevice.id + ",MV,50;";
                }


                // QUARTER button
                if (fields[1].equals (Customization.quarter))
                {
                    command = windowShutterDevice.id + ",MV,75;";
                }


                // GRID button
                if (fields[1].equals (Customization.grid) && windowShutterDevice instanceof WindowRollingShutterDevice)
                {
                    WindowRollingShutterDevice windowRollingShutterDeviceScopeDevice = (WindowRollingShutterDevice) windowShutterDevice;
                    command = windowShutterDevice.id + ",MV," + windowRollingShutterDeviceScopeDevice.gridPercentage + ";";
                }


                // ROTATE UP button
                if (fields[1].equals (Customization.rotateUp) && windowShutterDevice instanceof WindowLouvreShutterDevice && plate instanceof WindowLouvreShutterPlate)
                {
                    WindowLouvreShutterDevice windowLouvreShutterDevice = (WindowLouvreShutterDevice) windowShutterDevice;
                    WindowLouvreShutterPlate windowLouvreShutterPlate = (WindowLouvreShutterPlate) plate;

                    int newRotationPercent = windowLouvreShutterDevice.rotationPercent - windowLouvreShutterDevice.rotationStepPercent;
                    newRotationPercent = (newRotationPercent < 0 ? 0 : newRotationPercent);

                    command = windowShutterDevice.id + ",MV,-1," + newRotationPercent + ";";
                }


                // ROTATE DOWN button
                if (fields[1].equals (Customization.rotateDown) && windowShutterDevice instanceof WindowLouvreShutterDevice && plate instanceof WindowLouvreShutterPlate)
                {
                    WindowLouvreShutterDevice windowLouvreShutterDevice = (WindowLouvreShutterDevice) windowShutterDevice;
                    WindowLouvreShutterPlate windowLouvreShutterPlate = (WindowLouvreShutterPlate) plate;

                    int newRotationPercent = windowLouvreShutterDevice.rotationPercent + windowLouvreShutterDevice.rotationStepPercent;
                    newRotationPercent = (newRotationPercent > 100 ? 100 : newRotationPercent);

                    command = windowShutterDevice.id + ",MV,-1," + newRotationPercent + ";";
                }


                if (command != null)
                {
                    controlNodeMultiMessage.addMessage (windowShutterDevice, command);
                }
            }
        }
        else
        {
            logger.info ("No window shutters found");
        }


        // timed relays
        if (plate instanceof TimedRelayPlate)
        {
            TimedRelayPlate timedRelayPlate = (TimedRelayPlate) plate;

            if (fields[1].equals (Customization.start))
            {
                controlNodeMultiMessage.addMessage (timedRelayPlate.device, timedRelayPlate.device.id + ",START," + timedRelayPlate.device.durationMillis);
            }
            if (fields[1].equals (Customization.stop))
            {
                controlNodeMultiMessage.addMessage (timedRelayPlate.device, timedRelayPlate.device.id + ",STOP");
            }
        }

        controlNodeMultiMessage.flushMessages ();
    }


    private final static Logger logger = Logger.getLogger (PlateMessageHandler.class.getName ());
}
