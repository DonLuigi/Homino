package si.krulik.homino.configuration;

public interface IMessageHandler
{
    void handle (String source, String eventName, Configuration configuration);
}
