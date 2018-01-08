package si.krulik.homino.runtime;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import si.krulik.homino.R;
import si.krulik.homino.alarms.Alarms;
import si.krulik.homino.common.logger.CustomLogger;
import si.krulik.homino.devices.PhotoResistorDevice;
import si.krulik.homino.devices.ShellDevice;
import si.krulik.homino.devices.ThermometerDevice;
import si.krulik.homino.devices.TimedRelayDevice;
import si.krulik.homino.devices.WindowShutterDevice;
import si.krulik.homino.devices.base.Device;
import si.krulik.homino.devices.base.DeviceControlNode;
import si.krulik.homino.devices.base.Devices;
import si.krulik.homino.message.MultiMessage;
import si.krulik.homino.network.HominoNetworkService;
import si.krulik.homino.plates.ActionPlate;
import si.krulik.homino.plates.ActionPlateRow;
import si.krulik.homino.plates.ActionPlateRowButton;
import si.krulik.homino.plates.ClockPlate;
import si.krulik.homino.plates.IPulseHandler;
import si.krulik.homino.plates.SettingsPlate;
import si.krulik.homino.plates.base.IPlateActionHandler;
import si.krulik.homino.plates.base.Plate;
import si.krulik.homino.plates.base.PlatePage;
import si.krulik.homino.plates.base.PlatePosition;
import si.krulik.homino.plates.base.PlatesAndPages;
import si.krulik.homino.plates.fromdevices.PhotoResistorPlate;
import si.krulik.homino.plates.fromdevices.ShellPlate;
import si.krulik.homino.plates.fromdevices.ThermometerPlate;
import si.krulik.homino.plates.fromdevices.TimedRelayPlate;
import si.krulik.homino.plates.fromdevices.WindowShutterPlate;
import si.krulik.homino.settings.Settings;

import static si.krulik.homino.Constants.*;


public class Runtime
{
    // device control nodes
    static final public String ssid = "DonLuigi";

    static public DeviceControlNode mainArduinoDeviceControlNode = new DeviceControlNode ("MainArduino", "192.168.1.50:5555", ssid);

    static public DeviceControlNode atticArduinoDeviceControlNode = new DeviceControlNode ("AtticArduino", "192.168.1.51:5555", ssid);

    static public DeviceControlNode groundFloorAndroidDeviceControlNode = new DeviceControlNode ("GroundFloorAndroid", "192.168.1.152:5555", ssid);

    static public DeviceControlNode firstFloorAndroidDeviceControlNode = new DeviceControlNode ("FirstFloorAndroid", "192.168.1.153:5555", ssid);

    static public DeviceControlNode ludvikPhoneDeviceControlNode = new DeviceControlNode ("LudvikPhone", "localhost:5555", null);

    static public DeviceControlNode deviceControlNodeBarbiPhone = new DeviceControlNode ("BarbiPhone", "localhost:5555", null);


    // devices
    static int venetialBlindDefaultRotationPercent = 20;

    static public TimedRelayDevice frontDoorDevice = new TimedRelayDevice ("FrontDoor", mainArduinoDeviceControlNode, -1);

    static public WindowShutterDevice windowShutterKitchenEDevice = new WindowShutterDevice ("WrsKitchE", mainArduinoDeviceControlNode, -1, 97);

    static public WindowShutterDevice windowShutterKitchenSDevice = new WindowShutterDevice ("WrsKitchS", mainArduinoDeviceControlNode, -1, 97);

    static public WindowShutterDevice windowLouvreShutterLivingRoomSDevice = new WindowShutterDevice ("WlsLivingS", mainArduinoDeviceControlNode, venetialBlindDefaultRotationPercent, 97);

    static public WindowShutterDevice windowShutterLivingRoomWDevice = new WindowShutterDevice ("WrsLivingW", mainArduinoDeviceControlNode, -1, 97);

    static public WindowShutterDevice windowLouvreShutterDiningRoomSDevice = new WindowShutterDevice ("WlsDiningS", mainArduinoDeviceControlNode, venetialBlindDefaultRotationPercent, 97);

    static public WindowShutterDevice windowShutterStairsDevice = new WindowShutterDevice ("WrsStairs", mainArduinoDeviceControlNode, -1, 97);

