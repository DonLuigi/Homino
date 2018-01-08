package si.krulik.homino.activity;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import si.krulik.homino.common.logger.CustomLogger;
import si.krulik.homino.runtime.Runtime;
import si.krulik.homino.plates.base.Plate;
import si.krulik.homino.plates.base.PlatePage;
import si.krulik.homino.plates.base.PlatePosition;


public class PlatesPageAdapter extends PagerAdapter
{
    @Override public Object instantiateItem (ViewGroup container, int position)
    {
        logger.fine ("Inflating page ", position);
        LayoutInflater layoutInflater = LayoutInflater.from (container.getContext ());


        // page definition
        PlatePage platesPage = Runtime.platesAndPages.getPlatePages ().get (position);


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
        logger.fine ("Column count ", columnCount, ", row count ", rowCount);


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
            logger.fine ("Inflating plate ", plate, " on plate position ", platePosition);
            View view = plate.inflate (platePosition.getPlatePageId (), layoutInflater);


            // refresh
            logger.fine ("Refresh plate");
            plate.refresh ();


            // adjust colors and add plate to grid layout
            logger.fine ("Adjust colors and layout");
            GridLayout.LayoutParams params = adjustColorsAndLayout ((ViewGroup) view, platesPage, plate, Runtime.platesAndPages.getPlateBoxHorizontalSizeInPixels (), Runtime.platesAndPages.getPlateBoxVerticalSizeInPixels ());
            gridLayout.addView (view, params);
        }


        container.addView (rootLayout);

        logger.info ("Done inflating page ", position);
        return (rootLayout);
    }


    @Override public void destroyItem (View container, int position, Object object)
    {
        ((ViewPager) container).removeView ((View) object);
    }


    @Override public int getCount ()
    {
        return (Runtime.platesAndPages.getPlatePages ().size ());
    }


    @Override public boolean isViewFromObject (View view, Object object)
    {
        return (view == object);
    }


    @Override public CharSequence getPageTitle (int position)
    {
        PlatePage platePage = Runtime.platesAndPages.getPlatePages ().get (position);
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
}


