package com.geekplus.common.util.encrypt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * author     : geekplus
 * email      :
 * date       : 9/29/24 3:51 AM
 * description: //TODO
 */
public class CompressionUtil {
    public static String compress(String token) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);
        gzipOutputStream.write(token.getBytes("UTF-8"));
        gzipOutputStream.close();
        byte[] compressedBytes = outputStream.toByteArray();
        outputStream.close();
        return new String(compressedBytes, "UTF-8");
    }

    public static String decompress(String compressedToken) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedToken.getBytes("ISO-8859-1"));
        GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
        byte[] buffer = new byte[256];
        int n;
        while ((n = gzipInputStream.read(buffer)) >= 0) {
            outputStream.write(buffer, 0, n);
        }
        gzipInputStream.close();
        inputStream.close();
        outputStream.close();
        return new String(outputStream.toByteArray(), "UTF-8");
    }
}
