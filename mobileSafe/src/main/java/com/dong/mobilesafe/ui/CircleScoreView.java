package com.dong.mobilesafe.ui;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.dong.mobilesafe.R;
import com.dong.mobilesafe.utils.StringUtils;

public class CircleScoreView extends View {
	private final int DEFAULT_FULL_SCROE = 100;
	private final int DEFAULT_THICKNESS = 20;
	private long fullScore = DEFAULT_FULL_SCROE;
	private Timer animTimer;
	private Paint mPaint;
	private Paint insideCirclePaint;
	private Paint arcPaint;
	private Paint pointsPaint;
	private long scores;
	private int radius;
	private int startAngle = 0;
	private long sweepAngle;
	private RectF oval = new RectF();
	private int thickness;
	private long currentAngle;
	private int circleColor;
	private float scoreTextSize;
	private int scoreTextColor;
	private String description;
	private Paint descriptionPaint;
	private float descTextSize;
	private float percent;
	private String unit;

	
	
	public CircleScoreView(Context context) {
		super(context);
		initParams();
	}
	
	
	
    public CircleScoreView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        TypedArray arr = context.obtainStyledAttributes(attrs,R.styleable.CircleScoreView);
        thickness = arr.getDimensionPixelSize(R.styleable.CircleScoreView_circleThickness, DEFAULT_THICKNESS);
        scores = arr.getInteger(R.styleable.CircleScoreView_scores, 0);
        fullScore = arr.getInt(R.styleable.CircleScoreView_fullScore, DEFAULT_FULL_SCROE);
        radius = arr.getDimensionPixelSize(R.styleable.CircleScoreView_circleRadius, 0);
        circleColor = arr.getColor(R.styleable.CircleScoreView_circleColor, getResources().getColor(R.color.color_green));
        scoreTextSize = arr.getDimension(R.styleable.CircleScoreView_scoreTextSize, 64f);
        scoreTextColor = arr.getColor(R.styleable.CircleScoreView_scoreTextColor, Color.GREEN);
        description = arr.getString(R.styleable.CircleScoreView_description);
        descTextSize = arr.getDimension(R.styleable.CircleScoreView_descTextSize, 32f);
        unit = arr.getString(R.styleable.CircleScoreView_unit);
        arr.recycle();
        initParams();
        
    }
	
	
	
	private void initParams() {
		mPaint = new Paint();
		mPaint.setColor(Color.GRAY);
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		
		insideCirclePaint = new Paint();
		insideCirclePaint.setAntiAlias(true);
		insideCirclePaint.setColor(Color.WHITE);
		insideCirclePaint.setStyle(Paint.Style.FILL);
		
		arcPaint = new Paint();
		arcPaint.setAntiAlias(true);
		arcPaint.setColor(circleColor);
		arcPaint.setStyle(Paint.Style.FILL);
		
		pointsPaint = new Paint();
		pointsPaint.setAntiAlias(true);
		pointsPaint.setColor(scoreTextColor);
		pointsPaint.setTextSize(scoreTextSize);
		
		
		
		
		descriptionPaint = new Paint();
		descriptionPaint.setAntiAlias(true);
		descriptionPaint.setColor(Color.GRAY);
		descriptionPaint.setTextSize(descTextSize);
		
		sweepAngle = (360*scores)/fullScore;
		
		animTimer = new Timer(true);
		animTimer.schedule(new AnimTask(), 30);
	}
	
	
	class AnimTask extends TimerTask {
		@Override
		public void run() {
			while(true) {
				while(currentAngle < sweepAngle) {
					currentAngle += 1;
					if(currentAngle >= sweepAngle) {
						currentAngle = sweepAngle;
					}
					try {
						Thread.sleep(2);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					postInvalidate();
				}
				while(currentAngle > sweepAngle) {
					currentAngle -= 1;
					if(currentAngle <= sweepAngle) {
						currentAngle = sweepAngle;
					}
					try {
						Thread.sleep(2);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					postInvalidate();
				}
				
				try {
					Thread.sleep(150);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
	}
	
	
	
	private int measureHeight(int measureSpec) {

		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		int result = (radius*2)+(getPaddingTop()+getPaddingBottom());
		if (specMode == MeasureSpec.AT_MOST){
			result = Math.min(result, specSize);
		}else if (specMode == MeasureSpec.EXACTLY){
			result = Math.min(result, specSize);
		}

		radius = (int) ((result - (getPaddingTop()+getPaddingBottom()))/2f);
		
		return result;
	}
	
	private int measureWidth(int measureSpec) {
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		int result = (radius*2)+(getPaddingLeft()+getPaddingRight());
		if (specMode == MeasureSpec.AT_MOST){
			result = Math.min(result, specSize);
		}

		else if (specMode == MeasureSpec.EXACTLY){
			result = specSize;
		}

		return result;
	}
	
	



	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		percent = (float)scores/fullScore;
		percent = percent *100f;
		sweepAngle = (360*scores)/fullScore;
		
		String front = "";
		if(TextUtils.isEmpty(unit)) {
			String percentStr = String.format(Locale.CHINA,"%.1f", percent);
			front = percentStr+"%";
		}else {
			front = scores+"";
			front = front + unit;
		}

		final float cx = getWidth()/2f;
		final float cy = getHeight()/2f;
		
		
		canvas.drawCircle(cx, cy, radius, mPaint);
		oval.set(cx-radius, cy-radius, cx+radius, cy+radius);
		canvas.drawArc(oval, startAngle, currentAngle, true, arcPaint);
		canvas.drawCircle(cx, cy, (radius-thickness), insideCirclePaint);
		
		float frontHeight = FrontUtil.getFontHeight(pointsPaint);
		float txtWidth = FrontUtil.getStingWidth(pointsPaint,front);
		canvas.drawText(front, cx -(txtWidth/2f), cy+(frontHeight/2f), pointsPaint);
		if(!StringUtils.isEmpty(description)){
			canvas.drawText(description, cx-FrontUtil.getStingWidth(descriptionPaint,description)/2, cy-frontHeight/2, descriptionPaint);
		}

		
	}


	

	public void setFullScore(long fullScore) {
		if(this.fullScore != fullScore) {
			this.fullScore = fullScore;
			invalidate();
		}

	}



	public void setscores(long points) {
		if(this.scores != points) {
			this.scores = points;
			postInvalidate();
		}
		
	}



	public void setRadius(int radius) {
		if(this.radius != radius) {
			this.radius = radius;
			postInvalidate();
		}
		
	}



	public void setThickness(int thickness) {
		if(this.thickness != thickness) {
			this.thickness = thickness;
			invalidate();
		}
		
	}

}
