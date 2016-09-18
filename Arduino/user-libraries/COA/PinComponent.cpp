#include <Command.h>
#include "PinComponent.h"

///////////////////
//
// Component
//
///////////////////
PinComponent::PinComponent (uint8_t pin, uint8_t mode, uint8_t initialState, const char* name, uint32_t reportMillis) :
    Component (name, reportMillis)
{
    this->pin = pin;
    this->mode = mode;
    state = initialState;
}

void PinComponent::setup ()
{
    setMode (mode);

    if (mode == OUTPUT)
    {
        write (state);
    }
    else
    {
        read ();
    }

    COA_DEBUG (F("PN:%s(%d):SETUP:%d(%s)"), name, pin, state, mode == OUTPUT ? "OUT" : "IN");
}

///////////////////
//
// API
//
///////////////////
void PinComponent::write (uint8_t state)
{
    if (state != this->state)
    {
        COA_DEBUG (F("PN:%s(%d):%d"), name, pin, state);
    }
    digitalWrite (pin, state);
    this->state = state;
}

void PinComponent::pwmWrite (uint8_t ratio)
{
    COA_DEBUG (F("PN:%s(%d):%d"), name, pin, ratio);
    analogWrite (pin, ratio);
    this->state = ratio;
}

uint8_t PinComponent::read ()
{
    return (mode == OUTPUT ? state : (state = digitalRead (pin)));
}

void PinComponent::flip ()
{
    write (state == LOW ? HIGH : LOW);
}

boolean PinComponent::hasFlipped ()
{
    uint8_t previousState = state;
    uint8_t currentState = read ();
    return (currentState != previousState);
}

void PinComponent::setMode (uint8_t mode)
{
    pinMode (pin, mode);
}
