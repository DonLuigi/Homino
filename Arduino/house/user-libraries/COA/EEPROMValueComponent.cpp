#include <avr/eeprom.h>
#include "EEPROMValueComponent.h"

EEPROMValueComponent::EEPROMValueComponent (uint8_t address, uint8_t size, const char* name) :
    Component (name)
{
    this->address = address;
    this->size = size;
}

void EEPROMValueComponent::write (void* value)
{
    eeprom_write_block (value, (void*) address, size);
}

uint8_t EEPROMValueComponent::readAsUInt8 ()
{
    return (eeprom_read_byte ((uint8_t*) address));
}

uint32_t EEPROMValueComponent::readAsUInt32 ()
{
    return (eeprom_read_dword ((uint32_t*) address));
}
