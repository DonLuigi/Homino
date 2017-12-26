#include <Arduino.h>
#include "Hysteresis.h"

Hysteresis::Hysteresis ()
{
    this->tolerance = 0;
    previousAnswer = UNDEFINED;
}

Hysteresis::Hysteresis (float tolerance)
{
    this->tolerance = tolerance;
    previousAnswer = UNDEFINED;
}

void Hysteresis::setTolerance (float tolerance)
{
    this->tolerance = tolerance;
}

int Hysteresis::compare (float a, float b)
{
    if (previousAnswer == UNDEFINED || abs (a-b) > tolerance)
    {
        previousAnswer = (a == b ? 0 : a < b ? -1 : 1);
        return (previousAnswer);
    }
    return (previousAnswer);
}
