#include "MotionRangeComponent.h"

///////////////////
//
// Component
//
///////////////////
MotionRangeComponent::MotionRangeComponent (int32_t maximumPositionMillis, int32_t minMaxExtensionMillis, RelayComponent* upRelay, RelayComponent* downRelay, EEPROMValueComponent* positionEeprom,
    const char* name) :
    Component (name)
{
    this->maximumPositionMillis = maximumPositionMillis;
    this->minMaxExtensionMillis = minMaxExtensionMillis;
    this->upRelay = upRelay;
    this->downRelay = downRelay;
    this->positionEeprom = positionEeprom;

    currentPositionMillis = (positionEeprom != NULL ? positionEeprom->readAsUInt32 () : 0);
    TRIM_TO_RANGE (currentPositionMillis, 0, maximumPositionMillis);

    direction = 0;
    pulse (-1, false);
}

void MotionRangeComponent::setup ()
{
    if (upRelay != NULL)
    {
        upRelay->setup ();
    }

    if (downRelay != NULL)
    {
        downRelay->setup ();
    }
}

///////////////////
//
// API
//
///////////////////
int8_t MotionRangeComponent::move (int8_t percentOfMaximum)
{
    if (maximumPositionMillis < 0)
    {
        return (0);
    }

    unsigned long nowMillis = millis ();
    pulse (nowMillis, false);

    int8_t deltaInPercentOfMaximum = percentOfMaximum - currentPositionPercentOfMaximum;
    unsigned long deltaInPercentOfMaximumInMillis = abs (maximumPositionMillis * deltaInPercentOfMaximum / 100);

    if (deltaInPercentOfMaximum != 0)
    {
        motionStartMillis = nowMillis;
        currentPositionMillisAtMotionStart = currentPositionMillis;
        motionStopDeltaMillis = deltaInPercentOfMaximumInMillis;

        // extend travel in case of going all the way up or down to make sure shutter hits off switch
        if (percentOfMaximum == 0 || percentOfMaximum == 100)
        {
            motionStopDeltaMillis += minMaxExtensionMillis;
        }

        COA_DEBUG ("MR:%s:MV:%d%%(D:%d%%=%dms)", name, percentOfMaximum, deltaInPercentOfMaximum, deltaInPercentOfMaximumInMillis);
    }

    if (deltaInPercentOfMaximum < 0)
    {
        upRelay->write (HIGH);
        direction = -1;
    }
    else if (deltaInPercentOfMaximum > 0)
    {
        downRelay->write (HIGH);
        direction = 1;
    }

    pulse (nowMillis, false);
    return (direction);
}

void MotionRangeComponent::stop ()
{
    pulse (millis (), true);
}

void MotionRangeComponent::pulse (unsigned long nowMillis, bool forceStop)
{
    currentPositionPercentOfMaximum = (currentPositionMillis * 100) / maximumPositionMillis;
    TRIM_TO_RANGE (currentPositionPercentOfMaximum, 0, 100);

    if (direction != 0)
    {
        uint32_t durationMillis = nowMillis - motionStartMillis;

        currentPositionMillis = currentPositionMillisAtMotionStart + direction * durationMillis;
        TRIM_TO_RANGE (currentPositionMillis, 0, maximumPositionMillis);

        //COA_DEBUG("MR:%s:MOTION:%ldms->%ldms,D%d", name, durationMillis, motionStopDeltaMillis, direction);

        if (durationMillis > motionStopDeltaMillis || forceStop)
        {
            COA_DEBUG ("MR:%s:STOP:%ldms:@%ldms=%d%%:F%d", name, durationMillis, currentPositionMillis, currentPositionPercentOfMaximum, forceStop);

            direction = 0;
            upRelay->write (LOW);
            downRelay->write (LOW);
            if (positionEeprom != NULL)
            {
                positionEeprom->write (&currentPositionMillis);
            }
        }
    }
}

bool MotionRangeComponent::isMoving ()
{
    return (direction != 0);
}

void MotionRangeComponent::getStatus (int8_t* direction, int8_t* currentPositionPercent)
{
    if (direction != NULL)
    {
        *direction = this->direction;
    }

    if (currentPositionPercent != NULL)
    {
        *currentPositionPercent = this->currentPositionPercentOfMaximum;
    }
}

void MotionRangeComponent::overrideCurrentPosition (int8_t currentPositionPercent)
{
    currentPositionMillis = maximumPositionMillis * 100 / currentPositionPercent;
    TRIM_TO_RANGE (currentPositionMillis, 0, maximumPositionMillis);
}
