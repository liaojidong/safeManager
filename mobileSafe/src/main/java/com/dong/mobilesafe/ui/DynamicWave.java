
package com.dong.mobilesafe.ui;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import com.dong.mobilesafe.R;
import com.dong.mobilesafe.utils.thread.GlobalThreadPool;

public class DynamicWave extends View {
    private Executor singleTHread = Executors.newSingleThreadExecutor();
    private static int INIT_WAVE_AMPLITUDE;
    // 波纹颜色
    private static final int WAVE_PAINT_COLOR = 0x33FFFFFF;
    // y = Asin(wx+b)+h
    private int waveAmplitude;
    private int offsetY;
    private float percentY;
    private float mCycleFactorW;

    private int mTotalWidth, mTotalHeight;
    private float[] mYPositions;
    private float[] mResetOneYPositions;
    private int mXOffsetSpeedOne;
    private int mXOneOffset;

    private Paint mWavePaint;
    private DrawFilter mDrawFilter;
    private static Handler mHandler = new Handler(Looper.getMainLooper());

    public DynamicWave(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.DynamicWave);
        percentY = arr.getFloat(R.styleable.DynamicWave_wave_percent, 0.4f);
        mXOffsetSpeedOne = arr.getDimensionPixelOffset(R.styleable.DynamicWave_wave_speed, 4);
        INIT_WAVE_AMPLITUDE = arr.getDimensionPixelOffset(R.styleable.DynamicWave_wave_amplitude, 6);
        waveAmplitude = INIT_WAVE_AMPLITUDE;
        arr.recycle();
        initPaint();
    }

    private void initPaint() {
        // 初始绘制波纹的画笔
        mWavePaint = new Paint();
        // 去除画笔锯齿
        mWavePaint.setAntiAlias(true);
        // 设置风格为实线
        mWavePaint.setStyle(Style.FILL);
        // 设置画笔颜色
        mWavePaint.setColor(WAVE_PAINT_COLOR);
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    private Object lock = new Object();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 从canvas层面去除绘制时锯齿
        canvas.setDrawFilter(mDrawFilter);
        synchronized (lock) {
            resetPositonY();
            for (int i = 0; i < mTotalWidth; i++) {
                // 绘制水波纹
                canvas.drawLine(i, mResetOneYPositions[i] + offsetY, i,
                        mTotalHeight,
                        mWavePaint);
            }
            // 改变两条波纹的移动点
            mXOneOffset += mXOffsetSpeedOne;

            // 如果已经移动到结尾处，则重头记录
            if (mXOneOffset >= mTotalWidth) {
                mXOneOffset = 0;
            }
        }
        // 引发view重绘，一般可以考虑延迟20-30ms重绘，空出时间片
        updateView();
    }

    private void updateView() {
        GlobalThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(35);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                postInvalidate();
            }
        });
    }


    private  void  resetPositonY() {
        // mXOneOffset代表当前第一条水波纹要移动的距离
        int yOneInterval = mYPositions.length - mXOneOffset;
        // 使用System.arraycopy方式重新填充第一条波纹的数据
        System.arraycopy(mYPositions, mXOneOffset, mResetOneYPositions, 0, yOneInterval);
        System.arraycopy(mYPositions, 0, mResetOneYPositions, yOneInterval, mXOneOffset);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        generatePosition();
    }

    private void generatePosition() {
        // 记录下view的宽高
        mTotalWidth = getWidth();
        mTotalHeight = getHeight();
        // 用于保存原始波纹的y值
        mYPositions = new float[mTotalWidth];
        // 用于保存波纹一的y值
        mResetOneYPositions = new float[mTotalWidth];
        // 将周期定为view总宽度
        mCycleFactorW = (float) (2 * Math.PI / mTotalWidth);
        offsetY = (int) ((1.0f - percentY) * mTotalHeight);
        // 根据view总宽度得出所有对应的y值
        for (int i = 0; i < mTotalWidth; i++) {
            mYPositions[i] = (float) (waveAmplitude * Math.sin(mCycleFactorW * i));
        }
    }


    public interface OnWaveListener {
        /**
         * 波浪处于平静状态
         */
        public void onWaveQuite();
    }


    public void setOnWaveListener(OnWaveListener listener) {
        this.onWaveListener = listener;
    }


    public boolean isQuite() {
        return isQuite;
    }

    private boolean isQuite = true;
    private OnWaveListener onWaveListener;
    public void setWaveAmplitude(final int offsetAmplitude) {
        if (!isQuite) return;
        isQuite = false;
        GlobalThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                int offset = offsetAmplitude;
                if (offset < 0) return;
                while (offset > 0) {
                    synchronized (lock) {
                        DynamicWave.this.waveAmplitude = INIT_WAVE_AMPLITUDE + offset;
                        offset -= 2;
                        generatePosition();
                        resetPositonY();
                    }
                    postInvalidate();
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                DynamicWave.this.waveAmplitude = INIT_WAVE_AMPLITUDE;
                postInvalidate();
                isQuite = true;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(onWaveListener!= null) {
                            onWaveListener.onWaveQuite();
                        }
                    }
                });
            }

        });

    }


    public void setHeightPercent(float percent) {
        percentY = percent;
        int offsetY = (int) ((1.0f - percentY) * mTotalHeight);
        int distance = Math.abs(offsetY - this.offsetY);
        for (int i = 0; i < distance; i++) {
            if (offsetY > this.offsetY) {
                this.offsetY += 1;
            } else if (offsetY < this.offsetY) {
                this.offsetY -= 1;
            }
            invalidate();
        }
    }

}
