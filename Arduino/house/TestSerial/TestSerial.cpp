// Do not remove the include below
#include "TestSerial.h"

void setup ()
{
    Serial.begin (115200);
    Serial.println ("Begin");
}

void loop ()
{
    Serial.println ("Begin");
    delay (1000);
}
