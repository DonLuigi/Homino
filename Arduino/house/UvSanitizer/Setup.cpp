#include <OneWire.h>
#include <Shell.h>
#include <ButtonComponent.h>
#include <TimedRelayComponent.h>
#include <LiquidCrystalI2CComponent.h>
#include "ApplicationComponent.h"

#define ENABLE_SERIAL 1

ButtonComponent triggerButton (4, 50, "triggerButton");

// relays
TimedRelayComponent lightTimedRelayComponent (2, LOW, true, 20L * 60 * 1000, 0, &triggerButton, NULL, "relay");

// lcd
LiquidCrystalI2CComponent lcd (0x27, 16, 2, 20 * 1000L, 500L, 100, "lcd");

// application
ApplicationComponent application (&lightTimedRelayComponent, &lcd, "application");

// shell
Component* components[] =
{ &lightTimedRelayComponent, &application, &lcd, NULL };
Shell shell (components, NULL, NULL, NULL, 100, "shell");

void setup ()
{
#if ENABLE_SERIAL
    Serial.begin (115200);
    Serial.setTimeout (5000);
#endif

    // pin 3 is used as a button source; it has to be LOW because button input on pin 4 has enabled internal pullup
    pinMode (3, OUTPUT);
    digitalWrite (3, LOW);

    shell.setup ();
}

void loop ()
{
    shell.loop ();
}
