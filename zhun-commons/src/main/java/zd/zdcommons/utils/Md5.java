package zd.zdcommons.utils;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 类描述 :
 *
 * @author Jack Chen
 * @version 1.0
 * @date 2019/7/25 16:46
 */
public class Md5 {
    private static Charset charset_utf8 = Charset.forName("UTF-8");

    private static Charset charset_gbk = Charset.forName("GBK");

    public static String md5(String str) {
        String md5 = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = str.getBytes(charset_utf8);
            bytes = digest.digest(bytes);
            md5 = toHexStr(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }


    private static String toHexStr(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        int var;
        for (byte b : bytes) {
            var = ((int) b) & 0xFF;
            if (var < 16) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(var));
        }
        return sb.toString().toUpperCase();
    }

}
