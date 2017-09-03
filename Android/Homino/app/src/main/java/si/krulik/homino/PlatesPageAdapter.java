package si.krulik.homino;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.Gravity;
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
import android.widget.Toast;

import java.util.logging.Logger;

import si.krulik.homino.configuration.Configuration;
import si.krulik.homino.configuration.Plate;
import si.krulik.homino.configuration.plates.ActionPlate;
import si.krulik.homino.configuration.plates.ActionPlateRow;
import si.krulik.homino.configuration.plates.ActionPlateRowButton;
import si.krulik.homino.configuration.plates.PlatePage;
import si.krulik.homino.configuration.plates.TimedRelayPlate;
import si.krulik.homino.configuration.plates.WindowLouvreShutterPlate;
import si.krulik.homino.configuration.plates.WindowRollingShutterPlate;

public class PlatesPageAdapter extends PagerAdapter
{
    public PlatesPageAdapter (Configuration configuration, Context context)
    {
        this.configuration = configuration;
        this.context = context;
    }


    @Override
    public int getCount ()
    {
        return (configuration.platePages.size ());
    }


    @Override
    public boolean isViewFromObject (View view, Object object)
    {
        return (view == object);
    }


    @Override
    public Object instantiateItem (ViewGroup container, int position)
    {
        // layout inflater
        LayoutInflater layoutInflater = LayoutInflater.from (container.getContext ());


        // page definition
        PlatePage platesPage = configuration.platePages.get (position);
        logger.info ("New page " + position);


        // root
        LinearLayout rootLayout = new LinearLayout (container.getContext ());
        rootLayout.setOrientation (LinearLayout.VERTICAL);
        rootLayout.setBackgroundColor (Color.BLACK);


        // title
        TextView titleTextView = new TextView (container.getContext ());
        rootLayout.addView (titleTextView);

        titleTextView.setText (platesPage.title);
        LinearLayout.LayoutParams titleTextViewLayoutParams = new LinearLayout.LayoutParams (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleTextViewLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        titleTextView.setLayoutParams (titleTextViewLayoutParams);


        // calculate number of rows and columns and box size
        int rowCount = 0;
        int columnCount = 0;
        for (Plate plate : platesPage.plates)
        {
            if (plate.position == null)
            {
                continue;
            }
            columnCount = plate.position.x + plate.position.dx > columnCount ? plate.position.x + plate.position.dx : columnCount;
            rowCount = plate.position.y + plate.position.dy > rowCount ? plate.position.y + plate.position.dy : rowCount;
        }

        DisplayMetrics displayMetrics = container.getContext ().getResources ().getDisplayMetrics ();
        float boxSize = ((float) displayMetrics.widthPixels) / columnCount;

        logger.info ("Columns=" + columnCount + ", rows=" + rowCount + ", parent width=" + displayMetrics.widthPixels + ", boxSize=" + boxSize);


        // scroll view
        ScrollView scrollView = new ScrollView (container.getContext ());
        rootLayout.addView (scrollView);
        scrollView.setBackgroundColor (Color.BLACK);


        // grid view
        GridLayout gridLayout = new GridLayout (container.getContext ());
        scrollView.addView (gridLayout);
        gridLayout.setRowCount (rowCount);
        gridLayout.setColumnCount (columnCount);


        for (Plate plate : platesPage.plates)
        {
            if (plate.position == null)
            {
                continue;
            }
            logger.info ("New plate: eventName=" + plate.id + ", x=" + plate.position.x + ", y=" + plate.position.y + ", dx=" + plate.position.dx + ", dy=" + plate.position.dy);


            // window rolling shutter plate
            if (plate instanceof WindowRollingShutterPlate)
            {
                WindowRollingShutterPlate windowRollingShutterPlate = (WindowRollingShutterPlate) plate;
                logger.info ("New window shutter plate: " + windowRollingShutterPlate.text);


                // inflate shutter view
                windowRollingShutterPlate.view = layoutInflater.inflate (R.layout.window_rolling_shutter_layout, null);


                // button events
                ((ImageButton) windowRollingShutterPlate.view.findViewById (R.id.upButton)).setOnClickListener (new ButtonOnClickListener (windowRollingShutterPlate.upButtonEventName,
                    configuration, context));
                ((ImageButton) windowRollingShutterPlate.view.findViewById (R.id.downButton)).setOnClickListener (new ButtonOnClickListener (windowRollingShutterPlate.downButtonEventName,
                    configuration, context));
                ((ImageButton) windowRollingShutterPlate.view.findViewById (R.id.lockUnlockButton)).setOnClickListener (new ButtonOnClickListener (windowRollingShutterPlate.stopButtonEventName,
                    configuration, context));
                ((ImageButton) windowRollingShutterPlate.view.findViewById (R.id.gridButton)).setOnClickListener (new ButtonOnClickListener (windowRollingShutterPlate.gridButtonEventName,
                    configuration, context));
                ((ImageButton) windowRollingShutterPlate.view.findViewById (R.id.halfButton)).setOnClickListener (new ButtonOnClickListener (windowRollingShutterPlate.halfButtonEventName,
                    configuration, context));
                ((ImageButton) windowRollingShutterPlate.view.findViewById (R.id.quarterButton)).setOnClickListener (new ButtonOnClickListener (windowRollingShutterPlate.quarterButtonEventName,
                    configuration, context));
            }


            if (plate instanceof WindowLouvreShutterPlate)
            {
                WindowLouvreShutterPlate windowLouvreShutterPlate = (WindowLouvreShutterPlate) plate;
                logger.info ("New window louvre shutter plate: " + windowLouvreShutterPlate.text);


                // inflate shutter view
                windowLouvreShutterPlate.view = layoutInflater.inflate (R.layout.window_venetian_blind_layout, null);


                // button events
                ((ImageButton) windowLouvreShutterPlate.view.findViewById (R.id.upButton)).setOnClickListener (new ButtonOnClickListener (windowLouvreShutterPlate.upButtonEventName, configuration,
                    context));
                ((ImageButton) windowLouvreShutterPlate.view.findViewById (R.id.downButton)).setOnClickListener (new ButtonOnClickListener (windowLouvreShutterPlate.downButtonEventName,
                    configuration, context));
                ((ImageButton) windowLouvreShutterPlate.view.findViewById (R.id.lockUnlockButton)).setOnClickListener (new ButtonOnClickListener (windowLouvreShutterPlate.stopButtonEventName,
                    configuration, context));
                ((ImageButton) windowLouvreShutterPlate.view.findViewById (R.id.gridButton)).setOnClickListener (new ButtonOnClickListener (windowLouvreShutterPlate.halfButtonEventName,
                    configuration, context));
                ((ImageButton) windowLouvreShutterPlate.view.findViewById (R.id.halfButton)).setOnClickListener (new ButtonOnClickListener (windowLouvreShutterPlate.rotateUpButtonEventName,
                    configuration, context));
                ((ImageButton) windowLouvreShutterPlate.view.findViewById (R.id.quarterButton)).setOnClickListener (new ButtonOnClickListener (windowLouvreShutterPlate.rotateDownButtonEventName,
                    configuration, context));
            }


            if (plate instanceof ActionPlate)
            {
                ActionPlate actionPlate = (ActionPlate) plate;
                logger.info ("New action plate: " + actionPlate.text);

                actionPlate.view = layoutInflater.inflate (R.layout.action_plate_layout, null);
                TableLayout tableLayout = (TableLayout) actionPlate.view.findViewById (R.id.actionPlateLayoutTableView);


                for (ActionPlateRow actionPlateRow : actionPlate.rows)
                {
                    TableRow tableRow = new TableRow (container.getContext ());
                    tableLayout.addView (tableRow);

                    for (ActionPlateRowButton actionPlateRowButton : actionPlateRow.buttons)
                    {
                        if (actionPlateRowButton.text != null)
                        {
                            View buttonView = layoutInflater.inflate (R.layout.action_plate_layout_text_button, null);
                            TextView rowTextView = (TextView) buttonView.findViewById (R.id.actionPlateLayoutTextButton);
                            rowTextView.setText (actionPlateRowButton.text);

                            if (actionPlateRowButton.widthMultiplier != null)
                            {
                                rowTextView.getLayoutParams ().width *= actionPlateRowButton.widthMultiplier;
                            }

                            rowTextView.setOnClickListener (new ButtonOnClickListener (actionPlateRowButton.eventName, configuration, context));

                            tableRow.addView (buttonView);
                        }
                        else if (actionPlateRowButton.imageSource != null)
                        {
                            View buttonView = layoutInflater.inflate (R.layout.action_plate_layout_image_button, null);
                            ImageButton imageButton = (ImageButton) buttonView.findViewById (R.id.actionPlateLayoutImageButton);
                            imageButton.setImageResource (actionPlateRowButton.imageSource);

                            if (actionPlateRowButton.widthMultiplier != null)
                            {
                                imageButton.getLayoutParams ().width *= actionPlateRowButton.widthMultiplier;
                            }

                            imageButton.setOnClickListener (new ButtonOnClickListener (actionPlateRowButton.eventName, configuration, context));

                            tableRow.addView (buttonView);
                        }
                    }
                }
            }



            if (plate instanceof TimedRelayPlate)
            {
                TimedRelayPlate timedRelayPlate = (TimedRelayPlate ) plate;
                logger.info ("New timed relay plate: " + timedRelayPlate.text);


                // inflate shutter view
                timedRelayPlate.view = layoutInflater.inflate (R.layout.timed_relay_plate, null);


                // button events
                ((ImageButton) timedRelayPlate.view.findViewById (R.id.startStopButton)).setOnClickListener (new ButtonOnClickListener (timedRelayPlate.startStopButtonEventName,
                    configuration, context));
                ((ImageButton) timedRelayPlate.view.findViewById (R.id.lockUnlockButton)).setOnClickListener (new ButtonOnClickListener (timedRelayPlate.enableDisableButtonEventName,
                    configuration, context));
            }




            // refresh plate content
            plate.refresh ();


            // adjust colors and add plate to grid layout
            GridLayout.LayoutParams params = adjustColorsAndLayout ((ViewGroup) plate.view, platesPage, plate, boxSize);
            gridLayout.addView (plate.view, params);
        }


        container.addView (rootLayout);
        return (rootLayout);
    }


    private GridLayout.LayoutParams adjustColorsAndLayout (ViewGroup viewGroup, PlatePage platesPage, Plate plate, float boxSize)
    {
        adjustColors (viewGroup, plate);

        GridLayout.LayoutParams params = new GridLayout.LayoutParams (GridLayout.spec (plate.position.y, plate.position.dy), GridLayout.spec (plate.position.x, plate.position.dx));
        params.width = Math.round (plate.position.dx * boxSize - platesPage.marginInPx * 2);
        params.height = Math.round (plate.position.dy * boxSize - platesPage.marginInPx * 2);
        params.setMargins (platesPage.marginInPx, platesPage.marginInPx, platesPage.marginInPx, platesPage.marginInPx);
        viewGroup.setBackgroundColor (Color.parseColor (plate.backgroundColor));
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
                    tv.setTextColor (Color.parseColor (plate.foregroundColor));
                    tv.setBackgroundColor (Color.parseColor (plate.buttonBackgroundColor));
                }
            }

