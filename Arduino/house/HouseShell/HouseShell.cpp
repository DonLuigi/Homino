#include <Arduino.h>
#include <EEPROM.h>
#include "HouseShell.h"

///////////////////
//
// Component
//
///////////////////
HouseShell::HouseShell (Component** components, Component** commandComponents, Message* message, long loopMillis, ShutterComponent** shutters, U8GlibComponent* u8GlibComponent,
    KeypadComponent** keypads, KeyboardCommands* keypadCommands, int16_t inactivityTimeoutMillis, uint8_t renderButtonWidth, uint8_t renderButtonHeight, uint8_t renderShutterWidth, char* name,
    bool report) :
    Shell (components, commandComponents, message, loopMillis, name, report)
{
    this->shutters = shutters;
    this->u8GlibComponent = u8GlibComponent;
    this->keypads = keypads;
    this->keypadCommands = keypadCommands;
    this->inactivityTimeout = inactivityTimeoutMillis;
    lastActivityMillis = 0;

    // render config
    this->renderButtonWidth = renderButtonWidth;
    this->renderButtonHeight = renderButtonHeight;
    this->renderShutterWidth = renderShutterWidth;

    wereMoving = false;
    selectedShutters[0] = NULL;
    displayCurrentPage = DISPLAY_PAGE_MAIN;
    statusLineBuffer[0] = 0;
    for (noOfShutters = 0; shutters[noOfShutters] != NULL; noOfShutters++)
        ;
}

void HouseShell::setup ()
{
    Shell::setup ();
}

void HouseShell::pulse ()
{
    int32_t nowMillis = millis ();
    bool performActivity = false;

    Shell::pulse ();

    // keypad
}

///////////////////
//
// inactivity
//
///////////////////
// working components
for (int i = 0; i < noOfShutters; i++)
{
    if (shutters[i]->isMoving ())
    {
        lastActivityMillis = nowMillis;
        break;
    }
}

// timeout
if (nowMillis - lastActivityMillis > inactivityTimeout)
{
    statusLineBuffer[0] = 0;
    selectedShutters[0] = NULL;
}

///////////////////
//
// refresh display
//
///////////////////
if (u8GlibComponent != NULL)
{
    u8GlibDisplayRefresh (nowMillis, DISPLAY_PAGE_MAIN, statusLineBuffer, selectedShutters, currentKey != NO_KEY ? OPTION_TOUCH : OPTION_NONE);
}
}

void HouseShell::shuttersMove (int8_t heightPercent, int8_t rotationPercent)
{
COA_DEBUG ("SH:KEY:SHT:MV:%d,%d", heightPercent, rotationPercent);

for (int i = 0; selectedShutters[i] != NULL; i++)
{
    selectedShutters[i]->move (heightPercent, rotationPercent);

    if (selectedShutters[i + 1] != NULL)
    {
        delay (100);
    }
}

// selectedShutters[0] = NULL;

if (rotationPercent < 0)
{
    snprintf (statusLineBuffer, sizeof(statusLineBuffer), "MOVE %d", heightPercent);
}
else if (heightPercent < 0)
{
    snprintf (statusLineBuffer, sizeof(statusLineBuffer), "ROT %d", rotationPercent);
}
else
{
    snprintf (statusLineBuffer, sizeof(statusLineBuffer), "MV %d,%d", heightPercent, rotationPercent);
}
}
