package nl.haltedata.tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;

import org.springframework.core.io.AbstractResource;
import org.springframework.util.StringUtils;

public class GzipFileSystemResource extends AbstractResource {
    private Path filePath;
    private GZIPInputStream inputStream;
    
    public GzipFileSystemResource(Path filePath) {
        super();
        this.filePath = filePath;
    }

    public GzipFileSystemResource(String path) {
        if (path != null) {
            var path_ = StringUtils.cleanPath(path);
            this.filePath = new File(path_).toPath();
        }
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (this.inputStream == null) {
            inputStream = new GZIPInputStream(Files.newInputStream(filePath));
        }
        return inputStream;
    }

    @Override
    public boolean exists() {
        return Files.exists(filePath);
    }

    @Override
    public boolean isOpen() {
        // TODO Find a means to check whether the inputstream is closed
        return inputStream != null;
    }

    @Override
    public boolean isFile() {
        return super.isFile();
    }

    @Override
    public File getFile() throws IOException {
        return filePath.toFile();
    }

    /**
     * This implementation returns a description that includes the absolute
     * path of the file.
     * @see java.io.File#getAbsolutePath()
     * @see java.nio.file.Path#toAbsolutePath()
     */
    @Override
    public String getDescription() {
        return "file [" + this.filePath.toAbsolutePath() + "]";
    }   
}