            if (childView instanceof ImageView)
            {
                ImageView imageView = (ImageView) childView;

                ColorDrawable colorDrawable = ((ColorDrawable) imageView.getBackground ());
                if (colorDrawable != null && colorDrawable.getColor () == Color.WHITE)
                {
                    imageView.setColorFilter (Color.parseColor (plate.foregroundColor), PorterDuff.Mode.SRC_ATOP);
                    imageView.setBackgroundColor (Color.parseColor (plate.buttonBackgroundColor));
                }
            }

            if (childView instanceof ViewGroup)
            {
                ViewGroup childViewGroup = (ViewGroup) childView;
                adjustColors (childViewGroup, plate);
            }
        }
    }


    private final Logger logger = Logger.getLogger (PlatesPageAdapter.class.getName ());
    private final Configuration configuration;
    private final Context context;
}


class ButtonOnClickListener implements View.OnClickListener
{
    public ButtonOnClickListener (String eventMessage, Configuration configuration, Context context)
    {
        this.eventMessage = eventMessage;
        this.configuration = configuration;
        this.context = context;
    }


    public void onClick (View v)
    {
        try
        {
            configuration.plateMessageHandler.handle ("ui", eventMessage, configuration);
        }
        catch (Throwable t)
        {
            logger.severe ("Exception occured while handling event message " + eventMessage);
            Toast toast = Toast.makeText (context, t.getMessage (), Toast.LENGTH_LONG);
            toast.show ();
        }
    }


    private static final Logger logger = Logger.getLogger (PlatesPageAdapter.class.getName ());
    private String eventMessage;
    private Configuration configuration;
    private Context context;
}