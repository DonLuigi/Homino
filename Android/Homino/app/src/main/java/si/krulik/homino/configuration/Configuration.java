package si.krulik.homino.configuration;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import si.krulik.homino.R;
import si.krulik.homino.common.logger.CustomLogger;
import si.krulik.homino.common.validate.Validate;
import si.krulik.homino.configuration.command.Action;
import si.krulik.homino.configuration.device.AuthorizerDevice;
import si.krulik.homino.configuration.device.PhotoResistorDevice;
import si.krulik.homino.configuration.device.ShellDevice;
import si.krulik.homino.configuration.device.ThermometerDevice;
import si.krulik.homino.configuration.device.TimedRelayDevice;
import si.krulik.homino.configuration.device.WindowShutterDevice;
import si.krulik.homino.configuration.device.common.Device;
import si.krulik.homino.configuration.device.common.DeviceControlNode;
import si.krulik.homino.configuration.device.common.Devices;
import si.krulik.homino.configuration.plate.ActionPlate;
import si.krulik.homino.configuration.plate.ActionPlateRow;
import si.krulik.homino.configuration.plate.ActionPlateRowButton;
import si.krulik.homino.configuration.plate.IPulseHandler;
import si.krulik.homino.configuration.plate.common.IPlateActionHandler;
import si.krulik.homino.configuration.plate.common.Plate;
import si.krulik.homino.configuration.plate.common.PlatePage;
import si.krulik.homino.configuration.plate.common.PlatePosition;
import si.krulik.homino.configuration.plate.common.PlatesAndPages;
import si.krulik.homino.configuration.plate.device.AuthorizerPlate;
import si.krulik.homino.configuration.plate.device.PhotoResistorPlate;
import si.krulik.homino.configuration.plate.device.ShellPlate;
import si.krulik.homino.configuration.plate.device.ThermometerPlate;
import si.krulik.homino.configuration.plate.device.TimedRelayPlate;
import si.krulik.homino.configuration.plate.device.WindowShutterPlate;
import si.krulik.homino.message.IMultiMessageHandler;
import si.krulik.homino.message.Message;
import si.krulik.homino.message.MultiMessage;
import si.krulik.homino.network.HominoNetworkMessage;
import si.krulik.homino.network.HominoNetworkService;


public class Configuration
{
    // build
    enum Build
    {
        TabGroundFloor, TabFirstFloor, Phone
    }


    Build build = Build.Phone;


    // device control node ids
    enum DeviceControlNodeId
    {
        Main, Attic, LudvikPhone, BarbiPhone
    }


    // devices
    enum DeviceDescriptions
    {
        ShellMain ("ShellMain"), ShellAttic ("ShellAttic"), ShellLudvikPhone ("ShellLudvikPhone"),

        WindowShutterKitchenE ("WrsKitchE"), WindowShutterKitchenS ("WrsKitchS"), WindowLouvreShutterDiningRoomS ("WlsDiningS"), WindowLouvreShutterLivingRoomS ("WlsLivingS"), WindowShutterLivingRoomW ("WrsLivingW"),
        WindowShutterStairs ("WrsStairs"), WindowShutterBathroom0 ("WrsBath0"),

        WindowShutterBathroom1 ("WrsBathroom1"), WindowShutterKidsRoom ("WrsKids"), WindowLouvreShutterKidsRoom ("WlsKids"),

        WindowLouvreShutterGallery ("WlsGallery"),

        WindowShutterBedroom ("WrsBedroom"),

        HotWaterRecirculationPump ("HWPump"), TowelHeater ("TowelHeater"), FrontDoor ("FrontDoor"),

        RecouperatorOutputThermometer ("RecOutTemp"), OutsideThermometer ("OutTemp"), AtticThermometer ("AtticTemp"),

        PhotoResistor ("PhotoRes"), AtticVentilator ("Vent"), AtticDehumidifier ("Dehum"), RecouperatorX151 ("RecoupX151"),

