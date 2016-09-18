#ifndef DHT_TEMPERATURE_COMPONENT_H
#define DHT_TEMPERATURE_COMPONENT_H

#include <DHT.h>
#include <Component.h>

class DhtTemperatureComponent: public Component
{
    public:
        // component
        DhtTemperatureComponent (uint8_t pin, const char* name = NULL, uint32_t reportMillis = 0);
        void setup ();
        void writeToComponent (Command* command, Message* message, int subcomponent);

        // api
        bool read (float* temperature, float *humidity);

    private:
        uint8_t pin;
        DHT dht;
};

#endif
