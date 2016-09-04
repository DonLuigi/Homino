#include <Message.h>

Message::Message (char* buffer, uint16_t capacity)
{
    this->buffer = buffer;
    this->rawCapacity = capacity;
    clear ();
}

bool Message::append (const char* format, ...)
{
    // append
    va_list argPtr;
    va_start (argPtr, format);
    int remainingCapacity = getCapacity () - size;
    size += vsnprintf (buffer + size, remainingCapacity, format, argPtr);
    return (true);
}

char** Message::getParts ()
{
    // terminate
    buffer[size] = '\0';

    // split
    char* component = strtok (buffer, ";");

    uint8_t i;
    parts[0] = component;
    for (i = 1; i < MAX_PARTS - 1 && component != NULL; i++)
    {
        component = strtok (NULL, ";");
        parts[i] = component;
        parts[i + 1] = '\0';
    }

    return (parts);
}

void Message::clear ()
{
    size = 0;
}

uint16_t Message::getSize ()
{
    return (size);
}

void Message::setSize (uint16_t size)
{
    this->size = min (size, getCapacity ());
    buffer[size] = '\0';
}

uint16_t Message::getCapacity ()
{
    return (rawCapacity - 2);
}

char* Message::getBuffer ()
{
    return (buffer);
}
