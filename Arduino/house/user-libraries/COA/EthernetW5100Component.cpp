#include "EthernetW5100Component.h"

const char* EthernetW5100Component::messageMagicCookie = "MGCK";

EthernetW5100Component::EthernetW5100Component (byte* mac, IPAddress* ipAddress, uint16_t serverPort, const char* name) :
    Component (name), tcpServer (serverPort)
{
    this->mac = mac;
    this->ipAddress = ipAddress;
    this->serverPort = serverPort;
}

void EthernetW5100Component::setup ()
{
    Ethernet.begin (mac, *ipAddress);
    tcpServer.begin ();
}

int EthernetW5100Component::readFromComponent (Message* message)
{
    // can't read into non empty message
    if (message->getSize () != 0)
    {
        return (-1);
    }

    // close disconnected sockets
    for (int i = 0; i < maxClients; i++)
    {
        if (tcpClients[i] && !tcpClients[i].connected ())
        {
            tcpClients[i].stop ();
            COA_DEBUG (F ("ETH[%s]:CLIENT[%d],DISCONNECT"), name, i);
        }
    }

    // check for inbound data
    EthernetClient activeTcpClient = tcpServer.available ();
    if (!activeTcpClient)
    {
        return (-1);
    } COA_DEBUG (F ("ETH[%s]:CLIENT,SOCK=%d"), name, activeTcpClient.getSocketNumber ());

    int availableSpot = -1;
    int clientIndex = -1;
    for (int i = 0; i < maxClients; i++)
    {
        COA_DEBUG (F ("ETH[%s]:CLIENT[%d],SOCK=%d"), name, i, tcpClients[i].getSocketNumber ());

        if (tcpClients[i] == activeTcpClient)
        {
            clientIndex = i;
            activeTcpClient.flush ();
            COA_DEBUG (F ("ETH[%s]:EXISTING[%d]"), name, clientIndex);
            break;
        }
        if (!tcpClients[i])
        {
            availableSpot = i;
        }
    }
    if (clientIndex < 0)
    {
        if (availableSpot < 0)
        {
            COA_DEBUG (F ("ETH[%s]:ERROR:NO_SPACE"), name);
            return (-1);
        }
        tcpClients[clientIndex = availableSpot] = activeTcpClient;
        COA_DEBUG (F ("ETH[%s]:NEW[%d]"), name, clientIndex);
    }

    uint8_t byteRead;
    uint16_t available = activeTcpClient.available ();
    if (available > 0)
    {
        COA_DEBUG (F ("ETH[%s]:RECV=%d"), name, available);
        // magic cookie
        for (uint8_t i = 0; i < strlen (messageMagicCookie); i++)
        {
            byteRead = activeTcpClient.read ();
            COA_DEBUG (F ("ETH[%s]:MAGIC[%d]=%d"), name, i, byteRead);
            if (byteRead != messageMagicCookie[i])
            {
                message->append (Command::COMMAND_ERROR, "name", "ETH_MAGIC");
                return (-1);
            }
        } COA_DEBUG (F ("ETH[%s]:MAGIC_OK"), name);

        // size
        uint16_t size = activeTcpClient.read () & 0xFF;
        byteRead = activeTcpClient.read ();
        size = (size << 8) + byteRead;
        COA_DEBUG (F ("ETH[%s]:SIZE=%d"), name, size);

        if (size >= message->getCapacity ())
        {
            char sizeAsString[10];
            itoa (10, sizeAsString, 10);
            message->append (Command::COMMAND_ERROR, name, sizeAsString);
            return (-1);
        }

        // payload
        char* buffer = message->getBuffer ();
        for (uint16_t i = 0; i < size; i++)
        {
            buffer[i] = activeTcpClient.read ();
            buffer[i + 1] = '\0';
            // COA_DEBUG (F("ETH[%s][%d]"), name, buffer[i]);
        }

        message->setSize (size);
        COA_DEBUG (F ("ETH[%s]:DONE"), name);

        return (clientIndex);
    }
}

void EthernetW5100Component::writeToComponent (Command* command, Message* message, int subcomponent)
{
    int bufferSize = message->getSize ();
    if (bufferSize == 0)
    {
        COA_DEBUG (F ("ETH[%s]:WRITE:EMPTY"), name);
        return;
    }
    COA_DEBUG (F ("ETH[%s]:SIZE:%d,:%s"), name, bufferSize, message->getBuffer());

    // common loop for sending to all clients or to one specified in subcomponent
    for (int i = 0; i < maxClients; i++)
    {
        // if client specified, send only to that client
        if (subcomponent >= 0 && subcomponent != i)
        {
            continue;
        }

        // is client even connected
        if (!tcpClients[i] || !tcpClients[i].connected ())
        {
            continue;
        }

        // magic cookie
        COA_DEBUG (F ("ETH[%s]:WRITE[%d]:MAGIC"), name, i);
        tcpClients[i].write (messageMagicCookie, strlen (messageMagicCookie));

        // size
        COA_DEBUG (F ("ETH[%s]:WRITE[%d]:SIZE:%d"), name, i, bufferSize);
        char* buffer = message->getBuffer ();

        tcpClients[i].write (bufferSize >> 8);
        tcpClients[i].write (bufferSize & 0xFF);

        // payload
        COA_DEBUG (F ("ETH[%s]:WRITE[%d]:PAYLOAD"), name, i);
        tcpClients[i].write (buffer, bufferSize);

        COA_DEBUG (F ("ETH[%s]:WRITE[%d]:FLUSH"), name, i);
        tcpClients[i].flush ();
    }

    // clear message
    message->clear ();

    COA_DEBUG (F ("ETH[%s]:WRITE:DONE"), name);
}
