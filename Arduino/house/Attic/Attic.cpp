#include <Arduino.h>
#include <Shell.h>
#include <HardwareSerialComponent.h>
#include <EthernetW5100Component.h>
#include <DhtTemperatureComponent.h>

DhtTemperatureComponent thermometer1 (21, "t1");

///////////////////
//
// global
//
///////////////////
#define ENABLE_ETHERNET 1
#define ENABLE_SERIAL 1
uint8_t eepromPosition = 0;
const unsigned long reportMillis = 500;
const unsigned long windowLouvreShutterRotationMillis = 1500;

///////////////////
//
//
//
///////////////////
DhtTemperatureComponent dhtTemperatureComponent (21, "t1");

///////////////////
//
// Ethernet
//
///////////////////
#if ENABLE_ETHERNET
byte mac[] =
{ 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xEE };
IPAddress ip (192, 168, 1, 51);
EthernetW5100Component ethernetServer (mac, &ip, 5555, "eth");
#endif

///////////////////
//
// Serial
//
///////////////////
#if ENABLE_SERIAL
HardwareSerialComponent serial (&Serial, "serial");
#endif

///////////////////
//
// Message
//
///////////////////
#define MESSAGE_BUFFER_SIZE 512

char inboundMessageBuffer[MESSAGE_BUFFER_SIZE];
Message inboundMessage (inboundMessageBuffer, MESSAGE_BUFFER_SIZE);

char outboundMessageBuffer[MESSAGE_BUFFER_SIZE];
Message outboundMessage (outboundMessageBuffer, MESSAGE_BUFFER_SIZE);

///////////////////
//
// Components
//
///////////////////
Component* components[] =
{ &dhtTemperatureComponent, NULL };

Component* commandComponents[] =
{
#if ENABLE_ETHERNET
    &ethernetServer,
#endif

#if ENABLE_SERIAL
    &serial,
#endif
    NULL };

///////////////////
//
// Shell
//
///////////////////
Shell shell (components, commandComponents, &inboundMessage, &outboundMessage, 10, "shell");

///////////////////
//
// Main
//
///////////////////
void setup ()
{
    Serial.begin (115200);
    Serial.setTimeout (5000);
    shell.setup ();
}

void loop ()
{
    shell.loop ();
}
