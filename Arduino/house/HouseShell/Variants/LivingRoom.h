/*
 PIN LETTER
 RELAY
 PIN
 WHAT
 A 1  23 dnevna j dol
 B 2  25 dnevna Z dol
 C 3  27 jedilnica dol
 D 4  29 kop gor
 E 5  31 dnevna j gor
 F 6  33 kop dol
 G 7  35 kuh V dol
 H 8  37 dnevna Z gor
 J 10 41 kuh J gor
 K 11 43 stopn dol
 L 12 45 kuh V gor
 M 13 47 kuh J dol
 N 14 49 jedilnica gor
 O 15 51 stopn gor
 */

///////////////////
//
// global
//
///////////////////
#define ENABLE_ETHERNET 0
#define ENABLE_SERIAL 0
uint8_t eepromPosition = 0;

///////////////////
//
// OLED
//
///////////////////
U8GLIB_SH1106_128X64_2X u8glib (U8G_I2C_OPT_FAST | U8G_I2C_OPT_NO_ACK);
U8GlibComponent u8GlibComponent (&u8glib, 270);

///////////////////
//
// keypad
//
///////////////////
char keys1[] =
{ HouseShell::KEY_01, HouseShell::KEY_02, HouseShell::KEY_03, HouseShell::KEY_04, HouseShell::KEY_05, HouseShell::KEY_06, HouseShell::KEY_07, HouseShell::KEY_08, HouseShell::KEY_09,
    HouseShell::KEY_10, HouseShell::KEY_11, HouseShell::KEY_12, HouseShell::KEY_13, HouseShell::KEY_14, HouseShell::KEY_15, HouseShell::KEY_16 };

byte rowPins1[] =
{ A0, A1, A2, A3 }; //connect to the row pin-outs of the keypad
byte colPins1[] =
{ A4, A5, A6, A7 }; //connect to the column pin-outs of the keypad
KeypadComponent keypad1 (keys1, rowPins1, colPins1, 4, 4, 1000);

KeypadComponent* keypads[] =
{ &keypad1, NULL };

HouseShell::KeypadCommands keypadCommands[] =
{

HouseShell::KEY_16, HouseShell::SHUTTER_DESELECT_ALL, HouseShell::SHUTTER_SELECT_01, HouseShell::SHUTTER_SELECT_02,

HouseShell::KEY_16, HouseShell::SHUTTER_DESELECT_ALL, HouseShell::SHUTTER_SELECT_01,

HouseShell::KEY_15, HouseShell::SHUTTER_DESELECT_ALL, HouseShell::SHUTTER_SELECT_03,

HouseShell::KEY_14, HouseShell::SHUTTER_DESELECT_ALL, HouseShell::SHUTTER_SELECT_04,

HouseShell::KEY_13, HouseShell::SHUTTER_DESELECT_ALL, HouseShell::SHUTTER_SELECT_05,

HouseShell::KEY_12, HouseShell::SHUTTER_DESELECT_ALL, HouseShell::SHUTTER_SELECT_06,

HouseShell::KEY_12, HouseShell::SHUTTER_DESELECT_ALL, HouseShell::SHUTTER_SELECT_07,

HouseShell::KEY_11, HouseShell::SHUTTER_SELECT_ALL,

HouseShell::KEY_10, HouseShell::SHUTTER_MOVE_0,

HouseShell::KEY_09, HouseShell::SHUTTER_MOVE_100,

HouseShell::KEY_08, HouseShell::SHUTTER_STOP,

//HouseShell::KEY_10, HouseShell::SHUTTER_MOVE_X, (HouseShell::KeyboardCommands) 80,
//
//HouseShell::KEY_11, HouseShell::SHUTTER_ROTATE_X, (HouseShell::KeyboardCommands) 50,

//    HouseShell::KEY_02, HouseShell::SHUTTER_SELECT_01, HouseShell::SHUTTER_MOVE_0, // move 0
//    HouseShell::KEY_03, HouseShell::SHUTTER_SELECT_01, HouseShell::SHUTTER_MOVE_100, // move 100
//    HouseShell::KEY_04, HouseShell::SHUTTER_SELECT_01, HouseShell::SHUTTER_MOVE_X, (HouseShell::KeyboardCommands) 50, // move 50
//
//    HouseShell::KEY_05, HouseShell::SHUTTER_SELECT_02, HouseShell::SHUTTER_STOP, // stop
//    HouseShell::KEY_06, HouseShell::SHUTTER_SELECT_02, HouseShell::SHUTTER_MOVE_0, // move 0
//    HouseShell::KEY_07, HouseShell::SHUTTER_SELECT_02, HouseShell::SHUTTER_MOVE_100, // move 100
//    HouseShell::KEY_08, HouseShell::SHUTTER_SELECT_02, HouseShell::SHUTTER_MOVE_X, (HouseShell::KeyboardCommands) 50, // move 50
//
//    HouseShell::KEY_09, HouseShell::SHUTTER_SELECT_02, HouseShell::SHUTTER_ROTATE_0, // move 0
//    HouseShell::KEY_10, HouseShell::SHUTTER_SELECT_02, HouseShell::SHUTTER_ROTATE_100, // move 100
//    HouseShell::KEY_11, HouseShell::SHUTTER_SELECT_02, HouseShell::SHUTTER_ROTATE_X, (HouseShell::KeyboardCommands) 50, // move 50
//
//    HouseShell::KEY_05, HouseShell::SHUTTER_SELECT_02, HouseShell::SHUTTER_STOP, // stop

    HouseShell::TERMINATE }; // terminate

