#include <Arduino.h>

uint64_t cumulativeMillis = 0;
uint32_t lastMillis = 0;


 void setup ()
{
    Serial.begin (115200);
}

void loop ()
{
    uint32_t nowMillis = millis () + (UINT32_MAX - 10000);

    Serial.print ("NOW MS:");
    Serial.println (nowMillis);

    Serial.print ("LAST M:");
    Serial.println (lastMillis);

    uint32_t deltaMillis = nowMillis - lastMillis;
    Serial.print ("DELTA M:");
    Serial.println (deltaMillis);

    cumulativeMillis += deltaMillis;
    Serial.print ("CUMULATIVE MS:");
    Serial.println ((uint32_t) cumulativeMillis);

    uint32_t cumulativeSeconds = cumulativeMillis / 1000;
    Serial.print ("CUMULATIVE S:");
    Serial.println (cumulativeSeconds);

    lastMillis = nowMillis;

    Serial.println();
    Serial.println();
    Serial.println();

    delay (768);
}
