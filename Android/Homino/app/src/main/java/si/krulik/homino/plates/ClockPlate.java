package si.krulik.homino.plates;


import lombok.ToString;
import si.krulik.homino.plates.base.Plate;


@ToString (includeFieldNames = true, callSuper = true) public class ClockPlate extends Plate
{
    public ClockPlate (String id, String foregroundColor, String backgroundColor, int layoutId)
    {
        super (id, null, foregroundColor, backgroundColor, null, layoutId, null);
    }
}
