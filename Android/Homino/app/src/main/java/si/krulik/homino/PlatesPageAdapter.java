package si.krulik.homino;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import si.krulik.homino.common.logger.CustomLogger;
import si.krulik.homino.configuration.Configuration;
import si.krulik.homino.configuration.command.Action;
import si.krulik.homino.configuration.device.AuthorizerDevice;
import si.krulik.homino.configuration.device.WindowShutterDevice;
import si.krulik.homino.configuration.plate.ActionPlate;
import si.krulik.homino.configuration.plate.ActionPlateRow;
import si.krulik.homino.configuration.plate.ActionPlateRowButton;
import si.krulik.homino.configuration.plate.common.Plate;
import si.krulik.homino.configuration.plate.common.PlatePage;
import si.krulik.homino.configuration.plate.common.PlatePosition;
import si.krulik.homino.configuration.plate.device.AuthorizerPlate;
import si.krulik.homino.configuration.plate.device.PhotoResistorPlate;
import si.krulik.homino.configuration.plate.device.ShellPlate;
import si.krulik.homino.configuration.plate.device.ThermometerPlate;
import si.krulik.homino.configuration.plate.device.TimedRelayPlate;
import si.krulik.homino.configuration.plate.device.WindowShutterPlate;
import si.krulik.homino.message.MultiMessage;


public class PlatesPageAdapter extends PagerAdapter
{
    public PlatesPageAdapter (Configuration configuration, Context context)
    {
        this.configuration = configuration;
        this.context = context;
    }