    static public WindowShutterDevice windowShutterBathroom0Device = new WindowShutterDevice ("WrsBath0", mainArduinoDeviceControlNode, -1, 97);

    static public TimedRelayDevice hotWaterRecirculationPumpDevice = new TimedRelayDevice ("HWPump", mainArduinoDeviceControlNode, 60 * 1000);

    static public TimedRelayDevice towelHeaterDevice = new TimedRelayDevice ("TowelHeater", mainArduinoDeviceControlNode, -1);

    static public WindowShutterDevice windowShutterBathroom1Device = new WindowShutterDevice ("WrsBathroom1", mainArduinoDeviceControlNode, -1, 97);

    static public WindowShutterDevice windowShutterKidsRoomDevice = new WindowShutterDevice ("WrsKids", mainArduinoDeviceControlNode, -1, 97);

    static public WindowShutterDevice windowLouvreShutterKidsRoomDevice = new WindowShutterDevice ("WlsKids", mainArduinoDeviceControlNode, venetialBlindDefaultRotationPercent, 97);

    static public WindowShutterDevice windowLouvreShutterGalleryDevice = new WindowShutterDevice ("WlsGallery", mainArduinoDeviceControlNode, venetialBlindDefaultRotationPercent, 97);

    static public WindowShutterDevice windowShutterBedroomDevice = new WindowShutterDevice ("WrsBedroom", mainArduinoDeviceControlNode, -1, 97);

    static public ShellDevice shellMainDevice = new ShellDevice ("ShellMain", mainArduinoDeviceControlNode, 5 * 1000, 5 * 1000);

    static public ShellDevice shellAtticDevice = new ShellDevice ("ShellAttic", atticArduinoDeviceControlNode, 5 * 1000, 5 * 1000);

    static public ThermometerDevice atticThermometerDevice = new ThermometerDevice ("AtticTemp", atticArduinoDeviceControlNode, 5 * 1000);

    static public ThermometerDevice outsideThermometerDevice = new ThermometerDevice ("OutTemp", atticArduinoDeviceControlNode, 0);

    static public ThermometerDevice recouperatorOutputThermometerDevice = new ThermometerDevice ("RecOutTemp", atticArduinoDeviceControlNode, 0);

    static public PhotoResistorDevice photoResistorDevice = new PhotoResistorDevice ("PhotoRes", atticArduinoDeviceControlNode, 5 * 1000);

    static public TimedRelayDevice atticVentilatorDevice = new TimedRelayDevice ("Vent", atticArduinoDeviceControlNode, (3 * 60 + 10) * 60 * 1000);

    static public TimedRelayDevice atticDehumidifierDevice = new TimedRelayDevice ("Dehum", atticArduinoDeviceControlNode, 3 * 60 * 60 * 1000);

    static public TimedRelayDevice recouperatorX151Device = new TimedRelayDevice ("RecoupX151", atticArduinoDeviceControlNode, 3 * 60 * 1000);

    static public ShellDevice shellLudvikPhoneDevice = new ShellDevice ("ShellLudvikPhone", ludvikPhoneDeviceControlNode, 0, 60 * 1000);

    static public TimedRelayDevice eatingRoomLightDevice = new TimedRelayDevice ("ERL", mainArduinoDeviceControlNode, 6 * 60 * 60 * 1000);

    static public TimedRelayDevice entryRoomLightDevice = new TimedRelayDevice ("EntRL", mainArduinoDeviceControlNode, 5 * 60 * 1000);

    static public TimedRelayDevice outsideLightsDevice = new TimedRelayDevice ("OutL", mainArduinoDeviceControlNode, 5 * 60 * 1000);


    static public Devices devices =
        new Devices (frontDoorDevice, windowShutterKitchenEDevice, windowShutterKitchenSDevice, windowLouvreShutterLivingRoomSDevice, windowShutterLivingRoomWDevice, windowLouvreShutterDiningRoomSDevice, windowShutterStairsDevice,
            windowShutterBathroom0Device, hotWaterRecirculationPumpDevice, towelHeaterDevice, windowShutterBathroom1Device, windowShutterKidsRoomDevice, windowLouvreShutterKidsRoomDevice, windowLouvreShutterGalleryDevice, windowShutterBedroomDevice,
            shellMainDevice, shellAtticDevice, atticThermometerDevice, outsideThermometerDevice, recouperatorOutputThermometerDevice, photoResistorDevice, atticVentilatorDevice, atticDehumidifierDevice, recouperatorX151Device, shellLudvikPhoneDevice,
            eatingRoomLightDevice, entryRoomLightDevice, outsideLightsDevice);


