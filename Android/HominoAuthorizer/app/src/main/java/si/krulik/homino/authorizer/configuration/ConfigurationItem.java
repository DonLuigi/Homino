package si.krulik.homino.authorizer.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;


@AllArgsConstructor public class ConfigurationItem
{
    @Getter private String authorization, ssid, hostname;


    @Getter int port;


    @Override public String toString ()
    {
        return ("Authorization: " + authorization + System.lineSeparator () + "SSID: " + ssid + System.lineSeparator () + "Network address: " + hostname + ":" + port);
    }


    public String toCsv ()
    {
        return (authorization + "," + ssid + "," + hostname + "," + port);
    }


    static public List<String> toString (Set<ConfigurationItem> configurationItems)
    {
        List<String> itemsAsString = new ArrayList ();
        for (ConfigurationItem configurationItem : configurationItems)
        {
            itemsAsString.add (configurationItem.toString ());
        }

        return (itemsAsString);
    }


    static public ConfigurationItem newFromCsv (String csv)
    {
        String[] split = csv.split (",");
        if (split.length != 4)
        {
            throw new IllegalArgumentException ("Invalid csv format: " + csv);
        }

        return (new ConfigurationItem (split[0], split[1], split[2], Integer.parseInt (split[3])));
    }
}
