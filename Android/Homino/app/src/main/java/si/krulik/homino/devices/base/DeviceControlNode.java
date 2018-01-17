package si.krulik.homino.devices.base;


import java.io.Serializable;

import lombok.Getter;
import lombok.ToString;
import si.krulik.homino.common.validate.Validate;

import static si.krulik.homino.Constants.*;


@ToString (includeFieldNames = true, of = {"id", "networkAddress", "ssid"}) public class DeviceControlNode implements Serializable
{
    public DeviceControlNode (String id, String networkAddress, String ssid)
    {
        this.id = id;
        this.networkAddress = networkAddress;
        this.ssid = ssid;

        String[] split = networkAddress.split (":");
        Validate.isTrue (split.length == 2, "Invalid network address ", networkAddress);

        networkHost = split[0];
        networkPort = Integer.parseInt (split[1]);

        incommingConnection = (networkHost.equals (LOCALHOST));
    }


    @Getter private String id;


    @Getter private String networkAddress;


    @Getter private String networkHost;


    @Getter private int networkPort;


    // reachable via ssid
    @Getter private String ssid;


    @Getter private boolean incommingConnection;
}
