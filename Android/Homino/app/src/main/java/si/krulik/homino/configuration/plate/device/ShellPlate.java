package si.krulik.homino.configuration.plate.device;

import android.view.View;
import android.widget.TextView;

import lombok.ToString;
import si.krulik.homino.R;
import si.krulik.homino.configuration.Configuration;
import si.krulik.homino.configuration.device.ShellDevice;
import si.krulik.homino.configuration.device.common.Device;
import si.krulik.homino.configuration.plate.common.IPlateActionHandler;
import si.krulik.homino.configuration.plate.common.Plate;
import si.krulik.homino.configuration.plate.common.PlatePage;


@ToString (includeFieldNames = true, callSuper = true) public class ShellPlate extends Plate
{
    public ShellPlate (String foregroundColor, String backgroundColor, String buttonBackgroundColor, String title, int layoutId, Configuration configuration, ShellDevice device, IPlateActionHandler messageHandler)
    {
        super (device.getId (), title, foregroundColor, backgroundColor, buttonBackgroundColor, layoutId, configuration, messageHandler);
        this.device = device;
    }


    @Override public void refresh ()
    {
        for (View view : getViewByPlatePageId ().values ())
        {
            long seconds = device.getUptimeSeconds () % 60;
            long minutes = (device.getUptimeSeconds () / 60) % 60;
            long hours = (device.getUptimeSeconds () / 60 / 60) % 24;
            long days = (device.getUptimeSeconds () / 60 / 60 / 24);


            ((TextView) view.findViewById (R.id.titleTextView)).setText (title);
            ((TextView) view.findViewById (R.id.upTime)).setText (String.format ("%d:%02dH:%02dM:%02dS", days, hours, minutes, seconds));
        }
    }


    @Override public Device getDevice ()
    {
        return (device);
    }


    public ShellDevice getShellDevice ()
    {
        return (device);
    }


    private ShellDevice device;
}