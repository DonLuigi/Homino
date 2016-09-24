#ifndef DALLAS_TEMPERATURE_COMPONENT_H
#define DALLAS_TEMPERATURE_COMPONENT_H

#include <OneWire.h>
#include <DallasTemperature.h>
#include <Component.h>

class DallasTemperatureComponent: public DallasTemperature, public Component
{
    public:
        DallasTemperatureComponent (OneWire* oneWire, const char* name = NULL, bool report = false);
        float temperature;
        bool readTemperature ();
        void accept (Message* message);
};

#endif
