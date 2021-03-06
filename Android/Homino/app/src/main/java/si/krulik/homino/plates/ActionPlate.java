package si.krulik.homino.plates;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.ToString;
import si.krulik.homino.R;
import si.krulik.homino.runtime.Runtime;
import si.krulik.homino.plates.base.IPlateActionHandler;
import si.krulik.homino.plates.base.Plate;


@ToString (includeFieldNames = true, callSuper = true) public class ActionPlate extends Plate
{
    public ActionPlate (String id, String foregroundColor, String backgroundColor, String buttonBackgroundColor, String title, int layoutId, IPlateActionHandler messageHandler, ActionPlateRow... rows)
    {
        super (id, title, foregroundColor, backgroundColor, buttonBackgroundColor, layoutId, messageHandler);
        this.rows = Arrays.asList (rows);
    }


    @Override public void refresh ()
    {
        for (View view : getViewByPlatePageId ().values ())
        {
            ((TextView) view.findViewById (R.id.titleTextView)).setText (title);
        }
    }


    @Override public View inflate (String pageId, LayoutInflater layoutInflater)
    {
        View view = super.inflate (pageId, layoutInflater);
        TableLayout tableLayout = (TableLayout) view.findViewById (R.id.actionPlateLayoutTableView);


        for (ActionPlateRow actionPlateRow : getRows ())
        {
            TableRow tableRow = new TableRow (Runtime.context);
            tableLayout.addView (tableRow);

            for (ActionPlateRowButton actionPlateRowButton : actionPlateRow.buttons)
            {
                if (actionPlateRowButton.getTitle () != null)
                {
                    View buttonView = layoutInflater.inflate (R.layout.action_plate_layout_text_button, null);
                    TextView rowTextView = (TextView) buttonView.findViewById (R.id.actionPlateLayoutTextButton);
                    rowTextView.setText (actionPlateRowButton.getTitle ());

                    if (actionPlateRowButton.getWidthMultiplier () != null)
                    {
                        rowTextView.getLayoutParams ().width *= actionPlateRowButton.getWidthMultiplier ();
                    }

                    rowTextView.setOnClickListener (new ViewOnClickListener (actionPlateRowButton.getAction (), this));

                    tableRow.addView (buttonView);
                }
                else if (actionPlateRowButton.getImageSource () != null)
                {
                    View buttonView = layoutInflater.inflate (R.layout.action_plate_layout_image_button, null);
                    ImageButton imageButton = (ImageButton) buttonView.findViewById (R.id.actionPlateLayoutImageButton);
                    imageButton.setImageResource (actionPlateRowButton.getImageSource ());

                    if (actionPlateRowButton.getWidthMultiplier () != null)
                    {
                        imageButton.getLayoutParams ().width *= actionPlateRowButton.getWidthMultiplier ();
                    }

                    imageButton.setOnClickListener (new ViewOnClickListener (actionPlateRowButton.getAction (), this));

                    tableRow.addView (buttonView);
                }
            }
        }

        return (view);
    }


    @Getter private List<ActionPlateRow> rows;
}