package si.krulik.homino.configuration.plate.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import si.krulik.homino.common.logger.CustomLogger;
import si.krulik.homino.common.validate.Validate;


public class PlatesAndPages
{
    public PlatesAndPages (PlatePage[] platePagesArray, Plate[] platesArray, PlatePosition[] platePositionsArray)
    {
        // plate pages
        logger.info ("Constructing plates and pages");
        for (PlatePage platePage : platePagesArray)
        {
            logger.fine ("Indexing: ", platePage);
            platePages.add (platePage);
            platePagesById.put (platePage.getId (), platePage);
        }


        // plates
        for (Plate plate : platesArray)
        {
            logger.fine ("Indexing: ", plate);
            platesById.put (plate.id, plate);
        }


        // plate positions
        for (PlatePosition platePosition : platePositionsArray)
        {
            logger.fine ("Linking plate ", platePosition.getPlateId (), " to plate page ", platePosition.getPlatePageId ());

            Plate plate = platesById.get (platePosition.getPlateId ());
            Validate.notNull (plate, "Missing plate on position ", platePosition);

            PlatePage platePage = platePagesById.get (platePosition.getPlatePageId ());
            Validate.notNull (platePage, "Missing plate page on position ", platePosition);

            plate.setPlatePosition (platePosition);
            platePage.addPlate (plate);
        }


        logger.info ("Done, plate pages size ", platePages.size (), ", plates size ", getPlates ().size ());
    }


    // plate pages
    @Getter private List<PlatePage> platePages = new ArrayList ();


    @Getter private Map<String, PlatePage> platePagesById = new HashMap ();


    public PlatePage getPlatePage (String id)
    {
        return (platePagesById.get (id));
    }


    // plates
    @Getter private Map<String, Plate> platesById = new HashMap ();


    public Plate getPlate (String id)
    {
        return (platesById.get (id));
    }


    public Collection<Plate> getPlates ()
    {
        return (platesById.values ());
    }


    static final CustomLogger logger = CustomLogger.getLogger ("PLATES&PAGES");
}
