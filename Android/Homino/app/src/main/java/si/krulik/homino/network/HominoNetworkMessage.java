package si.krulik.homino.network;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;


@AllArgsConstructor @ToString (includeFieldNames = true) public class HominoNetworkMessage implements Serializable
{
    @Getter private String controlNodeIpPort;


    @Getter private String message;


    @Getter private boolean error;


    static public final String communicationRequestIntentAction = HominoNetworkMessage.class.getName () + "Broadcast";
}
