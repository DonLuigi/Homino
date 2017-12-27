#include <Arduino.h>
#include <Message.h>
#include "Shell.h"

Shell::Shell (Component** components, Component** commandComponents, Message* inboundMessage, Message* outboundMessage, long loopMillis, const char* name, bool report) :
    Component (name, report)
{
    this->components = components;
    this->commandComponents = commandComponents;
    this->loopMillis = loopMillis;
    this->inboundMessage = inboundMessage;
    this->outboundMessage = outboundMessage;
}

// Component
void Shell::writeToComponent (Command* command, Message* message, int subcomponent)
{
    char** parts = command->getParts ();
    if (strcasecmp (parts[0], "STATUS") == 0)
    {
        message->append ("%s,STATUS,%lu;", name, (uint32_t) uptimeMillis / 1000);

        // trigger all components to report
        for (int j = 0; components[j] != NULL; j++)
        {
            components[j]->setReported (0UL);
        }
    }
}

void Shell::setup ()
{

    for (int i = 0; components != NULL && components[i] != NULL; i++)
    {
        COA_DEBUG (F ("SH:C[%d] %s"), i, components[i]->name);
        components[i]->setup ();
    }

    for (int i = 0; commandComponents != NULL && commandComponents[i] != NULL; i++)
    {
        COA_DEBUG (F ("SH:CC[%d] %s"), i, commandComponents[i]->name);
        commandComponents[i]->setup ();
    }

    COA_DEBUG (F ("SH:SETUP DONE"));
}

void Shell::loop ()
{
    // mark start
    long startMillis = millis ();

    // update uptime
    uptimeMillis += (startMillis - uptimeLastUpdateMillis);
    uptimeLastUpdateMillis = startMillis;

    // process inbound commands
    for (int commandComponentsIdx = 0; commandComponents != NULL && inboundMessage != NULL && commandComponents[commandComponentsIdx] != NULL; commandComponentsIdx++)
    {
        // clear inbound message prior to reading
        inboundMessage->clear ();

        // remember subcomponent message was read from, if any
        int commandSubcomponent = commandComponents[commandComponentsIdx]->readFromComponent (inboundMessage);

        if (inboundMessage != NULL && inboundMessage->getSize () > 0)
        {
            COA_DEBUG (F ("SH:CC:INBOUND:SIZE=%d"), inboundMessage->getSize (), inboundMessage->getBuffer ());COA_DEBUG (inboundMessage->getBuffer ());

            // message was received
            outboundMessage->clear ();

            // split message into inboundMessageParts and process each one separately, aggregating results in outboundMessage
            char* *inboundMessageParts = inboundMessage->getParts ();

            int i;
            for (i = 0; inboundMessageParts[i] != NULL; i++)
                ;

            COA_DEBUG (F ("SH:MSG:PARTS:%d"), i);

            for (uint8_t inboundMessagePartsIdx = 0; inboundMessageParts[inboundMessagePartsIdx] != NULL; inboundMessagePartsIdx++)
            {
                // compose command
                Command command (inboundMessageParts[inboundMessagePartsIdx]);
                COA_DEBUG (F ("SH:COMM:NAME=%s"), command.getName ());

                // write command into components
                bool found = false;
                for (int j = 0; components[j] != NULL; j++)
                {
                    if (strcasecmp (components[j]->name, command.getName ()) == 0)
                    {
                        COA_DEBUG (F ("SH:PROCESS:%s"), command.getName ());
                        components[j]->writeToComponent (&command, outboundMessage, Component::ALL_SUBCOMPONENTS);
                        found = true;
                        break;
                    }
                }

                // write command into shell
                if (!found && strcasecmp (name, command.getName ()) == 0)
                {
                    COA_DEBUG (F ("SH:PROCESS:%s"), command.getName ());
                    writeToComponent (&command, outboundMessage, Component::ALL_SUBCOMPONENTS);
                    found = true;
                }

                // component not found
                if (!found)
                {
                    COA_DEBUG (F ("SH:ERROR:Unknown component '%s'"), command.getName ());
                    outboundMessage->append (Command::COMMAND_ERROR, command.getName (), "UNKNOWN");
                }

                // reply only to subcomponent, if it was specified
                if (outboundMessage->getSize () > 0)
                {
                    commandComponents[commandComponentsIdx]->writeToComponent (NULL, outboundMessage, commandSubcomponent);
                    outboundMessage->clear ();
                }
            }
        }
    }

    // ping components for any outbound commands
    for (int i = 0; components != NULL && components[i] != NULL; i++)
    {
        // COA_DEBUG("SH:PULSE %s", components[i]->name);
        components[i]->readFromComponent (outboundMessage);
    }

    // final reply
    if (outboundMessage != NULL && outboundMessage->getSize () > 0)
    {
        for (int j = 0; commandComponents[j] != NULL; j++)
        {
            commandComponents[j]->writeToComponent (NULL, outboundMessage, Component::ALL_SUBCOMPONENTS);
        }
        outboundMessage->clear ();
    }

    // delay
    long consumedMillis = millis () - startMillis;
    long remainingMillis = loopMillis - consumedMillis;
    remainingMillis = (remainingMillis > 0 ? remainingMillis : 0);
    delay (remainingMillis);
}
