#ifndef TIMED_RELAY_COMPONENT_H
#define TIMED_RELAY_COMPONENT_H

#include <Component.h>
#include <RelayComponent.h>
#include <ButtonComponent.h>

#define TIMED_RELAY_COMPONENT_OPTION_NONE 0
#define TIMED_RELAY_COMPONENT_OPTION_START_STOP 1
#define TIMED_RELAY_COMPONENT_OPTION_SINGLE_START 2
#define TIMED_RELAY_COMPONENT_OPTION_DOUBLE_TRIGGER_LOCK 4
#define TIMED_RELAY_COMPONENT_OPTION_UNLOCK_BUTTON_INHIBITS 8

class TimedRelayComponent: public RelayComponent
{
    public:
        // component
        TimedRelayComponent (uint8_t pin, uint8_t initialState, bool inverted, uint32_t defaultDurationMillis, uint32_t reengageDurationMillis, ButtonComponent* triggerButton,
            ButtonComponent* unlockButton, uint32_t whenRunningReportEveryMillis, int options, const char* name);
        void setup ();
        void writeToComponent (Command* command, Message* message, int subcomponent);
        int readFromComponent (Message* message);
        void reportStatus (Message* message);

        // api
        void start (uint32_t durationMillis, bool force);
        void stop ();
        void getStatus (int8_t* coveredTimePercent);

    private:
        int options;
        ButtonComponent* triggerButton;
        ButtonComponent* unlockButton;
        uint32_t defaultDurationMillis;
        uint32_t reengageDurationMillis;
        uint32_t whenRunningReportEveryMillis;
        uint32_t inhibitUnlockMillis;
        uint32_t noReengageMillis;
        uint32_t durationMillis; // timer total duration
        uint32_t stopMillis; // scheduled timer stop time
        uint32_t lastReportMillis; // last status report time
        boolean locked;
};

#endif
