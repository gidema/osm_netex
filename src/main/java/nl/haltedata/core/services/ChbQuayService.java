package nl.haltedata.core.services;

import java.util.Set;

import nl.haltedata.chb.dto.DimChbQuay;

public interface ChbQuayService {

    void fetchMissingCodes(Set<String> quayCodes);

    DimChbQuay getQuay(String quayCode);

}
