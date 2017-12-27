#ifndef PHOTO_RESISTOR_COMPONENT_H
#define PHOTO_RESISTOR_COMPONENT_H

#include <Component.h>

class PhotoResistorComponent: public virtual Component
{
    public:
        // component
        PhotoResistorComponent (uint8_t pin, uint32_t sampleEveryMillis, uint32_t sampleDurationMillis, const char* name);
        void setup ();
        void writeToComponent (Command* command, Message* message, int subcomponent);
        int readFromComponent (Message* message);
        void reportStatus (Message* message);

        // api
        uint16_t read ();

    private:
        void report (Message* message, uint32_t nowMillis);
        uint8_t pin;
        uint16_t numberOfSamples;
        int16_t lastAverageSamples;
        uint32_t sampleEveryMillis;
        uint32_t sampleDurationMillis;
        uint32_t cumulativeSamples;
        uint32_t lastSampleMillis;
        uint32_t lastCumulativeSamplesMillis;
};

#endif
