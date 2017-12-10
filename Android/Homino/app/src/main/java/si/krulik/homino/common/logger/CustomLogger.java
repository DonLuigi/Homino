package si.krulik.homino.common.logger;

import android.util.Log;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;


public class CustomLogger
{
    // factory
    static public CustomLogger getLogger (String name)
    {
        return (new CustomLogger (name));
    }


    // constructors
    public CustomLogger (String name)
    {
        tag = name;
    }


    // api
    public void log (int level, Throwable throwable, Object... args)
    {
        switch (level)
        {
            case Log.DEBUG:
                Log.d (tag, buildMessage (args).toString (), throwable);
                break;

            case Log.ERROR:
                Log.e (tag, buildMessage (args).toString (), throwable);
                break;

            case Log.INFO:
                Log.i (tag, buildMessage (args).toString (), throwable);
                break;

            case Log.VERBOSE:
                Log.v (tag, buildMessage (args).toString (), throwable);
                break;

            case Log.WARN:
                Log.w (tag, buildMessage (args).toString (), throwable);
                break;
        }
    }


    public void log (int level, Object... args)
    {
        log (level, (Throwable) null, args);
    }


    public void severe (Throwable throwable, Object... args)
    {
        log (Log.ERROR, throwable, args);
    }


    public void severe (Object... args)
    {
        log (Log.ERROR, args);
    }


    public void warning (Object... args)
    {
        log (Log.WARN, args);
    }


    public void info (Object... args)
    {
        log (Log.INFO, args);
    }


    public void fine (Object... args)
    {
        log (Log.VERBOSE, args);
    }


    public void finer (Object... args)
    {
        log (Log.DEBUG, args);
    }


    public void finest (Object... args)
    {
        log (Log.DEBUG, args);
    }


    public boolean isLoggable (int level)
    {
        return (Log.isLoggable (tag, level));
    }


    public String newLine ()
    {
        return (lineSeparator);
    }


    public String breakLine ()
    {
        return (lineBreak);
    }


    public String boldBreakLine ()
    {
        return (boldLineBreak);
    }


    // private
    private String tag;
    final private String lineSeparator = System.lineSeparator ();
    final private String lineBreak = lineSeparator + "________________________________________________________________________________" + lineSeparator;
    final private String boldLineBreak = lineSeparator + "################################################################################" + lineSeparator;


    private StringBuilder buildMessage (StringBuilder stringBuilder, Object... args)
    {
        stringBuilder.append (">>");
        if (args.length > 0)
        {
            for (Object arg : args)
            {
                if (arg == null)
                {
                    stringBuilder.append (arg);
                }
                else if (arg instanceof Map<?, ?>)
                {
                    Map<?, ?> map = (Map<?, ?>) arg;
                    if (map.size () == 0)
                    {
                        stringBuilder.append ("{}");
                    }
                    else
                    {
                        stringBuilder.append (newLine ());
                        stringBuilder.append ("{");
                        stringBuilder.append (newLine ());

                        for (Entry<?, ?> o : map.entrySet ())
                        {
                            stringBuilder.append ("\t");
                            stringBuilder.append (o.getKey ());
                            stringBuilder.append (": ");
                            stringBuilder.append (o.getValue ());
                            stringBuilder.append (newLine ());
                        }

                        stringBuilder.append ("}");
                    }
                }
                else if (arg instanceof Collection<?>)
                {
                    Collection<?> collection = (Collection<?>) arg;
                    if (collection.size () == 0)
                    {
                        stringBuilder.append ("[]");
                    }
                    else
                    {
                        stringBuilder.append (newLine ());
                        stringBuilder.append ("[");
                        stringBuilder.append (newLine ());

                        for (Object name : collection)
                        {
                            stringBuilder.append ("\t");
                            stringBuilder.append (name);
                            stringBuilder.append (newLine ());
                        }

                        stringBuilder.append ("]");
                    }
                }
                else if (arg.getClass ().isArray ())
                {
                    Object[] array = (Object[]) arg;
                    if (array.length == 0)
                    {
                        stringBuilder.append ("[]");
                    }
                    else
                    {
                        stringBuilder.append (newLine ());
                        stringBuilder.append ("[");
                        stringBuilder.append (newLine ());

                        for (Object element : array)
                        {
                            stringBuilder.append ("\t");
                            stringBuilder.append (element);
                            stringBuilder.append (newLine ());
                        }

                        stringBuilder.append ("]");
                    }
                }
                else
                {
                    stringBuilder.append (arg);
                }
            }
        }

        return (stringBuilder);
    }


    private StringBuilder buildMessage (Object... args)
    {
        return (buildMessage (new StringBuilder (), args));
    }
}