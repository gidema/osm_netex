package nl.haltedata.osm.dto;

import java.io.Serializable;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class OsmRouteQuayId implements Serializable {
        private static final long serialVersionUID = 1L;

        private Long osmRouteId;

        private Long quayIndex;

        public OsmRouteQuayId() {
            // No Arg constructor to prevent hibernate exception
        }

        public OsmRouteQuayId(Long osmRouteId, Long quayIndex) {
            super();
            this.osmRouteId = osmRouteId;
            this.quayIndex = quayIndex;
        }
    }