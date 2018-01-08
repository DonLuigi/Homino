package si.krulik.homino.plates.fromdevices;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import lombok.ToString;
import si.krulik.homino.R;
import si.krulik.homino.runtime.Runtime;
import si.krulik.homino.devices.TimedRelayDevice;
import si.krulik.homino.devices.base.Device;
import si.krulik.homino.plates.ViewOnClickListener;
import si.krulik.homino.plates.base.IPlateActionHandler;
import si.krulik.homino.plates.base.Plate;


@ToString (includeFieldNames = true, callSuper = true) public class TimedRelayPlate extends Plate
{
    public TimedRelayPlate (String foregroundColor, String backgroundColor, String buttonBackgroundColor, String title, int layoutId, Integer imageResource, Integer workingAnimation, boolean lockable, TimedRelayDevice device, IPlateActionHandler messageHandler)
    {
        super (device.getId (), title, foregroundColor, backgroundColor, buttonBackgroundColor, layoutId, messageHandler);
        this.imageResource = imageResource;
        this.workingAnimation = workingAnimation;
        this.lockable = lockable;
        this.device = device;
    }


    @Override public void refresh ()
    {
        for (View view : getViewByPlatePageId ().values ())
        {
            // title
            ((TextView) view.findViewById (R.id.titleTextView)).setText (title);

            // plate image

            // progress bar

            SeekBar seekBar = (SeekBar) view.findViewById (R.id.seekBar);
            if (device.getPercent () < 0)
            {
                // plate image
                if (imageResource != null)
                {
                    ((ImageView) view.findViewById (R.id.plateIcon)).setImageResource (imageResource);
                }


                // seek bar
                seekBar.setVisibility (View.INVISIBLE);
            }
            else
            {
                // seek bar visibility
                boolean seekBarVisible = (seekBar.getVisibility () == View.VISIBLE);


                // animation
                ImageView imageView = (ImageView) view.findViewById (R.id.plateIcon);
                if (workingAnimation != null)
                {
                    if (!seekBarVisible)
                    {
                        imageView.setImageResource (workingAnimation);
                        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable ();
                        animationDrawable.start ();
                    }
                }
                else if (imageResource != null)
                {
                    ((ImageView) view.findViewById (R.id.plateIcon)).setImageResource (imageResource);
                }

                // seek bar
                seekBar.setVisibility (View.VISIBLE);
                seekBar.setProgress (device.getPercent () / 10);

            }


            // start stop button
            ((ImageButton) view.findViewById (R.id.startStopButton)).setImageResource (device.getPercent () < 0 ? R.drawable.ic_play_arrow : R.drawable.ic_stop);


            // lock unlock
            ImageButton lockUnlockButton = (ImageButton) view.findViewById (R.id.lockUnlockButton);
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
    }



    @Override public View inflate (String pageId, LayoutInflater layoutInflater)
    {
        View view = super.inflate (pageId, layoutInflater);

        // button events
        ((ImageButton) view.findViewById (R.id.startStopButton)).setOnClickListener (new ViewOnClickListener (Runtime.Action.StartStop, this));
        ((ImageButton) view.findViewById (R.id.lockUnlockButton)).setOnClickListener (new ViewOnClickListener (Runtime.Action.LockUnlock, this));

        return (view);
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
    private Integer workingAnimation;
    private boolean lockable;
    private TimedRelayDevice device;
    private final static PorterDuffColorFilter greyFilter = new PorterDuffColorFilter (Color.GRAY, PorterDuff.Mode.MULTIPLY);
}
