package com.geekplus.common.util.base64;

import cn.hutool.core.codec.Base64Decoder;
import com.geekplus.common.enums.MimeTypeEnum;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Base64 编码和解码。
 *
 * @author geekplus
 * @date 2016年10月03日
 */
public class Base64Util {

    static String base64Img="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAANUAAADVCAYAAADAQLWDAAAACXBIWXMAAAsTAAALEwEAmpwYAAAG0mlUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS42LWMxNDUgNzkuMTYzNDk5LCAyMDE4LzA4LzEzLTE2OjQwOjIyICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtbG5zOmRjPSJodHRwOi8vcHVybC5vcmcvZGMvZWxlbWVudHMvMS4xLyIgeG1sbnM6cGhvdG9zaG9wPSJodHRwOi8vbnMuYWRvYmUuY29tL3Bob3Rvc2hvcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RFdnQ9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZUV2ZW50IyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ0MgMjAxOSAoTWFjaW50b3NoKSIgeG1wOkNyZWF0ZURhdGU9IjIwMTktMDktMjBUMTY6MzU6NTYrMDg6MDAiIHhtcDpNb2RpZnlEYXRlPSIyMDE5LTA5LTIwVDE2OjQ2OjI1KzA4OjAwIiB4bXA6TWV0YWRhdGFEYXRlPSIyMDE5LTA5LTIwVDE2OjQ2OjI1KzA4OjAwIiBkYzpmb3JtYXQ9ImltYWdlL3BuZyIgcGhvdG9zaG9wOkNvbG9yTW9kZT0iMyIgcGhvdG9zaG9wOklDQ1Byb2ZpbGU9InNSR0IgSUVDNjE5NjYtMi4xIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOjVhNTdlMGRmLTBmMzEtNDZhZS1iODRiLWQ4YmNjZGM3OTY4OCIgeG1wTU06RG9jdW1lbnRJRD0iYWRvYmU6ZG9jaWQ6cGhvdG9zaG9wOjczMzg4ZDhmLWFhNGEtZDQ0MS05NGFlLWU4NGRlNDY0ZmY1MCIgeG1wTU06T3JpZ2luYWxEb2N1bWVudElEPSJ4bXAuZGlkOjRiMjI0NTZjLTVhMzItNDVhOS05OGJiLWFiM2JmNTEwZTNlNiI+IDx4bXBNTTpIaXN0b3J5PiA8cmRmOlNlcT4gPHJkZjpsaSBzdEV2dDphY3Rpb249ImNyZWF0ZWQiIHN0RXZ0Omluc3RhbmNlSUQ9InhtcC5paWQ6NGIyMjQ1NmMtNWEzMi00NWE5LTk4YmItYWIzYmY1MTBlM2U2IiBzdEV2dDp3aGVuPSIyMDE5LTA5LTIwVDE2OjM1OjU2KzA4OjAwIiBzdEV2dDpzb2Z0d2FyZUFnZW50PSJBZG9iZSBQaG90b3Nob3AgQ0MgMjAxOSAoTWFjaW50b3NoKSIvPiA8cmRmOmxpIHN0RXZ0OmFjdGlvbj0ic2F2ZWQiIHN0RXZ0Omluc3RhbmNlSUQ9InhtcC5paWQ6YWRkMjk5NzAtNmMzZi00YjBiLTgzNTMtNDFkMzNkNWZiNmZjIiBzdEV2dDp3aGVuPSIyMDE5LTA5LTIwVDE2OjQ2OjAxKzA4OjAwIiBzdEV2dDpzb2Z0d2FyZUFnZW50PSJBZG9iZSBQaG90b3Nob3AgQ0MgMjAxOSAoTWFjaW50b3NoKSIgc3RFdnQ6Y2hhbmdlZD0iLyIvPiA8cmRmOmxpIHN0RXZ0OmFjdGlvbj0ic2F2ZWQiIHN0RXZ0Omluc3RhbmNlSUQ9InhtcC5paWQ6NWE1N2UwZGYtMGYzMS00NmFlLWI4NGItZDhiY2NkYzc5Njg4IiBzdEV2dDp3aGVuPSIyMDE5LTA5LTIwVDE2OjQ2OjI1KzA4OjAwIiBzdEV2dDpzb2Z0d2FyZUFnZW50PSJBZG9iZSBQaG90b3Nob3AgQ0MgMjAxOSAoTWFjaW50b3NoKSIgc3RFdnQ6Y2hhbmdlZD0iLyIvPiA8L3JkZjpTZXE+IDwveG1wTU06SGlzdG9yeT4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBl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";
    public Base64Util() {
    }

    public static void main(String[] args) {
//        System.out.println(""+isBase64(base64Img));
//        System.out.println(getFileMimeType(base64Img));
//        System.out.println(checkImageBase64Format(getBase64Str(base64Img)));
//        System.out.println(getBase64FileType(base64Img));
//        System.out.println(""+isImageFromBase64(getBase64Str(base64Img)));
    }

