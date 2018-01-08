package si.krulik.homino.runtime;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import si.krulik.homino.common.logger.CustomLogger;
import si.krulik.homino.common.time.DateInfo;
import si.krulik.homino.devices.ShellDevice;
import si.krulik.homino.message.MultiMessage;
import si.krulik.homino.plates.IPulseHandler;

import static si.krulik.homino.runtime.Runtime.*;


public class PulseHandler implements IPulseHandler
{
    public void pulse (Date now)
    {
        // prologue
        DateInfo nowInfo = new DateInfo (now);
        MultiMessage multiMessage = new MultiMessage ();


        // ping
        for (ShellDevice shellDevice : devices.getShellDevices ())
        {
            multiMessage.add (shellDevice.ping (now));
        }
        Runtime.flushMessages (multiMessage);


        ///////////////////
        //
        // automation
        //
        ///////////////////
        if (!Runtime.settings.isAutomationEnabled ())
        {
            return;
        }


        int minuteOffset = 0;


        //        // east, west, north window shutters
        //        for (String shutter : new String[] {Customization.DeviceDescriptions.WindowRollingShutterStairs.abbreviation, Customization.DeviceDescriptions.WindowRollingShutterBathroom0.abbreviation, Customization.DeviceDescriptions.WindowRollingShutterBathroom1.abbreviation, Customization.DeviceDescriptions.WindowRollingShutterLivingRoomW.abbreviation, Customization.DeviceDescriptions.WindowRollingShutterKitchenE.abbreviation, Customization.DeviceDescriptions.WindowRollingShutterKidsRoom.abbreviation, Customization.DeviceDescriptions.WindowRollingShutterBedroom.abbreviation})
        //        {
        //            int daytimeHeightPercent = 0;
        //            if (shutter.equals (Customization.DeviceDescriptions.WindowRollingShutterKitchenE.abbreviation) || shutter.equals (Customization.DeviceDescriptions.WindowRollingShutterLivingRoomW.abbreviation))
        //            {
        //                daytimeHeightPercent = 65;
        //            }
        //
        //            automate (NIGHT_MODE + 1, configuration, controlNodeMultiMessage, shutter, DateInfo.composeMillis (0, 0), DateInfo.composeMillis (7, minuteOffset), wholeWeekDaysOfWeek, nowInfo, 100);
        //
        //            if (!shutter.equals (Customization.DeviceDescriptions.WindowRollingShutterKidsRoom.abbreviation) && !shutter.equals (Customization.DeviceDescriptions.WindowRollingShutterBedroom.abbreviation))
        //            {
        //                automate (DAY_MODE, configuration, controlNodeMultiMessage, shutter, DateInfo.composeMillis (7, minuteOffset), nowInfo.sunsetMillisSinceStartOfDay - DateInfo.composeMillis (0, 20 +
        //                    minuteOffset), wholeWeekDaysOfWeek, nowInfo, daytimeHeightPercent);
        //            }
        //
        //            automate (NIGHT_MODE + 2, configuration, controlNodeMultiMessage, shutter, nowInfo.sunsetMillisSinceStartOfDay - DateInfo.composeMillis (0, 20 + minuteOffset), DateInfo.composeMillis
        //                (24, 0), wholeWeekDaysOfWeek, nowInfo, 100);
        //
        //            minuteOffset++;
        //        }
        //
        //
        //        // south window louvre shutters
        //        for (String shutter : new String[] {Customization.DeviceDescriptions.WindowLouvreShutterDiningRoomS.abbreviation, Customization.DeviceDescriptions.WindowLouvreShutterLivingRoomS.abbreviation, Customization.DeviceDescriptions.WindowLouvreShutterGallery.abbreviation, Customization.DeviceDescriptions.WindowLouvreShutterKidsRoom.abbreviation})
        //        {
        //            automate (NIGHT_MODE + 1, configuration, controlNodeMultiMessage, shutter, DateInfo.composeMillis (0, 0), DateInfo.composeMillis (7, minuteOffset), wholeWeekDaysOfWeek, nowInfo, 100, 100);
        //
        //            if (!shutter.equals (Customization.DeviceDescriptions.WindowLouvreShutterKidsRoom.abbreviation))
        //            {
        //                automate (DAY_MODE, configuration, controlNodeMultiMessage, shutter, DateInfo.composeMillis (7, minuteOffset), nowInfo.sunsetMillisSinceStartOfDay + DateInfo.composeMillis (0, 30 +
        //                    minuteOffset), wholeWeekDaysOfWeek, nowInfo, 100, 40);
        //            }
        //
        //            automate (NIGHT_MODE + 2, configuration, controlNodeMultiMessage, shutter, nowInfo.sunsetMillisSinceStartOfDay + DateInfo.composeMillis (0, 30 + minuteOffset), DateInfo.composeMillis
        //                (24, 0), wholeWeekDaysOfWeek, nowInfo, 100, 100);
        //
        //            minuteOffset++;
        //        }
        //
        //
        //        // south window roller shutters
        //        for (String shutter : new String[] {Customization.DeviceDescriptions.WindowRollingShutterKitchenS.abbreviation})
        //        {
        //            automate (NIGHT_MODE + 1, configuration, controlNodeMultiMessage, shutter, DateInfo.composeMillis (0, 0), DateInfo.composeMillis (7, minuteOffset), wholeWeekDaysOfWeek, nowInfo, 100);
        //
        //            automate (DAY_MODE + 1, configuration, controlNodeMultiMessage, shutter, DateInfo.composeMillis (7, minuteOffset), DateInfo.composeMillis (18, minuteOffset), wholeWeekDaysOfWeek,
        //                nowInfo, 70);
        //
        //            automate (DAY_MODE + 2, configuration, controlNodeMultiMessage, shutter, DateInfo.composeMillis (18, minuteOffset), nowInfo.sunsetMillisSinceStartOfDay + DateInfo.composeMillis (0, 30),
        //                wholeWeekDaysOfWeek, nowInfo, 0);
        //
        //            automate (NIGHT_MODE + 2, configuration, controlNodeMultiMessage, shutter, nowInfo.sunsetMillisSinceStartOfDay + DateInfo.composeMillis (0, 30 + minuteOffset), DateInfo.composeMillis
        //                (24, 0), wholeWeekDaysOfWeek, nowInfo, 100);
        //            minuteOffset++;
        //        }
        //
        //
        //        controlNodeMultiMessage.flushMessages ();
    }


