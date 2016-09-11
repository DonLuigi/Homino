#include <EEPROMValueComponent.h>
#include <PinComponent.h>
#include <DallasTemperatureComponent.h>
#include <U8GlibComponent.h>
#include <KeypadComponent.h>
#include <HardwareSerialComponent.h>
#include <UIPEthernetComponent.h>
#include <Shell.h>
#include "HouseShell.h"

///////////////////
//
// Variants
//
///////////////////
#define VARIANT_BEDROOM 1
#define VARIANT_GARAGE 2
#define VARIANT_LIVINGROOM 3
#define VARIANT_KIDROOM 4
#define VARIANT_HALLWAY_FIRST_FLOOR 5

#define VARIANT VARIANT_LIVINGROOM

///////////////////
//
// Variant includes
//
///////////////////
#if VARIANT == VARIANT_BEDROOM
#include "variants/BedRoom.h"
#endif

#if VARIANT == VARIANT_GARAGE
#include "variants/Garage.h"
#endif

#if VARIANT == VARIANT_LIVINGROOM
#include "variants/LivingRoom.h"
#endif

#if VARIANT == VARIANT_KIDROOM
#include "variants/KidRoom.h"
#endif

#if VARIANT == VARIANT_HALLWAY_FIRST_FLOOR
#include "variants/HallwayFirstFloor.h"
#endif

///////////////////
//
// COMMON
//
///////////////////
void setup ()
{
    Serial.begin (115200);
    Serial.setTimeout (5000);
    houseShell.setup ();
}

void loop ()
{
    houseShell.pulse ();
}
