package nl.haltedata.netex.osm;

import java.sql.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.openstreetmap.osmosis.core.domain.v0_6.CommonEntityData;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.OsmUser;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;

import jakarta.inject.Inject;
import nl.haltedata.chb.dto.ChbPsa;
import nl.haltedata.chb.dto.ChbPsaId;
import nl.haltedata.chb.dto.ChbPsaRepository;
import nl.haltedata.chb.dto.ChbQuay;
import nl.haltedata.chb.dto.ChbQuayRepository;
import nl.haltedata.netex.dto.NetexQuay;
import nl.haltedata.osm.OsmIdFactory;
import nl.haltedata.osm.OsmNodeFactory;

public class QuayNodeFactory implements OsmNodeFactory<NetexQuay> {
    @Inject
    private ChbQuayRepository chbQuayRepository;
    @Inject
    private ChbPsaRepository chbPsaRepository;
    private Map<String, ChbQuay> quayMap = new HashMap<>();
    private Map<ChbPsaId, ChbPsa> psaMap = new HashMap<>();

    public QuayNodeFactory() {
        super();
    }
    
    private Map<String, ChbQuay> getQuayMap() {
        if (quayMap.isEmpty()) {
            chbQuayRepository.findAll().forEach(quay -> quayMap.put(quay.getQuaycode(), quay));
        }
        return quayMap;
    }
    private Map<ChbPsaId, ChbPsa> getPsaMap() {
        if (psaMap.isEmpty()) {
            chbPsaRepository.findAll().forEach(psa -> 
                psaMap.put(new ChbPsaId(psa.getUserStopOwnerCode(), psa.getUserStopCode()), psa));
        }
        return psaMap;
    }

    @Override
    public Node create(NetexQuay quay, Date timestamp) {
        List<Tag> tags = new LinkedList<>();
        tags.add(new Tag("netex:id", quay.getId()));
//        tags.add(new Tag("gtfs:code", quay.getQuayCode()));
        tags.add(new Tag("highway", "bus_stop"));
        tags.add(new Tag("public_transport", "platform"));
        tags.add(new Tag("name", quay.getName()));
        ChbPsaId psaId = new ChbPsaId(quay.getUserStopOwnerCode(), quay.getUserStopCode());
        ChbPsa psa = getPsaMap().get(psaId);
        if (psa != null) {
            tags.add(new Tag("ref:IFOPT", psa.getQuayCode()));
            ChbQuay chbQuay = getQuayMap().get(psa.getQuayCode());
            if (chbQuay != null && chbQuay.getStopSideCode() != null) {
                tags.add(new Tag("ref", chbQuay.getStopSideCode()));
            }
        }
//        tags.add(new Tag("operator", quay.getOperator()));
//        if (quay.getWheelchairBoarding() != null) {
//            tags.add(new Tag("wheelchair", quay.getWheelchairBoarding() ? "yes" : "no"));            
//        }
        var nodeData = new CommonEntityData(OsmIdFactory.getNodeId(), 0, timestamp, OsmUser.NONE, 0L, tags);
        return new Node(nodeData, quay.getWgsLocation().getY(), quay.getWgsLocation().getX());
    }
}
