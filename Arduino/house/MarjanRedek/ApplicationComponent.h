#ifndef APPLICATION_COMPONENT_H
#define APPLICATION_COMPONENT_H

#include <Hysteresis.h>
#include <Component.h>
#include <PinComponent.h>
#include <EEPROMValueComponent.h>
#include <DallasTemperatureComponent.h>
#include <LiquidCrystalI2CComponent.h>
#include <KeypadComponent.h>

class ApplicationComponent: public Component
{
    public:
        ApplicationComponent (DallasTemperatureComponent* tZalogovnikZgoraj, DallasTemperatureComponent* tZalogovnikSpodaj, DallasTemperatureComponent* tDrva, DallasTemperatureComponent* tBojler,
            PinComponent* rOljniGorilec, PinComponent* rPecAliZalogovnik, PinComponent* rDrvaAliOlje, PinComponent* rCrpalkaZalogovnik, PinComponent* rCrpalkaBojler,
            EEPROMValueComponent* pCrpanjeZalogovnika, EEPROMValueComponent* pRazlikaZaVklopZalogovnika, EEPROMValueComponent* pVklopBojlerja, EEPROMValueComponent* pIzklopBojlerja,
            EEPROMValueComponent* pRazlikaZaVklopCrpalkeBojlerja, EEPROMValueComponent* pTemperaturnaHistereza, LiquidCrystalI2CComponent* lcd, KeypadComponent* keypad, PinComponent* buzzer,
            const char* name);
        int readFromComponent (Message* message);

    private:
        // components
        DallasTemperatureComponent* tZalogovnikZgoraj;
        DallasTemperatureComponent* tZalogovnikSpodaj;
        DallasTemperatureComponent* tDrva;
        DallasTemperatureComponent* tBojler;
        DallasTemperatureComponent* thermometers[4];

        PinComponent* rOljniGorilec;
        PinComponent* rPecAliZalogovnik;
        PinComponent* rDrvaAliOlje;
        PinComponent* rCrpalkaZalogovnik;
        PinComponent* rCrpalkaBojler;
        PinComponent* relays[5];

        EEPROMValueComponent* pCrpanjeZalogovnika;
        EEPROMValueComponent* pRazlikaZaVklopZalogovnika;
        EEPROMValueComponent* pVklopBojlerja;
        EEPROMValueComponent* pIzklopBojlerja;
        EEPROMValueComponent* pRazlikaZaVklopCrpalkeBojlerja;
        EEPROMValueComponent* pTemperaturnaHistereza;
        EEPROMValueComponent* parameters[6];

        LiquidCrystalI2CComponent* lcd;
        KeypadComponent* keypad;
        PinComponent* buzzer;

        // logic
        void logic ();
        unsigned long lastActivityMillis;
        static const long activityTimeout = 1 * 1000L;
        boolean manualMode;

        // hysteresis
        Hysteresis hysteresisDrvaVspCrpanjeZalogovnika;
        Hysteresis* hysteresis[1];
        void setHysteresis (float tolerance);

        // display
        static const int DISPLAY_COLUMNS = 16;
        enum DisplayPage
        {
            DISPLAY_PAGE_MAIN,

            DISPLAY_PAGE_MODE,

            DISPLAY_RELAY_FIRST, DISPLAY_RELAY_2, DISPLAY_RELAY_3, DISPLAY_RELAY_4, DISPLAY_RELAY_LAST,

            DISPLAY_PARAMETER_FIRST, DISPLAY_PARAMETER_2, DISPLAY_PARAMETER_3, DISPLAY_PARAMETER_4, DISPLAY_PARAMETER_5, DISPLAY_PARAMETER_LAST,

            DISPLAY_PAGE_ABOUT,

            DISPLAY_PAGE_LAST, DISPLAY_PAGE_CURRENT, DISPLAY_PAGE_LEFT, DISPLAY_PAGE_RIGHT
        };
        DisplayPage displayCurrentPage;

        enum RefreshCurrentPageOptions
        {
            OPTION_NONE = 0, OPTION_TOUCH = 1, OPTION_FLASH = 2
        };
        void displayRefresh (DisplayPage newCurrentPage, int options);
};

#endif
