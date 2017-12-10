package si.krulik.homino.configuration.plate.common;

import java.util.List;

import si.krulik.homino.configuration.Configuration;
import si.krulik.homino.configuration.command.Action;
import si.krulik.homino.message.Message;
import si.krulik.homino.message.MultiMessage;


public interface IPlateActionHandler
{
    MultiMessage handleAction (Action action, Configuration configuration);
}
