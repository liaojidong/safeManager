package com.dong.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

public class GrapeGridView extends GridView {

	public GrapeGridView(Context context) {
		super(context);
	}
	

    public GrapeGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public GrapeGridView(Context context, AttributeSet attrs, int defStyle) {
    	super(context, attrs, defStyle);
    }
	

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
		boolean flag = super.onTouchEvent(ev);
		if(ev.getAction() == MotionEvent.ACTION_MOVE){
				  this.setSelection(getTop());
		          flag =  true;
		}
		return flag;
    }
    


}