    @Override public Object instantiateItem (ViewGroup container, int position)
    {
        logger.fine ("Inflating page ", position);
        // layout inflater
        LayoutInflater layoutInflater = LayoutInflater.from (container.getContext ());


        // page definition
        PlatePage platesPage = configuration.getPlatesAndPages ().getPlatePages ().get (position);
        logger.info ("New page " + position);


        // root
        LinearLayout rootLayout = new LinearLayout (container.getContext ());
        rootLayout.setOrientation (LinearLayout.VERTICAL);
        rootLayout.setBackgroundColor (Color.BLACK);


        // title
        //        TextView titleTextView = new TextView (container.getContext ());
        //        rootLayout.addView (titleTextView);
        //
        //        titleTextView.setText (platesPage.getTitle ());
        //        LinearLayout.LayoutParams titleTextViewLayoutParams = new LinearLayout.LayoutParams (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //        titleTextViewLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        //        titleTextView.setLayoutParams (titleTextViewLayoutParams);


        // calculate number of rows and columns and box size
        int rowCount = 0;
        int columnCount = 0;
        for (Plate plate : platesPage.getPlates ())
        {
            PlatePosition platePosition = plate.getPlatePositionsByPageId ().get (platesPage.getId ());
            if (platePosition == null)
            {
                continue;
            }
            columnCount = platePosition.getX () + platePosition.getDx () > columnCount ? platePosition.getX () + platePosition.getDx () : columnCount;
            rowCount = platePosition.getY () + platePosition.getDy () > rowCount ? platePosition.getY () + platePosition.getDy () : rowCount;
        }

        DisplayMetrics displayMetrics = container.getContext ().getResources ().getDisplayMetrics ();


        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        int columns = (int) dpWidth / 175; // TODO: use this


        logger.info ("Columns=", columnCount, ", rows=", rowCount, ", parent width=", displayMetrics.widthPixels, ", dpHeight=", dpHeight, ", dpWidth=", dpWidth);


        // scroll view
        ScrollView scrollView = new ScrollView (container.getContext ());
        rootLayout.addView (scrollView);
        scrollView.setBackgroundColor (Color.BLACK);


        // grid view
        GridLayout gridLayout = new GridLayout (container.getContext ());
        scrollView.addView (gridLayout);
        gridLayout.setRowCount (rowCount);
        gridLayout.setColumnCount (columnCount);


        for (Plate plate : platesPage.getPlates ())
        {
            PlatePosition platePosition = plate.getPlatePositionsByPageId ().get (platesPage.getId ());
            if (platePosition == null)
            {
                continue;
            }
            logger.info ("New plate: " + plate);


            // window rolling shutter plate
            if (plate instanceof WindowShutterPlate)
            {
                logger.info ("New window shutter plate: " + plate);
                WindowShutterPlate windowShutterPlate = (WindowShutterPlate) plate;
                String id = plate.getId ();
                WindowShutterDevice windowShutterDevice = windowShutterPlate.getWindowShutterDevice ();


                // inflate
                View view;
                if (windowShutterDevice.getRotationStepPercent () < 0)
                {
                    // no rotation => rolling shutter
                    view = layoutInflater.inflate (R.layout.window_rolling_shutter_layout, null);
                    // button events
                    ((ImageButton) view.findViewById (R.id.upButton)).setOnClickListener (new ButtonOnClickListener (Action.Up, plate, configuration, context));
                    ((ImageButton) view.findViewById (R.id.downButton)).setOnClickListener (new ButtonOnClickListener (Action.Down, plate, configuration, context));
                    ((ImageButton) view.findViewById (R.id.stopButton)).setOnClickListener (new ButtonOnClickListener (Action.Stop, plate, configuration, context));
                    ((ImageButton) view.findViewById (R.id.gridButton)).setOnClickListener (new ButtonOnClickListener (Action.Grid, plate, configuration, context));
                    ((ImageButton) view.findViewById (R.id.halfButton)).setOnClickListener (new ButtonOnClickListener (Action.Half, plate, configuration, context));
                    //                    ((ImageButton) view.findViewById (R.id.quarterButton)).setOnClickListener (new ButtonOnClickListener (Action.Quarter, plate, configuration, context));
                }
                else
                {
                    // rotation => louvre shutter
                    view = layoutInflater.inflate (R.layout.window_venetian_blind_layout, null);
                    ((ImageButton) view.findViewById (R.id.upButton)).setOnClickListener (new ButtonOnClickListener (Action.Up, plate, configuration, context));
                    ((ImageButton) view.findViewById (R.id.downButton)).setOnClickListener (new ButtonOnClickListener (Action.Down, plate, configuration, context));
                    ((ImageButton) view.findViewById (R.id.stopButton)).setOnClickListener (new ButtonOnClickListener (Action.Stop, plate, configuration, context));
                    ((ImageButton) view.findViewById (R.id.rotateUpButton)).setOnClickListener (new ButtonOnClickListener (Action.RotateUp, plate, configuration, context));
                    ((ImageButton) view.findViewById (R.id.rotateDownButton)).setOnClickListener (new ButtonOnClickListener (Action.RotateDown, plate, configuration, context));
                    //                    ((ImageButton) view.findViewById (R.id.rotateMiddleButton)).setOnClickListener (new ButtonOnClickListener (Action.RotateMidddle, plate, configuration, context));
                }
                windowShutterPlate.getViewByPlatePageId ().put (platesPage.getId (), view);
            }


            if (plate instanceof ActionPlate)
            {
                ActionPlate actionPlate = (ActionPlate) plate;
                logger.info ("New action plate: " + actionPlate);

                View view = layoutInflater.inflate (R.layout.action_plate_layout, null);
                actionPlate.getViewByPlatePageId ().put (platesPage.getId (), view);
                TableLayout tableLayout = (TableLayout) view.findViewById (R.id.actionPlateLayoutTableView);


                for (ActionPlateRow actionPlateRow : actionPlate.getRows ())
                {
                    TableRow tableRow = new TableRow (container.getContext ());
                    tableLayout.addView (tableRow);

                    for (ActionPlateRowButton actionPlateRowButton : actionPlateRow.buttons)
                    {
                        if (actionPlateRowButton.getTitle () != null)
                        {
                            View buttonView = layoutInflater.inflate (R.layout.action_plate_layout_text_button, null);
                            TextView rowTextView = (TextView) buttonView.findViewById (R.id.actionPlateLayoutTextButton);
                            rowTextView.setText (actionPlateRowButton.getTitle ());

                            if (actionPlateRowButton.getWidthMultiplier () != null)
                            {
                                rowTextView.getLayoutParams ().width *= actionPlateRowButton.getWidthMultiplier ();
                            }

                            rowTextView.setOnClickListener (new ButtonOnClickListener (actionPlateRowButton.getAction (), plate, configuration, context));

                            tableRow.addView (buttonView);
                        }
                        else if (actionPlateRowButton.getImageSource () != null)
                        {
                            View buttonView = layoutInflater.inflate (R.layout.action_plate_layout_image_button, null);
                            ImageButton imageButton = (ImageButton) buttonView.findViewById (R.id.actionPlateLayoutImageButton);
                            imageButton.setImageResource (actionPlateRowButton.getImageSource ());

                            if (actionPlateRowButton.getWidthMultiplier () != null)
                            {
                                imageButton.getLayoutParams ().width *= actionPlateRowButton.getWidthMultiplier ();
                            }

                            imageButton.setOnClickListener (new ButtonOnClickListener (actionPlateRowButton.getAction (), plate, configuration, context));

                            tableRow.addView (buttonView);
                        }
                    }
                }
            }


            if (plate instanceof TimedRelayPlate)
            {
                TimedRelayPlate timedRelayPlate = (TimedRelayPlate) plate;
                logger.info ("New timed relay plate: " + timedRelayPlate);


                // inflate shutter view
                View view = layoutInflater.inflate (R.layout.timed_relay_plate, null);
                timedRelayPlate.getViewByPlatePageId ().put (platesPage.getId (), view);


                // button events
                ((ImageButton) view.findViewById (R.id.startStopButton)).setOnClickListener (new ButtonOnClickListener (Action.StartStop, plate, configuration, context));
                ((ImageButton) view.findViewById (R.id.lockUnlockButton)).setOnClickListener (new ButtonOnClickListener (Action.LockUnlock, plate, configuration, context));
            }


            if (plate instanceof ThermometerPlate)
            {
                logger.info ("Inflate ", plate);
                View view = layoutInflater.inflate (R.layout.thermometer_plate, null);
                plate.getViewByPlatePageId ().put (platesPage.getId (), view);
            }


            if (plate instanceof PhotoResistorPlate)
            {
                logger.info ("Inflate ", plate);
                View view = layoutInflater.inflate (R.layout.photo_resistor_plate, null);
                plate.getViewByPlatePageId ().put (platesPage.getId (), view);
            }


            if (plate instanceof ShellPlate)
            {
                logger.info ("Inflate ", plate);
                View view = layoutInflater.inflate (R.layout.shell_plate, null);
                plate.getViewByPlatePageId ().put (platesPage.getId (), view);
            }


            if (plate instanceof AuthorizerPlate)
            {
                logger.info ("Inflate ", plate);
                View view = layoutInflater.inflate (R.layout.authorizer_plate, null);
                plate.getViewByPlatePageId ().put (platesPage.getId (), view);
            }

            // refresh plate content
            plate.refresh ();


            // adjust colors and add plate to grid layout
            View view = plate.getViewByPlatePageId ().get (platesPage.getId ());


            GridLayout.LayoutParams params = adjustColorsAndLayout ((ViewGroup) view, platesPage, plate, configuration.getPlatesAndPages ().getPlateBoxHorizontalSizeInPixels (), configuration.getPlatesAndPages ().getPlateBoxVerticalSizeInPixels ());
            gridLayout.addView (view, params);
        }


        container.addView (rootLayout);
        return (rootLayout);
    }


