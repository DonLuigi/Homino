package si.krulik.homino.plates;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.HashSet;

import lombok.Getter;
import lombok.ToString;
import si.krulik.homino.R;
import si.krulik.homino.alarms.Alarms;
import si.krulik.homino.common.MultiSelectionSpinner;
import si.krulik.homino.runtime.Runtime;
import si.krulik.homino.devices.base.DeviceControlNode;
import si.krulik.homino.message.MultiMessage;
import si.krulik.homino.plates.base.IPlateActionHandler;
import si.krulik.homino.plates.base.Plate;
import si.krulik.homino.settings.Settings;

import static si.krulik.homino.runtime.Runtime.*;



@ToString (includeFieldNames = true, callSuper = true) public class SettingsPlate extends Plate
{
    public SettingsPlate (String id, String foregroundColor, String backgroundColor, int layoutId)
    {
        super (id, null, foregroundColor, backgroundColor, null, layoutId, null);
    }


    @Override public View inflate (String pageId, LayoutInflater layoutInflater)
    {
        View view = super.inflate (pageId, layoutInflater);

        ImageView imageView = (ImageView) view.findViewById (R.id.plateIcon);
        imageView.setImageResource (R.drawable.settings);
        imageView.setOnClickListener (new ViewOnClickListener (Runtime.Action.Settings, this));


        actionHandler = new IPlateActionHandler ()
        {
            Settings newSettings;


            public MultiMessage handleAction (Runtime.Action action)
            {
                switch (action)
                {
                    case Settings:
                        newSettings = new Settings (Runtime.context);

                        AlertDialog.Builder builder = new AlertDialog.Builder (Runtime.activity);

                        // title
                        builder.setTitle ("Settings");


                        // buttons
                        builder.setPositiveButton ("Save", new DialogInterface.OnClickListener ()
                        {
                            @Override public void onClick (DialogInterface dialog, int id)
                            {
                                AlertDialog alertDialog = (AlertDialog) dialog;

                                newSettings.setKeepConnectionAsDeviceControlNodeId (((Spinner) alertDialog.findViewById (R.id.keepConnectionAsSpinner)).getSelectedItem ().toString ());
                                newSettings.setKeepConnectionToDeviceControlNodeIds (new HashSet (((MultiSelectionSpinner) alertDialog.findViewById (R.id.keepConnectionWithSpinner)).getSelectedStrings ()));
                                newSettings.setAutomationEnabled (((Switch) alertDialog.findViewById (R.id.automationEnabledSwitch)).isChecked ());

                                newSettings.saveToSharedPreferences ();
                                settings = newSettings;
                                dialog.dismiss ();

                                Alarms.enableAlarms ();
                            }
                        });

                        builder.setNegativeButton (android.R.string.cancel, new DialogInterface.OnClickListener ()
                        {
                            public void onClick (DialogInterface dialog, int id)
                            {
                                dialog.cancel ();
                            }
                        });


                        // layout
                        View dialogView = Runtime.activity.getLayoutInflater ().inflate (R.layout.settings_dialog_layout, null); // Pass null as the parent view because its going in the dialog layout
                        builder.setView (dialogView);


                        // keep connection to and keep connection as
                        ArrayList<String> keepConnectionToDeviceControlNodeIds = new ArrayList ();
                        ArrayList<String> keepConnectionAsControlDeviceNodeIds = new ArrayList ();
                        for (DeviceControlNode deviceControlNode : devices.getDeviceControlNodesById ().values ())
                        {
                            if (!deviceControlNode.isIncommingConnection ())
                            {
                                keepConnectionToDeviceControlNodeIds.add (deviceControlNode.getId ());
                            }
                            else
                            {
                                keepConnectionAsControlDeviceNodeIds.add (deviceControlNode.getId ());
                            }
                        }
                        MultiSelectionSpinner authorizationSpinner = (MultiSelectionSpinner) dialogView.findViewById (R.id.keepConnectionWithSpinner);
                        authorizationSpinner.setItems (keepConnectionToDeviceControlNodeIds);
                        authorizationSpinner.setSelection (new ArrayList (newSettings.getKeepConnectionToDeviceControlNodeIds ()));


                        Spinner controlDeviceNodeIdSpinner = (Spinner) dialogView.findViewById (R.id.keepConnectionAsSpinner);
                        controlDeviceNodeIdSpinner.setAdapter ( //
                            new ArrayAdapter<String> ( //
                                Runtime.context, //
                                android.R.layout.simple_spinner_item, //
                                keepConnectionAsControlDeviceNodeIds));
                        int position = keepConnectionAsControlDeviceNodeIds.indexOf (newSettings.getKeepConnectionAsDeviceControlNodeId ());
                        if (position >= 0)
                        {
                            controlDeviceNodeIdSpinner.setSelection (position);
                        }


                        // automation
                        ((Switch) dialogView.findViewById (R.id.automationEnabledSwitch)).setChecked (newSettings.isAutomationEnabled ());


                        // show
                        AlertDialog alertDialog = builder.create ();
                        alertDialog.show ();
                        break;
                }

                return (null);
            }
        };

        return (view);
    }


    @Getter private Settings settings;
}