package si.krulik.homino.authorizer;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import static si.krulik.homino.authorizer.Constants.*;

import si.krulik.homino.authorizer.configuration.Configuration;
import si.krulik.homino.authorizer.configuration.ConfigurationItem;
import si.krulik.homino.common.logger.CustomLogger;


public class MainActivity extends Activity
{
    @Override protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);


        // load configuration
        configuration = new Configuration (this);


        // edit texts
//        editTextDeviceControlNodeId = ((EditText) findViewById (R.id.editTextControlNodeId));
//        editTextDeliverySec = ((EditText) findViewById (R.id.editTextDeliveryInterval));
//        editTextValiditySec = ((EditText) findViewById (R.id.editTextValidity));


//        editTextDeviceControlNodeId.setText (configuration.getDeviceControlNodeId ());
//        editTextDeliverySec.setText (configuration.getDeliverySecString ());
//        editTextValiditySec.setText (configuration.getValiditySecString ());


        // list view
        ListView ipsListView = (ListView) findViewById (R.id.mainActivityListViewAuthorizations);


        // list view context menu
        registerForContextMenu (ipsListView);

        // list view adapter
        ipsListAdapter = new ArrayAdapter<ConfigurationItem> (this, android.R.layout.simple_list_item_1, configuration.getConfigurationItems ());
        ipsListView.setAdapter (ipsListAdapter);


        // intent
        pendingIntent = PendingIntent.getBroadcast (MainActivity.this, 133, new Intent (INTENT_START).setClass (MainActivity.this, AlarmBroadcastReceiver.class), 0);


//        Button saveButton = (Button) findViewById (R.id.saveButton);
//        saveButton.setOnClickListener (new View.OnClickListener ()
//        {
//            @Override public void onClick (View view)
//            {
//                configuration.setDeviceControlNodeId (editTextDeviceControlNodeId.getText ().toString ());
//                configuration.setAuthorizerDeviceId (editTextAuthorizerDeviceId.getText ().toString ());
//                configuration.setDeliverySecString (editTextDeliverySec.getText ().toString ());
//                configuration.setValiditySecString (editTextValiditySec.getText ().toString ());
//                configuration.saveToSharedPreferences ();
//                Toast.makeText (MainActivity.this, "Saved", Toast.LENGTH_SHORT).show ();
//            }
//        });


        // add button
//        Button addButton = (Button) findViewById (R.id.addAuthorizationButton);
//        addButton.setOnClickListener (new View.OnClickListener ()
//        {
//            @Override public void onClick (View view)
//            {
//                AlertDialog.Builder builder = new AlertDialog.Builder (MainActivity.this);
//                builder.setTitle ("Add authorization");
//                final View dialogView = MainActivity.this.getLayoutInflater ().inflate (R.layout.dialog_add_authorization, null); // Pass null as the parent view because its going in the dialog layout
//                builder.setView (dialogView).setPositiveButton (android.R.string.ok, new DialogInterface.OnClickListener ()
//                {
//                    @Override public void onClick (DialogInterface dialog, int id)
//                    {
//                        configuration.getConfigurationItems ().add (new ConfigurationItem ( //
//                            ((EditText) dialogView.findViewById (R.id.addAuthorizationEditTextAuthorization)).getText ().toString (), //
//                            ((EditText) dialogView.findViewById (R.id.addAuthorizationEditTextSsid)).getText ().toString (), //
//                            ((EditText) dialogView.findViewById (R.id.addAuthorizationEditTextNetworkHost)).getText ().toString (), //
//                            Integer.parseInt (((EditText) dialogView.findViewById (R.id.addAuthorizationEditTextNetworkPort)).getText ().toString ())));
//                        ipsListAdapter.notifyDataSetChanged ();
//                    }
//                }).setNegativeButton (android.R.string.ok, new DialogInterface.OnClickListener ()
//                {
//                    public void onClick (DialogInterface dialog, int id)
//                    {
//                        dialog.cancel ();
//                    }
//                });
//                AlertDialog alertDialog = builder.create ();
//                alertDialog.show ();
//            }
//        });


        // start button
        //        Button startButton = (Button) findViewById (R.id.startButton);
        //        startButton.setOnClickListener (new View.OnClickListener ()
        //        {
        //            @Override public void onClick (View view)
        //            {
        //                if (configuration.getDeliverySec () > 0)
        //                {
        //                    // start network service
        //                    //                    Intent startServiceIntent = new Intent (MainActivity.this, HominoNetworkService.class);
        //                    //                    MainActivity.this.startService (startServiceIntent);
        //
        //
        //                    // enable alarm
        //                    AlarmManager alarmManager = (AlarmManager) getSystemService (Context.ALARM_SERVICE);
        //                    alarmManager.setInexactRepeating (AlarmManager.ELAPSED_REALTIME_WAKEUP, configuration.getDeliverySec () * 1000, configuration.getDeliverySec () * 1000, pendingIntent);
        //
        //
        //                    // update configuration
        //                    configuration.setRunning (true);
        //                    configuration.saveToSharedPreferences ();
        //
        //
        //                    Toast.makeText (MainActivity.this, "Started", Toast.LENGTH_SHORT).show ();
        //                }
        //            }
        //        });


        // stop button
        //        Button stopButton = (Button) findViewById (R.id.stopButton);
        //        stopButton.setOnClickListener (new View.OnClickListener ()
        //        {
        //            @Override public void onClick (View view)
        //            {
        //                // disable alarm
        //                AlarmManager manager = (AlarmManager) getSystemService (Context.ALARM_SERVICE);
        //                manager.cancel (pendingIntent);
        //
        //                configuration.setRunning (false);
        //                configuration.saveToSharedPreferences ();
        //
        //                Toast.makeText (MainActivity.this, "Stopped", Toast.LENGTH_SHORT).show ();
        //            }
        //        });
    }


    @Override public void onCreateContextMenu (ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo)
    {
        if (view.getId () == R.id.mainActivityListViewAuthorizations)
        {
            ListView lv = (ListView) view;
            menu.add (DELETE);
            menu.add (EDIT);
        }
    }


    @Override public boolean onContextItemSelected (MenuItem menuItem)
    {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo ();
        int position = menuInfo.position;
        String title = menuItem.getTitle ().toString ();


        if (title.equals (DELETE))
        {
            configuration.getConfigurationItems ().remove (position);
            configuration.saveToSharedPreferences ();
            ipsListAdapter.notifyDataSetChanged ();
        }


        if (title.equals (EDIT))
        {

            ConfigurationItem configurationItem = configuration.getConfigurationItems ().remove (position);
//            editTextAuthorization.setText (configurationItem.getAuthorization ());
//            editTextHostname.setText (configurationItem.getHostname ());
//            editTextPort.setText (configurationItem.getPort ());
            ipsListAdapter.notifyDataSetChanged ();
        }

        return true;
    }


    private Configuration configuration;
    private ArrayAdapter ipsListAdapter;
    private PendingIntent pendingIntent;
    private EditText editTextDeviceControlNodeId;
    private EditText editTextAuthorizerDeviceId;
    private EditText editTextDeliverySec;
    private EditText editTextValiditySec;
    static private final CustomLogger logger = CustomLogger.getLogger ("MAIN_ACTIVITY");
    static private final String DELETE = "DELETE";
    static private final String EDIT = "EDIT";
}