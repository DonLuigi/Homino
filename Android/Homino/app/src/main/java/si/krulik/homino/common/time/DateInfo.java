package si.krulik.homino.common.time;

import java.util.Calendar;
import java.util.Date;

public class DateInfo
{
    public DateInfo (Date date)
    {
        this.date = date;

        millis = date.getTime ();

        Calendar beginningOfDay = Calendar.getInstance ();
        beginningOfDay.setTime (date);
        beginningOfDay.set (Calendar.HOUR_OF_DAY, 0);
        beginningOfDay.set (Calendar.MINUTE, 0);
        beginningOfDay.set (Calendar.SECOND, 0);
        beginningOfDay.set (Calendar.MILLISECOND, 0);

        Date beginningOfDayDate = beginningOfDay.getTime ();
        millisSinceStartOfDay = date.getTime () - beginningOfDayDate.getTime ();

        int month = beginningOfDay.get (Calendar.MONTH);
        sunriseMillisSinceStartOfDay = sunrises[month];
        sunsetMillisSinceStartOfDay = sunsets[month];
        dayOfYear = beginningOfDay.get (Calendar.DAY_OF_YEAR);
        dayOfWeek = beginningOfDay.get (Calendar.DAY_OF_WEEK);
    }

    static public long composeMillis (long hour, long minute)
    {
        return (hour * 60 * 60 * 1000 + minute * 60 * 1000);
    }

    public Date date;
    public long millis;
    public int dayOfYear;
    public int dayOfWeek;
    public long millisSinceStartOfDay;
    public long sunriseMillisSinceStartOfDay, sunsetMillisSinceStartOfDay;


    static private long[] sunrises = {composeMillis (07, 40), composeMillis (07, 06), composeMillis (06, 14), composeMillis (06, 15), composeMillis (05, 29), composeMillis (05, 10), composeMillis
        (05, 26), composeMillis (06, 02), composeMillis (06, 40), composeMillis (07, 19), composeMillis (07, 03), composeMillis (07, 37)};


    static private long[] sunsets = {composeMillis (16, 44), composeMillis (17, 27), composeMillis (18, 8), composeMillis (19, 49), composeMillis (20, 28), composeMillis (20, 55), composeMillis
        (20, 49), composeMillis (20, 10), composeMillis (19, 13), composeMillis (18, 15), composeMillis (16, 30), composeMillis (16, 17)};


}