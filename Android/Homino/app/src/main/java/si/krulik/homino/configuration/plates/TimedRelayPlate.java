package si.krulik.homino.configuration.plates;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import si.krulik.homino.R;
import si.krulik.homino.configuration.Plate;
import si.krulik.homino.configuration.devices.TimedRelayDevice;

public class TimedRelayPlate extends Plate
{
    public TimedRelayPlate (String id, ActionPlatePosition position, String foregroundColor, String backgroundColor, String buttonBackgroundColor, String text, String startStopButtonEventName,
        String enableDisableButtonEventName, Integer imageResource, TimedRelayDevice device)
    {
        super (id, position, foregroundColor, backgroundColor, buttonBackgroundColor);
        this.startStopButtonEventName = startStopButtonEventName;
        this.enableDisableButtonEventName = enableDisableButtonEventName;
        this.imageResource = imageResource;
        this.device = device;
    }


    @Override
    public void refresh ()
    {
        // text
        ((TextView) view.findViewById (R.id.titleTextView)).setText (text);

        // plate image
        if (imageResource != null)
        {
            ((ImageView) view.findViewById (R.id.timedRelayIconView)).setImageResource (imageResource);
        }

        // progress bar
        ProgressBar progressBar = (ProgressBar) view.findViewById (R.id.progressBar);
        if (device.percent < 0)
        {
            progressBar.setVisibility (View.INVISIBLE);
        }
        else
        {
            progressBar.setVisibility (View.VISIBLE);
            progressBar.setProgress (device.percent);
        }


        // start stop button
        ((ImageButton) view.findViewById (R.id.startStopButton)).setImageResource (device.percent < 0 ? R.drawable.ic_play_arrow : R.drawable.ic_stop);


        // lock unlock
        ImageButton lockUnlockButton = (ImageButton) view.findViewById (R.id.lockUnlockButton);
        if (enableDisableButtonEventName == null)
        {
            lockUnlockButton.setVisibility (View.INVISIBLE);
        }
        else
        {
            lockUnlockButton.setImageResource (device.locked ? R.drawable.ic_action_lock_open : R.drawable.ic_action_lock_closed);
            view.setAlpha (device.locked ? 0.3f : 1f);
        }
    }


    public String text;
    public String startStopButtonEventName;
    public String enableDisableButtonEventName;
    public Integer imageResource;

    public TimedRelayDevice device;
    private final static PorterDuffColorFilter greyFilter = new PorterDuffColorFilter (Color.GRAY, PorterDuff.Mode.MULTIPLY);
}
