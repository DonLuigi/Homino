#ifndef HOUSE_SHELL_H
#define HOUSE_SHELL_H

#include <Component.h>
#include <Shell.h>
#include <KeypadComponent.h>
#include <ShutterComponent.h>

class HouseShell: public Shell
{
    public:
        ///////////////////
        //
        // Component
        //
        ///////////////////
        enum KeypadCommands
        {
            KEY_01 = 1, KEY_02, KEY_03, KEY_04, KEY_05, KEY_06, KEY_07, KEY_08, KEY_09, KEY_10, KEY_11, KEY_12, KEY_13, KEY_14, KEY_15, KEY_16,

            TERMINATE
        };

        HouseShell (Component** components, Component** commandComponents, Message* message, long loopMillis, ShutterComponent** shutters, KeypadComponent** keypads, KeypadCommands* keypadCommands,
            int16_t inactivityTimeoutMillis, uint8_t renderButtonWidth, uint8_t renderButtonHeight, uint8_t renderShutterWidth, char* name = NULL, bool report = false);
        void pulse ();
        void setup ();

    private:
        // components
        ShutterComponent** shutters;

        // keypads
        KeypadComponent** keypads;
        KeypadCommands* keypadCommands;

        static const int maxSelectedShutters = 8;
        int8_t noOfShutters;
        ShutterComponent* selectedShutters[maxSelectedShutters + 1];
        bool wereMoving;

        // render config
        uint8_t renderButtonWidth;
        uint8_t renderButtonHeight;
        uint8_t renderShutterWidth;

        // logic
        int16_t inactivityTimeout;
        int32_t lastActivityMillis;

        void shuttersMove (int8_t heightPercent, int8_t rotattionPercent);

        // display
        static const uint8_t shuttersCustomCharactersOffset = 0;
        static const int MAX_DISPLAY_COLUMNS = 20;
        char statusLineBuffer[MAX_DISPLAY_COLUMNS];
        enum DisplayPage
        {
            DISPLAY_PAGE_MAIN, DISPLAY_PAGE_LAST
        };
        DisplayPage displayCurrentPage;

        enum RefreshCurrentPageOptions
        {
            OPTION_NONE = 0, OPTION_TOUCH = 1, OPTION_FLASH = 2
        };
        void u8GlibDisplayRefresh (int32_t nowMillis, DisplayPage newCurrentPage, char statusLineBuffer[MAX_DISPLAY_COLUMNS], ShutterComponent* selectedShutters[maxSelectedShutters], int options);
};

#endif HOUSE_SHELL_H
