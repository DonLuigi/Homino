#include <Arduino.h>
#include <Shell.h>
#include <TimedRelayComponent.h>
#include <ShutterComponent.h>
#include <HardwareSerialComponent.h>
#include <EthernetW5100Component.h>

///////////////////
//
// global
//
///////////////////
#define ENABLE_ETHERNET 1
#define ENABLE_SERIAL 1
const unsigned long whenRunningReportEveryMillis = 500; // report every millis when shutter is running
const unsigned long windowLouvreShutterRotationMillis = 1500;

///////////////////
//
// shutter kitchen east
//
///////////////////
// relays
RelayComponent kitchenEastUpRelay (A9, LOW, true, "KEUR");
RelayComponent kitchenEastDownRelay (A12, LOW, true, "KEDR");

// height motion
// EEPROMValueComponent kitchenEastHeightEeprom (eepromPosition += sizeof(int32_t), sizeof(int32_t));
MotionRangeComponent kitchenEastHeightMotion (15500, 100, &kitchenEastUpRelay, &kitchenEastDownRelay, NULL, "KEHH");

// shutter
ShutterComponent kitchenEast (&kitchenEastHeightMotion, NULL, NULL, NULL, whenRunningReportEveryMillis, "WrsKitchE");

///////////////////
//
// shutter kitchen south
//
///////////////////
// relays
RelayComponent kitchenSouthUpRelay (A11, LOW, true, "KSUR");
RelayComponent kitchenSouthDownRelay (A14, LOW, true, "KSDR");

// height motion
// EEPROMValueComponent kitchenSouthHeightEeprom (eepromPosition += sizeof(int32_t), sizeof(int32_t));
MotionRangeComponent kitchenSouthHeightMotion (15500, 100, &kitchenSouthUpRelay, &kitchenSouthDownRelay, NULL, "KSHM");

// shutter
ShutterComponent kitchenSouth (&kitchenSouthHeightMotion, NULL, NULL, NULL, whenRunningReportEveryMillis, "WrsKitchS");

///////////////////
//
// shutter living room west
//
///////////////////
// relays
RelayComponent livingroomWestUpRelay (A15, LOW, true, "LRWUR");
RelayComponent livingroomWestDownRelay (A13, LOW, true, "LRWDR");

// height motion
// EEPROMValueComponent livingroomWestHeightEeprom (eepromPosition += sizeof(int32_t), sizeof(int32_t));
MotionRangeComponent livingroomWestHeightMotion (22500, 100, &livingroomWestUpRelay, &livingroomWestDownRelay, NULL, "LRWHM");

// shutter
ShutterComponent livingroomWest (&livingroomWestHeightMotion, NULL, NULL, NULL, whenRunningReportEveryMillis, "WrsLivingW");

///////////////////
//
// shutter living room south
//
///////////////////
// relays
RelayComponent livingroomSouthUpRelay (15, LOW, true, "LRSUR");
RelayComponent livingroomSouthDownRelay (18, LOW, true, "LRSDR");

// height motion
MotionRangeComponent livingroomSouthHeightMotion (60000, 100, &livingroomSouthUpRelay, &livingroomSouthDownRelay, NULL, "LRSHM");

// rotation motion
MotionRangeComponent livingroomSouthRotationMotion (windowLouvreShutterRotationMillis, 100, &livingroomSouthUpRelay, &livingroomSouthDownRelay, NULL, "LRSRM");

// shutter
ShutterComponent livingroomSouth (&livingroomSouthHeightMotion, &livingroomSouthRotationMotion, NULL, NULL, whenRunningReportEveryMillis, "WlsLivingS");

///////////////////
//
// shutter dining room
//
///////////////////
// relays
RelayComponent diningRoomUpRelay (14, LOW, true, "DRUR");
RelayComponent diningRoomDownRelay (19, LOW, true, "DRDR");

// height motion
MotionRangeComponent diningRoomHeightMotion (60000, 100, &diningRoomUpRelay, &diningRoomDownRelay, NULL, "DRHM");

// rotation motion
MotionRangeComponent diningRoomRotationMotion (windowLouvreShutterRotationMillis, 100, &diningRoomUpRelay, &diningRoomDownRelay, NULL, "DRRM");

// shutter
ShutterComponent diningRoom (&diningRoomHeightMotion, &diningRoomRotationMotion, NULL, NULL, whenRunningReportEveryMillis, "WlsDiningS");

///////////////////
//
// shutter bathroom
//
///////////////////
// relays
RelayComponent bathroom0UpRelay (20, LOW, true, "BR0UR");
RelayComponent bathroom0DownRelay (A10, LOW, true, "BR0DR");

