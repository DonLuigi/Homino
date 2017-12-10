#ifndef SHELL_H
#define SHELL_H

#include <Arduino.h>
#include <Component.h>

#define COA_SHELL_MAX_MESSAGE_SIZE 128

class Shell: public Component
{
    public:
        Shell (Component** components, Component** commandComponents, Message* inboundMessage, Message* outboundMessage, long loopMillis, const char* name = NULL, bool report = false);
        void setup ();
        void loop ();

        // Component
        void writeToComponent (Command* command, Message* message, int subcomponent);


    private:
        Component** components;
        Component** commandComponents;
        Message* inboundMessage;
        Message* outboundMessage;
        long loopMillis;

        // uptime
        uint64_t uptimeMillis = 0;
        uint32_t uptimeLastUpdateMillis = 0;
};

#endif
