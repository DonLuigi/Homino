/*
 P crpanje zalogovnika 35
 P razlika vklop zalogovnika 20
 P vklop bojlerja 45
 P izklop bojlerja 80
 P razlika vklop bojlerja 1
 P histereza 1
 */

#include <Arduino.h>
#include <EEPROM.h>
#include "ApplicationComponent.h"

ApplicationComponent::ApplicationComponent (DallasTemperatureComponent* tZalogovnikZgoraj, DallasTemperatureComponent* tZalogovnikSpodaj, DallasTemperatureComponent* tDrva,
    DallasTemperatureComponent* tBojler, PinComponent* rOljniGorilec, PinComponent* rPecAliZalogovnik, PinComponent* rDrvaAliOlje, PinComponent* rCrpalkaZalogovnik, PinComponent* rCrpalkaBojler,
    EEPROMValueComponent* pCrpanjeZalogovnika, EEPROMValueComponent* pRazlikaZaVklopZalogovnika, EEPROMValueComponent* pVklopBojlerja, EEPROMValueComponent* pIzklopBojlerja,
    EEPROMValueComponent* pRazlikaZaVklopCrpalkeBojlerja, EEPROMValueComponent* pTemperaturnaHistereza, LiquidCrystalI2CComponent* lcd, KeypadComponent* keypad, PinComponent* buzzerconst,
    const char* name) :
    Component (name)
{
    thermometers[0] = this->tZalogovnikZgoraj = tZalogovnikZgoraj;
    thermometers[1] = this->tZalogovnikSpodaj = tZalogovnikSpodaj;
    thermometers[2] = this->tDrva = tDrva;
    thermometers[3] = this->tBojler = tBojler;

    relays[0] = this->rOljniGorilec = rOljniGorilec;
    relays[1] = this->rPecAliZalogovnik = rPecAliZalogovnik;
    relays[2] = this->rDrvaAliOlje = rDrvaAliOlje;
    relays[3] = this->rCrpalkaZalogovnik = rCrpalkaZalogovnik;
    relays[4] = this->rCrpalkaBojler = rCrpalkaBojler;

    parameters[0] = this->pCrpanjeZalogovnika = pCrpanjeZalogovnika;
    parameters[1] = this->pRazlikaZaVklopZalogovnika = pRazlikaZaVklopZalogovnika;
    parameters[2] = this->pVklopBojlerja = pVklopBojlerja;
    parameters[3] = this->pIzklopBojlerja = pIzklopBojlerja;
    parameters[4] = this->pRazlikaZaVklopCrpalkeBojlerja = pRazlikaZaVklopCrpalkeBojlerja;
    parameters[5] = this->pTemperaturnaHistereza = pTemperaturnaHistereza;

    hysteresis[0] = &hysteresisDrvaVspCrpanjeZalogovnika;
    setHysteresis ((float) pTemperaturnaHistereza->readAsUInt8 ());

    this->lcd = lcd;
    this->keypad = keypad;
    this->buzzer = buzzer;
    manualMode = false;

    lastActivityMillis = 0;
    displayCurrentPage = DISPLAY_PAGE_MAIN;

    uint8_t value;
    if (pCrpanjeZalogovnika->readAsUInt8 () == 0xFF)
    {
        value = 35;
        pCrpanjeZalogovnika->write (&value);

        value = 20;
        pRazlikaZaVklopZalogovnika->write (&value);

        value = 45;
        pVklopBojlerja->write (&value);

        value = 80;
        pIzklopBojlerja->write (&value);

        value = 1;
        pRazlikaZaVklopCrpalkeBojlerja->write (&value);
        pTemperaturnaHistereza->write (&value);
    }
}