// height motion
//MotionRangeComponent bathroom0HeightMotion (15500, 100, &bathroom0UpRelay, &bathroom0DownRelay, NULL, "BR0HM");
MotionRangeComponent bathroom0HeightMotion (18500, 100, &bathroom0UpRelay, &bathroom0DownRelay, NULL, "BR0HM"); // TODO: remove

// shutter
ShutterComponent bathroom0 (&bathroom0HeightMotion, NULL, NULL, NULL, whenRunningReportEveryMillis, "WrsBath0");

///////////////////
//
// shutter staircase
//
///////////////////
// relays
RelayComponent staircaseUpRelay (17, LOW, true, "ScUR");
RelayComponent staircaseDownRelay (16, LOW, true, "sCDR");

// height motion
//MotionRangeComponent staircaseHeightMotion (11000, 100, &staircaseUpRelay, &staircaseDownRelay, NULL, "ScHM");
MotionRangeComponent staircaseHeightMotion (15000, 100, &staircaseUpRelay, &staircaseDownRelay, NULL, "ScHM"); // TODO: remove

// shutter
ShutterComponent staircase (&staircaseHeightMotion, NULL, NULL, NULL, whenRunningReportEveryMillis, "WrsStairs");

///////////////////
//
// shutter bathroom 1st floor
//
///////////////////
// buttons
ButtonComponent bathroom1UpButton (48, 50, "BR1UB");
ButtonComponent bathroom1DownButton (47, 50, "BR1DB");

// relays
RelayComponent bathroom1UpRelay (34, LOW, true, "BR1UR");
RelayComponent bathroom1DownRelay (36, LOW, true, "BR1DR");

// height motion
MotionRangeComponent bathroom1HeightMotion (12500, 100, &bathroom1UpRelay, &bathroom1DownRelay, NULL, "BR1HM");

// shutter
ShutterComponent bathroom1 (&bathroom1HeightMotion, NULL, &bathroom1UpButton, &bathroom1DownButton, whenRunningReportEveryMillis, "WrsBathroom1");

///////////////////
//
// shutter bedroom
//
///////////////////
// buttons
ButtonComponent bedroomUpButton (40, 50, "BdrUB");
ButtonComponent bedroomDownButton (38, 50, "BdrDB");

// relays
RelayComponent bedroomUpRelay (28, LOW, true, "BdrUR");
RelayComponent bedroomDownRelay (30, LOW, true, "BdrDR");

// height motion
MotionRangeComponent bedroomHeightMotion (22500, 100, &bedroomUpRelay, &bedroomDownRelay, NULL, "BdrHM");

// shutter
ShutterComponent bedroom (&bedroomHeightMotion, NULL, &bedroomUpButton, &bedroomDownButton, whenRunningReportEveryMillis, "WrsBedroom");

///////////////////
//
// shutter kids room east
//
///////////////////
// buttons
ButtonComponent kidsRoomEastUpButton (45, 50, "KREUB");
ButtonComponent kidsRoomEastDownButton (39, 50, "KREDB");

// relays
RelayComponent kidsRoomEastUpRelay (37, LOW, true, "KREUR");
RelayComponent kidsRoomEastDownRelay (35, LOW, true, "KREDR");

// height motion
MotionRangeComponent kidsRoomEastHeightMotion (22500, 100, &kidsRoomEastUpRelay, &kidsRoomEastDownRelay, NULL, "KREHM");

// shutter
ShutterComponent kidsRoomEast (&kidsRoomEastHeightMotion, NULL, &kidsRoomEastUpButton, &kidsRoomEastDownButton, whenRunningReportEveryMillis, "WrsKids");

///////////////////
//
// shutter kids room south
//
///////////////////
// buttons
ButtonComponent kidsRoomSouthUpButton (43, 50, "KRSUB");
ButtonComponent kidsRoomSouthDownButton (41, 50, "KRSDB");

// relays
RelayComponent kidsRoomSouthUpRelay (24, LOW, true, "KRSUR");
RelayComponent kidsRoomSouthDownRelay (22, LOW, true, "KRSDR");

// height motion
MotionRangeComponent kidsRoomSouthHeightMotion (46000, 100, &kidsRoomSouthUpRelay, &kidsRoomSouthDownRelay, NULL, "KRSHM");

// rotation motion
MotionRangeComponent kidsRoomSouthRotationMotion (windowLouvreShutterRotationMillis, 100, &kidsRoomSouthUpRelay, &kidsRoomSouthDownRelay, NULL, "KRSRM");

