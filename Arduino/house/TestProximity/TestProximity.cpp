#include <Arduino.h>

#define PROXIMITY_PIN A0

void setup ()
{
    pinMode (PROXIMITY_PIN, INPUT_PULLUP);
    pinMode (LED_BUILTIN, OUTPUT);

}

void loop ()
{
    digitalWrite (LED_BUILTIN, digitalRead (PROXIMITY_PIN) == LOW ? LOW : HIGH);
    delay (10);
}