///////////////////
//
// shutter kitchen east
//
///////////////////
// relays
RelayComponent kitchenEastUpRelay (45, LOW, false, "keUr");
RelayComponent kitchenEastDownRelay (35, LOW, false, "keDr");

// height motion
EEPROMValueComponent kitchenEastHeightEeprom (eepromPosition += sizeof(int32_t), sizeof(int32_t));
MotionRangeComponent kitchenEastHeightMotion (15500, 100, &kitchenEastUpRelay, &kitchenEastDownRelay, &kitchenEastHeightEeprom, "keHm");

// shutter
ShutterComponent kitchenEast (&kitchenEastHeightMotion, NULL, "K", "ke");

///////////////////
//
// shutter kitchen south
//
///////////////////
// relays
RelayComponent kitchenSouthUpRelay (41, LOW, false, "ksUr");
RelayComponent kitchenSouthDownRelay (47, LOW, false, "ksDr");

// height motion
EEPROMValueComponent kitchenSouthHeightEeprom (eepromPosition += sizeof(int32_t), sizeof(int32_t));
MotionRangeComponent kitchenSouthHeightMotion (15500, 100, &kitchenSouthUpRelay, &kitchenSouthDownRelay, &kitchenSouthHeightEeprom, "ksHm");

// shutter
ShutterComponent kitchenSouth (&kitchenSouthHeightMotion, NULL, "K", "ks");

///////////////////
//
// shutter living room west
//
///////////////////
// relays
RelayComponent livingroomWestUpRelay (37, LOW, false, "lrwUr");
RelayComponent livingroomWestDownRelay (25, LOW, false, "lrwDr");

// height motion
EEPROMValueComponent livingroomWestHeightEeprom (eepromPosition += sizeof(int32_t), sizeof(int32_t));
MotionRangeComponent livingroomWestHeightMotion (22500, 100, &livingroomWestUpRelay, &livingroomWestDownRelay, &livingroomWestHeightEeprom, "lrwHm");

// shutter
ShutterComponent livingroomWest (&livingroomWestHeightMotion, NULL, "D", "lrw");

///////////////////
//
// shutter living room south
//
///////////////////
// relays
RelayComponent livingroomSouthUpRelay (31, LOW, false, "lrsUr");
RelayComponent livingroomSouthDownRelay (23, LOW, false, "lrsDr");

// height motion
EEPROMValueComponent livingroomSouthHeightEeprom (eepromPosition += sizeof(int32_t), sizeof(int32_t));
MotionRangeComponent livingroomSouthHeightMotion (60000, 100, &livingroomSouthUpRelay, &livingroomSouthDownRelay, &livingroomSouthHeightEeprom, "lrsHm");

// rotation motion
EEPROMValueComponent livingroomSouthRotationEeprom (eepromPosition += sizeof(int32_t), sizeof(int32_t));
MotionRangeComponent livingroomSouthRotationMotion (1000, 100, &livingroomSouthUpRelay, &livingroomSouthDownRelay, &livingroomSouthRotationEeprom, "lrsRm");