int ApplicationComponent::readFromComponent (Message* message)
{
    // logic
    unsigned long now = millis ();
    if (now - lastActivityMillis > activityTimeout)
    {
        logic ();
        lastActivityMillis = now;

        if (lcd->isInactive () || displayCurrentPage == DISPLAY_PAGE_MAIN)
        {
            displayRefresh (DISPLAY_PAGE_MAIN, OPTION_NONE);
        }
    }

    // keypad
    char currentKey = keypad->getKey ();
    if (keypad->keyStateChanged () && currentKey != NO_KEY)
    {
        COA_DEBUG(F("crrKey=%d,currPage=%d"), currentKey, displayCurrentPage);

        switch (currentKey)
        {
            case '9': // PAGE LEFT
                displayRefresh (DISPLAY_PAGE_LEFT, OPTION_TOUCH);
                break;

            case 'C': // PAGE RIGHT
                COA_DEBUG(F("right"));
                displayRefresh (DISPLAY_PAGE_RIGHT, OPTION_TOUCH);
                break;

            case '*':
                manualMode = false;
                displayRefresh (DISPLAY_PAGE_MODE, OPTION_TOUCH);
                break;

            case '#': // -
            case 'D': // +
                if (displayCurrentPage >= DISPLAY_RELAY_FIRST && displayCurrentPage <= DISPLAY_RELAY_LAST)
                {
                    PinComponent* relay = relays[displayCurrentPage - DISPLAY_RELAY_FIRST];
                    relay->write (currentKey == 'D');
                    displayRefresh (DISPLAY_PAGE_CURRENT, OPTION_TOUCH);
                    manualMode = true;
                }

                if (displayCurrentPage >= DISPLAY_PARAMETER_FIRST && displayCurrentPage <= DISPLAY_PARAMETER_LAST)
                {
                    EEPROMValueComponent* parameter = parameters[displayCurrentPage - DISPLAY_PARAMETER_FIRST];
                    uint8_t value = parameter->readAsUInt8 () + (currentKey == 'D' ? 1 : -1);
                    parameter->write (&value);
                    displayRefresh (DISPLAY_PAGE_CURRENT, OPTION_TOUCH);

                    if (parameter == pTemperaturnaHistereza)
                    {
                        setHysteresis ((float) value);
                    }
                }
                break;

            default:
                displayRefresh (DISPLAY_PAGE_MAIN, OPTION_TOUCH);
        }

        buzzer->pwmWrite (200);
        delay (100);
        buzzer->pwmWrite (0);
    }

    return (0);
}

void ApplicationComponent::displayRefresh (DisplayPage newCurrentPage, int options)
{
    // current page
    if (newCurrentPage != DISPLAY_PAGE_CURRENT)
    {
        int8_t displayCurrentPageDelta = (newCurrentPage == DISPLAY_PAGE_LEFT ? -1 : newCurrentPage == DISPLAY_PAGE_RIGHT ? 1 : 0);
        int8_t newRawPage = (displayCurrentPageDelta != 0 ? displayCurrentPage + displayCurrentPageDelta : newCurrentPage);
        displayCurrentPage = (DisplayPage) (newRawPage < 0 ? DISPLAY_PAGE_LAST - 1 : newRawPage % DISPLAY_PAGE_LAST);
    }

    // touch
    if (options & OPTION_TOUCH)
    {
        lcd->touch ();
    }

    // flash
    lcd->flash (options & OPTION_FLASH);

    // pages
    if (displayCurrentPage >= DISPLAY_RELAY_FIRST && displayCurrentPage <= DISPLAY_RELAY_LAST)
    {
        PinComponent* relay = relays[displayCurrentPage - DISPLAY_RELAY_FIRST];
        lcd->clear ();
        lcd->print (relay->name);
        lcd->setCursor (0, 1);
        lcd->print (relay->read () == HIGH ? "+" : "-");
    }

    if (displayCurrentPage >= DISPLAY_PARAMETER_FIRST && displayCurrentPage <= DISPLAY_PARAMETER_LAST)
    {
        EEPROMValueComponent* parameter = parameters[displayCurrentPage - DISPLAY_PARAMETER_FIRST];
        lcd->clear ();
        lcd->print (parameter->name);
        lcd->setCursor (0, 1);
        char buffer[6];
        snprintf (buffer, sizeof(buffer), "%i\xDF", (int32_t) parameter->readAsUInt8 ());
        lcd->print (buffer);
    }

    char line1[DISPLAY_COLUMNS + 1], line2[DISPLAY_COLUMNS + 1]; // lines have few spare characters to ease justification
    switch (displayCurrentPage)
    {
        case DISPLAY_PAGE_MAIN:
            char temperatureChar[DISPLAY_COLUMNS + 1];
            float temperature;
            boolean status;


            status = tZalogovnikZgoraj->read (&temperature, NULL);
            snprintf (temperatureChar, DISPLAY_COLUMNS, "%i\xDF", (int) temperature);
            snprintf (line1, DISPLAY_COLUMNS, "%s=%s  ", tZalogovnikZgoraj->name, status ? temperatureChar : "ERR");


            status = tZalogovnikSpodaj->read (&temperature, NULL);
            snprintf (temperatureChar, DISPLAY_COLUMNS, "%i\xDF", (int) temperature);
            snprintf (line1 + 8, DISPLAY_COLUMNS - 8 + 1, "%s=%s   ", tZalogovnikSpodaj->name, status ? temperatureChar : "ERR");


            status = tDrva->read (&temperature, NULL);
            snprintf (temperatureChar, DISPLAY_COLUMNS, "%i\xDF", (int) temperature);
            snprintf (line2, DISPLAY_COLUMNS, "%s=%s  ", tDrva->name, status ? temperatureChar : "ERR");


            status = tBojler->read (&temperature, NULL);
            snprintf (temperatureChar, DISPLAY_COLUMNS, "%i\xDF", (int) temperature);
            snprintf (line2 + 8, DISPLAY_COLUMNS - 8 + 1, "%s=%s   ", tBojler->name, status ? temperatureChar : "ERR");

            lcd->setCursor (0, 0); // no lcd clear
            lcd->print (line1);
            lcd->setCursor (0, 1);
            lcd->print (line2);
            break;

        case DISPLAY_PAGE_MODE:
            lcd->clear ();
            lcd->print (manualMode ? "MANUAL MODE" : "AUTOMATIC MODE");
            break;

        case DISPLAY_PAGE_ABOUT:
            lcd->clear ();
            lcd->print ("(C)");
            lcd->setCursor (0, 1);
            lcd->print ("Ludvik Krulik");
    }
}

