package com.dong.mobilesafe.ui;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.dong.mobilesafe.R;
import com.dong.mobilesafe.utils.DensityUtil;


public class ScrollerLinearLayout extends LinearLayout {
	private static final int DEFAULT_INTERVAL_SPACE = 2;
	private static final int DEFAULT_LINE_HEIGHT = 3;
	
	private int lineHeight;
	private Scroller mScroller;
	private int curScrollX;
	private int curScrolly;
	protected ImageView tabLine;
	private final int  duration = 250;
	protected int screenWidth;
	protected int screenHeight;
	protected float density;
	private int tabCount;
	
	private int tabInditorColor;
	
	public ScrollerLinearLayout(Context context) {
		super(context);
		init();
	}
	
	
	private void init() {
		setScrrenParms();
		mScroller = new Scroller(getContext());
		lineHeight = DEFAULT_LINE_HEIGHT;
		tabInditorColor = getResources().getColor(R.color.white);
	}


	private void setScrrenParms() {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		density = dm.density;
	}
	
	

	
	
	public void initTabLine(int tabCount) {
		if(tabCount < 0) {
			throw new IllegalArgumentException("tabCount must be greater than 0");
		}
		this.tabCount = tabCount;
		tabLine = new ImageView(getContext());
		tabLine.setId(R.id.ID_TAB_LINE);
		tabLine.setBackgroundColor(tabInditorColor);
		tabLine.setScaleType(ScaleType.MATRIX);
		this.addView(tabLine, new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT));
		android.view.ViewGroup.LayoutParams params = tabLine.getLayoutParams();
		params.height = dipToPx(lineHeight);
		params.width = (screenWidth - dipToPx(DEFAULT_INTERVAL_SPACE) *(tabCount-1)) / tabCount;
		tabLine.setLayoutParams(params);
	}
	
	
	
	public void move(int position ,int positionOffsetPixels) {
	
		int extra =(dipToPx(DEFAULT_INTERVAL_SPACE))*position;
		final int dx = -((position) * (tabLine.getWidth())+(positionOffsetPixels / tabCount)+extra);
		this.moveTo(dx,this.getScrollY());
		
	}
	
	
	
	public void scroollToPosition(int position) {
		final int toX = -(position *tabLine.getWidth()+position * dipToPx(DEFAULT_INTERVAL_SPACE));
		this.smoothScrollTo(toX,this.getScrollY());
	}
	
	
    public void smoothScrollTo(int x, int y) {  
    	  int dx = x - curScrollX; 
          int dy = y - curScrolly; 
          smoothScrollBy(dx, dy); 
    }  
    
    
    
    public void smoothScrollBy(int dx, int dy) {  
        mScroller.startScroll(curScrollX, curScrolly, dx, dy,duration);  
        invalidate();
    }  
    
    
    public void moveTo(int x,int y) {
    	curScrollX = x;
    	curScrolly = y;
    	this.scrollTo(x, y);

    }
    
    
    @Override
    public void computeScroll() {
    	if (mScroller.computeScrollOffset()) {  
    		this.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());  
    		invalidate();
    	}
    	super.computeScroll(); 
    }
    
	public int dipToPx(int dip) {
		return DensityUtil.dip2px(getContext(), dip);
	}

}
