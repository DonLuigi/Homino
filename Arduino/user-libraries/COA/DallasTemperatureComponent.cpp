#include "DallasTemperatureComponent.h"

DallasTemperatureComponent::DallasTemperatureComponent (OneWire* oneWire, const char* name, bool report) :
    Component (name, report), DallasTemperature (oneWire)
{
}

bool DallasTemperatureComponent::readTemperature ()
{
    bool isPreviousNan = isnan (temperature);
    requestTemperatures ();
    temperature = getTempCByIndex (0);
    if ((int) temperature == DEVICE_DISCONNECTED)
    {
        temperature = NAN;
        return (false);
    }

    return (true);
}

void DallasTemperatureComponent::accept (Message* message)
{
    Serial.print ("Current temperature: ");
    readTemperature ();
    Serial.println (temperature);
}
