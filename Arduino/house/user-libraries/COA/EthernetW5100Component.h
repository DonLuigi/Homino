#ifndef ETHERNET_W5100_COMPONENT_H
#define ETHERNET_W5100_COMPONENT_H

#include <Ethernet.h>
#include <IPAddress.h>
#include <Component.h>

class EthernetW5100Component: public Component
{
    public:
        EthernetW5100Component (byte* mac, IPAddress* ipAddress, uint16_t serverPort, const char* name = NULL);
        void setup ();
        int readFromComponent (Message* message);
        void writeToComponent (Command* command, Message* message, int subcomponent);

    private:
        static const int maxClients = 4;

        IPAddress* ipAddress;
        byte* mac;
        uint16_t serverPort;
        EthernetServer tcpServer;
        EthernetClient tcpClients[maxClients];

        static const char* messageMagicCookie;
};

#endif
