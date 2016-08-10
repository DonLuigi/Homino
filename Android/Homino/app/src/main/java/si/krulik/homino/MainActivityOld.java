package si.krulik.homino;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import si.krulik.homino.configuration.Configuration;

public class MainActivityOld extends AppCompatActivity
{

    public Configuration configuration;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager viewPager;


    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        // configure application
        try
        {
            //configuration = new Customization ().compose ();
        }
        catch (Throwable t)
        {
            t.printStackTrace ();
            throw (new RuntimeException ("Failed to configure application", t));
        }


        // determine number of columns if needed
//        if (configuration.numberOfPlateColumnsPerPage == null)
//        {
//            Display display = getWindowManager ().getDefaultDisplay ();
//            DisplayMetrics outMetrics = new DisplayMetrics ();
//            display.getMetrics (outMetrics);
//            float density = getResources ().getDisplayMetrics ().density;
//            float dpHeight = outMetrics.heightPixels / density;
//            float dpWidth = outMetrics.widthPixels / density;
//            configuration.numberOfPlateColumnsPerPage = (int) (dpWidth / 250);
//            configuration.numberOfPages = (configuration.plateColumns.size () + configuration.numberOfPlateColumnsPerPage - 1) / configuration.numberOfPlateColumnsPerPage;
//        }


        //
        super.onCreate (savedInstanceState);
        //setContentView (R.layout.activity_main);

        //Toolbar toolbar = (Toolbar) findViewById (R.eventName.toolbar);
//        setSupportActionBar (toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        //mSectionsPagerAdapter = new SectionsPagerAdapter (getSupportFragmentManager (), configuration.numberOfPages);

        // Set up the ViewPager with the sections adapter.
        //mViewPager = (ViewPager) findViewById (R.eventName.container);
        viewPager.setAdapter (mSectionsPagerAdapter);

        //TabLayout tabLayout = (TabLayout) findViewById (R.eventName.tabs);
        //tabLayout.setupWithViewPager (mViewPager);

        //FloatingActionButton fab = (FloatingActionButton) findViewById (R.eventName.fab);
//        fab.setOnClickListener (new View.OnClickListener ()
//        {
//            @Override
//            public void onClick (View view)
//            {
//                Snackbar.make (view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction ("Action", null).show ();
//            }
//        });

    }


    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater ().inflate (R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId ();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected (item);
    }


}
