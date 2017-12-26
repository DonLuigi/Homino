#ifndef TIMER_H_
#define TIMER_H_

#include <Arduino.h>

class Timer
{
    public:
        Timer ();
        void start (uint32_t countdownMillis);
        int getProgressPercent ();
        uint32_t getProgressMillis ();

    private:
        unsigned long startMillis;
        unsigned long countdownMillis;
};

#endif