    //    private void automate (String decisionId, final Runtime configuration, ControlNodeMultiMessage controlNodeMultiMessage, String deviceName, long startMillisOfTheDay, long
    //        stopMillisOfTheDay, Set<Integer> daysOfWeek, DateInfo nowInfo, Object... parameters)
    //    {
    //        if (nowInfo.millisSinceStartOfDay >= startMillisOfTheDay && nowInfo.millisSinceStartOfDay < stopMillisOfTheDay && daysOfWeek.contains (nowInfo.dayOfWeek))
    //        {
    //            // check if this decision was fired today
    //            String sharedPreferenceKey = decisionId + deviceName;
    //            int lastDecisionDayOfYear = configuration.runtimeSharedPreferences.getInt (sharedPreferenceKey, -1);
    //            if (lastDecisionDayOfYear == nowInfo.dayOfYear)
    //            {
    //                return;
    //            }
    //
    //
    //            Device device = configuration.devicesById.get (deviceName);
    //
    //            // execute
    //            if (device instanceof WindowRollingShutterDevice)
    //            {
    //                WindowRollingShutterDevice windowRollingShutterDevice = (WindowRollingShutterDevice) device;
    //                Integer desiredVerticalPercent = (Integer) parameters[0];
    //
    //                if (windowRollingShutterDevice.verticalPercent != desiredVerticalPercent)
    //                {
    //                    String command = device.id + ",MV," + desiredVerticalPercent;
    //                    controlNodeMultiMessage.addMessage (device, command);
    //                    logger.info ("Automation decision " + decisionId + ": device " + device.id + ": " + command);
    //                }
    //            }
    //
    //            if (device instanceof WindowLouvreShutterDevice)
    //            {
    //                WindowLouvreShutterDevice windowLouvreShutterDevice = (WindowLouvreShutterDevice) device;
    //                Integer desiredVerticalPercent = (Integer) parameters[0];
    //                Integer desiredRotationPercent = (Integer) parameters[1];
    //
    //                if (windowLouvreShutterDevice.verticalPercent != desiredVerticalPercent || windowLouvreShutterDevice.rotationPercent != desiredRotationPercent)
    //                {
    //                    String command = device.id + ",MV," + desiredVerticalPercent + "," + desiredRotationPercent;
    //                    controlNodeMultiMessage.addMessage (device, command);
    //                    logger.info ("Automation decision " + decisionId + ": device " + device.id + ": " + command);
    //                }
    //            }
    //
    //            // commit decision
    //            SharedPreferences.Editor editor = configuration.runtimeSharedPreferences.edit ();
    //            editor.putInt (sharedPreferenceKey, nowInfo.dayOfYear);
    //            editor.commit ();
    //        }
    //    }


    private static final CustomLogger logger = CustomLogger.getLogger ("PULSE_HANDLER");
    private final static String NIGHT_MODE = "NightMode";
    private final static String DAY_MODE = "DayMode";
    private static final Set<Integer> workingDaysOfWeek = new HashSet<Integer> (Arrays.asList (new Integer[] {
        Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY
    }));
    private static final Set<Integer> weekendDaysOfWeek = new HashSet<Integer> (Arrays.asList (new Integer[] {Calendar.SATURDAY, Calendar.SUNDAY}));
    private static final Set<Integer> wholeWeekDaysOfWeek = new HashSet<Integer> (Arrays.asList (new Integer[] {
        Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY
    }));
}