package org.yotsubar.mccustomskinloaderserver.skin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class TextureStorage {

    @Value("${app.textures.base}")
    private String basePath;

    public void save(String name, byte[] bytes) throws IOException {
        String pathname = basePath + name;
        File file = new File(pathname);
        if (file.exists()) {
            return;
        }
        if (!file.getParentFile().exists()
                && !file.getParentFile().mkdirs()
                && !file.createNewFile()) {
            throw new IOException("Failed to create file: " + pathname);
        }
        try (FileOutputStream os = new FileOutputStream(file)) {
            os.write(bytes);
        }
    }

    public void delete(String name) throws IOException {
        File file = new File(basePath + name);
        if (file.exists()) {
            Files.delete(file.toPath());
        }
    }

    public FileSystemResource getResource(String name) {
        return new FileSystemResource(basePath + name);
    }
}
