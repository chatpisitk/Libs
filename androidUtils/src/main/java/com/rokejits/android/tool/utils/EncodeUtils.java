package com.rokejits.android.tool.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class EncodeUtils {
  public static final String HMAC_SHA_256 = "HmacSHA256";
  
  public static byte[] encode(String algorithm, String key, String data) throws Exception {
    Mac sha256_HMAC = Mac.getInstance(algorithm);
    SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), algorithm);
    sha256_HMAC.init(secret_key);
    return sha256_HMAC.doFinal(data.getBytes());
  }
}
