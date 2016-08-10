package si.krulik.homino.configuration;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import si.krulik.homino.common.validate.Validate;
import si.krulik.homino.configuration.devices.Device;
import si.krulik.homino.configuration.plates.IPulseHandler;
import si.krulik.homino.configuration.plates.PlatePage;
import si.krulik.homino.service.CommunicationService;
import si.krulik.homino.service.ControlNodeCommunicationRequest;

public class Configuration
{
    public Configuration (Context context, Activity activity)
    {
        this.activity = activity;
        runtimeSharedPreferences = context.getSharedPreferences ("si.krulik.homino.runtimeSharedPreferences", Context.MODE_PRIVATE);
        configurationSharedPreferences = context.getSharedPreferences ("si.krulik.homino.configurationSharedPreferences", Context.MODE_PRIVATE);

//        SharedPreferences.Editor editor = configurationSharedPreferences.edit ();
//        editor.putBoolean ("automation", true);
//        editor.commit ();
    }


    public void addControlNodes (ControlNode... nodes)
    {
        for (ControlNode node : nodes)
        {
            controlNodesById.put (node.name, node);
        }
    }


    public void addDevices (Device... devices)
    {
        for (Device device : devices)
        {
            this.devicesById.put (device.id, device);
        }
    }


    public Device getDevice (String name)
    {
        return (devicesById.get (name));
    }


    public Plate getPlate (String id)
    {
        return (platesById.get (id));
    }


    public void addPlatePages (PlatePage... platePages)
    {
        this.platePages.addAll (Arrays.asList (platePages));

        for (PlatePage page : platePages)
        {
            for (Plate plate : page.plates)
            {
                platesById.put (plate.id, plate);
            }
        }
    }


    public void setPlateMessageHandler (IMessageHandler handler)
    {
        plateMessageHandler = handler;
    }


    public void setCommunicationMessageHandler (IMessageHandler handler)
    {
        communicationMessageHandler = handler;
    }


    public void setPulseHandler (IPulseHandler handler)
    {
        pulseHandler = handler;
    }


    public void sendRequestToControlNode (String controlNodeName, String request)
    {
        ControlNode controlNode = controlNodesById.get (controlNodeName);
        Validate.notNull (controlNode, "Control node ", controlNodeName, " not found");
        Validate.notNull (communicationService, "Missing communication service");

        communicationService.write (new ControlNodeCommunicationRequest (controlNode.ip, request));
    }


    public CommunicationService communicationService;
    public int connectSettleMillis;
    public Activity activity;
    public List<PlatePage> platePages = new ArrayList<PlatePage> ();
    public Map<String, ControlNode> controlNodesById = new HashMap<String, ControlNode> ();
    public Map<String, Device> devicesById = new HashMap<String, Device> ();


    public Map<String, Plate> platesById = new HashMap<String, Plate> ();

    //  handlers
    public IMessageHandler plateMessageHandler;
    public IMessageHandler communicationMessageHandler;
    public IPulseHandler pulseHandler;


    private Logger logger = Logger.getLogger (Configuration.class.getName ());

    //
    public SharedPreferences runtimeSharedPreferences;
    public SharedPreferences configurationSharedPreferences;
}