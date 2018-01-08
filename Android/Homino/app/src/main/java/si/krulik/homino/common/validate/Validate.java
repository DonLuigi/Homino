package si.krulik.homino.common.validate;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static si.krulik.homino.common.string.StringUtil.*;


public class Validate
{
    ////////////////////
    //
    // public
    //
    ////////////////////    
    static public void fail (Class<? extends Throwable> exceptionClass, Object... message)
    {
        throw (RuntimeException) composeException (exceptionClass, message).fillInStackTrace ();
    }


    static public void fail (Object... message)
    {
        throw (RuntimeException) composeException (null, message).fillInStackTrace ();
    }


    //////////
    static public void equals (Object a, Object b, Class<? extends Throwable> exceptionClass, Object... message)
    {
        if ((a != null && b != null && !a.equals (b)) || ((a == null || b == null) && a != b))
        {
            throw (RuntimeException) composeException (exceptionClass, message, a, " !equals ", b).fillInStackTrace ();
        }
    }


    static public void equals (Object a, Object b, Object... message)
    {
        if ((a != null && b != null && !a.equals (b)) || ((a == null || b == null) && a != b))
        {
            throw (RuntimeException) composeException (null, message, a, " !equals ", b).fillInStackTrace ();
        }
    }


    //////////
    static public void equals (BigDecimal a, BigDecimal b, Class<? extends Throwable> exceptionClass, Object... message)
    {
        if ((a != null && b != null && a.compareTo (b) != 0) || ((a == null || b == null) && a != b))
        {
            throw (RuntimeException) composeException (exceptionClass, message, a, " !equals ", b).fillInStackTrace ();
        }
    }


    static public void equals (BigDecimal a, BigDecimal b, Object... message)
    {
        if ((a != null && b != null && a.compareTo (b) != 0) || ((a == null || b == null) && a != b))
        {
            throw (RuntimeException) composeException (null, message, a, " !equals ", b).fillInStackTrace ();
        }
    }


    //////////
    static public void equals (String a, String b, Class<? extends Throwable> exceptionClass, Object... message)
    {
        if (a == b || a.equals (b))
        {
            return;
        }
        throw (RuntimeException) composeException (exceptionClass, message, a, " !equals ", b).fillInStackTrace ();
    }


    static public void equals (String a, String b, Object... message)
    {
        if (a == b || a.equals (b))
        {
            return;
        }
        throw (RuntimeException) composeException (null, message, a, " !equals ", b).fillInStackTrace ();
    }


    //////////
    static public void equals (Long a, Long b, Class<? extends Throwable> exceptionClass, Object... message)
    {
        if (a == b)
        {
            return;
        }
        if (a != null && a.equals (b))
        {
            return;
        }

        throw (RuntimeException) composeException (exceptionClass, message, a, " !equals ", b).fillInStackTrace ();
    }


    static public void equals (Long a, Long b, Object... message)
    {
        if (a == b)
        {
            return;
        }
        if (a != null && a.equals (b))
        {
            return;
        }

        throw (RuntimeException) composeException (null, message, a, " !equals ", b).fillInStackTrace ();
    }


    //////////
    static public void instanceOf (Object a, Class<?> b, Class<? extends Throwable> exceptionClass, Object... message)
    {
        if (!b.isInstance (a))
        {
            throw (RuntimeException) composeException (exceptionClass, message, a, " !instanceof ", b).fillInStackTrace ();
        }
    }


    static public void instanceOf (Object a, Class<?> b, Object... message)
    {
        if (!b.isInstance (a))
        {
            throw (RuntimeException) composeException (null, message, a, " !instanceof ", b).fillInStackTrace ();
        }
    }


    //////////
    static public void isEmpty (Collection<?> a, Class<? extends Throwable> exceptionClass, Object... message)
    {
        if (a == null || !a.isEmpty ())
        {
            throw (RuntimeException) composeException (exceptionClass, message, a, " !empty").fillInStackTrace ();
        }
    }


    static public void isEmpty (Collection<?> a, Object... message)
    {
        if (a == null || !a.isEmpty ())
        {
            throw (RuntimeException) composeException (null, message, a, " !empty").fillInStackTrace ();
        }
    }


    //////////
    static public void isEmpty (Map<?, ?> a, Class<? extends Throwable> exceptionClass, Object... message)
    {
        if (a == null || !a.isEmpty ())
        {
            throw (RuntimeException) composeException (exceptionClass, message, a, " !empty").fillInStackTrace ();
        }
    }


    static public void isEmpty (Map<?, ?> a, Object... message)
    {
        if (a == null || !a.isEmpty ())
        {
            throw (RuntimeException) composeException (null, message, a, " !empty").fillInStackTrace ();
        }
    }


