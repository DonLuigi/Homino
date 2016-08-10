package si.krulik.homino.configuration.devices;

public class WindowLouvreShutterDevice extends Device
{
    public WindowLouvreShutterDevice (String id, String controlNodeName, int rotationStepPercent)
    {
        super (id, controlNodeName);
        this.rotationStepPercent = rotationStepPercent;
    }


    @Override
    public String toString ()
    {
        return (WindowRollingShutterDevice.class.getSimpleName () + "[id=\" + id + \", vertical%=" + verticalPercent + ", rotation%=" + rotationPercent + ", motionDirection=" + motionDirection + "]");
    }


    public int verticalPercent = -1;
    public int rotationPercent = -1;
    public int rotationStepPercent;
    public Device.MotionDirection motionDirection = Device.MotionDirection.NONE;
}
