package si.krulik.homino.plates;

import android.content.DialogInterface;

import si.krulik.homino.common.logger.CustomLogger;
import si.krulik.homino.runtime.Runtime;
import si.krulik.homino.message.MultiMessage;
import si.krulik.homino.plates.base.Plate;


public class DialogOnClickListener implements DialogInterface.OnClickListener
{
    public DialogOnClickListener (Runtime.Action action, Plate plate)
    {
        this.action = action;
        this.plate = plate;
    }


    public void onClick (DialogInterface dialogInterface, int i)
    {
        try
        {
            logger.fine ("On click: ", action);
            MultiMessage multiMessage = plate.handleAction (action);
            Runtime.flushMessages (multiMessage);
        }
        catch (Throwable t)
        {
            logger.severe (t, "Exception occured while handling plate action ", action);
            // configuration.logError (null, t.getMessage ()); TODO
        }
    }


    private Runtime.Action action;
    private Plate plate;
    private static final CustomLogger logger = CustomLogger.getLogger ("BUTTON_ON_CLICK");
}