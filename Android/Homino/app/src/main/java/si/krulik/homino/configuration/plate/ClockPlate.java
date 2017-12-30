package si.krulik.homino.configuration.plate;


import android.view.View;

import lombok.ToString;
import si.krulik.homino.configuration.Configuration;
import si.krulik.homino.configuration.device.ShellDevice;
import si.krulik.homino.configuration.plate.common.IPlateActionHandler;
import si.krulik.homino.configuration.plate.common.Plate;


@ToString (includeFieldNames = true, callSuper = true) public class ClockPlate extends Plate
{
    public ClockPlate (String id, String foregroundColor, String backgroundColor, int layoutId, Configuration configuration)
    {
        super (id, null, foregroundColor, backgroundColor, null, layoutId, configuration, null);
    }


    @Override public void refresh ()
    {
    }
}
