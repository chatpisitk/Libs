package com.rokejits.android.tool.widget;

import android.view.View;
import android.view.ViewGroup;

public interface TabAdapter {
  public int getCount();
  public View getView(ViewGroup root, int position);
  public void onViewSelected(View view, int position);
  public void onViewUnSelected(View view, int position);
}
