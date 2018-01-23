#include "DallasTemperature.h"

OneWire oneWire (2);
DallasTemperature dallasTemperature (&oneWire);
int i = 0;

void setup ()
{
    Serial.begin (115200);
}

void loop ()
{
    Serial.print (i++);

    dallasTemperature.requestTemperatures ();
    float temperature = dallasTemperature.getTempCByIndex (0);

    if ((int) temperature == DEVICE_DISCONNECTED)
    {
        Serial.println (" ERROR");
    }
    else
    {
        Serial.print (" T: ");
        Serial.println (temperature);
    }

    delay (1000);
}
