#include "U8GlibComponent.h"

///////////////////
//
// Component
//
///////////////////
U8GlibComponent::U8GlibComponent (U8GLIB* u8glib, uint16_t rotation, char* name, bool report) :
    button (u8glib), Component (name, report)
{
    this->u8glib = u8glib;
    this->rotation = rotation;
}

void U8GlibComponent::setup ()
{
    switch (rotation)
    {
        case 90:
            u8glib->setRot90 ();
            break;

        case 180:
            u8glib->setRot180 ();
            break;

        case 270:
            u8glib->setRot270 ();
            break;
    }
}

///////////////////
//
// API
//
///////////////////
void U8GlibComponent::write ()
{
}

///////////////////
//
// Button
//
///////////////////

Button::Button (U8GLIB* u8glib)
{
    this->u8glib = u8glib;
}

void Button::initialize (u8g_uint_t xOffset, u8g_uint_t yOffset, u8g_uint_t width, u8g_uint_t height, u8g_uint_t radius, bool inverted)
{
    initialize (xOffset, yOffset, radius, inverted);
    finish (width, height);
}

void Button::initialize (u8g_uint_t xOffset, u8g_uint_t yOffset, u8g_uint_t radius, bool inverted)
{
    this->xOffset = xOffset + 2;
    this->yOffset = yOffset + 2;
    this->radius = radius;
    this->inverted = inverted;
}

void Button::finish (u8g_uint_t width, u8g_uint_t height)
{
    this->height = height + 4;
    this->width = width + 4;

    if (inverted)
    {
        u8glib->setColorIndex (1);
        u8glib->drawRBox (xOffset - 2, yOffset - 2, this->width, this->height, radius);
        u8glib->setColorIndex (0);
    }
    else
    {
        u8glib->setColorIndex (1);
        u8glib->drawRFrame (xOffset - 2, yOffset - 2, width + 4, height + 4, radius);
    }
}