        LudvikPhoneAutorizer ("LudvikPhoneAutorizer"), BarbiPhoneAutorizer ("BarbiPhoneAutorizer");


        DeviceDescriptions (String abbreviation)
        {
            this.abbreviation = abbreviation;
        }


        public String abbreviation;
    }


    @Getter private Devices devices = new Devices (new DeviceControlNode[]

        {
            new DeviceControlNode (DeviceControlNodeId.Main.name (), "192.168.1.50:5555"),

            new DeviceControlNode (DeviceControlNodeId.Attic.name (), "192.168.1.51:5555"),

            new DeviceControlNode (DeviceControlNodeId.LudvikPhone.name (), "localhost:5555"),

            new DeviceControlNode (DeviceControlNodeId.BarbiPhone.name (), "localhost:5555"),
        });


    // grid layout colors
    String white = "white";

    String cyan = "#40AfA5";
    String lighterCyan = "#50BfB5";

    String darkCyan = "#005f55";
    String lighterDarkCyan = "#106f65";

    String orange = "#FF9800";
    String lighterOrange = "#FFA810";

    String darkOrange = "#8F2800";
    String lighterDarkOrange = "#9F3810";

    String blue = "#4A65AC";
    String lighterBlue = "#5A75BC";

    String lightGray = "#6E6E6E";
    String lighterLightGray = "#7E7E7E";

    String darkGray = "#444444";
    String lighterDarkGray = "#555555";


    // action plates
    enum ActionPlateDescriptions
    {
        WindowShuttersLowerFloor, WindowShuttersKitchen, WindowShuttersLivingRoom
    }


    // plates
    // plate positions
    PlatePosition[][] platePositions = new PlatePosition[][]

