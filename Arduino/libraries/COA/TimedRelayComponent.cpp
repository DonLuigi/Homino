#include "TimedRelayComponent.h"

///////////////////
//
// Component
//
///////////////////
TimedRelayComponent::TimedRelayComponent (uint8_t pin, uint8_t initialState, bool inverted, const char* name, uint32_t reportMillis) :
    RelayComponent (pin, initialState, inverted, name, reportMillis)
{
    durationMillis = stopMillis = lastReportMillis = 0;
    forceReport = false;
}

void TimedRelayComponent::writeToComponent (Command* command, Message* message, int subcomponent)
{
    char** parts = command->getParts ();

    if (parts[0] == NULL)
    {
        message->append (Command::COMMAND_ERR_SYNTAX, name, 0);
        return;
    }

    if (strcasecmp (parts[0], "START") == 0)
    {
        if (parts[1] == NULL)
        {
            message->append (Command::COMMAND_ERR_SYNTAX, name, 1);
            return;
        }

        uint32_t durationMillis = atol (parts[1]);
        start (durationMillis);
    }

    else if (strcasecmp (parts[0], "STOP") == 0)
    {
        stop ();
    }

    else if (strcasecmp (parts[0], "STATUS") == 0)
    {
        forceReport = true;
        COA_DEBUG ("FC1=%d", forceReport);
    }

    else
    {
        message->append (Command::COMMAND_ERR_SYNTAX, name, 1);
    }
}

int TimedRelayComponent::readFromComponent (Message* message)
{
    // prologue
    uint32_t nowMillis = millis ();

    // stop
    if (durationMillis > 0 && stopMillis > 0 && nowMillis > stopMillis)
    {
        stop ();
    }

    // status
    int8_t coveredTimePercent;
    getStatus (&coveredTimePercent);
    if (forceReport || // force
        (coveredTimePercent > 0 && lastReportMillis > 0 && (nowMillis - lastReportMillis) > reportMillis) || // running
        (coveredTimePercent == -1 && lastReportMillis < stopMillis)) // last report after stop
    {
        COA_DEBUG ("FC2=%d,LRM=%ld,SM=%ld,P=%d,NM=%ld", forceReport, lastReportMillis, stopMillis, coveredTimePercent, nowMillis);
        message->append ("%s,STATUS,%d", name, coveredTimePercent);
        lastReportMillis = nowMillis;
        forceReport = false;
        COA_DEBUG ("FC3=%d", forceReport);
    }
    return (Component::ALL_SUBCOMPONENTS);
}

///////////////////
//
// API
//
///////////////////
void TimedRelayComponent::start (uint32_t durationMillis)
{
    RelayComponent::write (HIGH);

    this->durationMillis = durationMillis;
    stopMillis = millis () + durationMillis;
    forceReport = true;
    COA_DEBUG ("FC4=%d", forceReport);
}

void TimedRelayComponent::stop ()
{
    RelayComponent::write (LOW);
    stopMillis = millis ();
    durationMillis = 0;
    forceReport = true;
    COA_DEBUG ("FC5=%d", forceReport);

}

void TimedRelayComponent::getStatus (int8_t* coveredTimePercent)
{
    if (coveredTimePercent == NULL)
    {
        return;
    }

    if (durationMillis == 0)
    {
        *coveredTimePercent = -1;
    }
    else
    {
        uint32_t sinceStartMillis = millis () - (stopMillis - durationMillis);
        int8_t percent = sinceStartMillis * 100 / durationMillis;
        *coveredTimePercent = (percent < 0 ? 0 : (percent > 100 ? 100 : percent));
    }
}
