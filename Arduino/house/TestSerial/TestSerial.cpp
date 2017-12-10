#include <Arduino.h>

#include "PinComponent.h"

PinComponent p = PinComponent (5, OUTPUT, HIGH, "AAA");

void setup ()
{
    Serial.begin (115200);
    p.setup ();
}

void loop ()
{
    delay (1000);

    Serial.println ("X");
}