    //判断base64是否为图片
    public static boolean isImageFromBase64(String base64Str) {
        boolean flag = false;
        try {
            // 获取图片流
            BufferedImage bufImg = ImageIO.read(new ByteArrayInputStream(new Base64Decoder().decode(base64Str)));
            if (null == bufImg) {
                return false;
            }
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    //获取base64文件的mime类型
    public static String getFileMimeType(String base64Str){
        String mimeType="";
        // 使用正则表达式提取base64数据
        if(isBase64(base64Str) && base64Str.indexOf("data")!=-1) {
            //Pattern pattern = Pattern.compile("data:(.*)/(.*);base64,(.*)");
            Pattern pattern = Pattern.compile("data:(.*?);base64,(.*)");
            Matcher matcher = pattern.matcher(base64Str);

            if (matcher.find()) {
                mimeType = matcher.group(1);
            }
        }
        return mimeType;
    }

    //判断是否为合法的Base64编码
    public static boolean isBase64(String str) {
        try {
            // 尝试解码，如果抛出异常，则不是有效的Base64编码
            Base64.decodeBase64(str);
            return true;
        } catch (IllegalArgumentException e) {
            // 不是有效的Base64编码
            return false;
        }
    }

    //获取base64文件的后缀文件类型
    public static String getBase64FileSuffix(String str){
        String fileType = "";
        if(isBase64(str) && str.indexOf("data")!=-1){
            String[] strArr=str.split(",");
            Pattern pattern = Pattern.compile("data:(.*)/(.*);base64");
            //Pattern pattern = Pattern.compile("data:(.*?);base64");
            Matcher matcher = pattern.matcher(strArr[0]);
            //Matcher matcher = Pattern.compile("data:image/(.*?);base64").matcher(strArr[0]);
            if(matcher.find()) {
                fileType = matcher.group(2);
            }
        }else if(isImageFromBase64(getBase64Str(str))) {
            fileType="png";
        }
        return fileType;
    }

    //获取base64文件的字符串
    public static String getBase64Str(String str){
        String text = "";
        if(isBase64(str) && str.indexOf("data")!=-1){
            Matcher matcher = Pattern.compile("^data.(.*?);base64,").matcher(str);
            if (matcher.find()){
                text = str.replace(matcher.group(),"");
            }
        }else{
            text=str;
        }
        return text;
    }

    //判断图片base64字符串的文件格式
    public static String checkImageBase64Format(String base64ImgData) {
        byte[] b = Base64.decodeBase64(base64ImgData);
        String type = "";
        if ((b[0] & 0xFF) == 0x47 && (b[1] & 0xFF) == 0x49) {
            type = "gif";
        }
        if (0x424D == ((b[0] & 0xff) << 8 | (b[1] & 0xff))) {
            type = "bmp";
        } else if (0x8950 == ((b[0] & 0xff) << 8 | (b[1] & 0xff))) {
            type = "png";
        } else if (0xFFD8 == ((b[0] & 0xff) << 8 | (b[1] & 0xff))) {
            type = "jpg";
        }
        return type;
    }

    /**
     * 功能：编码字符串
     *
     * @author geekplus
     * @date 2016年10月03日
     * @param data
     *            源字符串
     * @return String
     */
    public static String encode(String data) {
        return new String(encode(data.getBytes()));
    }

    /**
     * 功能：解码字符串
     *
     * @author geekplus
     * @date 2016年10月03日
     * @param data
     *            源字符串
     * @return String
     */
    public static String decode(String data) {
        return new String(decode(data.toCharArray()));
    }

    /**
     * 功能：编码byte[]
     *
     * @author geekplus
     * @date 2016年10月03日
     * @param data
     *            源
     * @return char[]
     */
    public static char[] encode(byte[] data) {
        char[] out = new char[((data.length + 2) / 3) * 4];
        for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
            boolean quad = false;
            boolean trip = false;

            int val = (0xFF & (int) data[i]);
            val <<= 8;
            if ((i + 1) < data.length) {
                val |= (0xFF & (int) data[i + 1]);
                trip = true;
            }
            val <<= 8;
            if ((i + 2) < data.length) {
                val |= (0xFF & (int) data[i + 2]);
                quad = true;
            }
            out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];
            val >>= 6;
            out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];
            val >>= 6;
            out[index + 1] = alphabet[val & 0x3F];
            val >>= 6;
            out[index + 0] = alphabet[val & 0x3F];
        }
        return out;
    }

    /**
     * 功能：解码
     *
     * @author geekplus
     * @date 2016年10月03日
     * @param data
     *            编码后的字符数组
     * @return byte[]
     */
    public static byte[] decode(char[] data) {

        int tempLen = data.length;
        for (int ix = 0; ix < data.length; ix++) {
            if ((data[ix] > 255) || codes[data[ix]] < 0) {
                --tempLen; // ignore non-valid chars and padding
            }
        }
        // calculate required length:
        // -- 3 bytes for every 4 valid base64 chars
        // -- plus 2 bytes if there are 3 extra base64 chars,
        // or plus 1 byte if there are 2 extra.

        int len = (tempLen / 4) * 3;
        if ((tempLen % 4) == 3) {
            len += 2;
        }
        if ((tempLen % 4) == 2) {
            len += 1;

        }
        byte[] out = new byte[len];

        int shift = 0; // # of excess bits stored in accum
        int accum = 0; // excess bits
        int index = 0;

        // we now go through the entire array (NOT using the 'tempLen' value)
        for (int ix = 0; ix < data.length; ix++) {
            int value = (data[ix] > 255) ? -1 : codes[data[ix]];

            if (value >= 0) { // skip over non-code
                accum <<= 6; // bits shift up by 6 each time thru
                shift += 6; // loop, with new bits being put in
                accum |= value; // at the bottom.
                if (shift >= 8) { // whenever there are 8 or more shifted in,
                    shift -= 8; // write them out (from the top, leaving any
                    out[index++] = // excess at the bottom for next iteration.
                            (byte) ((accum >> shift) & 0xff);
                }
            }
        }

        // if there is STILL something wrong we just have to throw up now!
        if (index != out.length) {
            throw new Error("Miscalculated data length (wrote " + index
                    + " instead of " + out.length + ")");
        }

        return out;
    }

    /**
     * 功能：编码文件
     *
     * @author geekplus
     * @date 2016年10月03日
     * @param file
     *            源文件
     */
    public static void encode(File file) throws IOException {
        if (!file.exists()) {
            System.exit(0);
        }

        else {
            byte[] decoded = readBytes(file);
            char[] encoded = encode(decoded);
            writeChars(file, encoded);
        }
        file = null;
    }

    /**
     * 功能：解码文件。
     *
     * @author geekplus
     * @date 2016年10月03日
     * @param file
     *            源文件
     * @throws IOException
     */
    public static void decode(File file) throws IOException {
        if (!file.exists()) {
            System.exit(0);
        } else {
            char[] encoded = readChars(file);
            byte[] decoded = decode(encoded);
            writeBytes(file, decoded);
        }
        file = null;
    }

    //
    // code characters for values 0..63
    //
    private static char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
            .toCharArray();

    //
    // lookup table for converting base64 characters to value in range 0..63
    //
    private static byte[] codes = new byte[256];
    static {
        for (int i = 0; i < 256; i++) {
            codes[i] = -1;
            // LoggerUtil.debug(i + "&" + codes[i] + " ");
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            codes[i] = (byte) (i - 'A');
            // LoggerUtil.debug(i + "&" + codes[i] + " ");
        }

        for (int i = 'a'; i <= 'z'; i++) {
            codes[i] = (byte) (26 + i - 'a');
            // LoggerUtil.debug(i + "&" + codes[i] + " ");
        }
        for (int i = '0'; i <= '9'; i++) {
            codes[i] = (byte) (52 + i - '0');
            // LoggerUtil.debug(i + "&" + codes[i] + " ");
        }
        codes['+'] = 62;
        codes['/'] = 63;
    }

    private static byte[] readBytes(File file) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] b = null;
        InputStream fis = null;
        InputStream is = null;
        try {
            fis = new FileInputStream(file);
            is = new BufferedInputStream(fis);
            int count = 0;
            byte[] buf = new byte[16384];
            while ((count = is.read(buf)) != -1) {
                if (count > 0) {
                    baos.write(buf, 0, count);
                }
            }
            b = baos.toByteArray();

        } finally {
            try {
                if (fis != null)
                    fis.close();
                if (is != null)
                    is.close();
                if (baos != null)
                    baos.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        return b;
    }

    private static char[] readChars(File file) throws IOException {
        CharArrayWriter caw = new CharArrayWriter();
        Reader fr = null;
        Reader in = null;
        try {
            fr = new FileReader(file);
            in = new BufferedReader(fr);
            int count = 0;
            char[] buf = new char[16384];
            while ((count = in.read(buf)) != -1) {
                if (count > 0) {
                    caw.write(buf, 0, count);
                }
            }

        } finally {
            try {
                if (caw != null)
                    caw.close();
                if (in != null)
                    in.close();
                if (fr != null)
                    fr.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        return caw.toCharArray();
    }

    private static void writeBytes(File file, byte[] data) throws IOException {
        OutputStream fos = null;
        OutputStream os = null;
        try {
            fos = new FileOutputStream(file);
            os = new BufferedOutputStream(fos);
            os.write(data);

        } finally {
            try {
                if (os != null)
                    os.close();
                if (fos != null)
                    fos.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private static void writeChars(File file, char[] data) throws IOException {
        Writer fos = null;
        Writer os = null;
        try {
            fos = new FileWriter(file);
            os = new BufferedWriter(fos);
            os.write(data);

        } finally {
            try {
                if (os != null)
                    os.close();
                if (fos != null)
                    fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // /////////////////////////////////////////////////
    // end of test code.
    // /////////////////////////////////////////////////

}
