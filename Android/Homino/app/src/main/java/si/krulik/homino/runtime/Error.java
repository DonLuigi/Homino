package si.krulik.homino.runtime;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import si.krulik.homino.devices.base.DeviceControlNode;


@AllArgsConstructor
@Data public class Error implements Serializable
{
    final private String message;

    final private Date date;

    final private String deviceControlNodeId;


    public Error (String message, DeviceControlNode deviceControlNode)
    {
        this.message = message;
        this.deviceControlNodeId = deviceControlNode.getId ();
        this.date = null;
    }
}
