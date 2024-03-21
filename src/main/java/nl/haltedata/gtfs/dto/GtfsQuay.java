package nl.haltedata.gtfs.dto;

import org.locationtech.jts.geom.Point;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity()
@Table(schema = "chb")
public class GtfsQuay {
    private Long quayId;
    private String quayCode;
    private String refIfopt;
    private String place;
    private String name;
    private String operator;
    private Point coordinates;

    private Long stopAreaId = null;
    private Boolean wheelchairBoarding;

    @Id
    public Long getQuayId() {
        return quayId;
    }

    public void setQuayId(Long quayId) {
        this.quayId = quayId;
    }

    public String getQuayCode() {
        return quayCode;
    }

    public void setQuayCode(String quayCode) {
        this.quayCode = quayCode;
    }

    public String getRefIfopt() {
        return refIfopt;
    }

    public void setRefIfopt(String refIfopt) {
        this.refIfopt = refIfopt;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Point getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Point coordinates) {
        this.coordinates = coordinates;
    }

    public Long getStopAreaId() {
        return stopAreaId;
    }

    public void setStopAreaId(Long stopAreaId) {
        this.stopAreaId = stopAreaId;
    }

    public Boolean getWheelchairBoarding() {
        return wheelchairBoarding;
    }

    public void setWheelchairBoarding(Boolean wheelchairBoarding) {
        this.wheelchairBoarding = wheelchairBoarding;
    }
}
