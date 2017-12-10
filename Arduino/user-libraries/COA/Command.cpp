#include <Command.h>

const char* Command::COMMAND_OK = "%s,OK;";
const char* Command::COMMAND_ERROR_SYNTAX = "%s,ERROR,SYNTAX,%d;";
const char* Command::COMMAND_ERROR = "%s,ERROR,%s;";
const char* Command::STATUS = "STATUS";
const char* Command::READ = "READ";

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
