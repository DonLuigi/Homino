#include <Arduino.h>
#include <Command.h>
#include <Message.h>
#include <stdbool.h>
#include <stdint.h>
#include <string.h>
#include <TemperatureComponent.h>


int TemperatureComponent::readFromComponent (Message* message)
{
    uint32_t nowMillis = millis ();

    if (!shouldReport (nowMillis))
    {
        return (Component::ALL_SUBCOMPONENTS);
    }

    reportTemperature (message);

    setReported (nowMillis);

    return (Component::ALL_SUBCOMPONENTS);
}

void TemperatureComponent::writeToComponent (Command* command, Message* message, int subcomponent)
{
    char** parts = command->getParts ();

    if (parts[0] == NULL)
    {
        message->append (Command::COMMAND_ERROR_SYNTAX, name, 0);
        return;
    }

    if (strcasecmp (parts[0], Command::STATUS) == 0)
    {
        reportTemperature (message);
    }

    else
    {
        message->append (Command::COMMAND_ERROR_SYNTAX, name, 1);
    }
}

///////////////////
//
// private
//
///////////////////

void TemperatureComponent::reportTemperature (Message* message)
{
    float temperature;
    float humidity;
    bool ok = read (&temperature, &humidity);

    COA_DEBUG(F("TEMP[%s]:REPORT:OK=%d"), name, ok);
    if (ok)
    {
        message->append ("%s,STATUS,%d.%02d", name, (int) temperature, (int) temperature * 100 % 100);
        if (humidity >= 0 && humidity <= 100)
        {
            message->append (",%d", (int) humidity);
        }
        message->append (";");
    }
    else
    {
        message->append (Command::COMMAND_ERROR, name, Command::READ);
    }
}
