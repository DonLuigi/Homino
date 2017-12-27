package si.krulik.homino.configuration.plate.device;

import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import lombok.ToString;
import si.krulik.homino.R;
import si.krulik.homino.common.validate.Validate;
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
        for (View view : getViewByPlatePageId ().values ())
        {
            // title
            ((TextView) view.findViewById (R.id.titleTextView)).setText (title);


            // vertical
            ImageView plateIconImageView = (ImageView) view.findViewById (R.id.plateIcon);
            int verticalImageIndex = device.getVerticalPercent () / windowShutterImageIds.length;
            verticalImageIndex = verticalImageIndex < windowShutterImageIds.length ? verticalImageIndex : windowShutterImageIds.length - 1;
            plateIconImageView.setImageResource (windowShutterImageIds[verticalImageIndex]);


            // rotation
            int rotationPercent = device.getRotationPercent ();
            if (rotationPercent >= 0)
            {
                SeekBar seekBarView = (SeekBar) view.findViewById (R.id.seekBar);
                Validate.notNull (seekBarView, "Missing seekBar in view for ", WindowShutterPlate.class, " ", getId ());
                seekBarView.setProgress (rotationPercent / 10);
            }
        }
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
    private static int[] windowShutterImageIds = new int[] {
        R.drawable.window_shutter_0, R.drawable.window_shutter_1, R.drawable.window_shutter_2, R.drawable.window_shutter_3, R.drawable.window_shutter_4, R.drawable.window_shutter_5, R.drawable.window_shutter_6, R.drawable.window_shutter_7,
        R.drawable.window_shutter_8, R.drawable.window_shutter_9
    };
}
