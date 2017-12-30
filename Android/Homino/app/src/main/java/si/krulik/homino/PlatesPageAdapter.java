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
import android.widget.TextClock;
import android.widget.TextView;

import si.krulik.homino.common.logger.CustomLogger;
import si.krulik.homino.configuration.Configuration;
import si.krulik.homino.configuration.command.Action;
import si.krulik.homino.configuration.device.AuthorizerDevice;
import si.krulik.homino.configuration.device.WindowShutterDevice;
import si.krulik.homino.configuration.plate.ActionPlate;
import si.krulik.homino.configuration.plate.ActionPlateRow;
import si.krulik.homino.configuration.plate.ActionPlateRowButton;
import si.krulik.homino.configuration.plate.ClockPlate;
import si.krulik.homino.configuration.plate.RtspPlate;
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


            // inflate
            View view = plate.inflate (platePosition.getPlatePageId (), layoutInflater);


            // refresh
            plate.refresh ();


            // adjust colors and add plate to grid layout
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


