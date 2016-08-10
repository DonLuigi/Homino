package si.krulik.homino.configuration.plates;

import java.util.Arrays;
import java.util.List;

public class ActionPlateRow
{
    public ActionPlateRow (ActionPlateRowButton... buttons)
    {
        this.buttons = Arrays.asList (buttons);
    }

    public List<ActionPlateRowButton> buttons;
}
