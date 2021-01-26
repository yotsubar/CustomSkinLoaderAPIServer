package org.yotsubar.mccustomskinloaderserver.skin;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.WebRequest;
import org.yotsubar.mccustomskinloaderserver.entity.Texture;
import org.yotsubar.mccustomskinloaderserver.error.ResourceNotFoundException;
import org.yotsubar.mccustomskinloaderserver.jpa.TextureRepository;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
public class CustomSkinAPIController {

    private static final Map<String, Integer> typeOrderMap;

    private final TextureRepository textureRepository;

    private final TextureStorage textureStorage;

    static {
        Map<String, Integer> tmp = new HashMap<>();
        tmp.put("default", 0);
        tmp.put("slim", 1);
        tmp.put("cape", 2);
        tmp.put("elytra", 3);
        typeOrderMap = tmp;
    }

    public CustomSkinAPIController(TextureRepository textureRepository, TextureStorage textureStorage) {
        this.textureRepository = textureRepository;
        this.textureStorage = textureStorage;
    }

    @GetMapping("{username}.json")
    public ResponseEntity<SkinInfo> getSkin(@PathVariable String username, WebRequest request) {
        List<Texture> textures = textureRepository.findByUsername(username);
        textures.removeIf(Texture::isRemoved);
        if (CollectionUtils.isEmpty(textures)) {
            throw new ResourceNotFoundException(username);
        }
        long lastUpdateTs = textures.stream().mapToLong(Texture::getUpdateTs).max().orElse(0L);
        if (request.checkNotModified(lastUpdateTs)) {
            return null;
        }
        textures.sort(Comparator.comparing(this::getOrder));
        return ResponseEntity.ok()
                .lastModified(lastUpdateTs)
                .cacheControl(CacheControl.maxAge(30, TimeUnit.SECONDS))
                .body(SkinInfo.build(username, textures));
    }

    private Integer getOrder(Texture texture) {
        return typeOrderMap.getOrDefault(texture.getType(), Integer.MAX_VALUE);
    }

    @GetMapping("textures/{hash}")
    public ResponseEntity<InputStreamResource> getTexture(@PathVariable String hash,
                                                          WebRequest request) throws IOException {
        FileSystemResource fileSystemResource = textureStorage.getResource(hash);
        if (!fileSystemResource.exists()) {
            throw new ResourceNotFoundException(hash);
        }
        long lastModified = fileSystemResource.lastModified();
        if (request.checkNotModified(lastModified)) {
            return null;
        }
        return ResponseEntity.ok()
                .lastModified(lastModified)
                .cacheControl(CacheControl.maxAge(30, TimeUnit.DAYS))
                .contentLength(fileSystemResource.contentLength())
                .contentType(MediaType.IMAGE_PNG)
                .body(new InputStreamResource(fileSystemResource.getInputStream()));
    }
}
