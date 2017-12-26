#include <Command.h>
#include "ButtonComponent.h"

///////////////////
//
// Component
//
///////////////////
ButtonComponent::ButtonComponent (uint8_t pin, uint16_t intervalMillis, const char* name, uint32_t reportMillis) :
    Component (name, reportMillis)
{
    pinMode (pin, INPUT_PULLUP);
    bounce.attach (pin);
    bounce.interval (intervalMillis);
    pressStartMillis = 0;
}

int ButtonComponent::readFromComponent (Message* message)
{
    bounce.update ();
    return (0);
}

///////////////////
//
// API
//
///////////////////
bool ButtonComponent::isPressed ()
{
    bounce.update ();
    return (bounce.read () == LOW);
}

bool ButtonComponent::onPress ()
{
    bounce.update ();
    bool fell = bounce.fell ();
    if (fell)
    {
        COA_DEBUG (F("BT[%s]:PRESS"), name);
        pressStartMillis = millis ();
    }
    return (fell);
}
uint32_t ButtonComponent::onRelease ()
{
    uint32_t ret = 0;
    bool rose = bounce.rose ();
    if (rose)
    {
        ret = millis () - pressStartMillis;
        COA_DEBUG (F("BT[%s]:RELEASE:%dMS"), name, ret);
    }

    return (ret);
}
