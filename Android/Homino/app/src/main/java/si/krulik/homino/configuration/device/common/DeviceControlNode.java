package si.krulik.homino.configuration.device.common;


import lombok.Getter;
import lombok.ToString;


@ToString (includeFieldNames = true) public class DeviceControlNode
{
    public DeviceControlNode (String id, String networkAddress)
    {
        this.id = id;
        this.networkAddress = networkAddress;
    }


    @Getter private String id;


    @Getter private String networkAddress;
}