    @Override public void destroyItem (View container, int position, Object object)
    {
        ((ViewPager) container).removeView ((View) object);
    }


    @Override public int getCount ()
    {
        return (configuration.getPlatesAndPages ().getPlatePages ().size ());
    }


    @Override public boolean isViewFromObject (View view, Object object)
    {
        return (view == object);
    }


    @Override public CharSequence getPageTitle (int position)
    {
        PlatePage platePage = configuration.getPlatesAndPages ().getPlatePages ().get (position);
        return (platePage.getTitle ());
    }


    private GridLayout.LayoutParams adjustColorsAndLayout (ViewGroup viewGroup, PlatePage platesPage, Plate plate, int plateBoxHorizontalSizeInPixels, int plateBoxVerticalSizeInPixels)
    {
        adjustColors (viewGroup, plate);

        PlatePosition platePosition = plate.getPlatePositionsByPageId ().get (platesPage.getId ());
        GridLayout.LayoutParams params = new GridLayout.LayoutParams (GridLayout.spec (platePosition.getY (), platePosition.getDy ()), GridLayout.spec (platePosition.getX (), platePosition.getDx ()));
        params.width = Math.round (platePosition.getDx () * plateBoxHorizontalSizeInPixels - platesPage.getMarginInPx () * 2);
        params.height = Math.round (platePosition.getDy () * plateBoxVerticalSizeInPixels - platesPage.getMarginInPx () * 2);
        params.setMargins (platesPage.getMarginInPx (), platesPage.getMarginInPx (), platesPage.getMarginInPx (), platesPage.getMarginInPx ());
        viewGroup.setBackgroundColor (Color.parseColor (plate.getBackgroundColor ()));
        return (params);
    }


