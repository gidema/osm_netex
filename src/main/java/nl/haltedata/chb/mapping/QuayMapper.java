package nl.haltedata.chb.mapping;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import nl.chb.Quay;
import nl.chb.Stopplace;
import nl.haltedata.chb.dto.ChbQuay;
import nl.haltedata.tools.RdToWgs84Transformation;

public class QuayMapper implements ParentChildMapper<Quay, ChbQuay, Stopplace> {
    @Inject
    @Named("rdGeometryFactory")
    private GeometryFactory rdGeometryFactory;
    
    @Inject
    private RdToWgs84Transformation transformation;
    
    @Override
    public ChbQuay map(Quay quay, Stopplace stopPlace) {
        var chbQuay = new ChbQuay();
        chbQuay.setID(quay.getID());
        chbQuay.setStopPlaceId(stopPlace.getID());
        chbQuay.setStopPlaceName(stopPlace.getStopplacename().getPublicname());
        chbQuay.setStopPlaceLongName(stopPlace.getStopplacename().getPublicnamelong());
        chbQuay.setMutationdate(quay.getMutationdate());
        chbQuay.setOnlygetout(quay.isOnlygetout());
        chbQuay.setQuaycode(quay.getQuaycode());
        chbQuay.setValidfrom(quay.getValidfrom());
        chbQuay.setQuayType(quay.getQuaytypedata().getQuaytype());
        chbQuay.setQuayStatus(quay.getQuaystatusdata().getQuaystatus());
        int rdX = quay.getQuaylocationdata().getRdX();
        int rdY = quay.getQuaylocationdata().getRdY();
        Point rdPoint = rdGeometryFactory.createPoint(new Coordinate(rdX, rdY));
        chbQuay.setRdLocation(rdPoint);
        chbQuay.setWgsLocation(transformation.transform(rdPoint));
        chbQuay.setBearing(quay.getQuaybearing().getCompassdirection());
        chbQuay.setTown(quay.getQuaylocationdata().getTown());
        chbQuay.setLevel(quay.getQuaylocationdata().getLevel());
        chbQuay.setStreet(quay.getQuaylocationdata().getStreet());
        chbQuay.setLocation(quay.getQuaylocationdata().getLocation());  

        return chbQuay;
    }
}
