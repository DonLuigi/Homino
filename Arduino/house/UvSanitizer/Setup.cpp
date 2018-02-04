#include <OneWire.h>
#include <Shell.h>
#include <ButtonComponent.h>
#include <TimedRelayComponent.h>
#include <LiquidCrystalI2CComponent.h>
#include "ApplicationComponent.h"

#define ENABLE_SERIAL 1

ButtonComponent triggerButton1 (4, 50, "triggerButton1");
ButtonComponent triggerButton2 (4, 50, "triggerButton2");

// relays
TimedRelayComponent lightTimedRelay1Component (2, LOW, true, 20L * 60 * 1000, 0, &triggerButton1, NULL, 0, TIMED_RELAY_COMPONENT_OPTION_NONE, "relay1");
TimedRelayComponent lightTimedRelay2Component (5, LOW, true, 20L * 60 * 1000, 0, &triggerButton2, NULL, 0, TIMED_RELAY_COMPONENT_OPTION_NONE, "relay2");

// lcd
LiquidCrystalI2CComponent lcd (0x27, 16, 2, 20 * 1000L, 500L, 100, "lcd");

// application
ApplicationComponent application (&lightTimedRelay1Component, &lcd, "application");

// shell
Component* components[] =
{ &lightTimedRelay1Component, &lightTimedRelay2Component, &application, &lcd, NULL };
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
