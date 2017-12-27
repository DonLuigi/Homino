#include "Component.h"

Component::Component (const char* name)
{
    this->name = (name != NULL ? name : "");
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

void Component::reportStatus (Message* message)
{
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
