package si.krulik.homino.configuration;

import android.view.View;

public class Plate
{
    public Plate (String id, int x, int y, int dx, int dy, String foregroundColor, String backgroundColor, String buttonBackgroundColor)
    {
        this.id = id;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.buttonBackgroundColor = buttonBackgroundColor;
    }


    public void refresh ()
    {
    }


    public View view;
    public String id;
    public int x, y, dx, dy;
    public String foregroundColor, backgroundColor, buttonBackgroundColor;
}
