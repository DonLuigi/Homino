#include <DhtTemperatureComponent.h>
#include <stdbool.h>
#include <stddef.h>
#include <stdint.h>

///////////////////
//
// Component
//
///////////////////
DhtTemperatureComponent::DhtTemperatureComponent (uint8_t pin, const char* name, uint32_t reportMillis) :
    Component (name, reportMillis)
{
    this->pin = pin;
}

void DhtTemperatureComponent::setup ()
{
    dht.setup (pin);
}

///////////////////
//
// API
//
///////////////////
bool DhtTemperatureComponent::read (float* temperature, float *humidity)
{
    if (temperature != NULL)
    {
        *temperature = dht.getTemperature ();
    }
    if (humidity != NULL)
    {
        *humidity = dht.getHumidity ();
    }

    return (dht.getStatus () == DHT::ERROR_NONE);
}
