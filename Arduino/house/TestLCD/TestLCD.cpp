#include "LiquidCrystalI2CComponent.h"

LiquidCrystalI2CComponent lcd (0x27, 16, 2, 20 * 1000L, 500L, 100, "lcd");

void setup ()
{
    lcd.setup ();
}

void loop ()
{
    lcd.clear ();
    lcd.setCursor (0, 0);
    lcd. print ("1");
    lcd.print ("2");
    delay (1000);

    lcd.clear ();
    lcd.setCursor (1, 1);
    lcd.print ("DEF");
    delay (1000);
}
