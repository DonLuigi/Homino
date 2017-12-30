package si.krulik.homino.configuration.plate.device;

import android.view.View;
import android.widget.TextView;

import java.util.List;

import lombok.ToString;
import si.krulik.homino.R;
import si.krulik.homino.common.validate.Validate;
import si.krulik.homino.configuration.Configuration;
import si.krulik.homino.configuration.device.PhotoResistorDevice;
import si.krulik.homino.configuration.device.common.Device;
import si.krulik.homino.configuration.plate.common.IPlateActionHandler;
import si.krulik.homino.configuration.plate.common.Plate;
import si.krulik.homino.configuration.plate.common.PlatePage;
import si.krulik.homino.message.Message;
import si.krulik.homino.message.MultiMessage;


@ToString (includeFieldNames = true, callSuper = true) public class PhotoResistorPlate extends Plate
{
    public PhotoResistorPlate (String foregroundColor, String backgroundColor, String buttonBackgroundColor, String title, int layoutId, Configuration configuration, PhotoResistorDevice device, IPlateActionHandler messageHandler)
    {
        super (device.getId (), title, foregroundColor, backgroundColor, buttonBackgroundColor, layoutId, configuration, messageHandler);
        this.device = device;
    }


    @Override public void refresh ()
    {
        for (View view : getViewByPlatePageId ().values ())
        {
            if (device.getLightValue () >= 0)
            {
                ((TextView) view.findViewById (R.id.titleTextView)).setText (title);
                ((TextView) view.findViewById (R.id.light)).setText (Integer.toString (device.getLightValue ()));
            }
        }
    }


    public void handleMessage (Message sourceMessage, MultiMessage targetMultiMessage)
    {
        Validate.fail ("Cannot send message to ", PhotoResistorPlate.class.getSimpleName ());
    }


    @Override public Device getDevice ()
    {
        return (device);
    }


    public PhotoResistorDevice getPhotoResistorDevice ()
    {
        return (device);
    }


    private PhotoResistorDevice device;
}