package si.krulik.homino.customization;

import android.app.Activity;
import android.content.Context;

import java.util.Map;
import java.util.logging.Logger;

import si.krulik.homino.R;
import si.krulik.homino.common.validate.Validate;
import si.krulik.homino.configuration.Configuration;
import si.krulik.homino.configuration.ControlNode;
import si.krulik.homino.configuration.devices.Device;
import si.krulik.homino.configuration.devices.TimedRelayDevice;
import si.krulik.homino.configuration.devices.WindowLouvreShutterDevice;
import si.krulik.homino.configuration.devices.WindowRollingShutterDevice;
import si.krulik.homino.configuration.plates.ActionPlate;
import si.krulik.homino.configuration.plates.ActionPlateRow;
import si.krulik.homino.configuration.plates.ActionPlateRowButton;
import si.krulik.homino.configuration.plates.PlatePage;
import si.krulik.homino.configuration.plates.TimedRelayPlate;
import si.krulik.homino.configuration.plates.WindowLouvreShutterPlate;
import si.krulik.homino.configuration.plates.WindowRollingShutterPlate;

public class Customization
{
    public final static String messageDelimiter = ";";
    public final static String fieldDelimiter = ",";
    public final static String rotateUp = "RotateUp";
    public final static String rotateDown = "RotateDown";
    public final static String up = "Up";
    public final static String down = "Down";
    public final static String start = "Start";
    public final static String stop = "Stop";
    public final static String half = "Half";
    public final static String third = "Third";
    public final static String quarter = "Quarter";
    public final static String grid = "Grid";

    public final static String controlNodeMain = "CNMain";
    public final static String windowShuttersKitchen = "WsKitch";
    public final static String windowRollingShutterKitchenE = "WrsKitchE";
    public final static String windowRollingShutterKitchenS = "WrsKitchS";
    public final static String windowLouvreShutterDiningRoomS = "WlsDiningS";
    public final static String windowShuttersLivingRoom = "WsLiving";
    public final static String windowLouvreShutterLivingRoomS = "WlsLivingS";
    public final static String windowRollingShutterLivingRoomW = "WrsLivingW";
    public final static String windowRollingShutterStairs = "WrsStairs";
    public final static String windowRollingShutterBathroom0 = "WrsBath0";
    public final static String windowShuttersLowerFloor = "WsLower";
    public final static String windowShuttersAll = "WsAll";
    public final static String hotWaterRecirculationPump = "HWPump";


