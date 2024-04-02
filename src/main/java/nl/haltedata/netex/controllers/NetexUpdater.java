package nl.haltedata.netex.controllers;

import static nl.haltedata.netex.ndov.NetexFileCache.CACHE_FOLDER;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.zip.GZIPInputStream;

public class NetexUpdater {
    final PathMatcher gzPathMatcher = FileSystems.getDefault().getPathMatcher("glob:**/*.gz");
    final PathMatcher xmlPathMatcher = FileSystems.getDefault().getPathMatcher("glob:**/*.xml");
    final byte[] buffer = new byte[1024];

    public static void main(String ... args) throws IOException {
        var updater = new NetexUpdater();
        updater.updateXmlFiles();
    }
    
    public void updateZipFiles() {
        
    }
    
    /**
     * Extract the missing xml files from the zip files
     * @throws IOException 
     */
    public void updateXmlFiles() throws IOException {
        Files.walk(CACHE_FOLDER).filter(gzPathMatcher::matches).forEach(gzFile -> {
            var gzFileName = gzFile.getFileName().toString();
            var xmlFileName = gzFileName.substring(0, gzFileName.length() - 3);
            var xmlPath = gzFile.resolveSibling(xmlFileName);
            if (Files.notExists(xmlPath)) {
                try {
                    extractXmlFile(gzFile, xmlPath);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }        
        });
        
    }

    private void extractXmlFile(Path gzFile, Path xmlPath) throws IOException {
        GZIPInputStream zis = new GZIPInputStream(Files.newInputStream(gzFile));
        try (var fos = Files.newOutputStream(xmlPath)) {
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
        }
        // TODO Auto-generated method stub       
    }
}
