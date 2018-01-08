package si.krulik.homino.network;

import si.krulik.homino.devices.base.DeviceControlNode;
import si.krulik.homino.message.MultiMessage;


public interface IHominoNetworkSocketReaderWriterCallback
{
    void registerHominoNetworkSocketReaderWriter (DeviceControlNode deviceControlNode, HominoNetworkSocketReaderWriter hominoNetworkSocketReaderWriter);


    void receive (MultiMessage multiMessage);


    void error (DeviceControlNode deviceControlNode, Exception e, String message);
}
