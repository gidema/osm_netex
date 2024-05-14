package nl.haltedata.chb.mapping;

import nl.chb.psa.Quay;
import nl.chb.psa.Userstopcodedata;
import nl.haltedata.chb.dto.ChbPsa;
import nl.haltedata.tools.ParentChildMapper;

public class ChbPsaMapper implements ParentChildMapper<Userstopcodedata, ChbPsa, Quay> {
    
    @Override
    public ChbPsa map(Userstopcodedata stopcode, Quay quay) {
        var psa = new ChbPsa();
        psa.setUserStopOwnerCode(stopcode.getDataownercode());
        psa.setUserStopCode(stopcode.getUserstopcode());
        psa.setUserStopValidFrom(stopcode.getValidfrom());
        psa.setUserStopValidThru(stopcode.getValidthru());
        psa.setQuayCode(quay.getQuaycode());
        psa.setQuayRef(quay.getQuayref());
        psa.setStopplaceCode(quay.getStopplacecode());
        psa.setStopplaceRef(quay.getStopplaceref());
        return psa;
    }
}
