package com.rokejits.android.tool.data;

public interface DataReaderListener<T extends DataHolder> {
  public static final int EXTRACT_DATA_ERROR = 0xffffe0;
  public void onFinish(DataReader<T> reader);
}