    private void adjustColors (ViewGroup viewGroup, Plate plate)
    {
        for (int i = 0; i < viewGroup.getChildCount (); i++)
        {
            View childView = viewGroup.getChildAt (i);

            if (childView instanceof TextView)
            {
                TextView tv = (TextView) childView;
                ColorDrawable colorDrawable = (ColorDrawable) tv.getBackground ();
                if (colorDrawable != null && colorDrawable.getColor () == Color.WHITE)
                {
                    tv.setTextColor (Color.parseColor (plate.getForegroundColor ()));
                    tv.setBackgroundColor (Color.parseColor (plate.getButtonBackgroundColor ()));
                }
            }

            if (childView instanceof ImageView)
            {
                ImageView imageView = (ImageView) childView;

                ColorDrawable colorDrawable = ((ColorDrawable) imageView.getBackground ());
                if (colorDrawable != null && colorDrawable.getColor () == Color.WHITE)
                {
                    imageView.setColorFilter (Color.parseColor (plate.getForegroundColor ()), PorterDuff.Mode.SRC_ATOP);
                    imageView.setBackgroundColor (Color.parseColor (plate.getButtonBackgroundColor ()));
                }
            }

            if (childView instanceof ViewGroup)
            {
                ViewGroup childViewGroup = (ViewGroup) childView;
                adjustColors (childViewGroup, plate);
            }
        }
    }


    private static final CustomLogger logger = CustomLogger.getLogger ("PLATE_PAGES_ADAPTER");
    private final Configuration configuration;
    private final Context context;
}


class ButtonOnClickListener implements View.OnClickListener
{
    public ButtonOnClickListener (Action action, Plate plate, Configuration configuration, Context context)
    {
        this.action = action;
        this.plate = plate;
        this.configuration = configuration;
        this.context = context;
    }


    public void onClick (View v)
    {
        try
        {
            logger.fine ("On click: ", action);
            MultiMessage multiMessage = plate.handleAction (action, configuration);
            configuration.flushMessages (multiMessage);
        }
        catch (Throwable t)
        {
            logger.severe (t, "Exception occured while handling plate action ", action);
            configuration.error (null, t.getMessage ());
        }
    }


    private Action action;
    private Plate plate;
    private Configuration configuration;
    private Context context;
    private static final CustomLogger logger = CustomLogger.getLogger ("BUTTON_ON_CLICK");
}