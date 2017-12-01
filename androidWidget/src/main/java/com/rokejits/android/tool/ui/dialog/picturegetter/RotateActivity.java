package com.rokejits.android.tool.ui.dialog.picturegetter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.rokejits.android.tool.data.StoreDataForActivityManager;
import com.rokejits.android.tool.widgets.R;

public class RotateActivity extends Activity implements OnClickListener, OnGlobalLayoutListener{
	
  public static final String ORIGINAL_BITMAP = "ORIGINAL_BITMAP";	
  
  private static final int ANGLE_0 		= 0;
  private static final int ANGLE_90 	= 1;
  private static final int ANGLE_180 	= 2;
  private static final int ANGLE_270 	= 3;
  
  
  private static final int[] ANIM_LIST = {
    R.anim.rotate_0_90,
    R.anim.rotate_90_180,
    R.anim.rotate_180_270,
    R.anim.rotate_270_360
  };
  
  private ImageView imageView;
  private int rotateAngle = 0;
  private Bitmap originalBitmap, adjustedBitmap;
  private View rotateBtn, okBtn;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.rotate_activity);
	int id = getIntent().getIntExtra(ORIGINAL_BITMAP, -1);
	if(id != -1){
	  originalBitmap = (Bitmap) StoreDataForActivityManager.getInstance().get(id);	
	}
	imageView = (ImageView) findViewById(R.id.rotate_activity_imageview_image);
	rotateBtn = findViewById(R.id.rotate_activity_button_rotate);
	okBtn =findViewById(R.id.rotate_activity_button_ok);
	
	if(originalBitmap != null)
	  imageView.setImageBitmap(originalBitmap);
	//imageView.getViewTreeObserver().addOnGlobalLayoutListener(this);
	rotateBtn.setOnClickListener(this);
	okBtn.setOnClickListener(this);
  }
  @Override
  public void onBackPressed() {
	response(RESULT_CANCELED, originalBitmap);
  }

  @Override
  public void onClick(View view) {
	if(view.equals(rotateBtn)){
	  
	  
	  RotateAnimation rotateAnim = (RotateAnimation) AnimationUtils.loadAnimation(this, ANIM_LIST[rotateAngle]);
	  rotateAnim.setFillAfter(true);  
	  
	  imageView.startAnimation(rotateAnim);
	  rotateAngle++;
	  if(rotateAngle >= 4)
	    rotateAngle = 0;
    }else if(view.equals(okBtn)){
      Bitmap result = null;
      if(rotateAngle == 0){
        result = originalBitmap;	  
      }else{
    	int angle = 0;
    	switch(rotateAngle){
    	  case ANGLE_90:
    	    angle = 90;	  
    	  break;
    	  case ANGLE_180:
      	    angle = 180;	  
      	  break;
    	  case ANGLE_270:
      	    angle = 270;	  
      	  break;
    	  
    	}
        Matrix matrix = new Matrix();
        matrix.preRotate(angle);
    	
    	result = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true); 
    	//originalBitmap.recycle();
      }
      
      response(RESULT_OK, result);     
    }
	
  }
  
  private void response(int resultCode, Bitmap bitmap){ 
    int id = bitmap.hashCode();
    StoreDataForActivityManager.getInstance().put(id, bitmap);
    Intent intent = new Intent();
    intent.putExtra(ORIGINAL_BITMAP, id);
    setResult(resultCode, intent);
    finish();	  
  }
  
  @Override
  public void onGlobalLayout() {
	int width = imageView.getWidth();
	int height = imageView.getHeight();
	
	int expectSize = width <= height?width:height;
    RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
	param.width = expectSize;
	param.height = expectSize;
	imageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
	imageView.requestLayout();
	
	
	
  }
  
  
  
}