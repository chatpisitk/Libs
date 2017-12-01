package com.rokejits.android.tool.ui.photoeffect;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public interface PhotoEffectAdapter {
  public View getContentView(ViewGroup viewGroup);
  public LinearLayout getChoiceLayout();
  public ImageView getCenterImageView();
  public Bitmap getOriginalBitmap();	
  public int getChoiceCount();	
  public View initChoice(int index);  
  public Bitmap onSetEffect(int effectIndex);
}
