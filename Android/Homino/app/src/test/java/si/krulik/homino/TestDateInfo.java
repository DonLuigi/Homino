package si.krulik.homino;

import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import si.krulik.homino.common.time.DateInfo;
import si.krulik.homino.configuration.Configuration;

public class TestDateInfo
{
    Configuration configuration;


    @Before
    public void setup ()
    {
    }


    @Test
    public void test () throws Exception
    {
        Date date = new SimpleDateFormat ("dd.MM.yyyy HH:mm:ss").parse ("13.10.2000 13:12:11");
        DateInfo dateInfo = new DateInfo (date);
        int i = 0;
    }
}
