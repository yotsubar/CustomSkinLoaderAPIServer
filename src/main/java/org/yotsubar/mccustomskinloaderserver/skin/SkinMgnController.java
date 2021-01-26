package org.yotsubar.mccustomskinloaderserver.skin;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yotsubar.mccustomskinloaderserver.entity.Texture;
import org.yotsubar.mccustomskinloaderserver.error.BadRequestException;
import org.yotsubar.mccustomskinloaderserver.jpa.TextureRepository;
import org.yotsubar.mccustomskinloaderserver.utils.Sha256Utils;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/skin/")
public class SkinMgnController {

    private final TextureRepository textureRepository;

    private final TextureStorage textureStorage;

    public SkinMgnController(TextureRepository textureRepository, TextureStorage textureStorage) {
        this.textureRepository = textureRepository;
        this.textureStorage = textureStorage;
    }

    @GetMapping("{username}")
    public SkinInfo listTextures(@PathVariable String username) {
        List<Texture> textures = textureRepository.findByUsername(username);
        textures.removeIf(Texture::isRemoved);
        return SkinInfo.build(username, textures);
    }

    @PostMapping("{username}/{type}")
    public Map<String, String> saveTexture(@RequestParam String base64,
                                           @PathVariable String username,
                                           @PathVariable String type) throws IOException, NoSuchAlgorithmException {
        checkParam("base64", base64);

        byte[] bytes = Base64.getDecoder().decode(base64);
        String hash = Sha256Utils.sha256Hex(bytes);
        textureStorage.save(hash, bytes);

        Texture texture = new Texture();
        texture.setUsername(username);
        texture.setType(type);
        texture.setHash(hash);
        texture.setUpdateTs(System.currentTimeMillis());
        texture.setRemoved(false);
        Texture lastTexture = textureRepository.findByUsernameAndType(username, type);
        String lastHash = null;
        if (lastTexture != null) {
            texture.setId(lastTexture.getId());
            lastHash = lastTexture.getHash();
        }
        textureRepository.save(texture);
        if (lastHash != null) {
            tryDeleteFile(lastHash);
        }

        return Collections.singletonMap("hash", hash);
    }

    @DeleteMapping("{username}/{type}")
    public Map<String, String> delTexture(@PathVariable String username, @PathVariable String type) {
        Texture tmp = textureRepository.findByUsernameAndType(username, type);
        if (tmp != null) {
            tmp.setRemoved(true);
            tmp.setUpdateTs(System.currentTimeMillis());
            textureRepository.save(tmp);
        }

        return Collections.singletonMap("code", "success");
    }

    private void tryDeleteFile(String hash) throws IOException {
        if (textureRepository.existsByHash(hash)) {
            return;
        }
        textureStorage.delete(hash);
    }

    private void checkParam(String param, String value) {
        if (!StringUtils.hasText(value)) {
            throw new BadRequestException(param + " can not be empty");
        }
    }
}
