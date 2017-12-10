#include <Arduino.h>
#include <Shell.h>
#include <DallasTemperatureComponent.h>
#include <DhtTemperatureComponent.h>
#include <TimedRelayComponent.h>
#include <PhotoResistorComponent.h>
#include <HardwareSerialComponent.h>
#include <EthernetW5100Component.h>

///////////////////
//
// global
//
///////////////////
#define ENABLE_ETHERNET 1
#define ENABLE_SERIAL 1

#define DEFAULT_REPORT 60 * 1000L

///////////////////
//
// thermometers
//
///////////////////
DallasTemperatureComponent recouperatorOutsideThermometer (33, "RecOutTemp", DEFAULT_REPORT);
DhtTemperatureComponent atticThermometer (31, "AtticTemp", DEFAULT_REPORT);
DhtTemperatureComponent outsideThermometer (43, "OutTemp", DEFAULT_REPORT);

///////////////////
//
// photo resistor
//
///////////////////
PhotoResistorComponent photoResistorComponent (A15, 10, 60 * 1000L, "PhotoRes", DEFAULT_REPORT);

///////////////////
//
// timed relays
//
///////////////////
TimedRelayComponent ventilator (41, LOW, true, 60 * 1000L, 0L, NULL, NULL, "Vent", DEFAULT_REPORT);
TimedRelayComponent dehumidifier (39, LOW, true, 60 * 1000L, 0L, NULL, NULL, "Dehum", DEFAULT_REPORT);
TimedRelayComponent recouperatorX151 (35, LOW, true, 60 * 1000L, 0L, NULL, NULL, "RecoupX151", DEFAULT_REPORT);
TimedRelayComponent spareRelay (37, LOW, true, 60 * 1000L, 0L, NULL, NULL, "spare");

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
{ &atticThermometer, &outsideThermometer, &recouperatorOutsideThermometer, &photoResistorComponent, &ventilator, &dehumidifier, &recouperatorX151, &spareRelay, NULL };

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
Shell shell (components, commandComponents, &inboundMessage, &outboundMessage, 10, "ShellAttic");

///////////////////
//
// Main
//
///////////////////
void setup ()
{
#if ENABLE_SERIAL
    Serial.begin (115200);
    Serial.setTimeout (5000);
#endif
    shell.setup ();
}

void loop ()
{
    shell.loop ();
}
