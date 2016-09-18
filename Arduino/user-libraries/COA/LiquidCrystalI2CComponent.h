#ifndef LIQUID_CRYSTAL_I2_COMPONENT_H
#define LIQUID_CRYSTAL_I2_COMPONENT_H

#include <Wire.h>
#include <LiquidCrystal_I2C.h>
#include <Component.h>

class LiquidCrystalI2CComponent: public LiquidCrystal_I2C, public Component
{
    public:
        ///////////////////
        //
        // Component
        //
        ///////////////////
        LiquidCrystalI2CComponent (uint8_t lcd_Addr, uint8_t columns, uint8_t rows, unsigned long activityTimeout, unsigned long flashTimeout, unsigned long refreshRateMillis,
            const char* name = NULL);
        void setup ();

        ///////////////////
        //
        // API
        //
        ///////////////////
        void write (char* buffer, uint8_t size);
        void touch ();
        void flash (bool on);
        void printFullLine (char* text);
        bool isInactive ();
        bool shouldRefresh ();
        uint8_t columns;
        uint8_t rows;

    private:
        bool backlightIsOn;
        bool flashIsOn;
        unsigned long activityTimeoutMillis;
        unsigned long flashIntervalMillis;
        unsigned long lastActivityMillis;
        unsigned long refreshRateMillis;
};

#endif
