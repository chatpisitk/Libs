package com.rokejits.android.tool.utils.encryption;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public abstract class Encryption {	 
  public static final int ENCRYPT_MODE = Cipher.ENCRYPT_MODE;
  public static final int DECRYPT_MODE = Cipher.DECRYPT_MODE;
  
  private String algorithm, key;
  private int mode;
  
  public Encryption(String key, String algorithm){
    this.algorithm = algorithm;
    this.key = key;
    setMode(ENCRYPT_MODE);
    
  }
  
  public final byte[] doFinal(byte[] source){
    SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), algorithm);
    return onDoFinal(skeySpec, source);
  }  
  
  protected abstract byte[] onDoFinal(SecretKeySpec skeySpec, byte[] source);
  
  protected int getMode(){
    return mode;	  
  }
  
  protected void setMode(int mode){
    this.mode = mode;	  
  }
  
}
