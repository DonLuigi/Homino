#include <arduino.h>
#include <limits.h>

int minPin = 22;
int maxPin = minPin + 32 - 1;

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
        for (int pin = 52; pin <= 52; pin++)
        {
            digitalWrite (pin, LOW);
        }
        delay (1500);

        for (int pin = 52; pin <= 52; pin++)
        {
            digitalWrite (pin, HIGH);
        }
        delay (1500);
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
