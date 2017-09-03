package si.krulik.homino.configuration.plates;

import android.widget.TextView;

import si.krulik.homino.R;
import si.krulik.homino.configuration.devices.WindowLouvreShutterDevice;

public class WindowLouvreShutterPlate extends WindowShutterPlate
{
    public WindowLouvreShutterPlate (String id, ActionPlatePosition position, String foregroundColor, String backgroundColor, String buttonBackgroundColor, String text, String upButtonEventName,
        String downButtonEventName, String stopButtonEventName, String halfButtonEventName, String rotateUpButtonEventName, String rotateDownButtonEventName, WindowLouvreShutterDevice device)
    {
        super (id, position, foregroundColor, backgroundColor, buttonBackgroundColor, text, upButtonEventName, downButtonEventName, stopButtonEventName);
        this.halfButtonEventName = halfButtonEventName;
        this.rotateUpButtonEventName = rotateUpButtonEventName;
        this.rotateDownButtonEventName = rotateDownButtonEventName;
        this.device = device;

    }


    @Override
    public void refresh ()
    {
        ((TextView) view.findViewById (R.id.titleTextView)).setText (text);
        ((TextView) view.findViewById (R.id.percentTextView)).setText (device.verticalPercent >= 0 ? Integer.toString (device.verticalPercent) + "%" : "N/A");
        ((TextView) view.findViewById (R.id.rotationPercentView)).setText (device.rotationPercent >= 0 ? Integer.toString (device.rotationPercent) + "%" : "N/A");
    }


    public String halfButtonEventName;
    public String rotateUpButtonEventName;
    public String rotateDownButtonEventName;
    public WindowLouvreShutterDevice device;
}
