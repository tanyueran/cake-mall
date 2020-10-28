package com.github.tanyueran.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * md5 加密 32位小
 */
public class MD5Utils {

    /**
     * MD5加密
     *
     * @param data 待加密数据
     * @return byte[] 消息摘要
     * @throws Exception
     */
    public static byte[] encodeMD5(String data) throws Exception {

        // 执行消息摘要
        return DigestUtils.md5(data);
    }

    /**
     * MD5加密
     *
     * @param data 待加密数据
     * @return byte[] 消息摘要
     * @throws Exception
     */
    public static String encodeMD5Hex(String data) {
        // 执行消息摘要
        return DigestUtils.md5Hex(data);
    }

    public static void main(String[] args) {
        String c = MD5Utils.encodeMD5Hex("中国");
        MD5Utils.encodeMD5Hex(c);
        System.out.println(c);
    }
}
