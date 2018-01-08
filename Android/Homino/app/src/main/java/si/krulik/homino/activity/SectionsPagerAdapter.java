package si.krulik.homino.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter
{


    public SectionsPagerAdapter (FragmentManager fm, int numberOfPages, Runtime runtime)
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
    private Runtime runtime;
}