    //////////
    static public void isEmpty (String a, Class<? extends Throwable> exceptionClass, Object... message)
    {
        if (a == null || !a.isEmpty ())
        {
            throw (RuntimeException) composeException (exceptionClass, message, a, " !empty").fillInStackTrace ();
        }
    }


    static public void isEmpty (String a, Object... message)
    {
        if (a == null || !a.isEmpty ())
        {
            throw (RuntimeException) composeException (null, message, a, " !empty").fillInStackTrace ();
        }
    }


    //////////
    static public void isNull (Object a, Class<? extends Throwable> exceptionClass, Object... message)
    {
        if (a != null)
        {
            throw (RuntimeException) composeException (exceptionClass, message, a, " !null").fillInStackTrace ();
        }
    }


    static public void isNull (Object a, Object... message)
    {
        if (a != null)
        {
            throw (RuntimeException) composeException (null, message, a, " !null").fillInStackTrace ();
        }
    }


    //////////
    static public void isTrue (boolean a, Class<? extends Throwable> exceptionClass, Object... message)
    {
        if (!a)
        {
            throw (RuntimeException) composeException (exceptionClass, message, "false").fillInStackTrace ().fillInStackTrace ();
        }
    }


    static public void isTrue (boolean a, Object... message)
    {
        if (!a)
        {
            throw (RuntimeException) composeException (null, message, "false").fillInStackTrace ().fillInStackTrace ();
        }
    }


    //////////
    static public void isFalse (boolean a, Class<? extends Throwable> exceptionClass, Object... message)
    {
        if (a)
        {
            throw (RuntimeException) composeException (exceptionClass, message, "true").fillInStackTrace ().fillInStackTrace ();
        }
    }


    static public void isFalse (boolean a, Object... message)
    {
        if (a)
        {
            throw (RuntimeException) composeException (null, message, "true").fillInStackTrace ().fillInStackTrace ();
        }
    }


    //////////
    static public void notEmpty (Collection<?> a, Class<? extends Throwable> exceptionClass, Object... message)
    {
        if (a == null || a.isEmpty ())
        {
            throw (RuntimeException) composeException (exceptionClass, message, a, " empty").fillInStackTrace ();
        }
    }


    static public void notEmpty (Collection<?> a, Object... message)
    {
        if (a == null || a.isEmpty ())
        {
            throw (RuntimeException) composeException (null, message, a, " empty").fillInStackTrace ();
        }
    }


    //////////
    static public <T> void notEmpty (T[] a, Class<? extends Throwable> exceptionClass, Object... message)
    {
        if (a == null || a.length == 0)
        {
            throw (RuntimeException) composeException (exceptionClass, message, a, " empty").fillInStackTrace ();
        }
    }


    static public <T> void notEmpty (T[] a, Object... message)
    {
        if (a == null || a.length == 0)
        {
            throw (RuntimeException) composeException (null, message, a, " empty").fillInStackTrace ();
        }
    }


    //////////
    static public void notEmpty (Map<?, ?> a, Class<? extends Throwable> exceptionClass, Object... message)
    {
        if (a == null || a.isEmpty ())
        {
            throw (RuntimeException) composeException (exceptionClass, message, a, " empty").fillInStackTrace ();
        }
    }


    static public void notEmpty (Map<?, ?> a, Object... message)
    {
        if (a == null || a.isEmpty ())
        {
            throw (RuntimeException) composeException (null, message, a, " empty").fillInStackTrace ();
        }
    }


    //////////
    static public void notEmpty (String a, Class<? extends Throwable> exceptionClass, Object... message)
    {
        if (a == null || a.isEmpty ())
        {
            throw (RuntimeException) composeException (exceptionClass, message, a, " empty").fillInStackTrace ();
        }
    }


    static public void notEmpty (String a, Object... message)
    {
        if (a == null || a.isEmpty ())
        {
            throw (RuntimeException) composeException (null, message, a, " empty").fillInStackTrace ();
        }
    }


    //////////
    static public void notEquals (Object a, Object b, Class<? extends Throwable> exceptionClass, Object... message)
    {
        if (a == b)
        {
            throw (RuntimeException) composeException (exceptionClass, message, a, " equals ", b).fillInStackTrace ();
        }
    }


    static public void notEquals (Object a, Object b, Object... message)
    {
        if (a == b)
        {
            throw (RuntimeException) composeException (null, message, a, " equals ", b).fillInStackTrace ();
        }
    }


