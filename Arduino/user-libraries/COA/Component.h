#ifndef COMPONENT_H
#define COMPONENT_H

#include <Arduino.h>
#include <Command.h>

#define COA_DEBUG_ENABLED 1

#if COA_DEBUG_ENABLED
#warning "Debugging enabled!"
#define COA_DEBUG(...) debug(__VA_ARGS__)
#else
#define COA_DEBUG(...)
#endif

class Component
{
    public:
        Component (const char* name = NULL, uint32_t reportMillis = 0);
        virtual void setup ();
        virtual int readFromComponent (Message* message);
        virtual void writeToComponent (Command* command, Message* message, int subcomponent);
        uint8_t toString (char* buffer, uint8_t size);
        uint8_t toString (char* buffer, uint8_t size, uint8_t customFontOffset);

        static const int ALL_SUBCOMPONENTS = -1;

        // fileds
        const char* name;
        char* error;
        uint32_t reportMillis;

        // debug
#if COA_DEBUG_ENABLED
        void debug (char* text);
        void debug (const __FlashStringHelper* format, ...);
#endif
        };

#endif