    // layout colors
    static final public String white = "white";
    static final public String yellow = "yellow";

    static final public String cyan = "#40AfA5";
    static final public String lighterCyan = "#50BfB5";

    static final public String darkCyan = "#005f55";
    static final public String lighterDarkCyan = "#106f65";

    static final public String orange = "#FF9800";
    static final public String lighterOrange = "#FFA810";

    static final public String darkOrange = "#8F2800";
    static final public String lighterDarkOrange = "#9F3810";

    static final public String blue = "#4A65AC";
    static final public String lighterBlue = "#5A75BC";

    static final public String lightGray = "#6E6E6E";
    static final public String lighterLightGray = "#7E7E7E";

    static final public String darkGray = "#444444";
    static final public String lighterDarkGray = "#555555";


    static final public String red = "#F44F1F";
    static final public String lighterRed = "#FD7043";


    // action plates
    enum ActionPlateDescriptions
    {
        WindowShuttersLowerFloor, WindowShuttersKitchen, WindowShuttersLivingRoom
    }


    // plate positions
    static final private PlatePosition[] fourColumnsPlatePositions = new PlatePosition[]

        {
            // page 1
            new PlatePosition ("1", frontDoorDevice.getId (), 0, 0, 2, 2),

            new PlatePosition ("1", hotWaterRecirculationPumpDevice.getId (), 2, 0, 2, 2),

            new PlatePosition ("1", windowShutterKitchenEDevice.getId (), 0, 2, 2, 2),

            new PlatePosition ("1", windowShutterKitchenSDevice.getId (), 2, 2, 2, 2),

            new PlatePosition ("1", windowLouvreShutterLivingRoomSDevice.getId (), 0, 4, 2, 2),

            new PlatePosition ("1", windowShutterLivingRoomWDevice.getId (), 2, 4, 2, 2),

            new PlatePosition ("1", windowLouvreShutterDiningRoomSDevice.getId (), 0, 6, 2, 2),

            new PlatePosition ("1", windowShutterStairsDevice.getId (), 2, 6, 2, 2),

            new PlatePosition ("1", windowShutterBathroom0Device.getId (), 0, 8, 2, 2),

            new PlatePosition ("1", SETTINGS, 2, 8, 2, 2),


            // page 2
            new PlatePosition ("2", towelHeaterDevice.getId (), 0, 0, 2, 2),

            new PlatePosition ("2", windowShutterBedroomDevice.getId (), 2, 0, 2, 2),

            new PlatePosition ("2", windowShutterBathroom1Device.getId (), 0, 2, 2, 2),

            new PlatePosition ("2", windowShutterKidsRoomDevice.getId (), 2, 2, 2, 2),

            new PlatePosition ("2", windowLouvreShutterKidsRoomDevice.getId (), 0, 4, 2, 2),

            new PlatePosition ("2", windowLouvreShutterGalleryDevice.getId (), 2, 4, 2, 2),


            // page 3
            new PlatePosition ("3", shellMainDevice.getId (), 0, 0, 2, 2),

            new PlatePosition ("3", shellAtticDevice.getId (), 2, 0, 2, 2),

            new PlatePosition ("3", SETTINGS, 0, 2, 2, 2),

            new PlatePosition ("3", outsideThermometerDevice.getId (), 2, 2, 2, 2),

            new PlatePosition ("3", atticThermometerDevice.getId (), 0, 4, 2, 2),

            new PlatePosition ("3", recouperatorOutputThermometerDevice.getId (), 2, 4, 2, 2),

            new PlatePosition ("3", photoResistorDevice.getId (), 0, 6, 2, 2),

            new PlatePosition ("3", atticDehumidifierDevice.getId (), 2, 6, 2, 2),

            new PlatePosition ("3", atticVentilatorDevice.getId (), 0, 8, 2, 2),

            new PlatePosition ("3", shellLudvikPhoneDevice.getId (), 2, 8, 2, 2),

            new PlatePosition ("3", entryRoomLightDevice.getId (), 2, 10, 2, 2),

            new PlatePosition ("3", eatingRoomLightDevice.getId (), 0, 12, 2, 2),

            new PlatePosition ("3", outsideLightsDevice.getId (), 2, 12, 2, 2),
        };