// shutter
ShutterComponent kidsRoomSouth (&kidsRoomSouthHeightMotion, &kidsRoomSouthRotationMotion, &kidsRoomSouthUpButton, &kidsRoomSouthDownButton, whenRunningReportEveryMillis, "WlsKids");

///////////////////
//
// shutter gallery
//
///////////////////
// buttons
ButtonComponent galleryUpButton (44, 50, "GalUB");
ButtonComponent galleryDownButton (42, 50, "GalDB");

// relays
RelayComponent galleryUpRelay (26, LOW, true, "GalUR");
RelayComponent galleryDownRelay (32, LOW, true, "GalDR");

// height motion
MotionRangeComponent galleryHeightMotion (46000, 100, &galleryUpRelay, &galleryDownRelay, NULL, "GalHM");

// rotation motion
MotionRangeComponent galleryRotationMotion (windowLouvreShutterRotationMillis, 100, &galleryUpRelay, &galleryDownRelay, NULL, "GalRM");

// shutter
ShutterComponent gallery (&galleryHeightMotion, &galleryRotationMotion, &galleryUpButton, &galleryDownButton, whenRunningReportEveryMillis, "WlsGallery");

///////////////////
//
// hot water recirculation pump
//
///////////////////
// button
ButtonComponent hotWaterPumpButton (46, 50, "HWPB");

TimedRelayComponent hotWaterRecirculationPump (21, LOW, true, 60 * 1000L, 0L, &hotWaterPumpButton, NULL, whenRunningReportEveryMillis, TIMED_RELAY_COMPONENT_OPTION_START_STOP, "HWPump");

///////////////////
//
// towel heater
//
///////////////////
// button
ButtonComponent towelHeaterButton (49, 50, "THPB");
TimedRelayComponent towelHeater (A4, LOW, true, 20 * 60 * 1000L, 0L, &towelHeaterButton, NULL, whenRunningReportEveryMillis, TIMED_RELAY_COMPONENT_OPTION_START_STOP, "TowelHeater");

///////////////////
//
// front door
//
///////////////////
ButtonComponent frontDoorProximityButton (A5, 50, "FDPB");
ButtonComponent frontDoorUnlockButton (A3, 50, "FDEB");
TimedRelayComponent frontDoor (23, LOW, true, 2 * 1000L, 3 * 1000L, &frontDoorProximityButton, &frontDoorUnlockButton, whenRunningReportEveryMillis,
/*TIMED_RELAY_COMPONENT_OPTION_DOUBLE_TRIGGER_LOCK | TIMED_RELAY_COMPONENT_OPTION_UNLOCK_BUTTON_INHIBITS*/TIMED_RELAY_COMPONENT_OPTION_NONE, "FrontDoor");

///////////////////
//
// spare
//
///////////////////
//RelayComponent spareRelay1 (23, LOW, true, "spare1");
TimedRelayComponent eatingRoomLightsRelay (A8, LOW, true, 60 * 1000L, 0L, NULL, NULL, whenRunningReportEveryMillis, TIMED_RELAY_COMPONENT_OPTION_NONE, "ERL");

TimedRelayComponent relayOutsideLights (31, LOW, true, 60 * 1000L, 0L, NULL, NULL, whenRunningReportEveryMillis, TIMED_RELAY_COMPONENT_OPTION_NONE, "OutL");
TimedRelayComponent entryRoomLightsRelay (33, LOW, true, 60 * 1000L, 0L, NULL, NULL, whenRunningReportEveryMillis, TIMED_RELAY_COMPONENT_OPTION_NONE, "EntRL");

RelayComponent spareRelay3 (27, LOW, true, "spare3");
RelayComponent spareRelay4 (29, LOW, true, "spare4");

///////////////////
//
// Ethernet
//
///////////////////
#if ENABLE_ETHERNET
byte mac[] =
{ 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };
IPAddress ip (192, 168, 1, 50);
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
{ &kitchenEast, &kitchenSouth, &livingroomWest, &livingroomSouth, &diningRoom, &bathroom0, &staircase, &bathroom1, &kidsRoomEast, &kidsRoomSouth, &gallery, &bedroom, &hotWaterRecirculationPump,
    &frontDoor, &eatingRoomLightsRelay, &spareRelay3, &spareRelay4, &relayOutsideLights, &entryRoomLightsRelay, &towelHeater, NULL };

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
Shell shell (components, commandComponents, &inboundMessage, &outboundMessage, 10, "ShellMain");

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
