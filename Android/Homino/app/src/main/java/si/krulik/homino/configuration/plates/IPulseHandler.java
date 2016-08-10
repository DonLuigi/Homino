package si.krulik.homino.configuration.plates;

import java.util.Date;

import si.krulik.homino.configuration.Configuration;

public interface IPulseHandler
{
    void pulse (Configuration configuration, Date now);
}