    static final private PlatePosition[] sixColumnsPlatePositions = new PlatePosition[]

        {
            // page 1
            new PlatePosition ("1", frontDoorDevice.getId (), 0, 0, 2, 2),

            new PlatePosition ("1", CLOCK, 2, 0, 2, 1),

            new PlatePosition ("1", outsideThermometerDevice.getId (), 2, 1, 2, 1),

            new PlatePosition ("1", hotWaterRecirculationPumpDevice.getId (), 4, 0, 2, 2),

            new PlatePosition ("1", windowShutterKitchenEDevice.getId (), 0, 2, 2, 2),

            new PlatePosition ("1", windowShutterKitchenSDevice.getId (), 2, 2, 2, 2),

            new PlatePosition ("1", windowLouvreShutterLivingRoomSDevice.getId (), 0, 4, 2, 2),

            new PlatePosition ("1", windowShutterLivingRoomWDevice.getId (), 2, 4, 2, 2),

            new PlatePosition ("1", windowLouvreShutterDiningRoomSDevice.getId (), 0, 6, 2, 2),

            new PlatePosition ("1", windowShutterStairsDevice.getId (), 2, 6, 2, 2),

            new PlatePosition ("1", windowShutterBathroom0Device.getId (), 4, 6, 2, 2),


            // page 2
            new PlatePosition ("2", hotWaterRecirculationPumpDevice.getId (), 0, 0, 2, 2),

            new PlatePosition ("2", CLOCK, 2, 0, 2, 1),

            new PlatePosition ("2", outsideThermometerDevice.getId (), 2, 1, 2, 1),

            new PlatePosition ("2", windowShutterBathroom1Device.getId (), 0, 2, 2, 2),

            new PlatePosition ("2", windowShutterKidsRoomDevice.getId (), 2, 2, 2, 2),

            new PlatePosition ("2", windowLouvreShutterKidsRoomDevice.getId (), 4, 2, 2, 2),

            new PlatePosition ("2", windowLouvreShutterGalleryDevice.getId (), 0, 4, 2, 2),

            new PlatePosition ("2", windowShutterBedroomDevice.getId (), 2, 4, 2, 2),

            new PlatePosition ("2", recouperatorX151Device.getId (), 4, 4, 2, 2),

            new PlatePosition ("2", towelHeaterDevice.getId (), 0, 6, 2, 2),


            // page 3
            new PlatePosition ("3", shellMainDevice.getId (), 0, 0, 2, 2),

            new PlatePosition ("3", shellAtticDevice.getId (), 2, 0, 2, 2),

            new PlatePosition ("3", SETTINGS, 4, 0, 1, 1),

            new PlatePosition ("3", atticThermometerDevice.getId (), 0, 2, 2, 2),

            new PlatePosition ("3", recouperatorOutputThermometerDevice.getId (), 2, 2, 2, 2),

            new PlatePosition ("3", photoResistorDevice.getId (), 4, 2, 2, 2),

            new PlatePosition ("3", atticDehumidifierDevice.getId (), 0, 4, 2, 2),

            new PlatePosition ("3", atticVentilatorDevice.getId (), 2, 4, 2, 2),

            new PlatePosition ("3", shellLudvikPhoneDevice.getId (), 0, 6, 2, 2),

            new PlatePosition ("3", entryRoomLightDevice.getId (), 4, 4, 2, 2),

            new PlatePosition ("3", eatingRoomLightDevice.getId (), 4, 6, 2, 2),

            new PlatePosition ("3", outsideLightsDevice.getId (), 0, 8, 2, 2),
        };


    static public PlatePosition[][] platePositions = new PlatePosition[][]

        {
            null, // 1 column
            null, // 2 columns
            null, // 3 columns
            fourColumnsPlatePositions, // 4 columns
            fourColumnsPlatePositions, // 5 columns
            sixColumnsPlatePositions, // 6 columns
        };


    // plate pages
    static public PlatePage[] platePages = new PlatePage[]

