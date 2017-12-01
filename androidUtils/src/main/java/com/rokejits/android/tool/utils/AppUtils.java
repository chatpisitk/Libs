package com.rokejits.android.tool.utils;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

public class AppUtils {

	
  public static final void openBrowser(Context context, String url){
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(url));
    context.startActivity(intent);	  
  }	
	
  public static final String getUniqueId(){
    Random random = new Random();
    String id = Math.abs(random.nextLong()) + "_" + Math.abs(random.nextLong());
    return id;
  }	
	
  public static String getAppVersion(Context context) throws NameNotFoundException{
    return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;	  
  }	
  
  
  public static final boolean isDebugKeyStore(Context context){
	String myDebugKeySotre = null, keyStore = null;   
	myDebugKeySotre = getMetaData(context, context.getString(R.string.debug_hash_key));
	keyStore = getSignKey(context);   
//	Log.e("AndroidUtils","myDebugKeySotre = |"+myDebugKeySotre+"|");
//	Log.e("AndroidUtils","keyStore = |"+keyStore+"|");
//	Log.e("AndroidUtils","myDebugKeySotre = |"+myDebugKeySotre+"|");
//	Log.e("AndroidUtils","keyStore = |"+keyStore+"|");
	//Log.e("myDebugKeySotre.equalsIgnoreCase(keyStore) = "+myDebugKeySotre.equalsIgnoreCase(keyStore));
    if(myDebugKeySotre == null || keyStore == null || !myDebugKeySotre.equalsIgnoreCase(keyStore)){
      return false;	
    }    
    return true;
    
  }
  
  public static final String getMetaData(Context context, String metaName){
    String value = null;
	try {
	  ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
	  Bundle bundle = ai.metaData;
	  //value = bundle.getString(context.getString(R.string.debug_hash_key));
	  value = bundle.getString(metaName);
	  
	} catch (NameNotFoundException e) {
	  Log.e("AndroidUtils","Failed to load meta-data, NameNotFound: " + e.getMessage());
	} catch (NullPointerException e) {
      Log.e("AndroidUtils","Failed to load meta-data, NullPointer: " + e.getMessage());         
	}
	return value;
  }
  
  
  public static final String getSignKey(Context context){
    PackageInfo info;
	try {
	  info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
	  for (Signature signature : info.signatures) {
	    MessageDigest md;
	    md = MessageDigest.getInstance("SHA");
	    md.update(signature.toByteArray());
	    String something = new String(Base64.encode(md.digest(), 0));
	    //String something = new String(Base64.encodeBytes(md.digest()));
	    
	    return something.trim();
	  }
	} catch (NameNotFoundException e1) {
	  Log.e("AndroidUtils","name not found " + e1.toString());
	} catch (NoSuchAlgorithmException e) {
	  Log.e("AndroidUtils","no such an algorithm " + e.toString());
	} catch (Exception e) {
	  Log.e("AndroidUtils","exception " + e.toString());
	} 
	return null;
  }
  
  
  public static boolean isAvailable(Context ctx, Intent intent) {	  
    final PackageManager mgr = ctx.getPackageManager();	  
    List<ResolveInfo> list = mgr.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);  
    return list.size() > 0;
	  
  }

  
  public static final void viewPdf(Context context, File pdfFile){
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setDataAndType(Uri.fromFile(pdfFile), "application/pdf");
    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    context.startActivity(intent); 	  
  }
  
	
}

