#ifndef TIMED_RELAY_COMPONENT_H
#define TIMED_RELAY_COMPONENT_H

#include <Component.h>
#include <RelayComponent.h>
#include <ButtonComponent.h>

class TimedRelayComponent: public RelayComponent
{
    public:
        // component
        TimedRelayComponent (uint8_t pin, uint8_t initialState, bool inverted, uint32_t defaultDurationMillis, uint32_t reengageDurationMillis, ButtonComponent* triggerButton,
            ButtonComponent* unlockButton, const char* name = NULL, uint32_t reportMillis = 0);
        void setup ();
        void writeToComponent (Command* command, Message* message, int subcomponent);
        int readFromComponent (Message* message);

        // api
        void start (uint32_t durationMillis, bool force);
        void stop ();
        void getStatus (int8_t* coveredTimePercent);

    private:
        ButtonComponent* triggerButton;
        ButtonComponent* unlockButton;
        uint32_t defaultDurationMillis;
        uint32_t reengageDurationMillis;
        uint32_t noReengageMillis;
        uint32_t durationMillis; // timer total duration
        uint32_t stopMillis; // scheduled timer stop time
        uint32_t lastReportMillis; // last status report time
        boolean forceReport;
        boolean locked;
};

#endif
