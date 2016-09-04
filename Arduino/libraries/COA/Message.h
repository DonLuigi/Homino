#ifndef MESSAGE_H
#define MESSAGE_H

#include <Arduino.h>

class Message
{
    public:
        Message (char* buffer, uint16_t capacity);

        bool append (const char* format, ...);
        char** getParts ();
        uint16_t getSize ();
        void setSize (uint16_t size);
        void clear ();
        uint16_t getCapacity ();
        char* getBuffer ();

    private:
        char* buffer;
        uint16_t rawCapacity;
        uint16_t size;

        static const int MAX_PARTS = 32;
        char* parts[MAX_PARTS + 1];
};

#endif