        {
            new PlatePage ("1", "PRITLIČJE", 5),

            new PlatePage ("2", "NADSTROPJE", 5),

            new PlatePage ("3", "OSTALO", 5)
        };


    // plates
    static public Plate[] plates = new Plate[]

        {
            new ActionPlate (ActionPlateDescriptions.WindowShuttersLowerFloor.name (), white, blue, lighterBlue, "PRITLIČJE", R.layout.action_plate_layout, new IPlateActionHandler ()
            {
                public MultiMessage handleAction (Action action)
                {
                    MultiMessage multiMessage = new MultiMessage ();
                    for (Device device : new Device[] {
                        windowLouvreShutterLivingRoomSDevice, //
                        windowShutterKitchenEDevice, //
                        windowShutterKitchenSDevice, //
                        windowLouvreShutterDiningRoomSDevice, //
                        windowLouvreShutterLivingRoomSDevice, //
                        windowShutterLivingRoomWDevice, //
                        windowShutterStairsDevice, //
                        windowShutterBathroom0Device
                    })
                    {
                        multiMessage.add (device.handleAction (action));
                    }

                    return (multiMessage);
                }
            },

                new ActionPlateRow (new ActionPlateRowButton (null, Action.Up, null, R.drawable.ic_action_arrow_top)),

                new ActionPlateRow (new ActionPlateRowButton (null, Action.Half, null, R.drawable.window_shutter_half),

                    new ActionPlateRowButton (null, Action.Stop, null, R.drawable.ic_action_cancel)),

                new ActionPlateRow (new ActionPlateRowButton (null, Action.Quarter, null, R.drawable.window_shutter_quarter),

                    new ActionPlateRowButton (null, Action.Down, null, R.drawable.ic_action_arrow_bottom))),


            new TimedRelayPlate (white, orange, lighterOrange, "VRATA", R.layout.timed_relay_plate, R.drawable.doorknob, null, true, frontDoorDevice, null),


            new WindowShutterPlate (white, darkCyan, lighterDarkCyan, "KUHINJA V", R.layout.window_rolling_shutter_layout, windowShutterKitchenEDevice, null),


            new WindowShutterPlate (white, darkCyan, lighterDarkCyan, "KUHINJA J", R.layout.window_rolling_shutter_layout, windowShutterKitchenSDevice, null),


            new ActionPlate (ActionPlateDescriptions.WindowShuttersKitchen.name (), white, cyan, lighterCyan, "KUHINJA", R.layout.action_plate_layout, new IPlateActionHandler ()
            {
                public MultiMessage handleAction (Action action)
                {
                    return (null);
                }
            },

                new ActionPlateRow (new ActionPlateRowButton (null, Action.Grid, null, R.drawable.ic_grid_on),

                    new ActionPlateRowButton (null, Action.Up, null, R.drawable.ic_action_arrow_top)),

                new ActionPlateRow (new ActionPlateRowButton (null, Action.Half, null, R.drawable.window_shutter_half),

                    new ActionPlateRowButton (null, Action.Stop, null, R.drawable.ic_action_cancel)),

                new ActionPlateRow (new ActionPlateRowButton (null, Action.Quarter, null, R.drawable.window_shutter_quarter),

                    new ActionPlateRowButton (null, Action.Down, null, R.drawable.ic_action_arrow_bottom))),


            new WindowShutterPlate (white, darkOrange, lighterDarkOrange, "DNEVNA J", R.layout.window_venetian_blind_layout, windowLouvreShutterLivingRoomSDevice, null),


            new WindowShutterPlate (white, darkOrange, lighterDarkOrange, "DNEVNA Z", R.layout.window_rolling_shutter_layout, windowShutterLivingRoomWDevice, null),


            new ActionPlate (ActionPlateDescriptions.WindowShuttersLivingRoom.name (), white, orange, lighterOrange, "DNEVNA", R.layout.action_plate_layout, new IPlateActionHandler ()
            {
                public MultiMessage handleAction (Action action)
                {
                    return (null);
                }
            },

                new ActionPlateRow (

                    new ActionPlateRowButton (null, Action.Grid, null, R.drawable.ic_grid_on),

                    new ActionPlateRowButton (null, Action.Up, null, R.drawable.ic_action_arrow_top)),

                new ActionPlateRow (new ActionPlateRowButton (null, Action.Half, null, R.drawable.window_shutter_half),

                    new ActionPlateRowButton (null, Action.Stop, null, R.drawable.ic_action_cancel)),

                new ActionPlateRow (new ActionPlateRowButton (null, Action.Quarter, null, R.drawable.window_shutter_quarter),

                    new ActionPlateRowButton (null, Action.Down, null, R.drawable.ic_action_arrow_bottom))),


            new WindowShutterPlate (white, lightGray, lighterLightGray, "JEDILNICA", R.layout.window_venetian_blind_layout, windowLouvreShutterDiningRoomSDevice, null),


            new WindowShutterPlate (white, lightGray, lighterLightGray, "STOPNIŠČE", R.layout.window_rolling_shutter_layout, windowShutterStairsDevice, null),


            new WindowShutterPlate (white, lightGray, lighterLightGray, "KOP. P", R.layout.window_rolling_shutter_layout, windowShutterBathroom0Device, null),


            new TimedRelayPlate (white, orange, lighterOrange, "VODA", R.layout.timed_relay_plate, R.drawable.faucet, R.drawable.animate_hot_water_pump, false, hotWaterRecirculationPumpDevice, null),


            new TimedRelayPlate (white, darkOrange, lighterDarkOrange, "BRISAČE", R.layout.timed_relay_plate, R.drawable.towel, null, false, towelHeaterDevice, null),


            new WindowShutterPlate (white, darkCyan, lighterDarkCyan, "KOPALNICA", R.layout.window_rolling_shutter_layout, windowShutterBathroom1Device, null),


            new WindowShutterPlate (white, darkCyan, lighterDarkCyan, "OTROŠKA V", R.layout.window_rolling_shutter_layout, windowShutterKidsRoomDevice, null),


            new WindowShutterPlate (white, darkOrange, lighterDarkOrange, "OTROŠKA J", R.layout.window_venetian_blind_layout, windowLouvreShutterKidsRoomDevice, null),


            new WindowShutterPlate (white, darkOrange, lighterDarkOrange, "GALERIJA", R.layout.window_venetian_blind_layout, windowLouvreShutterGalleryDevice, null),


            new WindowShutterPlate (white, lightGray, lighterLightGray, "SPALNICA", R.layout.window_rolling_shutter_layout, windowShutterBedroomDevice, null),


            new ShellPlate (white, lightGray, lighterLightGray, "KURILNICA", R.layout.shell_plate, shellMainDevice, null),


            new ShellPlate (white, lightGray, lighterLightGray, "PODSTREŠJE", R.layout.shell_plate, shellAtticDevice, null),


            new ThermometerPlate (white, lightGray, lighterLightGray, "PODSTREŠJE", R.layout.thermometer_plate, atticThermometerDevice, null),


            new ThermometerPlate (yellow, red, lighterRed, "ZUNAJ", R.layout.thermometer_half_plate, outsideThermometerDevice, null),


            new ThermometerPlate (white, lightGray, lighterLightGray, "PREZRAČEVANJE", R.layout.thermometer_plate, recouperatorOutputThermometerDevice, null),


            new PhotoResistorPlate (white, lightGray, lighterLightGray, "SVETLOBA", R.layout.photo_resistor_plate, photoResistorDevice, null),


            new TimedRelayPlate (white, darkOrange, lighterDarkOrange, "VENTILATOR", R.layout.timed_relay_plate, R.drawable.fan, null, false, atticVentilatorDevice, null),


            new TimedRelayPlate (white, darkOrange, lighterDarkOrange, "RAZVLAŽEVALEC", R.layout.timed_relay_plate, R.drawable.humidifier, null, false, atticDehumidifierDevice, null),


            new TimedRelayPlate (white, darkOrange, lighterDarkOrange, "PREZRAČEVANJE", R.layout.timed_relay_plate, R.drawable.ventilation_0, R.drawable.animate_ventilation, false, recouperatorX151Device, null),


            new ShellPlate (white, lightGray, lighterLightGray, "LUDVIK", R.layout.shell_plate, shellLudvikPhoneDevice, null),


            new ClockPlate (CLOCK, white, blue, R.layout.clock_plate),


            // new RtspPlate (DeviceDescriptions.Rtsp.abbreviation, white, blue, "X", "rtsp://192.168.1.165/user=admin_password=tlJwpbo6_channel=1_stream=1.sdp?real_stream")


            new SettingsPlate (SETTINGS, white, blue, R.layout.single_icon_quarter_plate),


            new TimedRelayPlate (white, darkOrange, lighterDarkOrange, "EATING ROOM", R.layout.timed_relay_plate, R.drawable.idea, null, false, eatingRoomLightDevice, null),


            new TimedRelayPlate (white, darkOrange, lighterDarkOrange, "ENTRY ROOM", R.layout.timed_relay_plate, R.drawable.idea, null, false, entryRoomLightDevice, null),


            new TimedRelayPlate (white, darkOrange, lighterDarkOrange, "OUTSIDE", R.layout.timed_relay_plate, R.drawable.idea, null, false, outsideLightsDevice, null),
        };


