#ifndef DALLAS_TEMPERATURE_COMPONENT_H
#define DALLAS_TEMPERATURE_COMPONENT_H

#include <DallasTemperature.h>
#include <OneWire.h>
#include <Component.h>
#include <TemperatureComponent.h>

class DallasTemperatureComponent: public TemperatureComponent
{
    public:
        // component
        DallasTemperatureComponent (uint8_t pin, const char* name);

        // api
        bool read (float* temperature, float *humidity);

    private:
        uint8_t pin;
        OneWire oneWire;
        DallasTemperature dallasTemperature;
};

#endif
