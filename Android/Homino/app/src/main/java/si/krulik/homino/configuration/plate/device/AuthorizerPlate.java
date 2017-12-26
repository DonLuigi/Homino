package si.krulik.homino.configuration.plate.device;

import android.view.View;
import android.widget.TextView;

import lombok.ToString;
import si.krulik.homino.R;
import si.krulik.homino.configuration.device.AuthorizerDevice;
import si.krulik.homino.configuration.device.common.Device;
import si.krulik.homino.configuration.plate.common.IPlateActionHandler;
import si.krulik.homino.configuration.plate.common.Plate;


@ToString (includeFieldNames = true, callSuper = true) public class AuthorizerPlate extends Plate
{
    public AuthorizerPlate (String foregroundColor, String backgroundColor, String buttonBackgroundColor, String title, AuthorizerDevice device, IPlateActionHandler messageHandler)
    {
        super (device.getId (), title, foregroundColor, backgroundColor, buttonBackgroundColor, messageHandler);
        this.device = device;
    }


    @Override public void refresh ()
    {
        for (View view : getViewByPlatePageId ().values ())
        {
            ((TextView) view.findViewById (R.id.titleTextView)).setText (title);

            if (device.getLastSeen () != null)
            {
                long lastSeenSecondsAgo = (System.currentTimeMillis () - device.getLastSeen ().getTime ()) / 1000;
                long seconds = lastSeenSecondsAgo % 60;
                long minutes = (lastSeenSecondsAgo / 60) % 60;
                long hours = (lastSeenSecondsAgo / 60 / 60) % 24;
                long days = (lastSeenSecondsAgo / 60 / 60 / 24);
                ((TextView) view.findViewById (R.id.lastSeen)).setText (String.format ("%dD:%02dH:%02dM:%02dS", days, hours, minutes, seconds));
            }
            else
            {
                ((TextView) view.findViewById (R.id.lastSeen)).setText ("Never");
            }
        }
    }


    @Override public Device getDevice ()
    {
        return (device);
    }


    public AuthorizerDevice getAuthorizerDevice ()
    {
        return (device);
    }


    private AuthorizerDevice device;
}