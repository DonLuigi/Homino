package si.krulik.homino.configuration.device.common;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import si.krulik.homino.common.logger.CustomLogger;
import si.krulik.homino.common.validate.Validate;
import si.krulik.homino.configuration.Configuration;
import si.krulik.homino.configuration.device.ShellDevice;
import si.krulik.homino.message.IMultiMessageHandler;
import si.krulik.homino.message.Message;
import si.krulik.homino.message.MultiMessage;


public class Devices implements IMultiMessageHandler
{
    public Devices (DeviceControlNode[] deviceControlNodes)
    {
        for (DeviceControlNode deviceControlNode : deviceControlNodes)
        {
            addDeviceControlNode (deviceControlNode);
        }
    }


    @Getter Map<String, Device> devicesById = new HashMap ();


    @Getter Map<String, DeviceControlNode> deviceControlNodesById = new HashMap ();




    @Getter List<ShellDevice> shellDevices = new ArrayList ();


    public void addDeviceControlNode (DeviceControlNode deviceControlNode)
    {
        deviceControlNodesById.put (deviceControlNode.getId (), deviceControlNode);
        logger.info ("Added device control node ", deviceControlNode);
    }


    public DeviceControlNode getDeviceControlNode (String id)
    {
        return (deviceControlNodesById.get (id));
    }


    public void addDevice (Device device, Configuration configuration)
    {
        device.setConfiguration (configuration);
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


    public Device getDevice (String id)
    {
        return (devicesById.get (id));
    }


    public Collection<Device> getDevices ()
    {
        return (devicesById.values ());
    }


    public void handleMultiMessage (MultiMessage multiMessage, Configuration configuration)
    {
        long now = System.currentTimeMillis ();

        for (Message message : multiMessage.getMessages ())
        {
            Device device = devicesById.get (message.getDeviceId ());
            Validate.notNull (device, "Device ", message.getDeviceId (), " not found");

            device.handleMessage (message);
        }
    }


    static final private CustomLogger logger = CustomLogger.getLogger ("DEVICES");
    static final private String CSV_DELIMITER = ",";

}