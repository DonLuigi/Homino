#include <Arduino.h>
#include <ButtonComponent.h>

ButtonComponent kidsRoomEastUpButton (45, 50, "otrRolGor");
ButtonComponent kidsRoomEastDownButton (39, 50, "otrRolDol");

ButtonComponent kidsRoomSouthUpButton (43, 50, "otrZalGor");
ButtonComponent buttonKidsRoomSouthDown (41, 50, "otrZalDol");

ButtonComponent galleryUpButton (44, 50, "galGor");
ButtonComponent galleryDownButton (42, 50, "galDol");

ButtonComponent bedroomUpButton (40, 50, "spalnicaGor");
ButtonComponent bedroomDownButton (38, 50, "spalnicaDol");

ButtonComponent hotWaterPumpButton (46, 50, "hwp");
ButtonComponent bathroom1DownButton (47, 50, "bath1Down");
ButtonComponent bathroom1UpButton (48, 50, "bath1Up");
ButtonComponent test4 (49, 50, "test4");

ButtonComponent frontDoorButton (A3, 50, "FDB");

ButtonComponent* buttons[] =
{ &kidsRoomEastUpButton, &kidsRoomSouthUpButton, &buttonKidsRoomSouthDown, &kidsRoomEastDownButton, &galleryDownButton, &galleryUpButton, &bedroomUpButton, &bedroomDownButton, &hotWaterPumpButton,
    &bathroom1DownButton, &bathroom1UpButton, &test4, &frontDoorButton, NULL };

void setup ()
{

    Serial.begin (115200);
    Serial.println ("BEGIN");

    for (int i = 0; i < 8 * 4; i++)
    {
        pinMode (i, OUTPUT);
        digitalWrite (i, HIGH);
    }
}

void loop ()
{
    for (int i = 0; buttons[i] != NULL; i++)
    {
        if (buttons[i]->onPress ())
        {
            Serial.print (millis () / 1000);
            Serial.print (" PRESSED: ");
            Serial.println (buttons[i]->name);
        }
        uint32_t duration = buttons[i]->onRelease ();
        if (duration > 0)
        {
            Serial.print (millis () / 1000);
            Serial.print (" RELEASED: ");
            Serial.print (duration);
            Serial.print (": ");
            Serial.println (buttons[i]->name);
        }
    }
}
