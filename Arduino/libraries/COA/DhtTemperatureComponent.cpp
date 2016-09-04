#include <Command.h>
#include "DhtTemperatureComponent.h"

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

void DhtTemperatureComponent::writeToComponent (Command* command, Message* message, int subcomponent)
{
    char** parts = command->getParts ();

    if (parts[0] == NULL)
    {
        message->append (Command::COMMAND_ERR_SYNTAX, name, 0);
        return;
    }

    if (strcasecmp (parts[0], "STATUS") == 0)
    {
        float temperature;
        float humidity;
        bool ok = read (&temperature, &humidity);

        if (ok)
        {
            message->append ("%s,STATUS,%d,%d", name, (int) temperature, (int) humidity);
        }
        else
        {
            message->append ("%s,STATUS,ERROR", name);
        }
    }

    else
    {
        message->append (Command::COMMAND_ERR_SYNTAX, name, 1);
    }
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
