#include <DallasTemperatureComponent.h>
#include <math.h>
#include <stdbool.h>
#include <stdint.h>

DallasTemperatureComponent::DallasTemperatureComponent (uint8_t pin, const char* name) :
    Component (name), oneWire (pin), dallasTemperature (&oneWire)
{
}

bool DallasTemperatureComponent::read (float* temperature, float *humidity)
{
    if (humidity != NULL)
    {
        *humidity = -1;
    }

    if (temperature != NULL)
    {
        dallasTemperature.requestTemperatures ();
        *temperature = dallasTemperature.getTempCByIndex (0);
        if ((int) temperature == DEVICE_DISCONNECTED)
        {
            return (false);
        }
    }

    return (true);
}
