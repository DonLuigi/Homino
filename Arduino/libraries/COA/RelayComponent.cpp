#include "RelayComponent.h"

///////////////////
//
// Component
//
///////////////////
RelayComponent::RelayComponent (uint8_t pin, uint8_t initialState, bool inverted, const char* name, uint32_t reportMillis) :
    PinComponent (pin, OUTPUT, (inverted ? (initialState == HIGH ? LOW : HIGH) : initialState), name, reportMillis)
{
    this->inverted = inverted;
}

void RelayComponent::writeToComponent (Command* command, Message* message, int subcomponent)
{
    char** parts = command->getParts ();

    if (parts[0] == NULL)
    {
        message->append (Command::COMMAND_ERR_SYNTAX, name, 0);
    }

    if (strcasecmp (parts[0], "HIGH") == 0)
    {
        write (HIGH);
        message->append (Command::COMMAND_OK, name);
    }
    else if (strcasecmp (parts[0], "LOW") == 0)
    {
        write (LOW);
        message->append (Command::COMMAND_OK, name);
    }
    else if (strcasecmp (parts[0], "READ") == 0)
    {
        message->append ("%s,OK,%d", name, read ());
    }
    else
    {
        message->append (Command::COMMAND_ERR_SYNTAX, name, 1);
    }
}
///////////////////
//
// API
//
///////////////////
void RelayComponent::write (uint8_t state)
{
    PinComponent::write (inverted ? (state == HIGH ? LOW : HIGH) : state);
}

uint8_t RelayComponent::read ()
{
    uint8_t read = PinComponent::read ();

    return (inverted ? (read == HIGH ? LOW : HIGH) : read);
}
