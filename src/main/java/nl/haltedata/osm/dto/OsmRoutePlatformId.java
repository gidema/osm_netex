package nl.haltedata.osm.dto;

import java.io.Serializable;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class OsmRoutePlatformId implements Serializable {
        private static final long serialVersionUID = 1L;

        private Long osmRouteId;

        private Long platformIndex;

        public OsmRoutePlatformId() {
            // No Arg constructor to prevent hibernate exception
        }

        public OsmRoutePlatformId(Long osmRouteId, Long platformIndex) {
            super();
            this.osmRouteId = osmRouteId;
            this.platformIndex = platformIndex;
        }
    }