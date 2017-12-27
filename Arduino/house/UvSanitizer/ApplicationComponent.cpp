/*
 P crpanje zalogovnika 35
 P razlika vklop zalogovnika 20
 P vklop bojlerja 45
 P izklop bojlerja 80
 P razlika vklop bojlerja 1
 P histereza 1
 */

#include "ApplicationComponent.h"

#include <Arduino.h>
#include <stdbool.h>
#include <stdint.h>
#include <string.h>

ApplicationComponent::ApplicationComponent (TimedRelayComponent* lightTimedRelayComponent, LiquidCrystalI2CComponent* lcd, const char* name) :
    Component (name)
{
    this->lightTimedRelayComponent = lightTimedRelayComponent;
    this->lcd = lcd;
}

int ApplicationComponent::readFromComponent (Message* message)
{
    long now = millis ();
    if (now - lastLcdUpdateMillis > 100)
    {
        lastLcdUpdateMillis = now;

        int8_t coveredTimePercent;
        lightTimedRelayComponent->getStatus (&coveredTimePercent);

        int size = 16;
        char line1[16 + 1];
        line1[16] = '\0';

        char line2[16 + 1];
        line2[16] = '\0';

        memset (line1, ' ', size);
        memset (line2, ' ', size);

        if (coveredTimePercent >= 0)
        {
            uint32_t characters = ((((uint32_t) size) * 2) + 1) * coveredTimePercent / 100;
            characters = characters == 0 ? 1 : characters;

            uint8_t charactersLine1 = (characters > size ? size : characters);
            uint8_t charactersLine2 = (characters > size ? characters - size : 0);

            memset (line1, 255, charactersLine1);
            memset (line2, 255, charactersLine2);
            lcd->backlight ();
        }
        else
        {
            lcd->noBacklight ();
        }

        lcd->setCursor (0, 0);
        lcd->print (line1);
        lcd->setCursor (0, 1);
        lcd->print (line2);
    }

    return (0);
}
