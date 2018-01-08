package si.krulik.homino.plates.fromdevices;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import lombok.ToString;
import si.krulik.homino.R;
import si.krulik.homino.common.validate.Validate;
import si.krulik.homino.runtime.Runtime;
import si.krulik.homino.devices.WindowShutterDevice;
import si.krulik.homino.devices.base.Device;
import si.krulik.homino.plates.ViewOnClickListener;
import si.krulik.homino.plates.base.IPlateActionHandler;
import si.krulik.homino.plates.base.Plate;


@ToString (includeFieldNames = true, callSuper = true) public class WindowShutterPlate extends Plate
{
    public WindowShutterPlate (String foregroundColor, String backgroundColor, String buttonBackgroundColor, String title, int layoutId, WindowShutterDevice device, IPlateActionHandler messageHandler)
    {
        super (device.getId (), title, foregroundColor, backgroundColor, buttonBackgroundColor, layoutId, messageHandler);
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


    @Override public View inflate (String pageId, LayoutInflater layoutInflater)
    {
        View view = super.inflate (pageId, layoutInflater);

        // button events
        if (layoutId == R.layout.window_rolling_shutter_layout)
        {
            ((ImageButton) view.findViewById (R.id.upButton)).setOnClickListener (new ViewOnClickListener (Runtime.Action.Up, this));
            ((ImageButton) view.findViewById (R.id.downButton)).setOnClickListener (new ViewOnClickListener (Runtime.Action.Down, this));
            ((ImageButton) view.findViewById (R.id.stopButton)).setOnClickListener (new ViewOnClickListener (Runtime.Action.Stop, this));
            ((ImageButton) view.findViewById (R.id.gridButton)).setOnClickListener (new ViewOnClickListener (Runtime.Action.Grid, this));
            ((ImageButton) view.findViewById (R.id.halfButton)).setOnClickListener (new ViewOnClickListener (Runtime.Action.Half, this));
            //                    ((ImageButton) view.findViewById (R.id.quarterButton)).setOnClickListener (new ViewOnClickListener (Action.Quarter, this, configuration));
        }
        else if (layoutId == R.layout.window_venetian_blind_layout)
        {
            ((ImageButton) view.findViewById (R.id.upButton)).setOnClickListener (new ViewOnClickListener (Runtime.Action.Up, this));
            ((ImageButton) view.findViewById (R.id.downButton)).setOnClickListener (new ViewOnClickListener (Runtime.Action.Down, this));
            ((ImageButton) view.findViewById (R.id.stopButton)).setOnClickListener (new ViewOnClickListener (Runtime.Action.Stop, this));
            ((ImageButton) view.findViewById (R.id.rotateUpButton)).setOnClickListener (new ViewOnClickListener (Runtime.Action.RotateUp, this));
            ((ImageButton) view.findViewById (R.id.rotateDownButton)).setOnClickListener (new ViewOnClickListener (Runtime.Action.RotateDown, this));
            //                    ((ImageButton) view.findViewById (R.id.rotateMiddleButton)).setOnClickListener (new ViewOnClickListener (Action.RotateMidddle, plate, configuration));
        }
        else
        {
            Validate.fail ("Unknown window shutter layout id ", layoutId);
        }

        return (view);
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
