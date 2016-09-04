#include <Arduino.h>
#include <Timer.h>

Timer::Timer ()
{
    this->startMillis = 0;
    this->countdownMillis = 0;
}

void Timer::start (uint32_t countdownMillis)
{
    startMillis = millis ();
    this->countdownMillis = countdownMillis;
}

int Timer::getProgressPercent ()
{
    uint32_t durationMillis = millis () - startMillis;

    if (startMillis == 0 || countdownMillis == 0 || durationMillis >= countdownMillis)
    {
        return (100);
    }
    else
    {
        return ((durationMillis * 100) / countdownMillis);
    }
}

uint32_t Timer::getProgressMillis ()
{
    return (millis () - startMillis);
}
