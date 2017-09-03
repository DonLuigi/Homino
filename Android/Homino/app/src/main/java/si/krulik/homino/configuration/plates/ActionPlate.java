package si.krulik.homino.configuration.plates;

import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import si.krulik.homino.R;
import si.krulik.homino.configuration.Plate;

public class ActionPlate extends Plate
{
    public ActionPlate (String id, ActionPlatePosition position, String foregroundColor, String backgroundColor, String buttonBackgroundColor, String text, ActionPlateRow... rows)
    {
        super (id, position, foregroundColor, backgroundColor, buttonBackgroundColor);
        this.text = text;
        this.rows = Arrays.asList (rows);
    }


    @Override
    public void refresh ()
    {
        ((TextView) view.findViewById (R.id.titleTextView)).setText (text);
    }



    public String text;
    public List<ActionPlateRow> rows;
}