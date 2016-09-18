#ifndef HYSTERESIS_H
#define HYSTERESIS_H

class Hysteresis
{
    public:
        Hysteresis ();
        Hysteresis (float tolerance);
        enum Result { LESS=-1, EQUAL=0, MORE=1,UNDEFINED=2 };
        int compare (float a, float b);
        void setTolerance (float tolerance);

    private:
        int previousAnswer;
        float tolerance;
};

#endif HYSTERESIS_H
