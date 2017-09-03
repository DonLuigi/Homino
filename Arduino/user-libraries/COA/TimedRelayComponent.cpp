#include "TimedRelayComponent.h"

///////////////////
//
// Component
//
///////////////////
TimedRelayComponent::TimedRelayComponent (uint8_t pin, uint8_t initialState, bool inverted, uint32_t defaultDurationMillis, uint32_t reengageDurationMillis, ButtonComponent* triggerButton,
    ButtonComponent* unlockButton, const char* name, uint32_t reportMillis) :
    RelayComponent (pin, initialState, inverted, name, reportMillis)
{
    durationMillis = stopMillis = lastReportMillis = noReengageMillis = 0;
    forceReport = false;
    locked = false;
    this->defaultDurationMillis = defaultDurationMillis;
    this->reengageDurationMillis = reengageDurationMillis;
    this->triggerButton = triggerButton;
    this->unlockButton = unlockButton;
}

void TimedRelayComponent::setup ()
{
    RelayComponent::setup ();

    if (triggerButton != NULL)
    {
        triggerButton->setup ();
    }

    if (unlockButton != NULL)
    {
        unlockButton->setup ();
    }
}

void TimedRelayComponent::writeToComponent (Command* command, Message* message, int subcomponent)
{
    Component::writeToComponent (command, message, subcomponent);

    char** parts = command->getParts ();

    if (parts[0] == NULL)
    {
        message->append (Command::COMMAND_ERR_SYNTAX, name, 0);
        return;
    }

    if (strcasecmp (parts[0], "START") == 0)
    {
        if (parts[1] != NULL)
        {
            int32_t durationMillis = atol (parts[1]);
            start (durationMillis >= 0 ? durationMillis : defaultDurationMillis, parts[2] != NULL && strcasecmp (parts[2], "FORCE") == 0);
        }
    }
    else if (strcasecmp (parts[0], "STOP") == 0)
    {
        stop ();
    }
    else if (strcasecmp (parts[0], "LOCK") == 0)
    {
        locked = true;
    }
    else if (strcasecmp (parts[0], "UNLOCK") == 0)
    {
        locked = false;
    }
    else if (strcasecmp (parts[0], "STATUS") == 0)
    {
    }
    else
    {
        message->append (Command::COMMAND_ERR_SYNTAX, name, 1);
    }

    forceReport = true;
}

int TimedRelayComponent::readFromComponent (Message* message)
{
    // prologue
    uint32_t nowMillis = millis ();

    // trigger button
    if (triggerButton != NULL && triggerButton->onPress ())
    {
        int8_t coveredTimePercent;
        getStatus (&coveredTimePercent);
        if (coveredTimePercent < 0)
        {
            start (defaultDurationMillis, false);
        }
        else
        {
            stop ();
        }
    }

    // lock button
    if (unlockButton != NULL && unlockButton->onPress ())
    {
        locked = false;
        forceReport = true;
    }

    // stop
    if (durationMillis > 0 && stopMillis > 0 && nowMillis > stopMillis)
    {
        stop ();
    }

    // status
    int8_t coveredTimePercent;
    getStatus (&coveredTimePercent);
    if (message != NULL)
    {
        if (forceReport || // force
            (coveredTimePercent > 0 && lastReportMillis > 0 && (nowMillis - lastReportMillis) > reportMillis) || // running
            (coveredTimePercent == -1 && lastReportMillis < stopMillis)) // last report after stop
        {
            COA_DEBUG(F ("FC2=%d,LRM=%ld,SM=%ld,P=%d,NM=%ld,EN=%d"), forceReport, lastReportMillis, stopMillis, coveredTimePercent, nowMillis, locked);
            message->append ("%s,STATUS,%d,%d", name, coveredTimePercent, locked);
            lastReportMillis = nowMillis;
            forceReport = false;
            COA_DEBUG(F ("FC3=%d"), forceReport);
        }
    }
    return (Component::ALL_SUBCOMPONENTS);
}

///////////////////
//
// API
//
///////////////////
void TimedRelayComponent::start (uint32_t durationMillis, bool force)
{
    if (millis () >= noReengageMillis && (!locked || force))
    {
        RelayComponent::write (HIGH);
        this->durationMillis = durationMillis;
        stopMillis = millis () + durationMillis;
        forceReport = true;
        COA_DEBUG(F ("TR[%s]:START:STOP_MILLIS=%ld,DUR_MILLIS=%ld,FORCE=%d"), name, stopMillis, durationMillis, force);
    }
}

void TimedRelayComponent::stop ()
{
    RelayComponent::write (LOW);
    stopMillis = millis ();
    noReengageMillis = stopMillis + reengageDurationMillis;
    durationMillis = 0;
    forceReport = true;
    COA_DEBUG(F ("TR[%s]:STOP"), name);

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
