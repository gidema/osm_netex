package nl.haltedata.chb.dto;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class ChbPsaId implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userStopOwnerCode;
    private String userStopCode;

    public ChbPsaId() {
        // No Arg constructor to prevent hibernate exception
    }
    
    public ChbPsaId(String userStopOwnerCode, String userStopCode) {
        super();
        this.userStopOwnerCode = userStopOwnerCode;
        this.userStopCode = userStopCode;
    }
    
    
}