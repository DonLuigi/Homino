#ifndef KEYPAD_COMPONENT_H
#define KEYPAD_COMPONENT_H

#include <Arduino.h>
#include <Keypad.h>
#include <Component.h>

class KeypadComponent: public Keypad, public Component
{
    public:
        KeypadComponent (char* userKeymap, byte* row, byte* col, byte numRows, byte numCols, int32_t repeatTimeoutMillis, const char* name = NULL, bool report = false);
        int8_t press (char key, int32_t nowMillis);

    private:
        int32_t repeatTimeoutMillis;
        char lastKey;
        int8_t lastKeyCount;
        int32_t lastKeyMillis;
};

#endif
