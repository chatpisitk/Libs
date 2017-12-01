package com.rokejits.android.tool.utils.encryption;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Log;

public class AESEncryption extends Encryption{  
  
  public static final byte[] encode(String key, String source){	
    return encode(key, null, source);	  
  }
  
  public static final byte[] encode(String key, byte[] source){	
    return encode(key, null, source);	  
  }
	
  public static final byte[] decode(String key, String source){	
    return decode(key, null, source);	  
  }
  
  public static final byte[] decode(String key, byte[] source){	
    return decode(key, null, source);	  
  }
  
  public static final byte[] encode(String key, String iv, String source){	
    return encryption(Encryption.ENCRYPT_MODE, key, iv, source);	  
  } 
  
  public static final byte[] encode(String key, String iv, byte[] source){	
    return encryption(Encryption.ENCRYPT_MODE, key, iv, source);	  
  } 
		  
  public static final byte[] decode(String key, String iv, String source){	
    return encryption(Encryption.DECRYPT_MODE, key, iv, source);	  
  }
  
  public static final byte[] decode(String key, String iv, byte[] source){	
    return encryption(Encryption.DECRYPT_MODE, key, iv, source);	  
  }
	
  private static final byte[] encryption(int mode, String key, String iv, String source){
    return encryption(mode, key, iv, source.getBytes());
  }	
  
  private static final byte[] encryption(int mode, String key, String iv, byte[] source){
    AESEncryption aEncoder = new AESEncryption(key);
    aEncoder.setIV(iv);
    aEncoder.setMode(mode);
    return aEncoder.doFinal(source);
  }	
	
  private String iv;	
  public AESEncryption(String key){
    super(key, "AES");	  
  }	
  
  public void setIV(String iv){
    this.iv = iv;	  
  }

  @Override
  protected final byte[] onDoFinal(SecretKeySpec skeySpec, byte[] source) {
    Cipher cipher = null;
    try{
      if(iv != null){
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());		
	    cipher.init(getMode(), skeySpec, ivSpec);
      }else{	    
    	cipher = Cipher.getInstance("AES");
        cipher.init(getMode(), skeySpec);
      }		
      return cipher.doFinal(source);
    }catch (Exception e) {
      Log.e("AESEncoder", "encode fail = " + e.toString());
	}
	return null;
  }
  
  
  
}
