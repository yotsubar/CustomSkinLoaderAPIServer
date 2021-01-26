package org.yotsubar.mccustomskinloaderserver.error;

public class ResourceNotFoundException extends RuntimeException {
    private final String resource;

    public ResourceNotFoundException(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }
}
