package org.yotsubar.mccustomskinloaderserver.jpa;

import org.springframework.data.repository.CrudRepository;
import org.yotsubar.mccustomskinloaderserver.entity.Texture;

import java.util.List;

public interface TextureRepository extends CrudRepository<Texture, Long> {

    List<Texture> findByUsername(String username);

    Texture findByUsernameAndType(String username, String type);

    boolean existsByHash(String hash);
}
