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

void Shell::setup ()
{
    COA_DEBUG ("SH:SETUP");
    for (int i = 0; components != NULL && components[i] != NULL; i++)
    {
        components[i]->setup ();
        COA_DEBUG ("SH:C:[%d] %s", i, components[i]->name);
    }

    for (int i = 0; commandComponents != NULL && commandComponents[i] != NULL; i++)
    {
        commandComponents[i]->setup ();
        COA_DEBUG ("SH:CC:[%d] %s", i, commandComponents[i]->name);
    }

    COA_DEBUG ("SH:SETUP DONE");
}

void Shell::loop ()
{
    // mark start
    long startMillis = millis ();

    // process inbound commands
    for (int commandComponentsIdx = 0; commandComponents != NULL && commandComponents[commandComponentsIdx] != NULL; commandComponentsIdx++)
    {
        // clear inbound message prior to reading
        inboundMessage->clear ();

        // remember subcomponent message was read from, if any
        int commandSubcomponent = commandComponents[commandComponentsIdx]->readFromComponent (inboundMessage);

        if (inboundMessage != NULL && inboundMessage->getSize () > 0)
        {
            COA_DEBUG ("SH:CC:INBOUND:SIZE=%d:%s", inboundMessage->getSize (), inboundMessage->getBuffer ());

            // message was received
            outboundMessage->clear ();

            // split message into inboundMessageParts and process each one separately, aggregating results in outboundMessage
            char* *inboundMessageParts = inboundMessage->getParts ();

            int i;
            for (i = 0; inboundMessageParts[i] != NULL; i++)
                ;
            COA_DEBUG ("SH:MSG:PARTS:%d", i);

            for (uint8_t inboundMessagePartsIdx = 0; inboundMessageParts[inboundMessagePartsIdx] != NULL; inboundMessagePartsIdx++)
            {
                // compose command
                Command command (inboundMessageParts[inboundMessagePartsIdx]);
                COA_DEBUG ("SH:COMM:NAME=%s", command.getName ());

                // write command into components
                bool found = false;
                for (int j = 0; components[j] != NULL; j++)
                {
                    if (strcasecmp (components[j]->name, command.getName ()) == 0)
                    {
                        COA_DEBUG ("SH:PROCESS:%s", command.getName ());
                        components[j]->writeToComponent (&command, outboundMessage, Component::ALL_SUBCOMPONENTS);
                        found = true;
                        break;
                    }
                }

                // write command into shell
                if (!found && strcasecmp (name, command.getName ()) == 0)
                {
                    COA_DEBUG ("SH:PROCESS:%s", command.getName ());
                    writeToComponent (&command, outboundMessage, Component::ALL_SUBCOMPONENTS);
                    found = true;
                }

                // component not found
                if (!found)
                {
                    COA_DEBUG ("SH:ERR:Unknown component '%s'", command.getName ());
                    outboundMessage->append ("%s,ERR,UNKNOWN;", command.getName ());
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
    if (outboundMessage->getSize () > 0)
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
