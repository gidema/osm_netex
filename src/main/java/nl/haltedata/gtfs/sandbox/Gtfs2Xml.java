package nl.haltedata.gtfs.sandbox;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import org.openstreetmap.osmosis.core.container.v0_6.NodeContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.CommonEntityData;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.OsmUser;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.xml.v0_6.XmlWriter;

import nl.haltedata.gtfs.QuayReader;
import nl.haltedata.gtfs.dto.GtfsQuay;
/**
 * Quick and dirty script to create an osm xml file with chb quays(platforms)
 * This version does't add operators (De Lijn)
 * 
 */
public class Gtfs2Xml {
    final private static String SOURCE="/home/gertjan/Downloads/stops.txt";
    final private static String target="/home/gertjan/Downloads/gtfs_quays.osm";
 
    AtomicLong id;
    java.sql.Date timestamp;

    public static void main(String ... args) {
        Gtfs2Xml gtfs2Xml = new Gtfs2Xml();
        gtfs2Xml.read();
    }
    
    private void read() {
        QuayReader reader = new QuayReader(new HashMap<>());
        try (
            BufferedWriter witer = new BufferedWriter(new FileWriter(target));
            XmlWriter xmlWriter = new XmlWriter(witer);         
        ) {
            File source = new File(SOURCE);
            id = new AtomicLong(-1);
            timestamp = new java.sql.Date(System.currentTimeMillis());
            var quayWriter = new QuayWriter(xmlWriter);
            reader.read(source, quayWriter);
            xmlWriter.complete();
            xmlWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }   
    }

    private class QuayWriter implements Consumer<GtfsQuay> {
        private final XmlWriter xmlWriter;

        public QuayWriter(XmlWriter xmlWriter) {
            super();
            this.xmlWriter = xmlWriter;
        }

        @Override
        public void accept(GtfsQuay quay) {
            List<Tag> tags = new LinkedList<>();
            tags.add(new Tag("gtfs:id", quay.getQuayId().toString()));
            tags.add(new Tag("gtfs:code", quay.getQuayCode()));
            tags.add(new Tag("highway", "bus_stop"));
            tags.add(new Tag("public_transport", "platform"));
            tags.add(new Tag("name", quay.getName()));
            tags.add(new Tag("ref:IFOPT", "NL:Q:" + quay.getRefIfopt()));
            tags.add(new Tag("operator", quay.getOperator()));
            if (quay.getWheelchairBoarding() != null) {
                tags.add(new Tag("wheelchair", quay.getWheelchairBoarding() ? "yes" : "no"));            
            }
            
            var nodeData = new CommonEntityData(id.getAndDecrement(), 0, timestamp, OsmUser.NONE, 0L, tags);
            var node = new Node(nodeData, quay.getCoordinates().getY(), quay.getCoordinates().getX());
            xmlWriter.process(new NodeContainer(node));
        }
    }
}
