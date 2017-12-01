package com.rokejits.android.tool.ui.dialog.picturegetter;

import java.util.Hashtable;

import android.content.Context;

import com.rokejits.android.tool.ui.dialog.picturegetter.PictureGetter.PictureGetterListener;

public class PictureGetterManager {

  private Hashtable<String, PictureGetterOption> pictureGetterListenerMapping;	
  private static PictureGetterManager self = null;  
  
  public static PictureGetterManager getInstance(){
	if(self == null)
      self = new PictureGetterManager();	  
    return self;
  }
  
  private PictureGetterManager(){	
    pictureGetterListenerMapping = new Hashtable<String, PictureGetterOption>();	  
  }
  
  public PictureGetterOption getPictureGetterOption(String id){
    return pictureGetterListenerMapping.get(id);	  
  }
  
  protected String addPictureGetterOption(PictureGetterOption pGetter){
	String id = ""+pGetter.hashCode();
    pictureGetterListenerMapping.put(id, pGetter);
    return id;
  }
  
  public PictureGetterOption createPictureGetter(Context context, PictureGetterCropOption cropOption, boolean autoRotateToPortrait, PictureGetterListener picGetterListener){	
	PictureGetterOption pOption = new PictureGetterOption(context, cropOption, autoRotateToPortrait, picGetterListener);
	return pOption;
  }
  
  protected PictureGetterOption removePictureGetterOption(PictureGetterOption option){	
	  PictureGetterOption lis = pictureGetterListenerMapping.remove(""+option.hashCode());
    return lis;
    
  }
  
//  public Hashtable<String, PictureGetterOption> get(){
//    return pictureGetterListenerMapping;	  
//  }
  
}
