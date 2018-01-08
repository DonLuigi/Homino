package si.krulik.homino.plates.fromdevices;

import android.view.View;
import android.widget.TextView;

import lombok.ToString;
import si.krulik.homino.R;
import si.krulik.homino.devices.ThermometerDevice;
import si.krulik.homino.devices.base.Device;
import si.krulik.homino.plates.base.IPlateActionHandler;
import si.krulik.homino.plates.base.Plate;


@ToString (includeFieldNames = true, callSuper = true) public class ThermometerPlate extends Plate
{
    public ThermometerPlate (String foregroundColor, String backgroundColor, String buttonBackgroundColor, String title, int layoutId, ThermometerDevice device, IPlateActionHandler messageHandler)
    {
        super (device.getId (), title, foregroundColor, backgroundColor, buttonBackgroundColor, layoutId, messageHandler);
        this.device = device;
    }


    @Override public void refresh ()
    {
        for (View view : getViewByPlatePageId ().values ())
        {
            TextView titleTextView = (TextView) view.findViewById (R.id.titleTextView);
            if (titleTextView != null && title != null)
            {
                titleTextView.setText (title);
            }

            ((TextView) view.findViewById (R.id.temperature)).setText (device.getTemperature () != null ? String.format ("%.01f°C", device.getTemperature ()) : "");
            ((TextView) view.findViewById (R.id.humidity)).setText (device.getHumidity () != null ? String.format ("%.0f%%", device.getHumidity ()) : "");
        }
    }


    @Override public Device getDevice ()
    {
        return (device);
    }


    public ThermometerDevice getThermometerDevice ()
    {
        return (device);
    }


    private ThermometerDevice device;
}