#include <arduino.h>
#include <limits.h>

int pins[]
{ 2, 5 };

void setup ()
{
    for (uint8_t i = 0; i <= sizeof(pins) / sizeof(pins[0]); i++)
    {
        pinMode (pins[i], OUTPUT);
        digitalWrite (pins[i], HIGH);
    }

    Serial.begin (115200);
    Serial.setTimeout (LONG_MAX);
}

void loop ()
{
    if (1)
    {
        for (uint8_t i = 0; i <= sizeof(pins) / sizeof(pins[0]); i++)
        {
            digitalWrite (pins[i], LOW);
        }
        delay (2500);

        for (uint8_t i = 0; i <= sizeof(pins) / sizeof(pins[0]); i++)
        {
            digitalWrite (pins[i], HIGH);
        }
        delay (2500);
    }

    if (0)
    {
        for (uint8_t i = 0; i <= sizeof(pins) / sizeof(pins[0]); i++)
        {
            Serial.println (pins[i]);

            digitalWrite (pins[i], LOW);

            char buffer[1];
            Serial.readBytesUntil ('\r', buffer, 1);

            digitalWrite (pins[i], HIGH);
        }
    }
}
