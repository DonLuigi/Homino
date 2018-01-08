package si.krulik.homino.plates.fromdevices;

import android.view.View;
import android.widget.TextView;

import lombok.ToString;
import si.krulik.homino.R;
import si.krulik.homino.common.validate.Validate;
import si.krulik.homino.devices.PhotoResistorDevice;
import si.krulik.homino.devices.base.Device;
import si.krulik.homino.message.Message;
import si.krulik.homino.message.MultiMessage;
import si.krulik.homino.plates.base.IPlateActionHandler;
import si.krulik.homino.plates.base.Plate;


@ToString (includeFieldNames = true, callSuper = true) public class PhotoResistorPlate extends Plate
{
    public PhotoResistorPlate (String foregroundColor, String backgroundColor, String buttonBackgroundColor, String title, int layoutId, PhotoResistorDevice device, IPlateActionHandler messageHandler)
    {
        super (device.getId (), title, foregroundColor, backgroundColor, buttonBackgroundColor, layoutId, messageHandler);
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