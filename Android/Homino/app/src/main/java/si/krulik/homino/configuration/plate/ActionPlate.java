package si.krulik.homino.configuration.plate;

import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.ToString;
import si.krulik.homino.R;
import si.krulik.homino.configuration.plate.common.IPlateActionHandler;
import si.krulik.homino.configuration.plate.common.Plate;


@ToString (includeFieldNames = true, callSuper = true) public class ActionPlate extends Plate
{
    public ActionPlate (String id, String foregroundColor, String backgroundColor, String buttonBackgroundColor, String title, IPlateActionHandler messageHandler, ActionPlateRow... rows)
    {
        super (id, title, foregroundColor, backgroundColor, buttonBackgroundColor, messageHandler);
        this.rows = Arrays.asList (rows);
    }


    @Override public void refresh ()
    {
        ((TextView) view.findViewById (R.id.titleTextView)).setText (title);
    }


    @Getter private List<ActionPlateRow> rows;
}