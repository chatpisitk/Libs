package com.rokejits.android.tool.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;



public class DeviceUtils {
	 
  public static final String getMacAddress(Context context){
    WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    WifiInfo info = manager.getConnectionInfo();
	return info.getMacAddress();	  
  }
	
	
  public static final String getIMEI(Context context){
	try{
	  TelephonyManager mTelephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
	  return mTelephonyMgr.getDeviceId().replaceAll(" ", "");
	}catch(Exception e){
	  e.printStackTrace();
	  return "";
	}
  }
  
//  public static String getMacAddress(Context context){
//    WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//    WifiInfo info = manager.getConnectionInfo();
//	String address = info.getMacAddress();
//	return address;
//  }
	
  public static final String getSignatureKey(Context context){
    PackageInfo info;
	try {
	info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
	for (Signature signature : info.signatures) {
	           MessageDigest md;
	md = MessageDigest.getInstance("SHA");
	md.update(signature.toByteArray());
	           //String something = new String(Base64.encode(md.digest(), 0));
	            String something = new String(Base64.encode(md.digest(), Base64.DEFAULT));
	            Log.e("AndroidUtils","hash key = " + something);
	            return something;
	           
	} 
	}
	catch (NameNotFoundException e1) {
	  Log.e("AndroidUtils","name not found = " + e1.toString());
	}
	catch (NoSuchAlgorithmException e) {
	  Log.e("AndroidUtils","no such an algorithm = " + e.toString());
	}
	catch (Exception e){
	  Log.e("AndroidUtils","exception = " + e.toString());
	}
    return null;	
  }	
  
  public static final String getReleaseOSVersion(){
    return Build.VERSION.RELEASE;	  
  }
}
