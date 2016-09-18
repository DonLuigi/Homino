#include "ShutterComponent.h"

///////////////////
//
// Component
//
///////////////////
ShutterComponent::ShutterComponent (MotionRangeComponent* heightMotionRange, MotionRangeComponent* rotationMotionRange, ButtonComponent* upButton, ButtonComponent* downButton, const char* name,
    uint32_t reportMillis) :
    Component (name, reportMillis)
{
    this->heightMotionRange = heightMotionRange;
    this->rotationMotionRange = rotationMotionRange;
    this->upButton = upButton;
    this->downButton = downButton;
    rotationMotionRangePercentQueue = -1;
    lastReportMillis = 0;
}

void ShutterComponent::setup ()
{
    if (heightMotionRange != NULL)
    {
        heightMotionRange->setup ();
    }

    if (rotationMotionRange != NULL)
    {
        rotationMotionRange->setup ();
    }

    if (upButton != NULL)
    {
        upButton->setup ();
    }

    if (downButton != NULL)
    {
        downButton->setup ();
    }
}

int ShutterComponent::readFromComponent (Message* message)
{
    // periodic
    uint32_t now = millis ();
    int8_t currentPositionPercent = -1;
    if (heightMotionRange != NULL)
    {
        heightMotionRange->pulse (now, false);
        heightMotionRange->getStatus (NULL, &currentPositionPercent);

    }
    if (rotationMotionRange != NULL)
    {
        rotationMotionRange->pulse (now, false);
    }

    if (rotationMotionRangePercentQueue >= 0 && !heightMotionRange->isMoving ())
    {
        // rotate only if not fully raised; it makes no sense to rotate fully raised shutter
        if (currentPositionPercent != 0)
        {
            rotationMotionRange->move (rotationMotionRangePercentQueue);
        }
        rotationMotionRangePercentQueue = -1;
    }

    bool moving = isMoving ();
    if (upButton != NULL)
    {
        if (upButton->onPress ())
        {
            if (moving)
            {
                COA_DEBUG (F("SHT[%s]:UP PRESS:STOP"), name);
                stop ();
            }
            else
            {
                COA_DEBUG (F("SHT[%s]:UP PRESS:MV 0,0"), name);
                move (0, 0);
            }
        }

        uint32_t releaseTime = upButton->onRelease ();
        if (releaseTime > 0 && releaseTime < 1000 && moving && rotationMotionRange != NULL)
        {
            COA_DEBUG (F("SHT[%s]:UP RELEASE:STOP"), name);
            stop ();
        }
    }

    if (downButton != NULL)
    {
        if (downButton->onPress ())
        {
            if (moving)
            {
                COA_DEBUG (F("SHT[%s]:DOWN PRESS:STOP"), name);
                stop ();
            }
            else
            {
                COA_DEBUG (F("SHT[%s]:DOWN PRESS:MV 100,100"), name);
                move (100, 100);
            }
        }

        uint32_t releaseTime = downButton->onRelease ();
        if (releaseTime > 0 && releaseTime < 1500 && moving && rotationMotionRange != NULL)
        {
            COA_DEBUG (F("SHT[%s]:DOWN RELEASE:STOP"), name);
            stop ();
        }
    }

    // reporting
    appendStatus (message, now);

    return (Component::ALL_SUBCOMPONENTS);
}

void ShutterComponent::writeToComponent (Command* command, Message* message, int subcomponent)
{
    char** parts = command->getParts ();

    if (!strcasecmp (parts[0], "MV"))
    {
        int8_t heightPercent = -1;
        int8_t rotationPercent = -1;

        if (parts[1] != NULL)
        {
            heightPercent = atoi (parts[1]);
        }

        if (parts[2] != NULL)
        {
            rotationPercent = atoi (parts[2]);
        }

        move (heightPercent, rotationPercent);
    }

    if (!strcasecmp (parts[0], "STATUS"))
    {
        appendStatus (message, 0);
    }

    if (!strcasecmp (parts[0], "STOP"))
    {
        stop ();
    }
}

