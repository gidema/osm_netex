package nl.haltedata.chb.mapping;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import nl.chb.Quay;
import nl.chb.Stopplace;
import nl.haltedata.chb.dto.QuayDto;

public class QuayMapper implements DTOMapper<Quay, Stopplace, QuayDto> {
    // TODO Inject
    private GeometryFactory geometryFactory = new GeometryFactory();
    private RdToWgs84Transformation transformation = new RdToWgs84Transformation();
    
    @Override
    public QuayDto map(Quay quay, Stopplace stopPlace) {
        var quayDto = new QuayDto();
        quayDto.setID(quay.getID());
        quayDto.setStopPlaceId(stopPlace.getID());
        quayDto.setStopPlaceName(stopPlace.getStopplacename().getPublicname());
        quayDto.setStopPlaceLongName(stopPlace.getStopplacename().getPublicnamelong());
        quayDto.setMutationdate(quay.getMutationdate());
        quayDto.setOnlygetout(quay.isOnlygetout());
        quayDto.setQuaycode(quay.getQuaycode());
        quayDto.setValidfrom(quay.getValidfrom());
        quayDto.setQuayType(quay.getQuaytypedata().getQuaytype());
        quayDto.setQuayStatus(quay.getQuaystatusdata().getQuaystatus());
        int rdX = quay.getQuaylocationdata().getRdX();
        int rdY = quay.getQuaylocationdata().getRdY();
        Point rdPoint = geometryFactory.createPoint(new Coordinate(rdX, rdY));
        quayDto.setCoordinates(transformation.transform(rdPoint));
        quayDto.setBearing(quay.getQuaybearing().getCompassdirection());
        quayDto.setTown(quay.getQuaylocationdata().getTown());
        quayDto.setLevel(quay.getQuaylocationdata().getLevel());
        quayDto.setStreet(quay.getQuaylocationdata().getStreet());
        quayDto.setLocation(quay.getQuaylocationdata().getLocation());  

        return quayDto;
    }
}
