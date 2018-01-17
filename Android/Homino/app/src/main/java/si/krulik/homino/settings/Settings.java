package si.krulik.homino.settings;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString (includeFieldNames = true, of = {"keepConnectionAsDeviceControlNodeId", "automationEnabled", "keepConnectionToDeviceControlNodeIds"}) public class Settings
{
    @Getter @Setter private Set<String> keepConnectionToDeviceControlNodeIds;


    @Getter @Setter private String keepConnectionAsDeviceControlNodeId;


    @Getter @Setter private boolean automationEnabled;


    @Getter @Setter private int defaultPlatePage;


    public Settings (Context context)
    {
        sharedPreferences = context.getSharedPreferences ("si.krulik.homino.sharedPreferences.settings", Context.MODE_PRIVATE);


        keepConnectionToDeviceControlNodeIds = sharedPreferences.getStringSet (KEEP_CONNECTION_TO_DEVICE_CONTROL_NODE_IDS, empty);


        keepConnectionAsDeviceControlNodeId = sharedPreferences.getString (KEEP_CONNECTION_AS_DEVICE_CONTROL_NODE_ID, null);


        automationEnabled = sharedPreferences.getBoolean (AUTOMATION_ENABLED, false);


        defaultPlatePage = sharedPreferences.getInt (DEFAULT_PLATE_PAGE, -1);
    }


    public void saveToSharedPreferences ()
    {
        SharedPreferences.Editor editor = sharedPreferences.edit ();


        editor.putStringSet (KEEP_CONNECTION_TO_DEVICE_CONTROL_NODE_IDS, keepConnectionToDeviceControlNodeIds);


        editor.putString (KEEP_CONNECTION_AS_DEVICE_CONTROL_NODE_ID, keepConnectionAsDeviceControlNodeId);


        editor.putBoolean (AUTOMATION_ENABLED, automationEnabled);


        editor.putInt (DEFAULT_PLATE_PAGE, defaultPlatePage);


        editor.commit ();
    }


    private SharedPreferences sharedPreferences;
    static private final String KEEP_CONNECTION_TO_DEVICE_CONTROL_NODE_IDS = "KEEP_CONNECTION_TO_DEVICE_CONTROL_NODE_IDS";
    static private final String KEEP_CONNECTION_AS_DEVICE_CONTROL_NODE_ID = "KEEP_CONNECTION_AS_DEVICE_CONTROL_NODE_ID";
    static private final String AUTOMATION_ENABLED = "AUTOMATION_ENABLED";
    static private final String DEFAULT_PLATE_PAGE = "DEFAULT_PLATE_PAGE";
    private static Set<String> empty = new HashSet ();
}
