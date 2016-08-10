package si.krulik.homino.configuration.devices;

public class TimedRelayDevice extends Device
{
    public TimedRelayDevice (String id, String controlNodeName, long durationMillis)
    {
        super (id, controlNodeName);
        this.durationMillis = durationMillis;
    }


    @Override
    public String toString ()
    {
        return (TimedRelayDevice.class.getSimpleName () + "[id=\" + id + \", percent%=" + percent + "]");
    }


    public long durationMillis;
    public int percent = -1;
}
