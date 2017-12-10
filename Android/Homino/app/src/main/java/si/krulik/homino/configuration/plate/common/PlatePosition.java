package si.krulik.homino.configuration.plate.common;

import lombok.Getter;
import lombok.ToString;


@ToString(includeFieldNames=true)
public class PlatePosition
{
    public PlatePosition (String platePageId, String plateId, int x, int y, int dx, int dy)
    {
        this.platePageId = platePageId;
        this.plateId = plateId;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }


    @Getter private String platePageId;


    @Getter private String plateId;


    @Getter private int x, y, dx, dy;
}
