#include <Arduino.h>
#include <Command.h>
#include "LiquidCrystalI2CComponent.h"

///////////////////
//
// Component
//
///////////////////

LiquidCrystalI2CComponent::LiquidCrystalI2CComponent (uint8_t i2cAddres, uint8_t columns, uint8_t rows, unsigned long activityTimeoutMillis, unsigned long flashIntervalMillis,
    unsigned long refreshRateMillis, const char* name) :
    LiquidCrystal_I2C (i2cAddres, columns, rows), Component (name)
{
    this->activityTimeoutMillis = activityTimeoutMillis;
    this->flashIntervalMillis = flashIntervalMillis;
    this->refreshRateMillis = refreshRateMillis;
    this->columns = columns;
    this->rows = rows;
    flashIsOn = false;
    backlightIsOn = false;
    lastActivityMillis = 0;
}

void LiquidCrystalI2CComponent::setup ()
{
    init ();
    touch ();
}

///////////////////
//
// API
//
///////////////////
void LiquidCrystalI2CComponent::write (char* buffer, uint8_t size)
{
    delay (10);
    for (int i = 0; i < size; i++)
    {
        LiquidCrystal_I2C::write (buffer[i]);
    }
}

void LiquidCrystalI2CComponent::touch ()
{
    if (!backlightIsOn)
    {
        backlight ();
        backlightIsOn = true;
    }
    lastActivityMillis = millis ();
}

void LiquidCrystalI2CComponent::flash (bool on)
{
    this->flashIsOn = on;
}

void LiquidCrystalI2CComponent::printFullLine (char* text)
{
    int size = strlen (text);
    char* buffer = (char*) calloc (columns + 1, 1);
    strncpy (buffer, text, columns);

    if (size < columns)
    {
        buffer[columns] = '\0';
        memset (buffer + size, columns - size, ' ');
    }
    print (text);
}

bool LiquidCrystalI2CComponent::isInactive ()
{
    unsigned long currentMillis = millis ();
    unsigned long delta = currentMillis - lastActivityMillis;
    return (flashIsOn || delta > activityTimeoutMillis);
}

bool LiquidCrystalI2CComponent::shouldRefresh ()
{
    return (millis () - lastActivityMillis > refreshRateMillis);
}
