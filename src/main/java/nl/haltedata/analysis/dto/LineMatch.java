package nl.haltedata.analysis.dto;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "v_line_match")
public class LineMatch {
    @Id
    Long id;
    String administrativeZone;
    String network;
    String lineNumber;
    String lineSort;
    String netexLineId;
    Long osmLineId;
    String osmName;
    String netexName;
    String transportMode;
    String netexColour;
    String osmColour;
    String productCategory;
    @OneToMany(mappedBy = "lineMatch")
    @JsonManagedReference
    private List<RouteMatch> routes = new LinkedList<>();

}
