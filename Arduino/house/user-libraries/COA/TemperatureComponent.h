#ifndef TEMPERATURE_COMPONENT_H
#define TEMPERATURE_COMPONENT_H

#include <DHT.h>
#include <Component.h>

class TemperatureComponent: public virtual Component
{
    public:
        // component
        void writeToComponent (Command* command, Message* message, int subcomponent);
        int readFromComponent (Message* message);

        // api
        virtual bool read (float* temperature, float *humidity) = 0;

    private:
        void reportTemperature (Message* message);
};

#endif