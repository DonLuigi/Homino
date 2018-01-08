package si.krulik.homino.plates.base;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.ToString;


@ToString (includeFieldNames = true) public class PlatePage
{
    public PlatePage (String id, String title, int marginInPx)
    {
        this.id = id;
        this.title = title;
        this.marginInPx = marginInPx;
    }


    public void addPlate (Plate plate)
    {
        plates.add (plate);
    }


    // serializable fields
    @Getter private String id;


    @Getter private String title;


    @Getter private int marginInPx;


    @Getter private List<Plate> plates = new ArrayList ();
}
