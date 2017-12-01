package com.rokejits.android.tool.ui.photoeffect;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;


public class PhotoEffectView extends LinearLayout implements OnClickListener {
  
  private PhotoEffectAdapter photoEffectAdapter;
  private OnItemClickedListener onItemClickedListener;
  	
  public PhotoEffectView(Context context){
    super(context);	  
  }
  
  public PhotoEffectView(Context context, AttributeSet attrs){
    super(context, attrs);	  
  }
  
  public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener){
    this.onItemClickedListener = onItemClickedListener;	  
  } 
  
  public void setPhotoEffectAdapter(PhotoEffectAdapter photoEffectAdapter){
    this.photoEffectAdapter = photoEffectAdapter;
    init();
  }
  
  private void init(){
    if(getChildCount() > 0)
      removeAllViews();
    addView(photoEffectAdapter.getContentView(this));
    for(int i = 0;i < photoEffectAdapter.getChoiceCount();i++){
      View view = photoEffectAdapter.initChoice(i);
      view.setTag(new Integer(i));
      view.setOnClickListener(this);
      photoEffectAdapter.getChoiceLayout().addView(view);
    }
    setCenterImageView(0);    
  }

  @Override
  public void onClick(View v) {
	int index = (Integer) v.getTag();
    setCenterImageView(index);
    if(onItemClickedListener != null)
      onItemClickedListener.onItemClicked(v, index); 
	
  }
  
  private void setCenterImageView(int effectIndex){
	  photoEffectAdapter.getCenterImageView().setImageBitmap(
              photoEffectAdapter.onSetEffect(effectIndex)
            );	  
  }
  
  
    
  public interface OnItemClickedListener{
    public void onItemClicked(View view, int index);	  
  }
  
  
  
  
	
}