uint8_t ShutterComponent::toString (char* buffer, uint8_t size, uint8_t customFontOffset)
{
    if (size > 0)
    {
        int8_t percent;
        heightMotionRange->getStatus (NULL, &percent);
        buffer[0] = customFontOffset + percent / 21;
        return (1);
    }
    else
    {
        return (0);
    }
}

///////////////////
//
// API
//
///////////////////
void ShutterComponent::move (int8_t heightPercent, int8_t rotationPercent)
{
    COA_DEBUG (F("SHT[%s]:MV:%d:%d"), name, heightPercent, rotationPercent);

    // vertical motion
    if (heightMotionRange != NULL && heightPercent >= 0)
    {
        heightMotionRange->move (heightPercent);

        if (rotationMotionRange != NULL)
        {
            // examine current rotation state and put it into queue; this way rotation is kept between vertical-only movement
            int8_t currentPositionPercent;
            rotationMotionRange->getStatus (NULL, &currentPositionPercent);
            //rotationMotionRangePercentQueue = currentPositionPercent;

            // TODO

// stop rotation, if any
            rotationMotionRange->stop ();

            // direction of vertical travel determines starting point of rotation, once vertical travel stops, so override current position accordingly
            int8_t verticalDirection;
            heightMotionRange->getStatus (&verticalDirection, NULL);
            COA_DEBUG (F("SHT[%s]:VERT:DIR,%d"), name, verticalDirection);

            if (verticalDirection < 0)
            {
                rotationMotionRange->overrideCurrentPosition (0);
            }
            if (verticalDirection > 0)
            {
                rotationMotionRange->overrideCurrentPosition (100);
            }
        }
    }

    // rotation motion
    if (rotationMotionRange != NULL && rotationPercent >= 0)
    {
        int8_t currentPositionPercent = -1;
        if (heightMotionRange != NULL)
        {
            heightMotionRange->getStatus (NULL, &currentPositionPercent);
        }

        if (heightMotionRange == NULL || !heightMotionRange->isMoving ())
        {
            // rotate only if not fully raised; it makes no sense to rotate fully raised shutter
            if (currentPositionPercent != 0)
            {
                rotationMotionRange->move (rotationPercent);
            }
        }
        else
        {
            // queue rotation
            rotationMotionRangePercentQueue = rotationPercent;
        }
    }
}

bool ShutterComponent::isMoving ()
{
    return (((heightMotionRange != NULL && heightMotionRange->isMoving ()) || (rotationMotionRange != NULL && rotationMotionRange->isMoving ())));
}

void ShutterComponent::stop ()
{
    COA_DEBUG (F("SHT[%s]:STOP"), name);
    heightMotionRange->stop ();
    if (rotationMotionRange != NULL)
    {
        rotationMotionRange->stop ();
        rotationMotionRangePercentQueue = -1;
    }
}

void ShutterComponent::appendStatus (Message* message, uint32_t now)
{
// reporting
    bool isMoving = (heightMotionRange != NULL && heightMotionRange->isMoving ()) || (rotationMotionRange != NULL && rotationMotionRange->isMoving ());
    if (isMoving /* is moving */|| lastReportMillis > 0 /* was moving */|| now == 0 /* force immediate report */)
    {
        if (now == 0 /* report immediately */|| (!isMoving && lastReportMillis > 0) /* was moving -> report immediately */
        || (now - lastReportMillis > reportMillis) /* is moving -> report once in a while */)
        {
            // compose message
            message->append ("%s,STATUS", name);
            if (heightMotionRange != NULL)
            {
                int8_t direction, positionPercent;
                heightMotionRange->getStatus (&direction, &positionPercent);
                message->append (",%s,%d", direction == -1 ? "UP" : (direction == 1 ? "DOWN" : "STOP"), positionPercent);
            }
            if (rotationMotionRange != NULL)
            {
                int8_t direction, positionPercent;
                rotationMotionRange->getStatus (&direction, &positionPercent);
                message->append (",%d", positionPercent);
            }
            message->append (";");

            // mark last report time
            lastReportMillis = (now > 0 ? now : lastReportMillis);
        }

        if (!isMoving)
        {
            lastReportMillis = 0;
        }
    }
}