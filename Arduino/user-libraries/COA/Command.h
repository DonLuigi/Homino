#ifndef COMMAND_H
#define COMMAND_H

#include <Arduino.h>
#include <Message.h>

class Command
{
    public:
        Command (char* contents);

        char * getName ();
        char** getParts ();
        uint16_t getSize ();

        static const char* COMMAND_OK;
        static const char* COMMAND_ERR_SYNTAX;

    private:
        static const int MAX_PARTS = 32;
        char* parts[MAX_PARTS + 1];
        char* name;
        uint16_t size;

        void init (char* buffer, uint16_t capacity);
};

#endif
