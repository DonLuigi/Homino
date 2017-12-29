package si.krulik.homino.configuration.plate;

import android.view.View;

import si.krulik.homino.common.logger.CustomLogger;
import si.krulik.homino.configuration.Configuration;
import si.krulik.homino.configuration.command.Action;
import si.krulik.homino.configuration.plate.common.Plate;
import si.krulik.homino.message.MultiMessage;


public class ButtonOnClickListener implements View.OnClickListener
{
    public ButtonOnClickListener (Action action, Plate plate, Configuration configuration)
    {
        this.action = action;
        this.plate = plate;
        this.configuration = configuration;
    }


    public void onClick (View v)
    {
        try
        {
            logger.fine ("On click: ", action);
            MultiMessage multiMessage = plate.handleAction (action);
            configuration.flushMessages (multiMessage);
        }
        catch (Throwable t)
        {
            logger.severe (t, "Exception occured while handling plate action ", action);
            configuration.error (null, t.getMessage ());
        }
    }


    private Action action;
    private Plate plate;
    private Configuration configuration;
    private static final CustomLogger logger = CustomLogger.getLogger ("BUTTON_ON_CLICK");
}