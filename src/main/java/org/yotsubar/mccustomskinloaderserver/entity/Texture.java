package org.yotsubar.mccustomskinloaderserver.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(indexes = {@Index(columnList = "username,type", unique = true), @Index(columnList = "hash")})
public class Texture {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String hash;

    @Column(nullable = false)
    private Long updateTs;

    @Column(nullable = false)
    private boolean removed;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Long getUpdateTs() {
        return updateTs;
    }

    public void setUpdateTs(Long updateTs) {
        this.updateTs = updateTs;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }
}
