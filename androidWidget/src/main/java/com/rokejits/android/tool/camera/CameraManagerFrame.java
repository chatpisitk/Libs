package com.rokejits.android.tool.camera;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CameraManagerFrame extends View{

  protected CameraManager cameraManager;
  protected Paint paint;
	
  public CameraManagerFrame(Context context) {
	super(context);
	init();
	
  } 
  
  public CameraManagerFrame(Context context, AttributeSet attrs) {
	super(context, attrs);
	init();
	
  }
  
  private void init(){
  // Initialize these once for performance rather than calling them every time in onDraw().
    paint = new Paint(Paint.ANTI_ALIAS_FLAG);
  }
  
  public void setCameraManager(CameraManager cameraManager){
    this.cameraManager = cameraManager;	  
  }	
	
}
