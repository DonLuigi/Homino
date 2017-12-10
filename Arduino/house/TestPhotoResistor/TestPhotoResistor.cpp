#include <Arduino.h>
#include <PhotoResistorComponent.h>

int lightPin = A15; //define a pin for Photo resistor
PhotoResistorComponent  photoResistorComponent (lightPin, 10, 60*1000L, "PR");

void setup ()
{
    Serial.begin (115200);
    photoResistorComponent.setup ();
}

void loop ()
{



    Serial.print (photoResistorComponent.read());
    Serial.print (", ");
    Serial.println (photoResistorComponent.read());
    delay (1000);
}