        {
            // single columns
            null,


            // double columns
            new PlatePosition[]

                {
                    // page 1
                    new PlatePosition ("1", ActionPlateDescriptions.WindowShuttersLowerFloor.name (), 0, 0, 2, 2),

                    new PlatePosition ("1", DeviceDescriptions.FrontDoor.abbreviation, 2, 0, 2, 2),

                    new PlatePosition ("1", DeviceDescriptions.HotWaterRecirculationPump.abbreviation, 0, 2, 2, 2),

                    new PlatePosition ("1", DeviceDescriptions.WindowShutterKitchenE.abbreviation, 2, 2, 2, 2),

                    new PlatePosition ("1", DeviceDescriptions.WindowShutterKitchenS.abbreviation, 0, 4, 2, 2),

                    new PlatePosition ("1", ActionPlateDescriptions.WindowShuttersKitchen.name (), 2, 4, 2, 2),

                    new PlatePosition ("1", DeviceDescriptions.WindowLouvreShutterLivingRoomS.abbreviation, 0, 6, 2, 2),

                    new PlatePosition ("1", DeviceDescriptions.WindowShutterLivingRoomW.abbreviation, 2, 6, 2, 2),

                    new PlatePosition ("1", ActionPlateDescriptions.WindowShuttersLivingRoom.name (), 0, 8, 2, 2),

                    new PlatePosition ("1", DeviceDescriptions.WindowLouvreShutterDiningRoomS.abbreviation, 2, 8, 2, 2),

                    new PlatePosition ("1", DeviceDescriptions.WindowShutterStairs.abbreviation, 0, 10, 2, 2),

                    new PlatePosition ("1", DeviceDescriptions.WindowShutterBathroom0.abbreviation, 2, 10, 2, 2),


                    // page 2
                    new PlatePosition ("2", DeviceDescriptions.HotWaterRecirculationPump.abbreviation, 0, 0, 2, 2),

                    new PlatePosition ("2", DeviceDescriptions.TowelHeater.abbreviation, 2, 0, 2, 2),

                    new PlatePosition ("2", DeviceDescriptions.WindowShutterBathroom1.abbreviation, 0, 2, 2, 2),

                    new PlatePosition ("2", DeviceDescriptions.WindowShutterKidsRoom.abbreviation, 2, 2, 2, 2),

                    new PlatePosition ("2", DeviceDescriptions.WindowLouvreShutterKidsRoom.abbreviation, 0, 4, 2, 2),

                    new PlatePosition ("2", DeviceDescriptions.WindowLouvreShutterGallery.abbreviation, 2, 4, 2, 2),

                    new PlatePosition ("2", DeviceDescriptions.WindowShutterBedroom.abbreviation, 0, 6, 2, 2),


                    // page 3
                    new PlatePosition ("3", DeviceDescriptions.ShellMain.abbreviation, 0, 0, 2, 2),

                    new PlatePosition ("3", DeviceDescriptions.ShellAttic.abbreviation, 2, 0, 2, 2),

                    new PlatePosition ("3", DeviceDescriptions.OutsideThermometer.abbreviation, 0, 2, 2, 2),

                    new PlatePosition ("3", DeviceDescriptions.AtticThermometer.abbreviation, 2, 2, 2, 2),

                    new PlatePosition ("3", DeviceDescriptions.RecouperatorOutputThermometer.abbreviation, 0, 4, 2, 2),

                    new PlatePosition ("3", DeviceDescriptions.PhotoResistor.abbreviation, 2, 4, 2, 2),

                    new PlatePosition ("3", DeviceDescriptions.AtticDehumidifier.abbreviation, 0, 6, 2, 2),

                    new PlatePosition ("3", DeviceDescriptions.AtticVentilator.abbreviation, 2, 6, 2, 2),

                    new PlatePosition ("3", DeviceDescriptions.RecouperatorX151.abbreviation, 0, 8, 2, 2),

                    new PlatePosition ("3", DeviceDescriptions.ShellLudvikPhone.abbreviation, 2, 8, 2, 2),

                    new PlatePosition ("3", DeviceDescriptions.LudvikPhoneAutorizer.abbreviation, 0, 10, 2, 2),

                    // new PlatePosition ("3", DeviceDescriptions.BarbiPhoneAutorizer.abbreviation, 2, 6, 2, 2),
                },


            // triple columns
            new PlatePosition[]

                {
                    // page 1
                    new PlatePosition ("1", ActionPlateDescriptions.WindowShuttersLowerFloor.name (), 0, 0, 2, 2),

                    new PlatePosition ("1", DeviceDescriptions.FrontDoor.abbreviation, 2, 0, 2, 2),

                    new PlatePosition ("1", DeviceDescriptions.HotWaterRecirculationPump.abbreviation, 4, 0, 2, 2),

                    new PlatePosition ("1", DeviceDescriptions.WindowShutterKitchenE.abbreviation, 0, 2, 2, 2),

                    new PlatePosition ("1", DeviceDescriptions.WindowShutterKitchenS.abbreviation, 2, 2, 2, 2),

                    new PlatePosition ("1", ActionPlateDescriptions.WindowShuttersKitchen.name (), 4, 2, 2, 2),

                    new PlatePosition ("1", DeviceDescriptions.WindowLouvreShutterLivingRoomS.abbreviation, 0, 4, 2, 2),

                    new PlatePosition ("1", DeviceDescriptions.WindowShutterLivingRoomW.abbreviation, 2, 4, 2, 2),

                    new PlatePosition ("1", ActionPlateDescriptions.WindowShuttersLivingRoom.name (), 4, 4, 2, 2),

                    new PlatePosition ("1", DeviceDescriptions.WindowLouvreShutterDiningRoomS.abbreviation, 0, 6, 2, 2),

                    new PlatePosition ("1", DeviceDescriptions.WindowShutterStairs.abbreviation, 2, 6, 2, 2),

                    new PlatePosition ("1", DeviceDescriptions.WindowShutterBathroom0.abbreviation, 4, 6, 2, 2),


                    // page 2
                    new PlatePosition ("2", DeviceDescriptions.HotWaterRecirculationPump.abbreviation, 0, 0, 2, 2),

                    new PlatePosition ("2", DeviceDescriptions.TowelHeater.abbreviation, 2, 0, 2, 2),

                    new PlatePosition ("2", DeviceDescriptions.WindowShutterBathroom1.abbreviation, 0, 2, 2, 2),

                    new PlatePosition ("2", DeviceDescriptions.WindowShutterKidsRoom.abbreviation, 2, 2, 2, 2),

                    new PlatePosition ("2", DeviceDescriptions.WindowLouvreShutterKidsRoom.abbreviation, 4, 2, 2, 2),

                    new PlatePosition ("2", DeviceDescriptions.WindowLouvreShutterGallery.abbreviation, 0, 4, 2, 2),

                    new PlatePosition ("2", DeviceDescriptions.WindowShutterBedroom.abbreviation, 2, 4, 2, 2),


                    // page 3
                    new PlatePosition ("3", DeviceDescriptions.ShellMain.abbreviation, 0, 0, 2, 2),

                    new PlatePosition ("3", DeviceDescriptions.ShellAttic.abbreviation, 2, 0, 2, 2),

                    new PlatePosition ("3", DeviceDescriptions.OutsideThermometer.abbreviation, 4, 0, 2, 2),

                    new PlatePosition ("3", DeviceDescriptions.AtticThermometer.abbreviation, 0, 2, 2, 2),

                    new PlatePosition ("3", DeviceDescriptions.RecouperatorOutputThermometer.abbreviation, 2, 2, 2, 2),

                    new PlatePosition ("3", DeviceDescriptions.PhotoResistor.abbreviation, 4, 2, 2, 2),

                    new PlatePosition ("3", DeviceDescriptions.AtticDehumidifier.abbreviation, 0, 4, 2, 2),

                    new PlatePosition ("3", DeviceDescriptions.AtticVentilator.abbreviation, 2, 4, 2, 2),

                    new PlatePosition ("3", DeviceDescriptions.RecouperatorX151.abbreviation, 4, 4, 2, 2),

                    new PlatePosition ("3", DeviceDescriptions.ShellLudvikPhone.abbreviation, 0, 6, 2, 2),

                    new PlatePosition ("3", DeviceDescriptions.LudvikPhoneAutorizer.abbreviation, 2, 6, 2, 2),
                }
        };