    public Configuration compose (Context context, Activity activity)
    {
        final Configuration configuration = new Configuration (context, activity);


        configuration.addControlNodes (new ControlNode (controlNodeMain, "192.168.1.50:5555"));


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


        // grid layout coordinates
        int dx = 10;
        int dx2 = dx * 2;

        int x0 = 0;
        int x1 = dx * 1;
        int x2 = dx * 2;
        int x3 = dx * 3;
        int x4 = dx * 4;
        int x5 = dx * 5;

        int dy = 10;
        int dy2 = dy * 2;

        int y0 = 0;
        int y1 = dy * 1;
        int y2 = dy * 2;
        int y3 = dy * 3;
        int y4 = dy * 4;
        int y5 = dy * 5;
        int y6 = dy * 6;


        // @formatter:off


        // devices
        TimedRelayDevice hotWaterRecirculationPumpDevice;
        configuration.addDevices (
            new WindowRollingShutterDevice (windowRollingShutterKitchenE, controlNodeMain,97),
            new WindowRollingShutterDevice (windowRollingShutterKitchenS, controlNodeMain,97),
            new WindowLouvreShutterDevice (windowLouvreShutterDiningRoomS, controlNodeMain, 20),
            new WindowLouvreShutterDevice (windowLouvreShutterLivingRoomS, controlNodeMain, 20),
            new WindowRollingShutterDevice (windowRollingShutterLivingRoomW, controlNodeMain,97),
            new WindowRollingShutterDevice (windowRollingShutterStairs, controlNodeMain,97),
            new WindowRollingShutterDevice (windowRollingShutterBathroom0, controlNodeMain,97),
            hotWaterRecirculationPumpDevice = new TimedRelayDevice (hotWaterRecirculationPump, controlNodeMain, 60 * 1000));


        // plate pages
        configuration.addPlatePages (
            new PlatePage ("ROLETE IN ŽALUZIJE PRITLIČJE", 5,

                new ActionPlate (windowShuttersLowerFloor, x0, y0, dx2, dy2, white, blue, lighterBlue, "PRITLIČJE",
                    new ActionPlateRow (
                        new ActionPlateRowButton (windowShuttersLowerFloor + fieldDelimiter + up, null, R.drawable.ic_action_arrow_top, null)),

                    new ActionPlateRow (
                        new ActionPlateRowButton (windowShuttersLowerFloor + fieldDelimiter + half, null, R.drawable.window_shutter_half, null),
                        new ActionPlateRowButton (windowShuttersLowerFloor + fieldDelimiter + stop, null, R.drawable.ic_action_cancel, null)),

                    new ActionPlateRow (
                        new ActionPlateRowButton (windowShuttersLowerFloor + fieldDelimiter + quarter, null, R.drawable.window_shutter_quarter, null),
                        new ActionPlateRowButton (windowShuttersLowerFloor + fieldDelimiter + down, null, R.drawable.ic_action_arrow_bottom, null))),

                new TimedRelayPlate (hotWaterRecirculationPump, x2, y0, dx2, dy2, white, orange, lighterOrange, "VODA", hotWaterRecirculationPump + fieldDelimiter + start, hotWaterRecirculationPump +
                    fieldDelimiter + stop, hotWaterRecirculationPumpDevice),

                composeWindowRollingShutterPlate (windowRollingShutterKitchenE, x0, y2, dx2, dy2, white, darkCyan, lighterDarkCyan, "KUHINJA V", configuration.devicesById),

                composeWindowRollingShutterPlate (windowRollingShutterKitchenS, x2, y2, dx2, dy2, white, darkCyan, lighterDarkCyan, "KUHINJA J", configuration.devicesById),

                new ActionPlate (windowShuttersKitchen, x4, y2, dx2, dy2,  white, cyan, lighterCyan, "KUHINJA",
                    new ActionPlateRow (
                        new ActionPlateRowButton (windowShuttersKitchen + fieldDelimiter + grid, null, R.drawable.ic_grid_on, null),
                        new ActionPlateRowButton (windowShuttersKitchen + fieldDelimiter + up, null, R.drawable.ic_action_arrow_top, null)),

                    new ActionPlateRow (
                        new ActionPlateRowButton (windowShuttersKitchen + fieldDelimiter + half, null, R.drawable.window_shutter_half, null),
                        new ActionPlateRowButton (windowShuttersKitchen + fieldDelimiter + stop, null, R.drawable.ic_action_cancel, null)),

                    new ActionPlateRow (
                        new ActionPlateRowButton (windowShuttersKitchen + fieldDelimiter + quarter, null, R.drawable.window_shutter_quarter, null),
                        new ActionPlateRowButton (windowShuttersKitchen + fieldDelimiter + down, null, R.drawable.ic_action_arrow_bottom, null))),

                composeWindowLouvreShutterPlate (windowLouvreShutterLivingRoomS, x0, y4, dx2, dy2, white, darkOrange, lighterDarkOrange, "DNEVNA J", configuration.devicesById),
                composeWindowRollingShutterPlate (windowRollingShutterLivingRoomW, x2, y4, dx2, dy2, white, darkOrange, lighterDarkOrange,  "DNEVNA Z", configuration.devicesById),
                new ActionPlate (windowShuttersLivingRoom, x4, y4, dx2, dy2,  white, orange, lighterOrange, "DNEVNA",
                    new ActionPlateRow (
                        new ActionPlateRowButton (windowShuttersLivingRoom + fieldDelimiter + grid, null, R.drawable.ic_grid_on, null),
                        new ActionPlateRowButton (windowShuttersLivingRoom + fieldDelimiter + up, null, R.drawable.ic_action_arrow_top, null)),

                    new ActionPlateRow (
                        new ActionPlateRowButton (windowShuttersLivingRoom + fieldDelimiter + half, null, R.drawable.window_shutter_half, null),
                        new ActionPlateRowButton (windowShuttersLivingRoom + fieldDelimiter + stop, null, R.drawable.ic_action_cancel, null)),

                    new ActionPlateRow (
                        new ActionPlateRowButton (windowShuttersLivingRoom + fieldDelimiter + quarter, null, R.drawable.window_shutter_quarter, null),
                        new ActionPlateRowButton (windowShuttersLivingRoom + fieldDelimiter + down, null, R.drawable.ic_action_arrow_bottom, null))),

                composeWindowLouvreShutterPlate (windowLouvreShutterDiningRoomS, x0, y6, dx2, dy2, white, lightGray, lighterLightGray, "JEDILNICA", configuration.devicesById),
                composeWindowRollingShutterPlate (windowRollingShutterStairs, x2, y6, dx2, dy2, white, lightGray, lighterLightGray, "STOPNIŠČE", configuration.devicesById),
                composeWindowRollingShutterPlate (windowRollingShutterBathroom0, x4, y6, dx2, dy2, white, lightGray, lighterLightGray, "KOP. P", configuration.devicesById))
//
//            new PlatePage ("Drugi", 5,
//                composeWindowRollingShutterPlate (windowShuttersAll, 0, 0, 2, 2, white, darkGray, lighterDarkGray, "Vsi"))

        );
        // @formatter:on


        configuration.setPlateMessageHandler (new PlateMessageHandler ());
        configuration.setCommunicationMessageHandler (new ControlNodesMessageHandler ());
        configuration.setPulseHandler (new PulseHandler ());

        return (configuration);
    }


