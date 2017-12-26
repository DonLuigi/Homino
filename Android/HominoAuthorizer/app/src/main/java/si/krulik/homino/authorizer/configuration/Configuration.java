package si.krulik.homino.authorizer.configuration;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;


public class Configuration
{
    @Getter @Setter private String deviceControlNodeId, authorizerDeviceId;

    @Getter @Setter private int deliverySec, validitySec;

    @Getter @Setter private boolean isRunning;

    @Getter private List<ConfigurationItem> configurationItems = new ArrayList ();


    public Configuration (Context context)
    {
        sharedPreferences = context.getSharedPreferences ("si.krulik.homino.authorizer.sharedPreferences", Context.MODE_PRIVATE);


        // items
        Set<String> csvSet = sharedPreferences.getStringSet (ITEMS, null);
        if (csvSet != null)
        {
            for (String csv : csvSet)
            {
                configurationItems.add (ConfigurationItem.newFromCsv (csv));
            }
        }


        // other
        deviceControlNodeId = sharedPreferences.getString (DEVICE_CONTROL_NODE_ID, "");
        authorizerDeviceId = sharedPreferences.getString (DEVICE_CONTROL_NODE_ID, "");
        validitySec = sharedPreferences.getInt (VALIDITY_SEC, -1);
        deliverySec = sharedPreferences.getInt (DELIVERY_SEC, -1);
        isRunning = sharedPreferences.getBoolean (IS_RUNNING, false);
    }


    public void saveToSharedPreferences ()
    {
        SharedPreferences.Editor editor = sharedPreferences.edit ();


        // items
        Set<String> csvs = new HashSet ();
        for (ConfigurationItem configurationItem : configurationItems)
        {
            csvs.add (configurationItem.toCsv ());
        }
        editor.putStringSet (ITEMS, csvs);


        // other
        editor.putString (DEVICE_CONTROL_NODE_ID, deviceControlNodeId);
        editor.putInt (VALIDITY_SEC, validitySec);
        editor.putInt (DELIVERY_SEC, deliverySec);
        editor.putBoolean (IS_RUNNING, isRunning);


        editor.commit ();
    }


    public String getDeliverySecString ()
    {
        return (deliverySec > 0 ? Integer.toString (deliverySec) : "");
    }


    public void setDeliverySecString (String value)
    {
        deliverySec = (value != null && !value.isEmpty () ? Integer.parseInt (value) : -1);
    }


    public String getValiditySecString ()
    {
        return (validitySec > 0 ? Integer.toString (validitySec) : "");
    }


    public void setValiditySecString (String value)
    {
        validitySec = (value != null && !value.isEmpty () ? Integer.parseInt (value) : -1);
    }


    public String isRunningString ()
    {
        return (isRunning ? "Yes" : "No");
    }


    private SharedPreferences sharedPreferences;
    static private final String ITEMS = "ITEMS";
    static private final String DEVICE_CONTROL_NODE_ID = "DEVICE_CONTROL_NODE_ID";
    static private final String AUTHORIZER_DEVICE_ID = "AUTHORIZER_DEVICE_ID";
    static private final String VALIDITY_SEC = "VALIDITY_SEC";
    static private final String DELIVERY_SEC = "DELIVERY_SEC";
    static private final String IS_RUNNING = "IS_RUNNING";
}