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
        Component (const char* name = NULL, uint32_t reportEveryMillis = 0);
        virtual void setup ();
        virtual int readFromComponent (Message* message);
        virtual void writeToComponent (Command* command, Message* message, int subcomponent);
        uint8_t toString (char* buffer, uint8_t size);
        uint8_t toString (char* buffer, uint8_t size, uint8_t customFontOffset);

        static const int ALL_SUBCOMPONENTS = -1;

        // fileds
        const char* name;
        // char* error;

        // reporting
        boolean shouldReport (uint32_t timeMillis);
        boolean shouldReport (uint32_t timeMillis, uint32_t reportEveryMillis);
        void setReported (uint32_t timeMillis);

        // debug
#if COA_DEBUG_ENABLED
        void debug (char* text);
        void debug (const __FlashStringHelper* format, ...);
#endif

            private:
            // reporting
            uint32_t reportEveryMillis;
            uint32_t lastReportMillis;
        };
#endif