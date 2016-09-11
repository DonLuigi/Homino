#include "DHT.h"

DHT dht;

void setup ()
{
    Serial.begin (115200);
    Serial.println ();
    Serial.println ("Status\tHumidity (%)\tTemperature (C)\t(F)");

    dht.setup (21); // data pin 2
}

void loop ()
{
    delay (dht.getMinimumSamplingPeriod ());

    long start = millis ();
    float humidity = dht.getHumidity ();
    float temperature = dht.getTemperature ();

    long stop = millis ();

    Serial.print (dht.getStatusString ());
    Serial.print ("\t");
    Serial.print (humidity, 1);
    Serial.print ("\t\t");
    Serial.print (temperature, 1);
    Serial.print ("\t\t");
    Serial.print (stop - start);

}
