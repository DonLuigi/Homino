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

        return (platesPageLinearLayout);
    }


    private int pageNumber;
    private Configuration configuration;
    private Map<Integer, LinearLayout> platesPageViews = new HashMap<> ();

}
