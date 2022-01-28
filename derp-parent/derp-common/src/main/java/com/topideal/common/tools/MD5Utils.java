package com.topideal.common.tools;

import java.security.MessageDigest;

/**
 * <b>功能：密码工具</b><br>
 */
public class MD5Utils {
    /**
     * 获得加密后的密码
     * @param password 原密码
     * @return
     */
    public static String encode(String password) {
        return MD5(password);
    }

    /**
     * 获得32位的MD5值
     * @param sourceStr 加密字符串
     * @return
     */
    private static String MD5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    public static void main(String[] args){
        System.out.println(MD5("123456"));
    }


}
