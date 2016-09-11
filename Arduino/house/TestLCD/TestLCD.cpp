#include "TestLCD.h"
#include "LiquidCrystalI2CComponent.h"

LiquidCrystalI2CComponent lcd (0x27, 16, 2, 20 * 1000L, 500L, 100, "lcd");

void setup ()
{
//    Serial.begin (115200);
//    Serial.println ("Begin");

    lcd.setup ();
}

void loop ()
{
//    Serial.println ("AAA");

    lcd.clear ();
    lcd.setCursor (0, 0);
    lcd.print (123);
    lcd.print (456);
//    Serial.println ("DONE AAA");
    delay (1000);

    lcd.clear ();
    lcd.setCursor (1, 1);
    lcd.print ("DEF");
//    Serial.println ("DONE BBB");
    delay (1000);
}