    // plate pages
    PlatePage[] platePages = new PlatePage[]

        {
            new PlatePage ("1", "PRITLIČJE", 5),

            new PlatePage ("2", "NADSTROPJE", 5),

            new PlatePage ("3", "OSTALO", 5)
        };


    // plates
    Plate[] plates = new Plate[]

        {
            new ActionPlate (ActionPlateDescriptions.WindowShuttersLowerFloor.name (), white, blue, lighterBlue, "PRITLIČJE", new IPlateActionHandler ()
            {
                public MultiMessage handleAction (Action action, Configuration configuration)
                {
                    DeviceDescriptions deviceDescriptions[] = { //
                        DeviceDescriptions.WindowLouvreShutterLivingRoomS, //
                        DeviceDescriptions.WindowShutterKitchenE, //
                        DeviceDescriptions.WindowShutterKitchenS, //
                        DeviceDescriptions.WindowLouvreShutterDiningRoomS, //
                        DeviceDescriptions.WindowLouvreShutterLivingRoomS, //
                        DeviceDescriptions.WindowShutterLivingRoomW, //
                        DeviceDescriptions.WindowShutterStairs, //
                        DeviceDescriptions.WindowShutterBathroom0
                    };

                    MultiMessage multiMessage = new MultiMessage ();
                    for (DeviceDescriptions deviceDescription : deviceDescriptions)
                    {
                        Device device = configuration.getDevices ().getDevice (deviceDescription.abbreviation);
                        multiMessage.add (device.handleAction (action));
                    }

                    return (multiMessage);
                }
            },

                new ActionPlateRow (new ActionPlateRowButton (Action.Up, null, R.drawable.ic_action_arrow_top, null)),

                new ActionPlateRow (new ActionPlateRowButton (Action.Half, null, R.drawable.window_shutter_half, null),

                    new ActionPlateRowButton (Action.Stop, null, R.drawable.ic_action_cancel, null)),

                new ActionPlateRow (new ActionPlateRowButton (Action.Quarter, null, R.drawable.window_shutter_quarter, null),

                    new ActionPlateRowButton (Action.Down, null, R.drawable.ic_action_arrow_bottom, null))),


            new TimedRelayPlate (white, orange, lighterOrange, "VRATA", R.drawable.doorknob, true,

                new TimedRelayDevice (DeviceDescriptions.FrontDoor.abbreviation, DeviceControlNodeId.Main.name (), -1), null),


            new WindowShutterPlate (white, darkCyan, lighterDarkCyan, "KUHINJA V",

                new WindowShutterDevice (DeviceDescriptions.WindowShutterKitchenE.abbreviation, DeviceControlNodeId.Main.name (), -1, 97), null),


            new WindowShutterPlate (white, darkCyan, lighterDarkCyan, "KUHINJA J",

                new WindowShutterDevice (DeviceDescriptions.WindowShutterKitchenS.abbreviation, DeviceControlNodeId.Main.name (), -1, 97), null),


            new ActionPlate (ActionPlateDescriptions.WindowShuttersKitchen.name (), white, cyan, lighterCyan, "KUHINJA", new IPlateActionHandler ()
            {
                public MultiMessage handleAction (Action action, Configuration configuration)
                {
                    return (null);
                }
            },

                new ActionPlateRow (new ActionPlateRowButton (Action.Grid, null, R.drawable.ic_grid_on, null),

                    new ActionPlateRowButton (Action.Up, null, R.drawable.ic_action_arrow_top, null)),

                new ActionPlateRow (new ActionPlateRowButton (Action.Half, null, R.drawable.window_shutter_half, null),

                    new ActionPlateRowButton (Action.Stop, null, R.drawable.ic_action_cancel, null)),

                new ActionPlateRow (new ActionPlateRowButton (Action.Quarter, null, R.drawable.window_shutter_quarter, null),

                    new ActionPlateRowButton (Action.Down, null, R.drawable.ic_action_arrow_bottom, null))),


            new WindowShutterPlate (white, darkOrange, lighterDarkOrange, "DNEVNA J",

                new WindowShutterDevice (DeviceDescriptions.WindowLouvreShutterLivingRoomS.abbreviation, DeviceControlNodeId.Main.name (), -1, 97), null),

            new WindowShutterPlate (white, darkOrange, lighterDarkOrange, "DNEVNA Z",

                new WindowShutterDevice (DeviceDescriptions.WindowShutterLivingRoomW.abbreviation, DeviceControlNodeId.Main.name (), -1, 97), null),


            new ActionPlate (ActionPlateDescriptions.WindowShuttersLivingRoom.name (), white, orange, lighterOrange, "DNEVNA", new IPlateActionHandler ()
            {
                public MultiMessage handleAction (Action action, Configuration configuration)
                {
                    return (null);
                }
            },

                new ActionPlateRow (

                    new ActionPlateRowButton (Action.Grid, null, R.drawable.ic_grid_on, null),

                    new ActionPlateRowButton (Action.Up, null, R.drawable.ic_action_arrow_top, null)),

                new ActionPlateRow (new ActionPlateRowButton (Action.Half, null, R.drawable.window_shutter_half, null),

                    new ActionPlateRowButton (Action.Stop, null, R.drawable.ic_action_cancel, null)),

                new ActionPlateRow (new ActionPlateRowButton (Action.Quarter, null, R.drawable.window_shutter_quarter, null),

                    new ActionPlateRowButton (Action.Down, null, R.drawable.ic_action_arrow_bottom, null))),


            new WindowShutterPlate (white, lightGray, lighterLightGray, "JEDILNICA",

                new WindowShutterDevice (DeviceDescriptions.WindowLouvreShutterDiningRoomS.abbreviation, DeviceControlNodeId.Main.name (), 20, 97), null),


            new WindowShutterPlate (white, lightGray, lighterLightGray, "STOPNIŠČE",

                new WindowShutterDevice (DeviceDescriptions.WindowShutterStairs.abbreviation, DeviceControlNodeId.Main.name (), -1, 97), null),


            new WindowShutterPlate (white, lightGray, lighterLightGray, "KOP. P",

                new WindowShutterDevice (DeviceDescriptions.WindowShutterBathroom0.abbreviation, DeviceControlNodeId.Main.name (), -1, 97), null),


            new TimedRelayPlate (white, orange, lighterOrange, "VODA", R.drawable.faucet, false,

                new TimedRelayDevice (DeviceDescriptions.HotWaterRecirculationPump.abbreviation, DeviceControlNodeId.Main.name (), 60 * 1000), null),


            new TimedRelayPlate (white, darkOrange, lighterDarkOrange, "BRISAČE", R.drawable.towel, false,

                new TimedRelayDevice (DeviceDescriptions.TowelHeater.abbreviation, DeviceControlNodeId.Main.name (), -1), null),


            new WindowShutterPlate (white, darkCyan, lighterDarkCyan, "KOPALNICA",

                new WindowShutterDevice (DeviceDescriptions.WindowShutterBathroom1.abbreviation, DeviceControlNodeId.Main.name (), -1, 97), null),

            new WindowShutterPlate (white, darkCyan, lighterDarkCyan, "OTROŠKA V",

                new WindowShutterDevice (DeviceDescriptions.WindowShutterKidsRoom.abbreviation, DeviceControlNodeId.Main.name (), -1, 97), null),


            new WindowShutterPlate (white, darkOrange, lighterDarkOrange, "OTROŠKA J",

                new WindowShutterDevice (DeviceDescriptions.WindowLouvreShutterKidsRoom.abbreviation, DeviceControlNodeId.Main.name (), 20, 97), null),


            new WindowShutterPlate (white, darkOrange, lighterDarkOrange, "GALERIJA",

                new WindowShutterDevice (DeviceDescriptions.WindowLouvreShutterGallery.abbreviation, DeviceControlNodeId.Main.name (), 20, 97), null),


            new WindowShutterPlate (white, lightGray, lighterLightGray, "SPALNICA",

                new WindowShutterDevice (DeviceDescriptions.WindowShutterBedroom.abbreviation, DeviceControlNodeId.Main.name (), -1, 97), null),


            new ShellPlate (white, lightGray, lighterLightGray, "KURILNICA",

                new ShellDevice (DeviceDescriptions.ShellMain.abbreviation, DeviceControlNodeId.Main.name (), 0, -1), null),


            new ShellPlate (white, lightGray, lighterLightGray, "PODSTREŠJE",

                new ShellDevice (DeviceDescriptions.ShellAttic.abbreviation, DeviceControlNodeId.Attic.name (), 5 * 1000, 5 * 1000), null),


            new ThermometerPlate (white, lightGray, lighterLightGray, "PODSTREŠJE",

                new ThermometerDevice (DeviceDescriptions.AtticThermometer.abbreviation, DeviceControlNodeId.Attic.name (), 5 * 1000), null),


            new ThermometerPlate (white, lightGray, lighterLightGray, "ZUNAJ",

                new ThermometerDevice (DeviceDescriptions.OutsideThermometer.abbreviation, DeviceControlNodeId.Attic.name (), 0), null),


            new ThermometerPlate (white, lightGray, lighterLightGray, "PREZRAČEVANJE",

                new ThermometerDevice (DeviceDescriptions.RecouperatorOutputThermometer.abbreviation, DeviceControlNodeId.Attic.name (), 0), null),


            new PhotoResistorPlate (white, lightGray, lighterLightGray, "SVETLOBA",

                new PhotoResistorDevice (DeviceDescriptions.PhotoResistor.abbreviation, DeviceControlNodeId.Attic.name (), 5 * 1000), null),


            new TimedRelayPlate (white, darkOrange, lighterDarkOrange, "VENTILATOR", R.drawable.fan, false,

                new TimedRelayDevice (DeviceDescriptions.AtticVentilator.abbreviation, DeviceControlNodeId.Attic.name (), (3 * 60 + 10) * 60 * 1000), null),


            new TimedRelayPlate (white, darkOrange, lighterDarkOrange, "RAZVLAŽEVALEC", R.drawable.humidifier, false,

                new TimedRelayDevice (DeviceDescriptions.AtticDehumidifier.abbreviation, DeviceControlNodeId.Attic.name (), 3 * 60 * 60 * 1000), null),


            new TimedRelayPlate (white, darkOrange, lighterDarkOrange, "PREZRAČEVANJE", R.drawable.ventilation, false,

                new TimedRelayDevice (DeviceDescriptions.RecouperatorX151.abbreviation, DeviceControlNodeId.Attic.name (), 3 * 60 * 1000), null),


            new ShellPlate (white, lightGray, lighterLightGray, "LUDVIK",

                new ShellDevice (DeviceDescriptions.ShellLudvikPhone.abbreviation, DeviceControlNodeId.LudvikPhone.name (), 0, 60 * 1000), null),

            new AuthorizerPlate (white, darkOrange, lighterDarkOrange, "LUDVIK",

                new AuthorizerDevice (DeviceDescriptions.LudvikPhoneAutorizer.abbreviation, DeviceControlNodeId.LudvikPhone.name (), 0), null),


        };


