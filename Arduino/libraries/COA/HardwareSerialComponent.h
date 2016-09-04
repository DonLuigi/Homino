#ifndef HARDWARE_SERIAL_COMPONENT_H
#define HARDWARE_SERIAL_COMPONENT_H

#include <Component.h>

class HardwareSerialComponent: public Component
{
    public:
        HardwareSerialComponent (HardwareSerial* serial, const char* name = NULL);
        int readFromComponent (Message* message);
        void writeToComponent (Command* command, Message* message, int subcomponent);

    private:
        HardwareSerial* serial;
};

#endif
