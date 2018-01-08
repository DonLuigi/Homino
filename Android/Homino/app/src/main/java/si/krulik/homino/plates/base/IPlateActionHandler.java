package si.krulik.homino.plates.base;

import si.krulik.homino.runtime.Runtime;
import si.krulik.homino.message.MultiMessage;


public interface IPlateActionHandler
{
    MultiMessage handleAction (Runtime.Action action);
}
