package shu.xyj.secdkill.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {

    // 和前端的 salt 保持一致，是固定值
    private static final String salt = "1a2b3c4d";

    // MD5 加密封装，加密后长度固定 32 位
    public static String MD5(String src) {
        return DigestUtils.md5Hex(src);
    }

    // 对前端中的明文密码进行加密
    public static String inputPassToServerPass(String inputPass) {
        String pass = "" + salt.charAt(0) + salt.charAt(3) + inputPass + salt.charAt(6) + salt.charAt(2);
        return MD5(pass);
    }

    // 对服务器密码再次加密形成最终保存在DB中的密码
    public static String serverPassToDBPass(String serverPass, String salt) {
        String pass = salt.charAt(3) + salt.charAt(0) + serverPass + salt.charAt(0) + salt.charAt(3);
        return MD5(pass);
    }

    // 从明文密码转换到DB 2次加密后的密码
    public static String inputPassToDBPass(String inputPass, String salt) {
        return serverPassToDBPass(inputPassToServerPass(inputPass), salt);
    }

    public static void main(String[] args) {
        String salt = "1a2b3c4d";
        // 96b24fb764ec7f6a810c97e19c3e13cf
        System.out.println(inputPassToServerPass("123456"));        // 24c9a268519f96340a32126a67adeac9
        System.out.println(serverPassToDBPass("96b24fb764ec7f6a810c97e19c3e13cf", salt));
        System.out.println(inputPassToDBPass("123456", salt));
    }
}
