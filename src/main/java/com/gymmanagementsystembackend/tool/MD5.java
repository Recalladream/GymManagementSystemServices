package com.gymmanagementsystembackend.tool;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5 {
    public static String getMd5edData(String data){
        String md5Hex=DigestUtils.md5Hex(data);
        return md5Hex;
    }
}
