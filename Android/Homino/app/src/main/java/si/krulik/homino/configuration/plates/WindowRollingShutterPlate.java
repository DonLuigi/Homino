package si.krulik.homino.configuration.plates;

import android.widget.TextView;

import si.krulik.homino.R;
import si.krulik.homino.configuration.devices.WindowRollingShutterDevice;

public class WindowRollingShutterPlate extends WindowShutterPlate
{
    public WindowRollingShutterPlate (String id, int x, int y, int dx, int dy, String foregroundColor, String backgroundColor, String buttonBackgroundColor, String text, String upButtonEventName,
        String downButtonEventName, String stopButtonEventName, String gridButtonEventName, String halfButtonEventName, String quarterButtonEventName, WindowRollingShutterDevice device)
    {
        super (id, x, y, dx, dy, foregroundColor, backgroundColor, buttonBackgroundColor, text, upButtonEventName, downButtonEventName, stopButtonEventName);
        this.gridButtonEventName = gridButtonEventName;
        this.halfButtonEventName = halfButtonEventName;
        this.quarterButtonEventName = quarterButtonEventName;
        this.device = device;
    }


    @Override
    public void refresh ()
    {
        ((TextView) view.findViewById (R.id.titleTextView)).setText (text);
        ((TextView) view.findViewById (R.id.percentTextView)).setText (device.verticalPercent >= 0 ? Integer.toString (device.verticalPercent) + "%" : "N/A");
    }


    public String gridButtonEventName;
    public String halfButtonEventName;
    public String quarterButtonEventName;
    public WindowRollingShutterDevice device;
}