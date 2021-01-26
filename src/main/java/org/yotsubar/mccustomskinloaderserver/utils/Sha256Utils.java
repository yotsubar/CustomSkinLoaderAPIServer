package org.yotsubar.mccustomskinloaderserver.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha256Utils {

    private Sha256Utils() {
        //hide
    }

    public static String sha256Hex(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(bytes);
        return byte2Hex(messageDigest.digest());
    }

    private static String byte2Hex(byte[] bytes) {
        StringBuilder stringBuffer = new StringBuilder();
        String temp;
        for (byte aByte : bytes) {
            temp = Integer.toHexString(aByte & 0xFF);
            if (temp.length() == 1) {
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }
}
