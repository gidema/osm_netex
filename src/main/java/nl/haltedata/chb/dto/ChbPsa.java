package nl.haltedata.chb.dto;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "chb")
@Getter
@Setter
@IdClass(ChbPsaId.class)
public class ChbPsa {
    @Id
    private String userStopOwnerCode;
    @Id
    private String userStopCode;
    private String quayCode;
    private String quayRef;
    private String stopplaceCode;
    private String stopplaceRef;
    private LocalDateTime userStopValidFrom;
    private LocalDateTime userStopValidThru;

}
