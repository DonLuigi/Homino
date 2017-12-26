#ifndef DHT_TEMPERATURE_COMPONENT_H
#define DHT_TEMPERATURE_COMPONENT_H

#include <DHT.h>
#include <TemperatureComponent.h>

class DhtTemperatureComponent: public TemperatureComponent
{
    public:
        // component
        DhtTemperatureComponent (uint8_t pin, const char* name = NULL, uint32_t reportMillis = 0);
        void setup ();

        // api
        bool read (float* temperature, float *humidity);

    private:
        uint8_t pin;
        DHT dht;
};

#endif
