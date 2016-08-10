package si.krulik.homino.configuration.plates;

public class ActionPlateRowButton
{
    public ActionPlateRowButton (String eventName, String text, Integer imageSource, Integer widthMultiplier)
    {
        this.eventName = eventName;
        this.text = text;
        this.imageSource = imageSource;
        this.widthMultiplier = widthMultiplier;
    }

    public String eventName;
    public String text;
    public Integer imageSource;
    public Integer widthMultiplier;
}
