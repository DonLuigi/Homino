#ifndef RELAY_COMPONENT_H
#define RELAY_COMPONENT_H

#include <Component.h>
#include <PinComponent.h>

class RelayComponent: public PinComponent
{
    public:
        // component
        RelayComponent (uint8_t pin, uint8_t initialState, bool inverted, const char* name);
        void writeToComponent (Command* command, Message* message, int subcomponent);

        // api
        void write (uint8_t state);
        uint8_t read ();

    private:
        bool inverted;
};

#endif
