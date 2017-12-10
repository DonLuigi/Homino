package si.krulik.homino.configuration.plate.device;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import lombok.ToString;
import si.krulik.homino.R;
import si.krulik.homino.configuration.device.TimedRelayDevice;
import si.krulik.homino.configuration.device.common.Device;
import si.krulik.homino.configuration.plate.common.IPlateActionHandler;
import si.krulik.homino.configuration.plate.common.Plate;


@ToString (includeFieldNames = true, callSuper = true) public class TimedRelayPlate extends Plate
{
    public TimedRelayPlate (String foregroundColor, String backgroundColor, String buttonBackgroundColor, String title, Integer imageResource, boolean lockable, TimedRelayDevice device, IPlateActionHandler messageHandler)
    {
        super (device.getId (), title, foregroundColor, backgroundColor, buttonBackgroundColor, messageHandler);
        this.imageResource = imageResource;
        this.lockable = lockable;
        this.device = device;
    }


    public void refresh ()
    {
        if (view == null)
        {
            return;
        }

        // title
        ((TextView) view.findViewById (R.id.titleTextView)).setText (title);

        // plate image
        if (imageResource != null)
        {
            ((ImageView) view.findViewById (R.id.plateIcon)).setImageResource (imageResource);
        }

        // progress bar
        ProgressBar progressBar = (ProgressBar) view.findViewById (R.id.progressBar);
        if (device.getPercent () < 0)
        {
            progressBar.setVisibility (View.INVISIBLE);
        }
        else
        {
            progressBar.setVisibility (View.VISIBLE);
            progressBar.setProgress (device.getPercent ());
        }


        // start stop button
        ((ImageButton) view.findViewById (R.id.startStopButton)).setImageResource (device.getPercent () < 0 ? R.drawable.ic_play_arrow : R.drawable.ic_stop);


        // lock unlock
        ImageButton lockUnlockButton = (ImageButton) view.findViewById (R.id.stopButton);
        if (!lockable)
        {
            lockUnlockButton.setVisibility (View.INVISIBLE);
        }
        else
        {
            lockUnlockButton.setImageResource (device.isLocked () ? R.drawable.ic_action_lock_open : R.drawable.ic_action_lock_closed);
            view.setAlpha (device.isLocked () ? 0.3f : 1f);
        }
    }


    @Override public Device getDevice ()
    {
        return (device);
    }


    public TimedRelayDevice getTimedRelayDevice ()
    {
        return (device);
    }


    private Integer imageResource;
    private boolean lockable;
    private TimedRelayDevice device;
    private final static PorterDuffColorFilter greyFilter = new PorterDuffColorFilter (Color.GRAY, PorterDuff.Mode.MULTIPLY);
}