void ApplicationComponent::logic ()
{
    // update thermometers
    DallasTemperatureComponent* failedThermometer = NULL;
    for (int i = 0; i < sizeof(thermometers) / sizeof(thermometers[0]); i++)
    {
        float temperature;
        if (!thermometers[i]->read (&temperature, NULL))
        {
            failedThermometer = thermometers[i];
        }
    }

    if (manualMode)
    {
        COA_DEBUG(F("Logic manual mode done"));
        return;
    }


    // temperatures
    float tDrvaTemperature;
    tDrva->read (&tDrvaTemperature, NULL);

    float tZalogovnikZgorajTemperature;
    tZalogovnikZgoraj->read (&tZalogovnikZgorajTemperature, NULL);

    float tZalogovnikSpodajTemperature;
    tZalogovnikSpodaj->read (&tZalogovnikSpodajTemperature, NULL);

    float tBojlerTemperature;
    tBojler->read (&tBojlerTemperature, NULL);



    // oljni gorilec
    bool cPecNaDrvaDela = tDrvaTemperature > pCrpanjeZalogovnika->readAsUInt8 ();
    COA_DEBUG(F("cPecNaDrvaDela=%c"), cPecNaDrvaDela ? 't' : 'f');

    bool cZalogovnikPripravljen = tZalogovnikZgorajTemperature > pCrpanjeZalogovnika->readAsUInt8 ();
    COA_DEBUG(F("cZalogovnikPripravljen=%c"), cZalogovnikPripravljen ? 't' : 'f');
    rOljniGorilec->write (!cZalogovnikPripravljen && !cPecNaDrvaDela); // normally closed

    // peèi ali zalogovnik
    boolean pec = cPecNaDrvaDela | !cZalogovnikPripravljen;
    COA_DEBUG(F("pec=%c"), pec ? 't' : 'f');
    rPecAliZalogovnik->write (!pec); // normally open -> invert

    // drva ali olje
    boolean drva = cPecNaDrvaDela;
    COA_DEBUG(F("drva=%c"), drva ? 't' : 'f');
    rDrvaAliOlje->write (!drva); // normally open -> invert

    // crpalka zalogovnika
    boolean crpalkaZalogovnik = tDrvaTemperature > (tZalogovnikSpodajTemperature + pRazlikaZaVklopZalogovnika->readAsUInt8 ());
    rCrpalkaZalogovnik->write (!crpalkaZalogovnik); // normally open -> invert

    // crpalka bojlerja
    bool cDrvaLahkoOgrevajoBojler = pec && (tDrvaTemperature > tBojlerTemperature + pRazlikaZaVklopCrpalkeBojlerja->readAsUInt8 ());
    COA_DEBUG(F("cDrvaLahkoOgrevajoBojler=%c"), cDrvaLahkoOgrevajoBojler ? 't' : 'f');
    bool cZalogovnikLahkoOgrevaBojler = !pec && (tZalogovnikZgorajTemperature > tBojlerTemperature + pRazlikaZaVklopCrpalkeBojlerja->readAsUInt8 ());
    COA_DEBUG(F("cZalogovnikLahkoOgrevaB=%c"), cZalogovnikLahkoOgrevaBojler ? 't' : 'f');
    bool crpalkaBoljerja = tBojlerTemperature < (rCrpalkaBojler->read () == LOW ? pVklopBojlerja->readAsUInt8 () : pIzklopBojlerja->readAsUInt8 ())
        && (cDrvaLahkoOgrevajoBojler || cZalogovnikLahkoOgrevaBojler);
    rCrpalkaBojler->write (crpalkaBoljerja);

    COA_DEBUG(F("Logic done"));
}

void ApplicationComponent::setHysteresis (float tolerance)
{
    for (int i = 0; i < sizeof(hysteresis) / sizeof(hysteresis[0]); i++)
    {
        hysteresis[i]->setTolerance (tolerance);
    }
}
