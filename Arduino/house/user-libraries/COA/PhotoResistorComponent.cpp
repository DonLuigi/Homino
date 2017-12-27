#include <Arduino.h>
#include <Command.h>
#include <Message.h>
#include <PhotoResistorComponent.h>
#include <stdint.h>
#include <string.h>

PhotoResistorComponent::PhotoResistorComponent (uint8_t pin, uint32_t sampleEveryMillis, uint32_t sampleDurationMillis, const char* name) :
    Component (name)
{
    this->pin = pin;
    this->sampleEveryMillis = sampleEveryMillis;
    this->sampleDurationMillis = sampleDurationMillis;

    numberOfSamples = 0;
    cumulativeSamples = 0L;
    lastSampleMillis = 0L;
    lastCumulativeSamplesMillis = 0L;
    lastAverageSamples = -1;
}

void PhotoResistorComponent::setup ()
{
    pinMode (pin, INPUT);

}

uint16_t PhotoResistorComponent::read ()
{
    return (lastAverageSamples);
}

int PhotoResistorComponent::readFromComponent (Message* message)
{
    uint32_t nowMillis = millis ();

    if (nowMillis > lastSampleMillis + sampleEveryMillis)
    {
        lastSampleMillis = nowMillis;

        cumulativeSamples += analogRead (pin);
        numberOfSamples++;
    }

    if (nowMillis > lastCumulativeSamplesMillis + sampleDurationMillis)
    {
        lastCumulativeSamplesMillis = nowMillis;

        lastAverageSamples = (cumulativeSamples / numberOfSamples);
        cumulativeSamples = 0;
        numberOfSamples = 0;
    }

    return (Component::ALL_SUBCOMPONENTS);
}

void PhotoResistorComponent::writeToComponent (Command* command, Message* message, int subcomponent)
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

void PhotoResistorComponent::reportStatus (Message* message)
{
    message->append ("%s,STATUS,%d;", name, lastAverageSamples);
}
