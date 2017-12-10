#include <HardwareSerialComponent.h>

HardwareSerialComponent::HardwareSerialComponent (HardwareSerial* serial, const char* name) :
    Component (name)
{
    this->serial = serial;
}

int HardwareSerialComponent::readFromComponent (Message* message)
{
    // TODO: check for message size
    if (message->getSize () == 0 && serial->available ())
    {
        uint16_t capacity = message->getCapacity ();
        COA_DEBUG(F ("HS[%s]:READING:SIZE=%d"), name, capacity);
        int size = serial->readBytesUntil (';', message->getBuffer (), capacity);
        if (size < capacity)
        {
            message->getBuffer ()[size++] = ';';
        }
        message->setSize (size);
        message->getBuffer ()[size] = '\0';
        COA_DEBUG(F ("HS[%s]:READ:SIZE=%d:'%s'"), name, size, message->getBuffer ());
    }

    return (Component::ALL_SUBCOMPONENTS);
}

void HardwareSerialComponent::writeToComponent (Command* command, Message* message, int subcomponent)
{
    if (message->getSize () > 0)
    {
        COA_DEBUG(F ("HS[%s]:WR:SIZE=%d"), name, message->getSize ());
        serial->write (message->getBuffer (), message->getSize ());
        message->clear ();
    }
}
