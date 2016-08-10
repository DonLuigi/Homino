package si.krulik.homino;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import si.krulik.homino.configuration.Configuration;

public  class PlatesPageFragment extends Fragment
{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";


    public PlatesPageFragment (int pageNumber, Configuration configuration)
    {
        this.pageNumber = pageNumber;
        this.configuration = configuration;
    }




    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
//    public static PlatesPageFragment newInstance (int sectionNumber, Configuration configuration)
//    {
//        PlatesPageFragment fragment = new PlatesPageFragment ();
//        Bundle args = new Bundle ();
//        args.putInt (ARG_SECTION_NUMBER, sectionNumber);
//        args.put (ARG_CONFIGURATION, configuration);
//        fragment.setArguments (args);
//        return fragment;
//    }


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState)
    {
        // determine page we are on
        Bundle arguments = getArguments ();
        int pageNumber = arguments.getInt (ARG_SECTION_NUMBER);

        // store page view
        LinearLayout platesPageLinearLayout = new LinearLayout (getContext ());
        platesPageViews.put (pageNumber, platesPageLinearLayout);

        platesPageLinearLayout.setOrientation (LinearLayout.HORIZONTAL);
        platesPageLinearLayout.setLayoutParams (new LinearLayout.LayoutParams (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));


        // compose columns
//        for (int i = 0; i < configuration.numberOfPlateColumnsPerPage; i++)
//        {
//            int pageColumnIndex = pageNumber * configuration.numberOfPlateColumnsPerPage + i;
//            if (pageColumnIndex >= configuration.plateColumns.size ())
//            {
//                break;
//            }
//
//            LinearLayout platesColumnLinearLayout = new LinearLayout (getContext ());
//            platesPageLinearLayout.addView (platesColumnLinearLayout);
//
//            platesColumnLinearLayout.setOrientation (LinearLayout.VERTICAL);
//            platesColumnLinearLayout.setLayoutParams (new LinearLayout.LayoutParams (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//
//            TextView textView = new TextView (getContext ());
//            textView.setText ("ITEM" + i);
//            platesColumnLinearLayout.addView (textView);
//        }

        return (platesPageLinearLayout);


//            //View rootView = inflater.inflate (R.layout.fragment_pages, container, false);
//            View rootView = inflater.inflate (R.layout.plates_page, container, false);
//
//            View window_rolling_shutter_layout = inflater.inflate (R.layout.window_rolling_shutter_layout, container, false);
//            LinearLayout linearLayout = (LinearLayout) rootView.findViewById (R.eventName.firstColumn);
//            linearLayout.addView (window_rolling_shutter_layout);
//
//            View plate2 = inflater.inflate (R.layout.window_rolling_shutter_layout, container, false);
//            plate2.setBackground (ContextCompat.getDrawable (getContext (), R.drawable.plate_background_red));
//            LinearLayout linearLayout2 = (LinearLayout) rootView.findViewById (R.eventName.firstColumn);
//            linearLayout2.addView (plate2);
//
//
//            //TextView textView = (TextView) rootView.findViewById (R.eventName.section_label);
        //     textView.setText (getString (R.string.section_format, getArguments ().getInt (ARG_SECTION_NUMBER)));
//            return rootView;
    }


    private int pageNumber;
    private Configuration configuration;
    private Map<Integer, LinearLayout> platesPageViews = new HashMap<> ();

}
