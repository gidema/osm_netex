package nl.haltedata.gtfs.dto;

import java.time.LocalTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(schema = "chb")
public class GtfsStopTime {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long stopTimeId;
    Long tripId;
    Integer stopSequence;

    Long stopId;
    String stopHeadsign;
    String arrival_time; // Use String as trips can run in to the next day (hour = 24+)
    String departure_time; // Use String as trips can run in to the next day (hour = 24+)
    Integer pickup_type;
    Integer drop_off_type;
    Integer timepoint;
    Double shape_dist_traveled;
    Double fare_units_traveled;

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public Integer getStopSequence() {
        return stopSequence;
    }

    public void setStopSequence(Integer stopSequence) {
        this.stopSequence = stopSequence;
    }

    public Long getStopTimeId() {
        return stopTimeId;
    }

    public Long getStopId() {
        return stopId;
    }

    public void setStopId(Long stopId) {
        this.stopId = stopId;
    }

    public String getStopHeadsign() {
        return stopHeadsign;
    }

    public void setStopHeadsign(String stopHeadsign) {
        this.stopHeadsign = stopHeadsign;
    }

    public String getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(String arrival_time) {
        this.arrival_time = arrival_time;
    }

    public String getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(String departure_time) {
        this.departure_time = departure_time;
    }

    public Integer getPickup_type() {
        return pickup_type;
    }

    public void setPickup_type(Integer pickup_type) {
        this.pickup_type = pickup_type;
    }

    public Integer getDrop_off_type() {
        return drop_off_type;
    }

    public void setDrop_off_type(Integer drop_off_type) {
        this.drop_off_type = drop_off_type;
    }

    public Integer getTimepoint() {
        return timepoint;
    }

    public void setTimepoint(Integer timepoint) {
        this.timepoint = timepoint;
    }

    public Double getShape_dist_traveled() {
        return shape_dist_traveled;
    }

    public void setShape_dist_traveled(Double shape_dist_traveled) {
        this.shape_dist_traveled = shape_dist_traveled;
    }

    public Double getFare_units_traveled() {
        return fare_units_traveled;
    }

    public void setFare_units_traveled(Double fare_units_traveled) {
        this.fare_units_traveled = fare_units_traveled;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stopSequence, tripId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GtfsStopTime other = (GtfsStopTime) obj;
        return Objects.equals(stopSequence, other.stopSequence) && Objects.equals(tripId, other.tripId);
    }

}
