package nl.haltedata.netex.ndov;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import nl.haltedata.netex.dto.NetexFileRepository;
import nl.haltedata.netex.dto.NetexLatestFile;

@RequiredArgsConstructor
public class NetexFileCache {
    public static Path CACHE_FOLDER = Path.of("/home/gertjan/projects/NLGeo/Haltedata/netex/filecache");
    private final NetexFileRepository fileRepository;
    private NdovNetexService netexService = new NdovNetexService();
    
    public void update() {
        for (NetexLatestFile file: getMissingFiles()) {
            var fileName = file.getFileName();
            var cacheFile = CACHE_FOLDER.resolve(fileName);
            try {
                netexService.downloadFile(file.getAgencyId(), fileName, cacheFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    private List<NetexLatestFile> getMissingFiles() {
        List<NetexLatestFile> missingFiles = new ArrayList<>();
        for (NetexLatestFile file : fileRepository.getLatestFiles()) {
            var fileName = file.getFileName();
            if (Files.notExists(CACHE_FOLDER.resolve(fileName))) {
                missingFiles.add(file);
            }
        }
        return missingFiles;
    }
    
    
}
