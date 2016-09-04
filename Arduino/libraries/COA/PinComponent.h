#ifndef PIN_COMPONENT_H
#define PIN_COMPONENT_H

#include <Component.h>

class PinComponent: public Component
{
    public:
        // component
        PinComponent (uint8_t pin, uint8_t mode, uint8_t initialState, const char* name = NULL, uint32_t reportMillis = 0);
        void setup ();

        // api
        void write (uint8_t state);
        void pwmWrite (uint8_t ratio);
        uint8_t read ();
        void flip ();
        boolean hasFlipped ();
        void setMode (uint8_t mode);

    private:
        uint8_t pin;
        uint8_t mode;
        uint8_t state;
};

#endif
