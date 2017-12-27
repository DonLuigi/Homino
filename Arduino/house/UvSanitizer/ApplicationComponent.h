#ifndef APPLICATION_COMPONENT_H
#define APPLICATION_COMPONENT_H

#include <Hysteresis.h>
#include <Component.h>
#include <TimedRelayComponent.h>
#include <LiquidCrystalI2CComponent.h>

class ApplicationComponent: public Component
{
    public:
        ApplicationComponent (TimedRelayComponent *lightTimedRelayComponent, LiquidCrystalI2CComponent* lcd, const char* name);
        int readFromComponent (Message* message);

    private:
        // components
        TimedRelayComponent* lightTimedRelayComponent;
        LiquidCrystalI2CComponent* lcd;

        // miscellaneous
        long lastLcdUpdateMillis = 0;
};

#endif
