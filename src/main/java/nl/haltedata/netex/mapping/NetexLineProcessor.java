package nl.haltedata.netex.mapping;

import org.rutebanken.netex.model.Line;
import org.rutebanken.netex.model.PresentationStructure;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import nl.haltedata.netex.dto.NetexLine;

@Component
public class NetexLineProcessor implements ItemProcessor<Line, NetexLine> {
 
    @Override
    public NetexLine process(Line line) throws Exception {
        var netexLine = new NetexLine();
        netexLine.setId(line.getId());
        netexLine.setName(line.getName() != null ? line.getName().getValue() : null);
        netexLine.setBrandingRef(getBrandingRef(line));
        netexLine.setTransportMode(getTransportMode(line));
        netexLine.setPublicCode(line.getPublicCode());
        netexLine.setPrivateCode(getPrivateCode(line));
        var presentation = line.getPresentation();
        if (presentation != null) {
            netexLine.setColour(getColour(presentation));
            netexLine.setTextColour(getTextColour(presentation));
        }
        return netexLine;       
    }
    
    private static String getBrandingRef(Line line) {
        if (line.getBrandingRef() == null) return null;
        return line.getBrandingRef().getRef();
    }

    private static String getTransportMode(Line line) {
        if (line.getTransportMode() == null) return null;
        return line.getTransportMode().value();
    }

    private static String getPrivateCode(Line line) {
        if (line.getPrivateCode() == null) return null;
        return line.getPrivateCode().getValue();
    }
    
    private static String getColour(PresentationStructure presentation) {
        // TODO Auto-generated method stub
        return null;
    }

    private static String getTextColour(PresentationStructure presentation) {
        // TODO Auto-generated method stub
        return null;
    }
}