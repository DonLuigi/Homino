package si.krulik.homino.message;

import si.krulik.homino.configuration.Configuration;


public interface IMultiMessageHandler
{
    void handleMultiMessage (MultiMessage multiMessage, Configuration configuration);
}
