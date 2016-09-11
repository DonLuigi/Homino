#include <Arduino.h>
#include <SPI.h>
#include <Ethernet.h>

byte mac[] =
{ 0xAA, 0xCC, 0xBE, 0xEF, 0xFE, 0xEA };
IPAddress ip (192, 168, 1, 55);

byte messageMagicCookie[] =
{ 0x43, 0x4b, 0x49, 0x45 };

EthernetServer server (5555);

int counter;

int receive (EthernetClient *client, char *buffer, uint16_t bufferSize)
{
    if (client == NULL)
    {
        return (0);
    }

    uint8_t byteRead;
    uint16_t available = client->available ();
    if (available > 0)
    {
        Serial.print ("RECV: ");
        Serial.println (available);

        // magic cookie
        for (uint8_t i = 0; i < sizeof(messageMagicCookie); i++)
        {
            byteRead = client->read ();
            Serial.println (byteRead);
            if (byteRead != messageMagicCookie[i])
            {
                Serial.println ("INVALID MAGIC COOKIE");
                return (-1);
            }
        }

        // size
        uint16_t size = client->read () & 0xFF;
        byteRead = client->read ();
        Serial.print ("SIZE: ");
        Serial.print (size);
        Serial.print (", ");
        Serial.println (byteRead);

        size = (size << 8) + byteRead;

        Serial.print ("SIZE: ");
        Serial.println (size);

        if (size >= bufferSize)
        {
            Serial.println ("INVALID MESSAGE SIZE");
            return (-1);
        }

        // payload
        for (uint16_t i = 0; i < size; i++)
        {
            buffer[i] = client->read ();
        }
        buffer[size] = '\0';
        return (size);
    }

    return (0);
}

int send (EthernetClient *client, char *buffer, uint16_t bufferSize)
{
    if (client == NULL || !client->connected ())
    {
        return (-1);
    }

    // magic cookie
    client->write (messageMagicCookie, sizeof(messageMagicCookie));

    // size
    client->write (bufferSize >> 8);
    client->write (bufferSize & 0xFF);
    Serial.print ("SEND SIZE ");
    Serial.print (bufferSize >> 8);
    Serial.print (", ");
    Serial.println (bufferSize & 0xFF);

    // payload
    Serial.println ("PAYLOAD");
    client->write (buffer, bufferSize);

    Serial.println ("FLUSH");
    client->flush ();

    Serial.println ("DONE");
    return (bufferSize);
}

void setup ()
{
    // Open serial communications and wait for port to open:

    Serial.begin (115200);
    Ethernet.begin (mac, ip);

    Serial.println ("SERVER BEGIN");
    server.begin ();

    Serial.print ("SERVER IP ");
    Serial.println (Ethernet.localIP ());
}

void loop ()
{
    char messageBuffer[512];

    EthernetClient client = server.available ();
    int received = receive (&client, messageBuffer, sizeof(messageBuffer));

    if (received > 0)
    {
        Serial.print ("R: ");
        Serial.println (messageBuffer);

        sprintf (messageBuffer, "BARBI %d", counter++);

        send (&client, messageBuffer, strlen (messageBuffer));
    }

    delay (10);
}
