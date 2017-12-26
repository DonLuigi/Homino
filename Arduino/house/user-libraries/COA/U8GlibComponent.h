#ifndef U8GLIB_COMPONENT_H
#define U8GLIB_COMPONENT_H

#include <Arduino.h>
#include <Component.h>
#include <U8glib.h>

class Button
{
    public:
        Button (U8GLIB* u8glib);
        void initialize (u8g_uint_t xOffset, u8g_uint_t yOffset, u8g_uint_t width, u8g_uint_t height, u8g_uint_t round, bool inverted);
        void initialize (u8g_uint_t xOffset, u8g_uint_t yOffset, u8g_uint_t radius, bool inverted);
        void finish (u8g_uint_t width, u8g_uint_t height);

        U8GLIB* u8glib;
        u8g_uint_t width;
        u8g_uint_t height;

        ///////////////////
        //
        // u8glib API
        //
        ///////////////////
        void drawFrame (u8g_uint_t x, u8g_uint_t y, u8g_uint_t w, u8g_uint_t h)
        {
            u8glib->drawFrame (x + xOffset, y + yOffset, w, h);
        }

        u8g_uint_t drawStr (u8g_uint_t x, u8g_uint_t y, const char *s)
        {
            return (u8glib->drawStr (x + xOffset, y + yOffset, s));
        }

        void drawRFrame (u8g_uint_t x, u8g_uint_t y, u8g_uint_t w, u8g_uint_t h, u8g_uint_t r)
        {
            u8glib->drawRFrame (x + xOffset, y + yOffset, w, h, r);
        }

        void drawBitmapP (u8g_uint_t x, u8g_uint_t y, u8g_uint_t cnt, u8g_uint_t h, const u8g_pgm_uint8_t *bitmap)
        {
            u8glib->drawBitmapP (x + xOffset, y + yOffset, cnt, h, bitmap);
        }

        void drawHLine (u8g_uint_t x, u8g_uint_t y, u8g_uint_t w)
        {
            u8glib->drawHLine (x + xOffset, y + yOffset, w);
        }

        void drawLine (u8g_uint_t x1, u8g_uint_t y1, u8g_uint_t x2, u8g_uint_t y2)
        {
            u8glib->drawLine (x1 + xOffset, y1 + yOffset, x2 + xOffset, y2 + yOffset);
        }

    private:
        u8g_uint_t xOffset;
        u8g_uint_t yOffset;
        u8g_uint_t radius;
        bool inverted;
};

class U8GlibComponent: public Component
{
    public:
        // component
        U8GlibComponent (U8GLIB* u8glib, uint16_t rotation, char* name = NULL, bool report = false);
        Button button;
        void setup ();

        // api
        void write ();

        U8GLIB* u8glib;

    private:
        uint16_t rotation;
};

#endif U8GLIB_COMPONENT_H
