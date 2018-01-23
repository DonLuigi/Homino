#include <Arduino.h>
// #include <PhotoResistorComponent.h>

int lightPin = A0; //define a pin for Photo resistor
//PhotoResistorComponent  photoResistorComponent (lightPin, 10, 60*1000L, "PR");

void setup ()
{
    Serial.begin (115200);
    // photoResistorComponent.setup ();
}

void loop ()
{
    int light = analogRead (lightPin);
    Serial.println (light);


//    Serial.print (photoResistorComponent.read());
//    Serial.print (", ");
//    Serial.println (photoResistorComponent.read());


    delay (1000);
}
