package si.krulik.homino.configuration.devices;

public class WindowRollingShutterDevice extends Device
{
    public WindowRollingShutterDevice (String id, String controlNodeName, int gridPercentage)
    {
        super (id, controlNodeName);
        this.gridPercentage = gridPercentage;
    }


    public int verticalPercent = -1;
    public int gridPercentage = -1;
    public MotionDirection motionDirection = MotionDirection.NONE;


    @Override
    public String toString ()
    {
        return (WindowRollingShutterDevice.class.getSimpleName () + "[id=" + id + ", vertical%=" + verticalPercent + ", motionDirection=" + motionDirection + "]");
    }
}