    @Getter private PlatesAndPages platesAndPages;


    public Configuration (Context context, Activity activity)
    {
        // prologue
        logger.info ("Construct");
        this.context = context;
        this.activity = activity;


        // determine number of columns
        DisplayMetrics displayMetrics = context.getResources ().getDisplayMetrics ();
        float widthInDp = displayMetrics.widthPixels / displayMetrics.density;
        int columns = (int) widthInDp / 175;
        logger.fine ("Screen: widthPixels ", displayMetrics.widthPixels, ", density ", displayMetrics.density, ", columns ", columns);

        int plateBoxHorizontalSizeInPixels = displayMetrics.widthPixels / columns;
        int plateBoxVerticalSizeInPixels = (plateBoxHorizontalSizeInPixels * 8) / 7;


        // Validate.isTrue (columns > 0, "Missing configuration for ", columns, " columns");
        columns = (columns < platePositions.length ? columns : platePositions.length);
        platesAndPages = new PlatesAndPages (platePages, plates, platePositions[columns - 1], plateBoxHorizontalSizeInPixels/2, plateBoxVerticalSizeInPixels/2);


        runtimeSharedPreferences = context.getSharedPreferences ("si.krulik.homino.runtimeSharedPreferences", Context.MODE_PRIVATE);
        configurationSharedPreferences = context.getSharedPreferences ("si.krulik.homino.configurationSharedPreferences", Context.MODE_PRIVATE);

        //        SharedPreferences.Editor editor = configurationSharedPreferences.edit ();
        //        editor.putBoolean ("automation", true);
        //        editor.commit ();


        // collect devices from plates
        logger.info ("Collecting devices from ", platesAndPages.getPlates ().size (), " plates");
        for (Plate plate : platesAndPages.getPlates ())
        {
            Device device = plate.getDevice ();
            if (device != null)
            {
                logger.info (device, " from ", plate);
                devices.addDevice (plate.getDevice (), this);
            }
        }
    }


