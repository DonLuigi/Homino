package si.krulik.homino.configuration.plate.common;

import android.view.View;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.ToString;
import si.krulik.homino.common.logger.CustomLogger;
import si.krulik.homino.configuration.Configuration;
import si.krulik.homino.configuration.command.Action;
import si.krulik.homino.configuration.device.common.Device;
import si.krulik.homino.message.Message;
import si.krulik.homino.message.MultiMessage;


@ToString (includeFieldNames = true, of = {"id", "title"}) abstract public class Plate
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


    @Getter protected Map<String, PlatePosition> platePositionsByPageId = new HashMap ();


    @Getter protected String foregroundColor, backgroundColor, buttonBackgroundColor;


    @Getter protected IPlateActionHandler actionHandler;


    @Getter private Map<String, View> viewByPlatePageId = new HashMap ();


    private static final CustomLogger logger = CustomLogger.getLogger ("PLATE");
}