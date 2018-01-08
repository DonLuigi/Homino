package si.krulik.homino.plates;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import si.krulik.homino.runtime.Runtime;


@AllArgsConstructor @ToString (includeFieldNames = true) public class ActionPlateRowButton
{
    @Getter private String title;


    @Getter private Runtime.Action action;


    @Getter private Integer widthMultiplier;


    @Getter private Integer imageSource;
}
