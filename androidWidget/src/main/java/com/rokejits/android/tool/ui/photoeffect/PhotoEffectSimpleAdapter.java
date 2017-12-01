package com.rokejits.android.tool.ui.photoeffect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rokejits.android.tool.ui.photoeffect.PhotoEffectView.OnItemClickedListener;
import com.rokejits.android.tool.utils.BitmapUtils;
import com.rokejits.android.tool.widgets.R;

public class PhotoEffectSimpleAdapter implements PhotoEffectAdapter, OnItemClickedListener{
  	
  private static final String[] EFFECT_CHOICES_TITLE = {
	"Original",  
    "Contrast",
    "Sepia",
    "Grey Scale",
    "Lomo"
    
  };
  
  private View[] choiceViews;
  private Context context;
  private LayoutInflater lInflater;
  private Bitmap originalBitmap, thumbnailBitmap;
  private LinearLayout mainLayout;
  
  public PhotoEffectSimpleAdapter(Context context, PhotoEffectView photoEffectView, Bitmap originlBitmap){
    this.context = context;    
    lInflater = LayoutInflater.from(context);
    this.originalBitmap = originlBitmap;
    
    photoEffectView.setOnItemClickedListener(this);
    
  }
  	
  @Override	
  public int getChoiceCount(){
    return EFFECT_CHOICES_TITLE.length;	  
  }	
	
  @Override
  public View initChoice(int index) {
	if(choiceViews == null)
	  choiceViews = new View[EFFECT_CHOICES_TITLE.length];
	View view = choiceViews[index];
	if(view == null){
	  view = lInflater.inflate(R.layout.photoeffect_choice_layout, getChoiceLayout(), false);
	  ImageView imageView = (ImageView) view.findViewById(R.id.photoeffecct_choice_layout_imageview_sampleimage);
	  TextView titleView = (TextView) view.findViewById(R.id.photoeffecct_choice_layout_textview_sampleimagename);
	  if(thumbnailBitmap == null){
		int width, height, newWidth, newHeight;
		width = newWidth = originalBitmap.getWidth();
		height = newHeight = originalBitmap.getHeight();
		if(width > 60){
		  newWidth = 60;
		  newHeight = (int) (((float)newWidth / (float)width) * height);
		}
	    thumbnailBitmap  = BitmapUtils.resize(originalBitmap, newWidth, newHeight);
	  }
	  imageView.setImageBitmap(setEffect(thumbnailBitmap, index));
	  titleView.setText(EFFECT_CHOICES_TITLE[index]);
	  choiceViews[index] = view;
	}
	return view;
  }

  @Override
  public Bitmap onSetEffect(int effectIndex) {
		
	return setEffect(originalBitmap, effectIndex);
  }
  
  private Bitmap setEffect(Bitmap bitmap, int effectIndex){
    switch(effectIndex){
	  case 0:
	    return bitmap;	  
	  case 1:
	    return PhotoEffectUtils.contrastScale(bitmap, 50);
	  case 2:
	    return PhotoEffectUtils.convertToSepia(bitmap);
	  case 3:
		return PhotoEffectUtils.convertToGreyScale(bitmap);
	  case 4:
		Bitmap bmp_mask = BitmapFactory.decodeResource(context.getResources(), R.drawable.effect_lomo_mask);
		return PhotoEffectUtils.applyMask(bitmap, bmp_mask);
	    
	}
    return null;
  }

  @Override
  public LinearLayout getChoiceLayout() {
	
	return (LinearLayout) mainLayout.findViewById(R.id.photoeffect_layout_linearlayout_choice_layout);
  }

  @Override
  public ImageView getCenterImageView() {
	
	return (ImageView) mainLayout.findViewById(R.id.photoeffect_layout_imageview_sampleimage);
  }

  @Override
  public Bitmap getOriginalBitmap() {
	
	return originalBitmap;
  }

  @Override
  public View getContentView(ViewGroup viewGroup) {	
	if(mainLayout == null)
	  mainLayout = (LinearLayout) lInflater.inflate(R.layout.photoeffect_layout, viewGroup, false);
	return mainLayout;
  }

  @Override
  public void onItemClicked(View view, int index) {
    for(int i = 0;i < choiceViews.length;i++){
      View v = choiceViews[i];
      TextView tv = (TextView) v.findViewById(R.id.photoeffecct_choice_layout_textview_sampleimagename);
      if(i != index){
        tv.setVisibility(View.GONE);  	  
      }else{
    	tv.setVisibility(View.VISIBLE);  
      }	
    }
	
  }

}
