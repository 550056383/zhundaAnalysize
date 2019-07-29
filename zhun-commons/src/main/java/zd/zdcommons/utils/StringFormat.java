package zd.zdcommons.utils;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * 类描述 :
 *
 * @author Jack Chen
 * @version 1.0
 * @date 2019/7/26 10:35
 */
public class StringFormat {

    private static Charset charset_utf8 = Charset.forName("UTF-8");

    private static Charset charset_gbk = Charset.forName("GBK");

    /**
     * 方法描述 : 处理关联设置无规则字符串,一组规则对应一个数组
     *
     * @param s
     * @return java.util.List<java.lang.String [ ]>
     * @author Jack Chen
     * @date 2019/7/26 14:36
     **/
    public static List<String[]> getString1(String s) {
       /* String replace = s.replace("{", "").replace("}", "").replace("[", "").replace("]", "").toString();
        String[] split = replace.split(",");
        String[] arr = new String[split.length];
        for (int i = 0; i < split.length; i++) {
            String str = split[i].substring(split[i].indexOf("=") + 1);
            arr[i] = str;
        }
        return  arr;*/

        List<String[]> list = new ArrayList<String[]>();
        String[] split = s.split("},");
        for (String s1 : split) {
            String[] arr = s1.replace("[{", "").replace("}]", "").replace("{", "").split(",");
            for (int i = 0; i < arr.length; i++) {
                String substring1 = arr[i].substring(arr[i].indexOf("=") + 1);
                arr[i] = substring1.trim();
            }
            list.add(arr);
        }
        return list;
    }

    /**
     * 方法描述 : 处理输出设置无规则字符串,一组规则对应一个数组
     *
     * @param s
     * @return java.util.List<java.lang.String [ ]>
     * @author Jack Chen
     * @date 2019/7/26 15:35
     **/
    public static List<String[]> getString2(String s) {
        List<String[]> list = new ArrayList<String[]>();
        String[] split = s.split("],");
        for (String s1 : split) {
            String[] arr = s1.replace("[[", "").replace("[", "").replace("]", "").replace("{", "").replace("}", "").split(",");
            for (int i = 0; i < arr.length; i++) {
                String substring1 = arr[i].substring(arr[i].indexOf("=") + 1);
                arr[i] = substring1.trim();
            }
            if (arr.length > 1) {
                list.add(arr);
            }
        }
        return list;
    }

    /**
     * 方法描述 : 处理条件设置无规则字符串,一组规则对应一个数组
     *
     * @param s
     * @return java.util.List<java.lang.String [ ]>
     * @author Jack Chen
     * @date 2019/7/26 16:30
     **/
    public static List<String[]> getString3(String s) {
        List<String[]> list = new ArrayList<String[]>();
        String[] split = s.split("]],");
        for (String s1 : split) {
            String[] arr = s1.replace("[", "").replace("]", "").replace("{", "").replace("}", "").split(",");
            for (int i = 0; i < arr.length; i++) {
                String substring1 = arr[i].substring(arr[i].indexOf("=") + 1);
                arr[i] = substring1.trim();
            }
            if (arr.length > 2) {
                list.add(arr);
            }
        }

        return list;
    }

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

    /**
     * 方法描述 : 用于将Excel表名加密后进行修改拼接作为临时表名
     *
     * @param str
     * @return java.lang.String
     * @author Jack Chen
     * @date 2019/7/26 10:47
     **/
    public static String uuid(String str) {
        String s = "ch" + zd.zdcommons.utils.StringFormat.md5(str).substring(0, 8) + "en";
        return s;
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