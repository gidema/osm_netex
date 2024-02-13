package nl.haltedata.chb.sandbox;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVFormat.Builder;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.openstreetmap.osmosis.core.container.v0_6.NodeContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.CommonEntityData;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.OsmUser;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.xml.v0_6.XmlWriter;
/**
 * Quick and dirty script to create an osm xml file with chb quays(platforms)
 * 
 */
public class Chb2Xml {
    final private static String source="/home/gertjan/Downloads/chb_quays.csv";
    final private static String target="/home/gertjan/Downloads/chb_quays.osm";
    final private static String[] header = {"ref_ifopt","name","bearing","lat","lon"};
    final private static CSVFormat csvFormat = getFormat();
 
    AtomicLong id;
    java.sql.Date timestamp;

    public static void main(String ... args) {
        Chb2Xml chb2Xml = new Chb2Xml();
        chb2Xml.read();
    }
    
    private void read() {
        try (Reader reader = new BufferedReader(new FileReader(source));
             CSVParser parser = new CSVParser(reader, csvFormat);
             BufferedWriter witer = new BufferedWriter(new FileWriter(target));
             XmlWriter xmlWriter = new XmlWriter(witer);         
            ) {
            id = new AtomicLong(-1);
            timestamp = new java.sql.Date(System.currentTimeMillis());
            var it = parser.iterator();
            // Check the file header
            if (!checkHeader(it.next())) throw new RuntimeException("The header of the source file doesn't match the expected format");
            // Process the data
            parser.forEach(rec -> {
                Node node = createOsmNode(rec);
                xmlWriter.process(new NodeContainer(node));
            });
            xmlWriter.complete();
            xmlWriter.close();
            parser.close();
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
     }

    private Node createOsmNode(CSVRecord rec) {
        String ref_ifopt = rec.get(0);
        String name = rec.get(1);
        Integer bearing = Integer.valueOf(rec.get(2));
        Double lat = Double.valueOf(rec.get(3));
        Double lon = Double.valueOf(rec.get(4));
        List<Tag> tags = new LinkedList<>();
        tags.add(new Tag("highway", "bus_stop"));
        tags.add(new Tag("public_transport", "platform"));
        tags.add(new Tag("name", name));
        tags.add(new Tag("ref:IFOPT", ref_ifopt));
        tags.add(new Tag("bearing", bearing.toString()));
        var nodeData = new CommonEntityData(id.getAndDecrement(), 0, timestamp, OsmUser.NONE, 0L, tags);
        return new Node(nodeData, lat, lon);
    }

    private static CSVFormat getFormat() {
        return Builder.create(CSVFormat.POSTGRESQL_CSV)
                .setHeader(header)
                .build();
    }

    private static boolean checkHeader(CSVRecord headerRecord) {
        return Arrays.equals(headerRecord.values(), header);
    }
}
