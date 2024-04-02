package nl.haltedata.netex.ndov;

import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import nl.haltedata.netex.dto.NetexFileInfo;

public class NdovNetexService implements Closeable {
    private static String FTP_SERVER = "data.ndovloket.nl";
    private static Pattern fileNamePattern = Pattern.compile("(NeTEx_)?(.+?)_(20\\d{6})(.+)");

    private FTPClient ftpClient;
    private List<String> agencyFolders;
    private List<NetexFileInfo> files;
    
    private void connect() throws IOException {
        if (ftpClient != null && ftpClient.isAvailable()) return;
        ftpClient = new FTPClient();
        ftpClient.connect(InetAddress.getByName(FTP_SERVER));
        ftpClient.login("anonymous", "anonymous@ndovloket.nl");
        ftpClient.changeWorkingDirectory("netex");
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
    }
    
    public List<String> getAgencyFolders() {
        if (agencyFolders == null) {
            try {
                connect();
                FTPFile[] folders = ftpClient.listDirectories();
                agencyFolders = new ArrayList<>(folders.length);
                for (FTPFile folder : folders) {
                    agencyFolders.add(folder.getName());
                }
            } catch (IOException e) {
                return Collections.emptyList();
            }
        }
        return agencyFolders;
    }
    
    public List<NetexFileInfo> getFileInfo(String folder) throws IOException {
        String agencyId = folder.toUpperCase();
        FTPFile[] ftpFiles = ftpClient.listFiles(folder);
        var fileInfoList = new ArrayList<NetexFileInfo>(ftpFiles.length);
        for (FTPFile ftpFile : ftpFiles) {
            var fileInfo = new NetexFileInfo();
            fileInfo.setAgencyId(agencyId);
            fileInfo.setFileName(ftpFile.getName());
            fileInfo.setLastModified(ftpFile.getTimestamp());
            fileInfo.setSize(ftpFile.getSize());
            Matcher m = fileNamePattern.matcher(ftpFile.getName());
            if (m.matches()) {
                var result = m.toMatchResult();
                fileInfo.setFileGroup(result.group(2));
            }
            fileInfoList.add(fileInfo);
        }
        return fileInfoList;    
    }
    
    public void downloadFile(String agencyId, String fileName, Path cacheFile) throws IOException {
        connect();
        try (FileOutputStream outputStream = new FileOutputStream(cacheFile.toFile())) {
            var ftpPath = String.format("%s/%s", agencyId.toLowerCase(), fileName);
            ftpClient.retrieveFile(ftpPath, outputStream);
        }
    }

    @Override
    public void close() throws IOException {
        if (ftpClient != null) {
            ftpClient.disconnect();
        }
    }

}
