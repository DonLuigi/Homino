package si.krulik.homino.configuration.plates;

import si.krulik.homino.configuration.Plate;
import si.krulik.homino.configuration.devices.Device;

public abstract class WindowShutterPlate extends Plate
{
    public WindowShutterPlate (String id, ActionPlatePosition position, String foregroundColor, String backgroundColor, String buttonBackgroundColor, String text, String upButtonEventName,
        String downButtonEventName, String stopButtonEventName)
    {
        super (id, position, foregroundColor, backgroundColor, buttonBackgroundColor);
        this.text = text;
        this.upButtonEventName = upButtonEventName;
        this.downButtonEventName = downButtonEventName;
        this.stopButtonEventName = stopButtonEventName;
    }


    public String text;
    public String upButtonEventName;
    public String downButtonEventName;
    public String stopButtonEventName;
}
