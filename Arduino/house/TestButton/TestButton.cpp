#include <Arduino.h>
#include <ButtonComponent.h>

ButtonComponent testButton (4, 50, "test");

ButtonComponent* buttons[] =
{ &testButton, NULL };

void setup ()
{

    Serial.begin (115200);
    Serial.println ("BEGIN");

    pinMode (3, OUTPUT);
    digitalWrite (3, LOW);
}

void loop ()
{
    for (int i = 0; buttons[i] != NULL; i++)
    {
        if (buttons[i]->onPress ())
        {
            Serial.print (millis () / 1000);
            Serial.print (" PRESSED: ");
            Serial.println (buttons[i]->name);
        }
        uint32_t duration = buttons[i]->onRelease ();
        if (duration > 0)
        {
            Serial.print (millis () / 1000);
            Serial.print (" RELEASED: ");
            Serial.print (duration);
            Serial.print (": ");
            Serial.println (buttons[i]->name);
        }
    }
}
