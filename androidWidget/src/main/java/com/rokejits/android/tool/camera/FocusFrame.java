package com.rokejits.android.tool.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.rokejits.android.tool.camera.AutoFocusManager.OnAutoFocusListener;
import com.rokejits.android.tool.os.UIHandler;

public class FocusFrame extends CameraManagerFrame implements OnAutoFocusListener{

  
  private int focusFrameColor;
  //  Rect bounds;
//  private Rect previewFrame;
//  private Rect rect;
  
	   
  
  private static final int FOCUS_FRAME_WIDTH 	= 40;
  private static final int FOCUS_FRAME_HEIGHT 	= 30;
 
  private static final int FOCUS_FRAME_TOUCH_MULTIPLY 	= 5;
  private static final int FOCUS_FRAME_NORMAL_MULTIPLY 	= 3;
  private static final int FOCUS_FRAME_MAX_MULTIPLY 	= 8;
  
  private boolean isDrawFocusFrame;
  private boolean onProcessAutoFocus;
  private RectF focusFrameRect;  	
  private boolean isSupporAutoFocusArea;	
  private boolean isStartAutoFocus;
  
  public FocusFrame(Context context, AttributeSet attrs) {
	super(context, attrs);
	
	isSupporAutoFocusArea = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    
	float focusFrameWidth = FOCUS_FRAME_WIDTH * FOCUS_FRAME_NORMAL_MULTIPLY;
	float focusFrameHeight = FOCUS_FRAME_HEIGHT * FOCUS_FRAME_NORMAL_MULTIPLY;
    focusFrameRect = new RectF(0, 0, focusFrameWidth, focusFrameHeight);  
    paint.setStyle(Style.STROKE);
    paint.setStrokeWidth(5);
  }
  
  @Override
  public void setCameraManager(CameraManager cameraManager) {
    super.setCameraManager(cameraManager);
    startAutoFocus();
  }
  
  public void startAutoFocus(){
    if(isStartAutoFocus)
      return;
    isStartAutoFocus = true;
    cameraManager.registerAutoFocusListener(this);
  }
  
  public void stopAutoFocus(){
    if(!isStartAutoFocus)
      return;
    isStartAutoFocus = false;
    cameraManager.unRegisterAutoFocusListener(this);
    new UIHandler().postDelayed(new Runnable() {		
  	  @Override
  	  public void run() {
  	    isDrawFocusFrame = false;
  	    onProcessAutoFocus = false;
  	    invalidate();			
  	  }
  	}, 350);
  }
  
  protected void onCloseDriver(){
    if(cameraManager != null)
      cameraManager.unRegisterAutoFocusListener(this);
    onProcessAutoFocus = false;
    isDrawFocusFrame = false;
    isStartAutoFocus = false;
    postInvalidate();
  }
  
  @Override
  public boolean onTouchEvent(MotionEvent event) {
	if(onProcessAutoFocus || !cameraManager.isSupportAutoFocus())
	  return false;
	int action = event.getAction();
	float pX = event.getX();
	float pY = event.getY();	
	
	if(!isSupporAutoFocusArea){
	  pX = getWidth()/2;
	  pY = getHeight()/2;
	}
	
	float focusFrameWidth = FOCUS_FRAME_WIDTH * FOCUS_FRAME_TOUCH_MULTIPLY;
	float focusFrameHeight = FOCUS_FRAME_HEIGHT * FOCUS_FRAME_TOUCH_MULTIPLY;  	
	focusFrameRect.left = pX - focusFrameWidth/2;
    focusFrameRect.right = pX + focusFrameWidth/2;
    focusFrameRect.top = pY - focusFrameHeight/2;
	focusFrameRect.bottom = pY + focusFrameHeight/2;
	switch(action){
	  case MotionEvent.ACTION_DOWN:
	    focusFrameColor = Color.WHITE;
	    isDrawFocusFrame = true;
	  break;	  
	  case MotionEvent.ACTION_UP:
		focusFrameWidth = FOCUS_FRAME_WIDTH * FOCUS_FRAME_NORMAL_MULTIPLY; 
		focusFrameHeight = FOCUS_FRAME_HEIGHT * FOCUS_FRAME_NORMAL_MULTIPLY;
	    RectF focusArea = new RectF(); 
	    focusArea.left = pX - focusFrameWidth/2;
	    focusArea.right = pX + focusFrameWidth/2;
	    focusArea.top = pY - focusFrameHeight/2;
	    focusArea.bottom = pY + focusFrameHeight/2;	    
	    onProcessAutoFocus = true;
	    cameraManager.requestAutoFocus(100, focusArea);	    
	  break;
	}
	
   
	
	postInvalidate();
	return true;
  }
  