// shutter
ShutterComponent livingroomSouth (&livingroomSouthHeightMotion, &livingroomSouthRotationMotion, "D", "lrs");

///////////////////
//
// shutter dining room
//
///////////////////
// relays
RelayComponent diningRoomUpRelay (49, LOW, false, "drUr");
RelayComponent diningRoomDownRelay (27, LOW, false, "drDr");

// height motion
EEPROMValueComponent diningRoomHeightEeprom (eepromPosition += sizeof(int32_t), sizeof(int32_t));
MotionRangeComponent diningRoomHeightMotion (60000, 100, &diningRoomUpRelay, &diningRoomDownRelay, &diningRoomHeightEeprom, "drHm");

// rotation motion
EEPROMValueComponent diningRoomRotationEeprom (eepromPosition += sizeof(int32_t), sizeof(int32_t));
MotionRangeComponent diningRoomRotationMotion (1000, 100, &diningRoomUpRelay, &diningRoomDownRelay, &diningRoomRotationEeprom, "drRm");

// shutter
ShutterComponent diningRoom (&diningRoomHeightMotion, &diningRoomRotationMotion, "J", "dr");

///////////////////
//
// shutter bathroom
//
///////////////////
// relays
RelayComponent bathroomUpRelay (29, LOW, false, "brUr");
RelayComponent bathroomDownRelay (33, LOW, false, "brDr");

// height motion
EEPROMValueComponent bathroomHeightEeprom (eepromPosition += sizeof(int32_t), sizeof(int32_t));
MotionRangeComponent bathroomHeightMotion (11000, 100, &bathroomUpRelay, &bathroomDownRelay, &bathroomHeightEeprom, "brHm");

// shutter
ShutterComponent bathroom (&bathroomHeightMotion, NULL, "P", "br");

///////////////////
//
// shutter staircase
//
///////////////////
// relays
RelayComponent staircaseUpRelay (51, LOW, false, "scUr");
RelayComponent staircaseDownRelay (43, LOW, false, "scDr");

// height motion
EEPROMValueComponent staircaseHeightEeprom (eepromPosition += sizeof(int32_t), sizeof(int32_t));
MotionRangeComponent staircaseHeightMotion (11000, 100, &staircaseUpRelay, &staircaseDownRelay, &staircaseHeightEeprom, "scHm");

// shutter
ShutterComponent staircase (&staircaseHeightMotion, NULL, "S", "sc");

///////////////////
//
// shutters
//
///////////////////
ShutterComponent* shutters[] =
{ &kitchenEast, &kitchenSouth, &diningRoom, &livingroomSouth, &livingroomWest, &bathroom, &staircase, NULL };

///////////////////
//
// Ethernet
//
///////////////////
#if ENABLE_ETHERNET
byte mac[] =
{   0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED};
IPAddress arduinoIpAddress (192, 168, 1, 111);
UIPEthernetComponent ethernetServer ("eth", false, mac, &arduinoIpAddress, 80, 100);
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
#if ENABLE_ETHERNET || ENABLE_SERIAL
char messageBuffer[128];
Message message (messageBuffer, 128, ",");
Message* messagePtr = &message;
#else
Message* messagePtr = NULL;
#endif

///////////////////
//
// Components
//
///////////////////
Component* components[] =
{ &u8GlibComponent, &kitchenEast, &kitchenEastUpRelay, &kitchenEastDownRelay, &kitchenEastHeightMotion, &kitchenSouth, &kitchenSouthUpRelay, &kitchenSouthDownRelay, &kitchenSouthHeightMotion,
    &livingroomWest, &livingroomWestUpRelay, &livingroomWestDownRelay, &livingroomWestHeightMotion,

    &livingroomSouth, &livingroomSouthUpRelay, &livingroomSouthDownRelay, &livingroomSouthHeightMotion, &diningRoom, &diningRoomUpRelay, &diningRoomDownRelay, &diningRoomHeightMotion,

    &bathroom, &bathroomUpRelay, &bathroomDownRelay, &bathroomHeightMotion, &staircase, &staircaseUpRelay, &staircaseDownRelay, &staircaseHeightMotion, NULL };

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
HouseShell houseShell (components, commandComponents, messagePtr, 10, shutters, &u8GlibComponent, keypads, keypadCommands, 5000, 26, 20, 12);
