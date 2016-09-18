#include <OneWire.h>
#include "Shell.h"
#include "PinComponent.h"
#include "EEPROMValueComponent.h"
#include "DallasTemperatureComponent.h"
#include "LiquidCrystalI2CComponent.h"
#include "KeypadComponent.h"
#include "ApplicationComponent.h"

#define ENABLE_SERIAL 1

// eeprom
EEPROMValueComponent pCrpanjeZalogovnika = EEPROMValueComponent(0, 1,
		"P crpanje zalog.");
EEPROMValueComponent pRazlikaZaVklopZalogovnika = EEPROMValueComponent(1, 1,
		"P raz. vklop zalog.");
EEPROMValueComponent pVklopBojlerja = EEPROMValueComponent(2, 1,
		"P vklop bojlerja");
EEPROMValueComponent pIzklopBojlerja = EEPROMValueComponent(3, 1,
		"P izklop bojlerja");
EEPROMValueComponent pRazlikaZaVklopCrpalkeBojlerja = EEPROMValueComponent(4, 1,
		"P raz. vklop bo.");
EEPROMValueComponent pTemperaturnaHistereza = EEPROMValueComponent(5, 1,
		"P temp. hister.");

// thermometers
OneWire oneWireTZalogovnikZgoraj(10);
DallasTemperatureComponent tZalogovnikZgoraj(&oneWireTZalogovnikZgoraj, "Tzz");

OneWire oneWireTZalogovnikSpodaj(11);
DallasTemperatureComponent tZalogovnikSpodaj(&oneWireTZalogovnikSpodaj, "Tzs");

OneWire oneWireTDrva(12);
DallasTemperatureComponent tDrva(&oneWireTDrva, "Tdr");

OneWire oneWireTBojer(13);
DallasTemperatureComponent tBojer(&oneWireTBojer, "Tbl");

// relays
PinComponent rOljniGorilec(A0, OUTPUT, LOW, "R oljni gorilec");
PinComponent rPecAliZalogovnik(A1, OUTPUT, LOW, "R pec/zalogovnik");
PinComponent rDrvaAliOlje(A2, OUTPUT, LOW, "R drva/olje");
PinComponent rCrpalkaZalogovnik(A3, OUTPUT, LOW, "R crpalka zalog.");
PinComponent rCrpalkaBojler(3, OUTPUT, LOW, "R crpalka bojler");

// buzzer
PinComponent buzzer(2, OUTPUT, 0, "buzzer");

// lcd
LiquidCrystalI2CComponent lcd(0x27, 16, 2, 20 * 1000L, 500L, 100, "lcd");

// keypad
char keys[] = { '7', '8', '9', 'C', '*', '0', '#', 'D' };
byte rowPins[] = { 4, 5 }; //connect to the row pin-outs of the keypad
byte colPins[] = { 6, 7, 8, 9 }; //connect to the column pin-outs of the keypad
KeypadComponent keypad(keys, rowPins, colPins, 2, 4, 500, "keypad");

// application
ApplicationComponent application("application", &tZalogovnikZgoraj,
		&tZalogovnikSpodaj, &tDrva, &tBojer, &rOljniGorilec, &rPecAliZalogovnik,
		&rDrvaAliOlje, &rCrpalkaZalogovnik, &rCrpalkaBojler,
		&pCrpanjeZalogovnika, &pRazlikaZaVklopZalogovnika, &pVklopBojlerja,
		&pIzklopBojlerja, &pRazlikaZaVklopCrpalkeBojlerja,
		&pTemperaturnaHistereza, &lcd, &keypad, &buzzer);

// shell
Component* components[] = { /*&tZalogovnikZgoraj, &tZalogovnikSpodaj, &tDrva, &tBojer, &rOljniGorilec, &rPecAliZalogovnik, &rDrvaAliOlje, &rCrpalkaZalogovnik, &rCrpalkaBojler, */
		&lcd, &keypad, &buzzer, &application, NULL };
Shell shell(components, NULL, NULL, NULL, 100, "shell");

void setup() {
#if ENABLE_SERIAL
	Serial.begin(115200);
	Serial.setTimeout(5000);
#endif
	shell.setup();
}

void loop() {
	shell.loop();
}
