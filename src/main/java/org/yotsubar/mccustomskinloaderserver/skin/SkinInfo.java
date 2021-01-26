package org.yotsubar.mccustomskinloaderserver.skin;

import org.yotsubar.mccustomskinloaderserver.entity.Texture;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SkinInfo {
    private String username;

    private Map<String, String> textures;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Map<String, String> getTextures() {
        return textures;
    }

    public void setTextures(Map<String, String> textures) {
        this.textures = textures;
    }

    public static SkinInfo build(String username, List<Texture> textures) {
        SkinInfo skinInfo = new SkinInfo();
        skinInfo.setUsername(username);
        Map<String, String> textureMap = new LinkedHashMap<>(textures.size());
        for (Texture texture : textures) {
            textureMap.put(texture.getType(), texture.getHash());
        }
        skinInfo.setTextures(textureMap);
        return skinInfo;
    }
}
