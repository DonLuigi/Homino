package si.krulik.homino.configuration.plate.common;

import android.view.View;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import si.krulik.homino.common.logger.CustomLogger;
import si.krulik.homino.configuration.Configuration;
import si.krulik.homino.configuration.command.Action;
import si.krulik.homino.configuration.device.common.Device;
import si.krulik.homino.message.Message;
import si.krulik.homino.message.MultiMessage;


@ToString (includeFieldNames = true, of = {"id", "title", "platePosition"}) abstract public class Plate
{
    public Plate (String id, String title, String foregroundColor, String backgroundColor, String buttonBackgroundColor, IPlateActionHandler actionHandler)
    {
        this.id = id;
        this.title = title;
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.buttonBackgroundColor = buttonBackgroundColor;
        this.actionHandler = actionHandler;
    }


    abstract public void refresh ();


    public Device getDevice ()
    {
        return (null);
    }


    public MultiMessage handleAction (Action action, Configuration configuration)
    {
        logger.fine ("Handle action ", action);
        MultiMessage multiMessage = null;
        if (actionHandler != null)
        {
            logger.fine ("Handler set");
            multiMessage = actionHandler.handleAction (action, configuration);
        }
        else if (getDevice () != null)
        {
            logger.fine ("Handler not set, calling device handle action");
            Message message = getDevice ().handleAction (action);
            logger.fine ("Message ", message);

            multiMessage = new MultiMessage ();
            multiMessage.add (message);
        }

        return (multiMessage);
    }


    @Getter protected String id;


    @Getter protected String title;


    @Getter @Setter protected PlatePosition platePosition;


    @Getter protected String foregroundColor, backgroundColor, buttonBackgroundColor;


    @Getter protected IPlateActionHandler actionHandler;


    @Getter @Setter protected View view;


    private static final CustomLogger logger = CustomLogger.getLogger ("PLATE");
}