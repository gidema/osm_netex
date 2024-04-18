package nl.haltedata.chb.mapping;

import nl.chb.Stopplace;
import nl.haltedata.chb.dto.ChbStopPlace;

public class StopPlaceMapper implements ParentChildMapper<Stopplace, ChbStopPlace, Void> {
    
    @Override
    public ChbStopPlace map(Stopplace stopplace, Void dummy) {
        var dto = new ChbStopPlace();

        dto.setId(stopplace.getID());
        dto.setValidfrom(stopplace.getValidfrom());
        dto.setStopplacecode(stopplace.getStopplacecode());
        dto.setStopplacetype(stopplace.getStopplacetype());
        dto.setPublicname(stopplace.getStopplacename().getPublicname());
        dto.setTown(stopplace.getStopplacename().getTown());
        dto.setPublicnamemedium(stopplace.getStopplacename().getPublicnamemedium());
        dto.setPublicnamelong(stopplace.getStopplacename().getPublicnamelong());
        dto.setDescription(stopplace.getStopplacename().getDescription());
        dto.setStopplaceindication(stopplace.getStopplacename().getStopplaceindication());
        dto.setStreet(stopplace.getStopplacename().getStreet());
        dto.setStopplacestatus(stopplace.getStopplacestatusdata().getStopplacestatus());
        dto.setMutationdate(stopplace.getMutationdate());
        dto.setUiccode(stopplace.getUiccode());
        dto.setInternalname(stopplace.getInternalname());
        dto.setStopplaceowner(stopplace.getStopplaceowner().getStopplaceownercode());
        dto.setPlacecode(stopplace.getPlacecode());
        return dto;
    }
}
