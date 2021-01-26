package org.yotsubar.mccustomskinloaderserver.error;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
