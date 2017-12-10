package si.krulik.homino.configuration.device;

import lombok.Getter;
import lombok.ToString;
import si.krulik.homino.common.validate.Validate;
import si.krulik.homino.configuration.device.common.Device;
import si.krulik.homino.configuration.command.Action;
import si.krulik.homino.message.Message;


@ToString (includeFieldNames = true, callSuper = true)
public class WindowShutterDevice extends Device
{
    public WindowShutterDevice (String id, String controlNodeName, int rotationStepPercent, int gridPercentage)
    {
        super (id, controlNodeName, 0);
        this.rotationStepPercent = rotationStepPercent;
        this.gridPercentage = gridPercentage;
    }


    public void handleMessage (Message message)
    {
        if ((message.getParts ().length == 4 || message.getParts ().length == 5) && message.getParts ()[1].equalsIgnoreCase ("STATUS"))
        {
            motionDirection = MotionDirection.valueOf (message.getParts ()[2]);
            verticalPercent = Integer.parseInt (message.getParts ()[3]);
            if (message.getParts ().length == 5)
            {
                rotationPercent = Integer.parseInt (message.getParts ()[4]);
            }
            statusUpdated = true;
        }
    }


    public Message handleAction (Action action)
    {
        switch (action)
        {
            case Stop:
                return (new Message (controlNodeNameId, id, "STOP"));


            case Up:
                return (new Message (controlNodeNameId, id, "MV", "0"));


            case Down:
                return (new Message (controlNodeNameId, id, "MV","100"));


            case Half:
                return (new Message (controlNodeNameId, id, "MV","50"));


            case Quarter:
                return (new Message (controlNodeNameId, id, "MV","75"));


            case Grid:
                if (gridPercentage > 0)
                {
                    return (new Message (controlNodeNameId, id, "MV", Integer.toString (gridPercentage)));
                }
                break;


            case RotateUp:
                if (rotationStepPercent > 0)
                {
                    int newRotationPercent = rotationPercent - rotationStepPercent;
                    newRotationPercent = (newRotationPercent < 0 ? 0 : newRotationPercent);
                    return (new Message (controlNodeNameId, id, "MV","-1", Integer.toString (newRotationPercent)));
                }
                break;


            case RotateDown:
                if (rotationStepPercent > 0)
                {
                    int newRotationPercent = rotationPercent + rotationStepPercent;
                    newRotationPercent = (newRotationPercent > 100 ? 100 : newRotationPercent);
                    return (new Message (controlNodeNameId, id, "MV","-1",Integer.toString (newRotationPercent)));
                }
                break;
        }

        Validate.fail ("Unknown command ", action);
        return (null);
    }


    @Getter private int verticalPercent = -1;


    @Getter private int rotationPercent = -1;


    private enum MotionDirection
    {
        NONE, UP, DOWN, LEFT, RIGHT, STOP
    }


    @Getter
    private int rotationStepPercent;

    private int gridPercentage;

    private MotionDirection motionDirection = MotionDirection.NONE;
}