package si.krulik.homino;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import si.krulik.homino.configuration.Configuration;

public class SectionsPagerAdapter extends FragmentPagerAdapter
{


    public SectionsPagerAdapter (FragmentManager fm, int numberOfPages, Configuration configuration)
    {
        super (fm);
        this.numberOfPages = numberOfPages;
    }


    @Override
    public Fragment getItem (int position)
    {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlatesPageFragment (defined as a static inner class below).


        return (null);
    }


    @Override
    public int getCount ()
    {
        return (numberOfPages);
    }


    @Override
    public CharSequence getPageTitle (int position)
    {
        return ("Page " + position);
    }


    private int numberOfPages;
    private Configuration configuration;
}