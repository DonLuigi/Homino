#ifndef BUTTON_COMPONENT_H
#define BUTTON_COMPONENT_H

#include <Component.h>
#include <Bounce2.h>

class ButtonComponent: public Component
{
    public:
        // component
        ButtonComponent (uint8_t pin, uint16_t intervalMillis, const char* name);
        virtual int readFromComponent (Message* message);

        // api
        bool isPressed (); //
        bool onPress (); //
        uint32_t onRelease ();

    private:
        Bounce bounce;
        uint32_t pressStartMillis;
};

#endif
