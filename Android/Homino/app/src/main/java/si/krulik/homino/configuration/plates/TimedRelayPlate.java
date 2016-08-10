package si.krulik.homino.configuration.plates;

import android.widget.TextView;

import si.krulik.homino.R;
import si.krulik.homino.configuration.Plate;
import si.krulik.homino.configuration.devices.TimedRelayDevice;

public class TimedRelayPlate extends Plate
{
    public TimedRelayPlate (String id, int x, int y, int dx, int dy, String foregroundColor, String backgroundColor, String buttonBackgroundColor, String text, String startButtonEventName, String
        stopButtonEventName, TimedRelayDevice device)
    {
        super (id, x, y, dx, dy, foregroundColor, backgroundColor, buttonBackgroundColor);
        this.startButtonEventName = startButtonEventName;
        this.stopButtonEventName = stopButtonEventName;
        this.device = device;
    }


    @Override
    public void refresh ()
    {
        ((TextView) view.findViewById (R.id.titleTextView)).setText (text);
        ((TextView) view.findViewById (R.id.percentTextView)).setText (device.percent >= 0 ? Integer.toString (device.percent) + "%" : "OFF");
    }


    public String text;
    public String startButtonEventName;
    public String stopButtonEventName;

    public TimedRelayDevice device;
}