  @Override
  public void onDraw(Canvas canvas) {	
    if(isDrawFocusFrame){      
      paint.setColor(focusFrameColor);
      canvas.drawRect(focusFrameRect, paint);          
    }   
    
  }

  @Override
  public void onStartAutoFocus(PointF center) {
    focusFrameColor = Color.WHITE;
    onProcessAutoFocus = true;
    isDrawFocusFrame = true;
    
    float width = focusFrameRect.width();
    float height = focusFrameRect.height();
    
    focusFrameRect.left = center.x - width/2;
    focusFrameRect.right = center.x + width/2;
    focusFrameRect.top = center.y - height/2;
    focusFrameRect.bottom = center.y + height/2;
    
//    Log.e(TAG, "center.x = " + center.x);
//    Log.e(TAG, "center.y = " + center.y);
//    Log.e(TAG, "focusFrameRect.left = " + focusFrameRect.left);
//    Log.e(TAG, "focusFrameRect.right = " + focusFrameRect.right);
//    Log.e(TAG, "focusFrameRect.top = " + focusFrameRect.top);
//    Log.e(TAG, "focusFrameRect.bottom = " + focusFrameRect.bottom);
    
    //postInvalidate();
    //new Thread(new FocusFrameAnimate()).start();
    
    new UIHandler().postDelayed(new FocusFrameAnimate(), 50);
  }

  @Override
  public void onAutoFocus(boolean success) {
    if(success){
      focusFrameColor = Color.GREEN;	
    }else{
      focusFrameColor = Color.RED;	
    } 
    postInvalidate();
    new UIHandler().postDelayed(new Runnable() {		
	  @Override
	  public void run() {
	    isDrawFocusFrame = false;
	    onProcessAutoFocus = false;
	    invalidate();			
	  }
	}, 350);
    
  }
  
  class FocusFrameAnimate implements Runnable{
	private int multiply = 1; 
	private boolean running = true;
    @Override
    public void run() {
      //while(running){
    	if(!running)
    	  return;
        float current = focusFrameRect.width() / FOCUS_FRAME_WIDTH;  
        current = current + multiply;
        if(multiply > 0){
          if(current > FOCUS_FRAME_MAX_MULTIPLY){
            current = FOCUS_FRAME_MAX_MULTIPLY;
            multiply = -1;
          }  	
        }else{
          if(current < FOCUS_FRAME_NORMAL_MULTIPLY){
            current = FOCUS_FRAME_NORMAL_MULTIPLY;
            running = false;
            if(!cameraManager.isAutoFocusManagerActive()){
              isDrawFocusFrame = false;
              onProcessAutoFocus = false;
            }
          }	
        }
        
        float frameWidth = FOCUS_FRAME_WIDTH * current;
        float frameHeight = FOCUS_FRAME_HEIGHT * current;
        
        float centerX = focusFrameRect.left + focusFrameRect.width() / 2;
        float centerY = focusFrameRect.top + focusFrameRect.height() / 2;
        
        focusFrameRect.left = centerX - frameWidth / 2;
        focusFrameRect.right = centerX + frameWidth / 2;
        focusFrameRect.top = centerY - frameHeight / 2;
        focusFrameRect.bottom = centerY + frameHeight / 2;
        
        postInvalidate();
        new UIHandler().postDelayed(this, 50);
      } 	
    //}     	  
  }

}
