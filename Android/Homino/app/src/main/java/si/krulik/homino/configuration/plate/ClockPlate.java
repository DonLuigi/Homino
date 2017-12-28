package si.krulik.homino.configuration.plate;


import android.view.View;

import lombok.ToString;
import si.krulik.homino.configuration.device.ShellDevice;
import si.krulik.homino.configuration.plate.common.IPlateActionHandler;
import si.krulik.homino.configuration.plate.common.Plate;


@ToString (includeFieldNames = true, callSuper = true) public class ClockPlate extends Plate
{
    public ClockPlate (String id, String foregroundColor, String backgroundColor)
    {
        super (id, null, foregroundColor, backgroundColor, null, null);
    }


    @Override public void refresh ()
    {
    }
}
