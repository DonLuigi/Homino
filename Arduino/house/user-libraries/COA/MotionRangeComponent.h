#ifndef MOTION_RANGE_COMPONENT_H
#define MOTION_RANGE_COMPONENT_H

#include <Component.h>
#include <RelayComponent.h>
#include <EEPROMValueComponent.h>

class MotionRangeComponent: public Component
{
    public:
        ///////////////////
        //
        // Component
        //
        ///////////////////
        MotionRangeComponent (int32_t maximumPositionMillis, int32_t minMaxExtensionMillis, RelayComponent* upRelay, RelayComponent* downRelay, EEPROMValueComponent* positionEeprom, const char* name);
        void setup ();

        ///////////////////
        //
        // API
        //
        ///////////////////
        int8_t move (int8_t percentOfMaximum);
        void stop ();
        void pulse (unsigned long nowMillis, bool forceStop); //
        bool isMoving ();
        void getStatus (int8_t* direction, int8_t* currentPositionPercent);
        void overrideCurrentPosition (int8_t currentPositionPercent);

    private:
        // relays
        RelayComponent* upRelay;
        RelayComponent* downRelay;

        // EEPROM
        EEPROMValueComponent* positionEeprom;

        //
        int32_t maximumPositionMillis;
        int32_t minMaxExtensionMillis;

        // current position
        int32_t currentPositionMillis;
        int8_t currentPositionPercentOfMaximum;

        // motion
        int8_t direction; // -1 up, 0 stop, 1 down
        int32_t currentPositionMillisAtMotionStart;

        // millis () related
        uint32_t motionStartMillis;
        uint32_t motionStopDeltaMillis;
};

#define TRIM_TO_RANGE(value, min, max) value = (value < min? min : (value > max ? max : value))

#endif
