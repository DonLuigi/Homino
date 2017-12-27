#ifndef COMPONENT_H
#define COMPONENT_H

#include <Arduino.h>
#include <stddef.h>
#include <stdint.h>
#include <Message.h>
#include <Command.h>

#define COA_DEBUG_ENABLED 0

#if COA_DEBUG_ENABLED
#warning "Debugging enabled!"
#define COA_DEBUG(...) debug(__VA_ARGS__)
#else
#define COA_DEBUG(...)
#endif

class Component
{
    public:
        Component (const char* name = NULL);
        virtual void setup ();
        virtual int readFromComponent (Message* message);
        virtual void writeToComponent (Command* command, Message* message, int subcomponent);
        virtual void reportStatus (Message* message);

        const char* name;

        static const int ALL_SUBCOMPONENTS = -1;

        // debug
#if COA_DEBUG_ENABLED
        void debug (char* text);
        void debug (const __FlashStringHelper* format, ...);
#endif
};
#endif
