package si.krulik.homino.configuration.devices;


public class Device
{
    public Device (String id, String controlNodeName)
    {
        this.id = id;
        this.controlNodeName = controlNodeName;
    }


    public enum MotionDirection
    {
        NONE, UP, DOWN, LEFT, RIGHT, STOP
    }


    public String id;
    public String controlNodeName;
    public long lastRefreshInMillis = 0;
}
