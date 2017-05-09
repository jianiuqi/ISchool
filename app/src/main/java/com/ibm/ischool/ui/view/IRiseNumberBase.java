package com.ibm.ischool.ui.view;

public interface IRiseNumberBase {

	public void start();  
    public RiseNumberTextView withNumber(float number);  
    public RiseNumberTextView withNumber(int number);  
    public RiseNumberTextView setDuration(long duration);  
    public void setOnEnd(RiseNumberTextView.EndListener callback);  
}
