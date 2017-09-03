package si.krulik.homino.configuration;

import android.view.View;

import si.krulik.homino.configuration.plates.ActionPlatePosition;

public class Plate
{
    public Plate (String id, ActionPlatePosition position, String foregroundColor, String backgroundColor, String buttonBackgroundColor)
    {
        this.id = id;
        this.position = position;
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.buttonBackgroundColor = buttonBackgroundColor;
    }


    public void refresh ()
    {
    }


    public View view;
    public String id;
    public ActionPlatePosition position;
    public String foregroundColor, backgroundColor, buttonBackgroundColor;
}
