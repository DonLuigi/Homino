package si.krulik.homino.common.string;

import java.math.BigDecimal;
import java.util.List;



public class StringUtil
{
    ////////////////////
    //
    // public
    //
    ////////////////////
    static public boolean isEmpty (String string)
    {
        return (string == null || string.isEmpty ());
    }


    static public boolean isNotEmpty (String string)
    {
        return (string != null && !string.isEmpty ());
    }


    static public String vaToString (Object... objects)
    {
        StringBuilder stringBuilder = new StringBuilder ();
        for (Object object : objects)
        {
            stringBuilder.append (object);
        }
        return (stringBuilder.toString ());
    }


    static public <T> String arrayToString (T[] array)
    {
        StringBuilder stringBuilder = new StringBuilder ("[");
        for (T element : array)
        {
            stringBuilder.append (element);
            stringBuilder.append (", ");
        }

        if (stringBuilder.length () > 2)
        {
            stringBuilder.setLength (stringBuilder.length () - 2);
        }
        stringBuilder.append ("]");
        return (stringBuilder.toString ());
    }


    static public <T> String listToString (List<T> list)
    {
        StringBuilder stringBuilder = new StringBuilder ("[");
        for (T element : list)
        {
            stringBuilder.append (element);
            stringBuilder.append (", ");
        }

        if (stringBuilder.length () > 2)
        {
            stringBuilder.setLength (stringBuilder.length () - 2);
        }
        stringBuilder.append ("]");
        return (stringBuilder.toString ());
    }


    static public String toString (BigDecimal value)
    {
        if (value == null)
        {
            return ("null");
        }
        else
        {
            return (value.toPlainString ());
        }
    }
}
