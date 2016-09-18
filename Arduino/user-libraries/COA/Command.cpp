#include <Command.h>

const char* Command::COMMAND_OK = "%s,OK";
const char* Command::COMMAND_ERR_SYNTAX = "%s,ERR,SYNTAX,%d";

Command::Command (char* buffer)
{
    char* component = strtok (buffer, ",");
    name = component;

    parts[0] = NULL;
    for (int i = 0; i < MAX_PARTS - 1 && component != NULL; i++)
    {
        component = strtok (NULL, ",");
        parts[i] = component;
        parts[i + 1] = NULL;
    }
}

char* Command::getName ()
{
    return (name);
}

char** Command::getParts ()
{
    return (parts);
}

uint16_t Command::getSize ()
{
    return (size);
}