    static public PlatesAndPages platesAndPages;


    // actions
    static public enum Action
    {
        Status, RotateUp, RotateDown, Up, Down, Start, StartStop, Stop, LockUnlock, Half, Third, Quarter, Grid, Settings
    }


    // settings
    static public Settings settings;


    // context
    static public Context context;


    // activity
    static public Activity activity;


    // keep connection
    static public int keepConnectionToDeviceControlNodesEverySec = 120;


    // preferences
    static public SharedPreferences runtimeSharedPreferences;


    // pulse
    static public IPulseHandler pulseHandler = new PulseHandler ();


    // network service
    static public HominoNetworkService hominoNetworkService;


    static public void flushMessages (MultiMessage multiMessage)
    {
        hominoNetworkService.write (multiMessage);
    }


    // errors
    static public List<Error> errors = new LinkedList ();


    static public void logError (Error error)
    {
        logger.fine ("ERROR: ", error);

        errors.add (error);
        while (errors.size () > 100)
        {
            errors.remove (errors.size () - 1);
        }
    }


    static public void logError (Exception exception, String message, DeviceControlNode deviceControlNode)
    {
        Error error = new Error (message + ": " + exception, new Date (), deviceControlNode.getId ());
        logger.fine ("ERROR: ", error);

        errors.add (error);

        // trim
        while (errors.size () > 100)
        {
            errors.remove (errors.size () - 1);
        }
    }


