package nl.haltedata.chb;

import java.io.FileNotFoundException;
import java.io.FileReader;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import nl.chb.Export;

public class ChbDataReader {
    public Export readChbData(String filename) {
        JAXBContext context;
        try {
            context = JAXBContext.newInstance(Export.class);
            return (Export) context.createUnmarshaller()
                    .unmarshal(new FileReader(filename));
        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
