package si.krulik.homino.configuration.device.common;


import lombok.Getter;
import lombok.ToString;


@ToString (includeFieldNames = true) public class DeviceControlNode
{
    public DeviceControlNode (String id, String ip)
    {
        this.id = id;
        this.ip = ip;
    }


    @Getter private String id;


    @Getter private String ip;
}
