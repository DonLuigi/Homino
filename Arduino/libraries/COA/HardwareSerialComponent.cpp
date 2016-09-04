#include <HardwareSerialComponent.h>

HardwareSerialComponent::HardwareSerialComponent (HardwareSerial* serial, const char* name) :
    Component (name)
{
    this->serial = serial;
}

int HardwareSerialComponent::readFromComponent (Message* message)
{
    if (message->getSize () == 0 && serial->available ())
    {
        int size = serial->readBytesUntil ('\r', message->getBuffer (), message->getCapacity ());
        message->setSize (size);
        COA_DEBUG ("HS[%s]:RD:SIZE=%d:%s", name, size, message->getBuffer ());
    }

    return (Component::ALL_SUBCOMPONENTS);
}

void HardwareSerialComponent::writeToComponent (Command* command, Message* message, int subcomponent)
{
    if (message->getSize () > 0)
    {
        COA_DEBUG ("HS[%s]:WR:SIZE=%d", name, message->getSize ());
        serial->write (message->getBuffer (), message->getSize ());
        message->clear ();
    }
}
