package si.krulik.homino.devices.base;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import si.krulik.homino.common.logger.CustomLogger;
import si.krulik.homino.common.validate.Validate;
import si.krulik.homino.devices.ShellDevice;
import si.krulik.homino.message.IMultiMessageHandler;
import si.krulik.homino.message.Message;
import si.krulik.homino.message.MultiMessage;


public class Devices implements IMultiMessageHandler
{
    // device control nodes
    @Getter Map<String, DeviceControlNode> deviceControlNodesById = new HashMap ();


    public DeviceControlNode getDeviceControlNode (String id)
    {
        return (deviceControlNodesById.get (id));
    }


    // devices

    @Getter Map<String, Device> devicesById = new HashMap ();


    public Collection<Device> getDevices ()
    {
        return (devicesById.values ());
    }


    public Device getDevice (String id)
    {
        return (devicesById.get (id));
    }


    // shell devices
    @Getter List<ShellDevice> shellDevices = new ArrayList ();


    // multi message
    public void handleMultiMessage (MultiMessage multiMessage, Runtime runtime)
    {
        long now = System.currentTimeMillis ();

        for (Message message : multiMessage.getMessages ())
        {
            Device device = devicesById.get (message.getDeviceId ());
            Validate.notNull (device, "Device ", message.getDeviceId (), " not found");

            device.handleMessage (message);
        }
    }


    // construct
    public Devices (Device... devices)
    {
        for (Device device : devices)
        {
            // add device control node
            deviceControlNodesById.put (device.getDeviceControlNode ().getId (), device.getDeviceControlNode ());
            logger.info ("Added device control node ", device.getDeviceControlNode ());


            // add device
            this.devicesById.put (device.getId (), device);
            if (device instanceof ShellDevice)
            {
                shellDevices.add ((ShellDevice) device);
                logger.fine ("Added shell device ", device);
            }
            else
            {
                logger.fine ("Added device ", device);
            }
        }
    }


    static final private CustomLogger logger = CustomLogger.getLogger ("DEVICES");
    static final private String CSV_DELIMITER = ",";

}