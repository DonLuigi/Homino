package si.krulik.homino.plates;

import java.util.Arrays;
import java.util.List;

import lombok.ToString;


@ToString (includeFieldNames = true) public class ActionPlateRow
{
    public ActionPlateRow (ActionPlateRowButton... buttons)
    {
        this.buttons = Arrays.asList (buttons);
    }


    public List<ActionPlateRowButton> buttons;
}
