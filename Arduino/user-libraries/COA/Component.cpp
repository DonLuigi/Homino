#include "Component.h"

Component::Component (const char* name, uint32_t reportMillis)
{
    this->name = (name != NULL ? name : "");
    this->reportEveryMillis = reportMillis;
    this->lastReportMillis = 0;
}

void Component::setup ()
{
}

int Component::readFromComponent (Message* message)
{
    return (Component::ALL_SUBCOMPONENTS);
}

void Component::writeToComponent (Command* command, Message *replyMessage, int subcomponent)
{
}

uint8_t Component::toString (char* buffer, uint8_t size, uint8_t customFontOffset)
{
    return (0);
}

uint8_t Component::toString (char* buffer, uint8_t size)
{
    return (0);
}

boolean Component::shouldReport (uint32_t timeMillis)
{
    return (reportEveryMillis > 0 ? timeMillis - lastReportMillis > reportEveryMillis || lastReportMillis == 0 : false);
}

boolean Component::shouldReport (uint32_t timeMillis, uint32_t reportEveryMillisArg)
{
    return (reportEveryMillisArg > 0 ? timeMillis - lastReportMillis > reportEveryMillisArg || lastReportMillis == 0 : false);
}

void Component::setReported (uint32_t timeMillis)
{
    lastReportMillis = timeMillis;
}

#if COA_DEBUG_ENABLED
void Component::debug (char* text)
{
    Serial.print ("#[");
    Serial.print (millis ());
    Serial.print ("] ");
    Serial.println (text);
}

void Component::debug (const __FlashStringHelper* format, ...)
{
    char buffer[64 + 1];
    va_list args;
    va_start(args, format);
    vsnprintf (buffer, sizeof (buffer) - 1, String (format).c_str(), args);
    va_end (args);
    Serial.print ("#[");
    Serial.print (millis ());
    Serial.print ("] ");
    Serial.println (buffer);
}

#endif
