package nl.haltedata.netex.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "v_netex_quay_in_sequence")
@Getter
@Setter
@IdClass(NetexQuayInSequenceId.class)
public class NetexQuayInSequence {
    @Id
    private Long sequenceId;
    @Id
    private Long quayIndex;
    private String lineNumber;
    private String quayCode;
    private String quayName;
    private String stopSideCode;
    private String stopPlaceCode;
    private String quayLocationType;
}
