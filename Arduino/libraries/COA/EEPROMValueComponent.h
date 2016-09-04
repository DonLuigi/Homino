#ifndef EEPROM_VALUE_COMPONENT_H
#define EEPROM_VALUE_COMPONENT_H

#include <Component.h>

class EEPROMValueComponent: public Component
{
    public:
        EEPROMValueComponent (uint8_t address, uint8_t size);
        void write (void* value);
        uint8_t readAsUInt8 ();
        uint32_t readAsUInt32 ();

    private:
        uint8_t size;
        uint32_t address;
};

#endif
