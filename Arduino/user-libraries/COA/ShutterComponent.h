#ifndef SHUTTER_COMPONENT_H
#define SHUTTER_COMPONENT_H

#include <Component.h>
#include <PinComponent.h>
#include <ButtonComponent.h>
#include <MotionRangeComponent.h>

class ShutterComponent: public Component
{
    public:
        ///////////////////
        //
        // Component
        //
        ///////////////////
        ShutterComponent (MotionRangeComponent* heightMotionRange, MotionRangeComponent* rotationMotionRange, ButtonComponent* upButton, ButtonComponent* downButton, const char* name = NULL,
            uint32_t reportMillis = 0);
        void setup ();
        int readFromComponent (Message* message);
        void writeToComponent (Command* command, Message* message, int subcomponent);
        uint8_t toString (char* buffer, uint8_t size, uint8_t customFontOffset);

        ///////////////////
        //
        // API
        //
        ///////////////////
        void move (int8_t heightPercent, int8_t rotationPercent);
        bool isMoving ();
        void stop ();

    private:
        MotionRangeComponent* heightMotionRange;
        MotionRangeComponent* rotationMotionRange;
        ButtonComponent* upButton;
        ButtonComponent* downButton;
        int8_t rotationMotionRangePercentQueue;
        uint32_t lastReportMillis = millis ();
        void appendStatus (Message* message, uint32_t now);

};

#endif