    // log
    static public void log (Date now)
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
        //                logError ("configuration", e.getMessage ());
        //            }
        //        }
    }


    // setup
    static public void setup (Context context, Activity activity)
    {
        // prologue
        logger.info ("Setting up runtime");


        logger.fine ("Context and activity...");
        Runtime.context = context;
        Runtime.activity = activity;


        logger.fine ("Settings...");
        settings = new Settings (context);


        logger.fine ("Enable alarms...");
        Alarms.enableAlarms ();


        logger.fine ("Pages and plates based on display...");
        DisplayMetrics displayMetrics = context.getResources ().getDisplayMetrics ();
        float widthInDp = displayMetrics.widthPixels / displayMetrics.density;
        int columns = (int) widthInDp / 87;
        int plateBoxHorizontalSizeInPixels = displayMetrics.widthPixels / columns;
        int plateBoxVerticalSizeInPixels = (plateBoxHorizontalSizeInPixels * 8) / 7;
        columns = (columns < platePositions.length ? columns : platePositions.length);
        logger.fine ("Screen: widthPixels ", displayMetrics.widthPixels, ", density ", displayMetrics.density, ", columns ", columns, ", plate box dimension in pixels: ", plateBoxHorizontalSizeInPixels, " * ", plateBoxVerticalSizeInPixels);
        platesAndPages = new PlatesAndPages (platePages, plates, platePositions[columns - 1], plateBoxHorizontalSizeInPixels, plateBoxVerticalSizeInPixels);


        logger.fine ("Runtime shared preferences...");
        runtimeSharedPreferences = context.getSharedPreferences ("si.krulik.homino.runtimeSharedPreferences", Context.MODE_PRIVATE);


        logger.fine ("Done");
    }


    // private
    static final private CustomLogger logger = CustomLogger.getLogger ("CONFIGURATION");
    static final private String CSV_DELIMITER = ",";
    static final private SimpleDateFormat myDateFormat = new SimpleDateFormat ("ddMMyyyy HH:mm:ss");
}