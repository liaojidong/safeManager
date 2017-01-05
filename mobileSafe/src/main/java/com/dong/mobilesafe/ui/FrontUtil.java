package com.dong.mobilesafe.ui;

import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;

public class FrontUtil {
	
	/**
	 * 获取字体的高度
	 * @param fontSize
	 * @return
	 */
	public static float getFontHeight(Paint paint)  
	{  
	    FontMetrics fm = paint.getFontMetrics();  
	    return (float) Math.ceil(fm.descent - fm.top);  
	}  
	
	
	public static float getStingWidth(Paint paint,String txt) {
		 
		return paint.measureText(txt);  
		
	}
}
