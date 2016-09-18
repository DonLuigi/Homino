#include "KeypadComponent.h"

KeypadComponent::KeypadComponent (char* userKeymap, byte* row, byte* col, byte numRows, byte numCols, int32_t repeatTimeout, const char* name, bool report) :
    Component (name, report), Keypad (userKeymap, row, col, numRows, numCols)
{
    this->repeatTimeoutMillis = repeatTimeout;
    lastKeyMillis = 0;

}

int8_t KeypadComponent::press (char key, int32_t nowMillis)
{
    bool timeout = nowMillis - lastKeyMillis > repeatTimeoutMillis;

    // press
    if (!timeout && key == lastKey)
    {
        lastKeyCount++;
    }
    else
    {
        lastKeyCount = 1;
        lastKey = key;
    }

    lastKeyMillis = nowMillis;
    return (lastKeyCount);
}

