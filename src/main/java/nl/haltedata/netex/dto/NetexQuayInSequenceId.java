package nl.haltedata.netex.dto;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class NetexQuayInSequenceId implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long sequenceId;
    private Long quayIndex;

    public NetexQuayInSequenceId() {
        // No Arg constructor to prevent hibernate exception
    }

    public NetexQuayInSequenceId(Long sequenceId, Long quayIndex) {
        super();
        this.sequenceId = sequenceId;
        this.quayIndex = quayIndex;
    }
}