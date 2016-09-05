#include <arduino.h>
#include <limits.h>

int minPin = 21;
int maxPin = 21;

void setup ()
{
    for (int i = minPin; i <= maxPin; i++)
    {
        pinMode (i, OUTPUT);
        digitalWrite (i, HIGH);
    }

    Serial.begin (115200);
    Serial.setTimeout (LONG_MAX);
}

void loop ()
{
    if (1)
    {
        for (int pin = minPin; pin <= maxPin; pin++)
        {
            digitalWrite (pin, LOW);
        }
        delay (2500);

        for (int pin = minPin; pin <= maxPin; pin++)
        {
            digitalWrite (pin, HIGH);
        }
        delay (2500);
    }

    if (0)
    {
        for (int pin = 41; pin <= maxPin; pin++)
        {
            Serial.println (pin);

            digitalWrite (pin, LOW);

            char buffer[1];
            Serial.readBytesUntil ('\r', buffer, 1);

            digitalWrite (pin, HIGH);
        }
    }
}
