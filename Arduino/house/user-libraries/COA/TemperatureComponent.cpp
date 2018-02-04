#include <Arduino.h>
#include <Command.h>
#include <Message.h>
#include <stdbool.h>
#include <stdint.h>
#include <string.h>
#include <TemperatureComponent.h>

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
        reportStatus (message);
    }

    else
    {
        message->append (Command::COMMAND_ERROR_SYNTAX, name, 1);
    }
}

void TemperatureComponent::reportStatus (Message* message)
{
    float temperature;
    float humidity;
    bool ok = read (&temperature, &humidity);

    COA_DEBUG(F("TEMP[%s]:REPORT:OK=%d"), name, ok);
    if (ok)
    {
        message->append ("%s,STATUS,%d.%01d", name, (int) temperature, ((int) (temperature * 100)) % 100);
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
