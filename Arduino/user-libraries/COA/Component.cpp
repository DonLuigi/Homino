#include "Component.h"

Component::Component (const char* name, uint32_t reportMillis)
{
    this->name = (name != NULL ? name : "");
    this->reportMillis = reportMillis;
    error = NULL;
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

#if COA_DEBUG_ENABLED
//void Component::debug (const char* format, ...)
//{
//    char buffer[32 + 1];
//    va_list args;
//    va_start(args, format);
//    vsnprintf (buffer, 32, format, args);
//    va_end(args);
//    Serial.print ("#[");
//    Serial.print (millis ());
//    Serial.print ("] ");
//    Serial.println (buffer);
//}

void Component::debug (const __FlashStringHelper* format, ...)
{
    char buffer[32 + 1];
    va_list args;
    va_start(args, format);
    vsnprintf (buffer, 32, String (format).c_str(), args);
    va_end (args);
    Serial.print ("#[");
    Serial.print (millis ());
    Serial.print ("] ");
    Serial.println (buffer);
}

#endif
