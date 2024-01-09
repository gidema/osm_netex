package nl.haltedata.chb.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.GregorianCalendar;



@Entity
@Table(name = "quays")
public class QuayDto {

    @Id
    private String iD;
    private String stopPlaceId;
    private Boolean onlygetout;
    private String quaycode;
    private GregorianCalendar validfrom;
    private GregorianCalendar mutationdate;

    public String getID() {
       return this.iD;
    }

    public void setID(String iD) {
       this.iD = iD;
    }

    public String getStopPlaceId() {
        return stopPlaceId;
    }

    public void setStopPlaceId(String stopPlaceId) {
        this.stopPlaceId = stopPlaceId;
    }

    public Boolean isOnlygetout() {
       return this.onlygetout;
    }

    public void setOnlygetout(Boolean onlygetout) {
       this.onlygetout = onlygetout;
    }

    public String getQuaycode() {
       return this.quaycode;
    }

    public void setQuaycode(String quaycode) {
       this.quaycode = quaycode;
    }

    public GregorianCalendar getValidfrom() {
       return this.validfrom;
    }

    public void setValidfrom(GregorianCalendar validfrom) {
       this.validfrom = validfrom;
    }

    public GregorianCalendar getMutationdate() {
       return this.mutationdate;
    }

    public void setMutationdate(GregorianCalendar mutationdate) {
       this.mutationdate = mutationdate;
    }
}