package si.krulik.homino.configuration.plate.device;

import android.widget.TextView;

import lombok.ToString;
import si.krulik.homino.R;
import si.krulik.homino.configuration.device.WindowShutterDevice;
import si.krulik.homino.configuration.device.common.Device;
import si.krulik.homino.configuration.plate.common.IPlateActionHandler;
import si.krulik.homino.configuration.plate.common.Plate;


@ToString (includeFieldNames = true, callSuper = true) public class WindowShutterPlate extends Plate
{
    public WindowShutterPlate (String foregroundColor, String backgroundColor, String buttonBackgroundColor, String title, WindowShutterDevice device, IPlateActionHandler messageHandler)
    {
        super (device.getId (), title, foregroundColor, backgroundColor, buttonBackgroundColor, messageHandler);
        this.device = device;
    }


    @Override public void refresh ()
    {
        ((TextView) view.findViewById (R.id.titleTextView)).setText (title);
        ((TextView) view.findViewById (R.id.percentTextView)).setText (device.getVerticalPercent () >= 0 ? Integer.toString (device.getVerticalPercent ()) + "%" : "N/A");
        //((TextView) view.findViewById (R.id.rotationPercentView)).setText (device.getRotationPercent () >= 0 ? Integer.toString (device.getRotationPercent ()) + "%" : "N/A");
    }


    @Override public Device getDevice ()
    {
        return (device);
    }


    public WindowShutterDevice getWindowShutterDevice ()
    {
        return (device);
    }


    private WindowShutterDevice device;
}