    private final static Logger logger = Logger.getLogger (Customization.class.getName ());


    private WindowRollingShutterPlate composeWindowRollingShutterPlate (String id, int x, int y, int dx, int dy, String foregroundColor, String backgroundColor, String buttonBackgroundColor, String
        text, Map<String, Device> devicesById)
    {
        Device device = devicesById.get (id);
        Validate.notNull (device, "Device ", id, " not found");
        Validate.isTrue (device instanceof WindowRollingShutterDevice, "Device ", id, " not of correct type");

        return (new WindowRollingShutterPlate (id, x, y, dx, dy, foregroundColor, backgroundColor, buttonBackgroundColor, text, id + fieldDelimiter + up, id + fieldDelimiter + down, id +
            fieldDelimiter + stop, id + fieldDelimiter + grid, id + fieldDelimiter + half, id + fieldDelimiter + quarter, (WindowRollingShutterDevice) device));
    }


    private WindowLouvreShutterPlate composeWindowLouvreShutterPlate (String id, int x, int y, int dx, int dy, String foregroundColor, String backgroundColor, String buttonBackgroundColor, String
        text, Map<String, Device> devicesById)
    {
        Device device = devicesById.get (id);
        Validate.notNull (device, "Device ", id, " not found");
        Validate.isTrue (device instanceof WindowLouvreShutterDevice, "Device ", id, " not of correct type");

        return (new WindowLouvreShutterPlate (id, x, y, dx, dy, foregroundColor, backgroundColor, buttonBackgroundColor, text, id + fieldDelimiter + up, id + fieldDelimiter + down, id +
            fieldDelimiter + stop, id + fieldDelimiter + half, id + fieldDelimiter + rotateUp, id +
            fieldDelimiter + rotateDown, (WindowLouvreShutterDevice) device));
    }
}