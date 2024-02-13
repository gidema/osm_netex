package nl.haltedata.chb.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "quay", schema = "chb")
public class QuayDto {

    @Id
    private String iD;
    private String stopPlaceId;
    private String stopPlaceName;
    private String stopPlaceLongName;
    private Boolean onlygetout;
    private String quaycode;
    private LocalDateTime validfrom;
    private LocalDateTime mutationdate;
    private String quayType;
    private String quayStatus;
    private Point coordinates;
    private Integer bearing;
    private String town;
    private String level;
    private String street;
    private String location;  

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

    public String getStopPlaceName() {
        return stopPlaceName;
    }

    public void setStopPlaceName(String stopPlaceName) {
        this.stopPlaceName = stopPlaceName;
    }

    public String getStopPlaceLongName() {
        return stopPlaceLongName;
    }

    public void setStopPlaceLongName(String stopPlaceLongName) {
        this.stopPlaceLongName = stopPlaceLongName;
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

    public LocalDateTime getValidfrom() {
       return this.validfrom;
    }

    public void setValidfrom(LocalDateTime validfrom) {
       this.validfrom = validfrom;
    }

    public LocalDateTime getMutationdate() {
       return this.mutationdate;
    }

    public void setMutationdate(LocalDateTime mutationdate) {
       this.mutationdate = mutationdate;
    }

    public String getQuayType() {
        return quayType;
    }

    public void setQuayType(String quayType) {
        this.quayType = quayType;
    }

    public String getQuayStatus() {
        return quayStatus;
    }

    public void setQuayStatus(String quayStatus) {
        this.quayStatus = quayStatus;
    }

    public Point getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Point coordinates) {
        this.coordinates = coordinates;
    }

    public Integer getBearing() {
        return bearing;
    }

    public void setBearing(Integer bearing) {
        this.bearing = bearing;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getOnlygetout() {
        return onlygetout;
    }
}