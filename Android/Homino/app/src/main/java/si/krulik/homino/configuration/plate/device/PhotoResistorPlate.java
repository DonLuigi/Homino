package si.krulik.homino.configuration.plate.device;

import android.widget.TextView;

import lombok.ToString;
import si.krulik.homino.R;
import si.krulik.homino.common.validate.Validate;
import si.krulik.homino.configuration.device.PhotoResistorDevice;
import si.krulik.homino.configuration.device.common.Device;
import si.krulik.homino.configuration.plate.common.IPlateActionHandler;
import si.krulik.homino.configuration.plate.common.Plate;
import si.krulik.homino.message.Message;
import si.krulik.homino.message.MultiMessage;


@ToString (includeFieldNames = true, callSuper = true) public class PhotoResistorPlate extends Plate
{
    public PhotoResistorPlate (String foregroundColor, String backgroundColor, String buttonBackgroundColor, String title, PhotoResistorDevice device, IPlateActionHandler messageHandler)
    {
        super (device.getId (), title, foregroundColor, backgroundColor, buttonBackgroundColor, messageHandler);
        this.device = device;
    }


    @Override public void refresh ()
    {
        if (view != null && device.getLightValue () >= 0)
        {
            ((TextView) view.findViewById (R.id.titleTextView)).setText (title);
            ((TextView) view.findViewById (R.id.light)).setText (Integer.toString (device.getLightValue ()));
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