    // pulse handler
    @Getter private IPulseHandler pulseHandler = new PulseHandler ();


    // communication service
    @Getter @Setter private HominoNetworkService hominoNetworkService;


    public void flushMessages (MultiMessage multiMessage)
    {
        if (multiMessage != null)
        {
            for (Message message : multiMessage.getMessages ())
            {
                logger.fine ("Flushing ", message);
                DeviceControlNode deviceControlNode = devices.getDeviceControlNodesById ().get (message.getDeviceControlNodeId ());
                Validate.notNull (deviceControlNode, "Device control node ", message.getDeviceControlNodeId (), " not found");
                Validate.notNull (hominoNetworkService, "Missing communication service");

                hominoNetworkService.write (new HominoNetworkMessage (deviceControlNode.getId (), deviceControlNode.getNetworkAddress (), message.toString (), false));
            }
        }
    }


    public void error (String deviceControlNodeId, String message)
    {
        Error error = new Error (message, new Date (), deviceControlNodeId);
        logger.fine ("ERROR: ", message);

        errors.add (error);
        while (errors.size () > 100)
        {
            errors.remove (errors.size () - 1);
        }
    }


    public void log (Date now)
    {
        //        String state = Environment.getExternalStorageState ();
        //        if (!Environment.MEDIA_MOUNTED.equals (state))
        //        {
        //            return;
        //        }
        //
        //
        //        try
        //        {
        //            Method checkSelfPermissionMethod = Context.class.getMethod ("checkSelfPermission", String.class);
        //            if ((int) checkSelfPermissionMethod.invoke (context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
        //            {
        //                Method requestPermissionsMethod = Activity.class.getMethod ("requestPermissions", String.class);
        //                requestPermissionsMethod.invoke (activity, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 112);
        //            }
        //
        //
        //            return;
        //        }
        //        catch (Exception e)
        //        {
        //            logger.severe (e, "fucked up");
        //        }
        ////            Activity.requestPermissions (new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 112);
        //
        //
        //        //    @Override
        //        //    public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //        //        int[] grantResults) {
        //        //        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //        //        // The result of the popup opened with the requestPermissions() method
        //        //        // is in that method, you need to check that your application comes here
        //        //        if (requestCode == 112) {
        //        //            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        //        //                // write
        //        //            }
        //        //        }
        //        //    }
        //
        //
        //        // collect logs
        //        String logEntries = "";
        //        for (Device device : getDevices ().getDevices ())
        //        {
        //            String logEntry = device.log (now);
        //            if (logEntry != null)
        //            {
        //                logEntries += logEntry + System.lineSeparator ();
        //            }
        //        }
        //
        //
        //        if (!logEntries.isEmpty ())
        //        {
        //
        //            File file = new File (Environment.getExternalStoragePublicDirectory (Environment.DIRECTORY_DOCUMENTS), String.format ("HOMINO_%d%02d%02d.CSV", now.getYear (), now.getMonth (), now.getDay ()));
        //
        //            FileOutputStream outputStream = null;
        //            try
        //            {
        //                file.createNewFile ();
        //                outputStream = new FileOutputStream (file, true);
        //                outputStream.write (logEntries.getBytes ());
        //                outputStream.close ();
        //            }
        //            catch (Exception e)
        //            {
        //                error ("configuration", e.getMessage ());
        //            }
        //        }
    }


    static final private String CSV_DELIMITER = ",";
    static final private SimpleDateFormat myDateFormat = new SimpleDateFormat ("ddMMyyyy HH:mm:ss");
    private long lastUpdateSeconds;


    private int connectSettleMillis;
    private Activity activity;


    //  handlers
    public IMultiMessageHandler plateMessageHandler;


    // preferences
    public SharedPreferences runtimeSharedPreferences;
    public SharedPreferences configurationSharedPreferences;


    // context
    Context context;


    // errors
    List<Error> errors = new LinkedList ();


    // logger
    static private CustomLogger logger = CustomLogger.getLogger ("CONFIGURATION");
}