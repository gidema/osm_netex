package nl.haltedata.chb.dto;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(schema = "chb")
public class ChbStopPlace {
    @Id
    protected String id;
    protected LocalDateTime validfrom;
    protected String stopplacecode;
    protected String stopplacetype;
    protected String publicname;
    protected String town;
    protected String publicnamemedium;
    protected String publicnamelong;
    protected String description;
    protected String stopplaceindication;
    protected String street;
    protected String stopplacestatus;
    protected LocalDateTime mutationdate;
    protected Long uiccode;
    protected String internalname;
    protected String stopplaceowner;
    protected String placecode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getValidfrom() {
        return validfrom;
    }

    public void setValidfrom(LocalDateTime validfrom) {
        this.validfrom = validfrom;
    }

    public String getStopplacecode() {
        return stopplacecode;
    }

    public void setStopplacecode(String stopplacecode) {
        this.stopplacecode = stopplacecode;
    }

    public String getStopplacetype() {
        return stopplacetype;
    }

    public void setStopplacetype(String stopplacetype) {
        this.stopplacetype = stopplacetype;
    }

    public String getPublicname() {
        return publicname;
    }

    public void setPublicname(String publicname) {
        this.publicname = publicname;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getPublicnamemedium() {
        return publicnamemedium;
    }

    public void setPublicnamemedium(String publicnamemedium) {
        this.publicnamemedium = publicnamemedium;
    }

    public String getPublicnamelong() {
        return publicnamelong;
    }

    public void setPublicnamelong(String publicnamelong) {
        this.publicnamelong = publicnamelong;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStopplaceindication() {
        return stopplaceindication;
    }

    public void setStopplaceindication(String stopplaceindication) {
        this.stopplaceindication = stopplaceindication;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStopplacestatus() {
        return stopplacestatus;
    }

    public void setStopplacestatus(String stopplacestatus) {
        this.stopplacestatus = stopplacestatus;
    }

    public LocalDateTime getMutationdate() {
        return mutationdate;
    }

    public void setMutationdate(LocalDateTime mutationdate) {
        this.mutationdate = mutationdate;
    }

    public Long getUiccode() {
        return uiccode;
    }

    public void setUiccode(Long uiccode) {
        this.uiccode = uiccode;
    }

    public String getInternalname() {
        return internalname;
    }

    public void setInternalname(String internalname) {
        this.internalname = internalname;
    }

    public String getStopplaceowner() {
        return stopplaceowner;
    }

    public void setStopplaceowner(String stopplaceowner) {
        this.stopplaceowner = stopplaceowner;
    }

    public String getPlacecode() {
        return placecode;
    }

    public void setPlacecode(String placecode) {
        this.placecode = placecode;
    }
}
