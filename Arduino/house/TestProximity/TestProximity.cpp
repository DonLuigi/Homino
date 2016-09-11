#include <Arduino.h>

#define PROXIMITY_PIN A5
#define RELAY_PIN A9

int previous = 0;

void setup ()
{
    Serial.begin (115200);
    Serial.println ("BEGIN");
    pinMode (PROXIMITY_PIN, INPUT_PULLUP);
    pinMode (RELAY_PIN, OUTPUT);
    digitalWrite (RELAY_PIN, HIGH);
}

void loop ()
{
//    int i = digitalRead (PROXIMITY_PIN);
//    if (i != previous)
//    {
//        Serial.println ("XXXXXXXXXXXXXXXXXXXXXXX");
//    }
//    Serial.println (i);
//    previous = i;
//    delay (100);

    if (digitalRead (PROXIMITY_PIN) == LOW)
    {
//        Serial.println (digitalRead (PROXIMITY_PIN));
        digitalWrite (RELAY_PIN, LOW);
        Serial.println (digitalRead (PROXIMITY_PIN));
        delay (3000);
        Serial.println (digitalRead (PROXIMITY_PIN));
        digitalWrite (RELAY_PIN, HIGH);
        Serial.println (digitalRead (PROXIMITY_PIN));
        delay (3000);
        Serial.println (digitalRead (PROXIMITY_PIN));
        Serial.println ("_------------------------");
    }

    delay (10);
}
