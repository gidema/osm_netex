package nl.haltedata.tools;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.CoordinateTransform;
import org.locationtech.proj4j.CoordinateTransformFactory;
import org.locationtech.proj4j.ProjCoordinate;

public class RdToWgs84Transformation {
    private CRSFactory crsFactory = new CRSFactory();
    private CoordinateReferenceSystem WGS84 = crsFactory.createFromName("epsg:4326");
    private CoordinateReferenceSystem RD = crsFactory.createFromName("epsg:28992");
    private CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
    private CoordinateTransform rdToWgs = ctFactory.createTransform(RD, WGS84);
    private GeometryFactory geoFactory = new GeometryFactory();
    
    public Point transform(Point from) {
        ProjCoordinate pc = new ProjCoordinate();
        rdToWgs.transform(pointToPc(from), pc);
        return pcToPoint(pc);

    }
    
    private static ProjCoordinate pointToPc(Point point) {
        return new ProjCoordinate(point.getX(), point.getY());
    }
    
    private Point pcToPoint(ProjCoordinate pc) {
        return geoFactory.createPoint(new Coordinate(pc.x, pc.y));
    }
}
