#ifndef TIMED_RELAY_COMPONENT_H
#define TIMED_RELAY_COMPONENT_H

#include <Component.h>
#include <RelayComponent.h>

class TimedRelayComponent: public RelayComponent
{
    public:
        // component
        TimedRelayComponent (uint8_t pin, uint8_t initialState, bool inverted, const char* name = NULL, uint32_t reportMillis = 0);
        void writeToComponent (Command* command, Message* message, int subcomponent);
        int readFromComponent (Message* message);

        // api
        void start (uint32_t durationMillis);
        void stop ();
        void getStatus (int8_t* coveredTimePercent);

    private:
        uint32_t durationMillis; // timer total duration
        uint32_t stopMillis; // scheduled timer stop time
        uint32_t lastReportMillis; // last status report time
        boolean forceReport;
};

#endif
