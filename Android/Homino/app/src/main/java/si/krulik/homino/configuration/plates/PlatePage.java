package si.krulik.homino.configuration.plates;

import java.util.Arrays;
import java.util.List;

import si.krulik.homino.configuration.Plate;

public class PlatePage
{
    public PlatePage (String title, int marginInPx, Plate... plates)
    {
        this.title = title;
        this.marginInPx = marginInPx;
        this.plates = Arrays.asList (plates);
    }


    // serializable fields
    public String title;
    public int marginInPx;
    public List<Plate> plates;
}