    //////////
    static public void notEquals (long a, long b, Class<? extends Throwable> exceptionClass, Object... message)
    {
        if (a == b)
        {
            throw (RuntimeException) composeException (exceptionClass, message, a, " equals ", b).fillInStackTrace ();
        }
    }


    static public void notEquals (long a, long b, Object... message)
    {
        if (a == b)
        {
            throw (RuntimeException) composeException (null, message, a, " equals ", b).fillInStackTrace ();
        }
    }


    //////////
    static public void notEquals (String a, String b, Class<? extends Throwable> exceptionClass, Object... message)
    {
        if (a == b || a.equals (b))
        {
            throw (RuntimeException) composeException (exceptionClass, message, a, " equals ", b).fillInStackTrace ();
        }
    }


    static public void notEquals (String a, String b, Object... message)
    {
        if (a == b || a.equals (b))
        {
            throw (RuntimeException) composeException (null, message, a, " equals ", b).fillInStackTrace ();
        }
    }


    //////////
    static public void notNegative (long a, Class<? extends Throwable> exceptionClass, Object... message)
    {
        if (a < 0)
        {
            throw (RuntimeException) composeException (exceptionClass, message, a, " < 0").fillInStackTrace ();
        }
    }


    static public void notNegative (long a, Object... message)
    {
        if (a < 0)
        {
            throw (RuntimeException) composeException (null, message, a, " < 0").fillInStackTrace ();
        }
    }


    //////////
    static public void notNull (Object a, Class<? extends Throwable> exceptionClass, Object... message)
    {
        if (a == null)
        {
            throw (RuntimeException) composeException (exceptionClass, message, "null").fillInStackTrace ();
        }
    }


    static public void notNull (Object a, Object... message)
    {
        if (a == null)
        {
            throw (RuntimeException) composeException (null, message, "null").fillInStackTrace ();
        }
    }


    //////////
    static public void hasSize (List<?> a, int b, Class<? extends Throwable> exceptionClass, Object... message)
    {
        if (a == null || a.size () != b)
        {
            throw (RuntimeException) composeException (exceptionClass, message, a, " !size ", b).fillInStackTrace ();
        }
    }


    static public void hasSize (List<?> a, int b, Object... message)
    {
        if (a == null || a.size () != b)
        {
            throw (RuntimeException) composeException (null, message, a, " !size ", b).fillInStackTrace ();
        }
    }


    //////////
    static public void hasSize (Map<?, ?> a, int b, Class<? extends Throwable> exceptionClass, Object... message)
    {
        if (a == null || a.size () != b)
        {
            throw (RuntimeException) composeException (exceptionClass, message, a, " !size ", b).fillInStackTrace ();
        }
    }


    static public void hasSize (Map<?, ?> a, int b, Object... message)
    {
        if (a == null || a.size () != b)
        {
            throw (RuntimeException) composeException (null, message, a, " !size ", b).fillInStackTrace ();
        }
    }


    ////////////////////
    //
    // private
    //
    ////////////////////
    static private RuntimeException composeException (Class<? extends Throwable> exceptionClass, Object[] message, Object... additionalMessage)
    {
        // message
        String finalMessage = vaToString (additionalMessage) + (message.length > 0 ? ": " : "") + vaToString (message);

        // no exception class
        if (exceptionClass == null)
        {
            return (new IllegalArgumentException (finalMessage));
        }

        // new instance
        RuntimeException newInstance;
        Constructor<? extends Throwable> constructor;
        try
        {
            constructor = exceptionClass.getConstructor (String.class);
        }
        catch (NoSuchMethodException e)
        {
            // provided class doesn't have constructor (String)
            return (new IllegalArgumentException (finalMessage));
        }
        catch (SecurityException e)
        {
            return (new IllegalArgumentException (finalMessage));
        }
        if (constructor != null)
        {
            try
            {
                newInstance = (RuntimeException) constructor.newInstance (finalMessage);
                if (newInstance == null)
                {
                    return (new IllegalArgumentException (finalMessage));
                }
            }
            catch (InstantiationException e)
            {
                return (new IllegalArgumentException (finalMessage));
            }
            catch (IllegalAccessException e)
            {
                return (new IllegalArgumentException (finalMessage));
            }
            catch (IllegalArgumentException e)
            {
                return (new IllegalArgumentException (finalMessage));
            }
            catch (InvocationTargetException e)
            {
                return (new IllegalArgumentException (finalMessage));
            }
        }
        else
        {
            return (new IllegalArgumentException (finalMessage));
        }


        // wrap if needed
        if (RuntimeException.class.isAssignableFrom (exceptionClass))
        {
            return (newInstance);
        }
        else
        {
            return (new RuntimeException (newInstance));
        }
    }
}
