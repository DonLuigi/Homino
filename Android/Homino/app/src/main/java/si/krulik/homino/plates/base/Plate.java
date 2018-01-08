package si.krulik.homino.plates.base;

import android.view.LayoutInflater;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.ToString;
import si.krulik.homino.common.logger.CustomLogger;
import si.krulik.homino.devices.base.Device;
import si.krulik.homino.message.Message;
import si.krulik.homino.message.MultiMessage;
import si.krulik.homino.runtime.Runtime;


@ToString (includeFieldNames = true, of = {"id", "title"}) abstract public class Plate
{
    public Plate (String id, String title, String foregroundColor, String backgroundColor, String buttonBackgroundColor, int layoutId, IPlateActionHandler actionHandler)
    {
        this.id = id;
        this.title = title;
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.layoutId = layoutId;
        this.buttonBackgroundColor = buttonBackgroundColor;
        this.actionHandler = actionHandler;
    }


    public void refresh ()
    {
    }


    public Device getDevice ()
    {
        return (null);
    }


    public MultiMessage handleAction (Runtime.Action action)
    {
        logger.fine ("Handle action ", action);
        MultiMessage multiMessage = null;
        if (actionHandler != null)
        {
            logger.fine ("Handler set");
            multiMessage = actionHandler.handleAction (action);
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


    public View inflate (String pageId, LayoutInflater layoutInflater)
    {
        View view = layoutInflater.inflate (layoutId, null);
        viewByPlatePageId.put (pageId, view);
        return (view);
    }


    @Getter protected String id;


    @Getter protected String title;


    @Getter protected Map<String, PlatePosition> platePositionsByPageId = new HashMap ();


    @Getter protected String foregroundColor, backgroundColor, buttonBackgroundColor;


    @Getter protected IPlateActionHandler actionHandler;


    @Getter protected int layoutId;


    @Getter protected Map<String, View> viewByPlatePageId = new HashMap ();


    private static final CustomLogger logger = CustomLogger.getLogger ("PLATE");
}