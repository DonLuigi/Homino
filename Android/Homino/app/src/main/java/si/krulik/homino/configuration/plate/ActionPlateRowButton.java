package si.krulik.homino.configuration.plate;

import lombok.Getter;
import si.krulik.homino.configuration.command.Action;


public class ActionPlateRowButton
{
    public ActionPlateRowButton (Action action, String title, Integer imageSource, Integer widthMultiplier)
    {
        this.action = action;
        this.title = title;
        this.imageSource = imageSource;
        this.widthMultiplier = widthMultiplier;
    }


    @Getter
    private Action action;

    @Getter
    private String title;

    @Getter
    private Integer imageSource;

    @Getter
    private Integer widthMultiplier;
}
