package nl.haltedata.analysis.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import nl.haltedata.netex.dto.NetexNetwork;
import nl.haltedata.osm.dto.OsmNetwork;

@Entity
@Getter
@Setter
public class NetworkMatch {
    @Id
    String id;
    String administrativeZone;
    String name;
    String shortName;
    @OneToOne
    @JoinColumn(name="osm_id", unique=true, nullable=true, updatable=false)
    OsmNetwork osmNetwork;
    @OneToOne
    @JoinColumn(name="netex_id", unique=true, nullable=true, updatable=false)
    NetexNetwork netexNetwork;
}
