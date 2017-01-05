package com.dong.mobilesafe;

import java.util.List;

import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.dong.mobilesafe.base.BaseActivity;
import com.dong.mobilesafe.base.BaseTitleActivity;
import com.dong.mobilesafe.ui.LockPatternView;
import com.dong.mobilesafe.ui.LockPatternView.Cell;
import com.dong.mobilesafe.ui.LockPatternView.DisplayMode;


public abstract class LockActivity extends BaseActivity implements
        LockPatternView.OnPatternListener {
    private static final String TAG = "LockActivity";
    protected TextView tipTV;
    private List<Cell> lockPattern;
    private LockPatternView lockPatternView;
    protected ImageView iconIV;

	@Override
	protected int onGetContentView() {
		return R.layout.activity_lock;
	}
	

    @Override
    protected void onInitData() {
        super.onInitData();
        String patternString = getPaternStr();
        if (TextUtils.isEmpty(patternString)) {
            startActivity(LockSetupActivity.class);
            LockActivity.this.finish();
            return;
        }
        lockPattern = LockPatternView.stringToPattern(patternString);
    }

    /**
	 * 获取正确的手势的密码
	 * @return 
	 */
	protected abstract String getPaternStr();


    @Override
    protected void onPrepareView() {
        super.onPrepareView();
        setEnableSlidFinish(false);
        lockPatternView = (LockPatternView) findViewById(R.id.lock_pattern);
        tipTV = (TextView) findViewById(R.id.tv_lock_tip);
        iconIV = (ImageView) findViewById(R.id.iv_lock_logo);
        lockPatternView.setOnPatternListener(this);
    }





    @Override
    public void onPatternStart() {
        Log.d(TAG, "onPatternStart");
    }

    @Override
    public void onPatternCleared() {
        Log.d(TAG, "onPatternCleared");
    }

    @Override
    public void onPatternCellAdded(List<Cell> pattern) {
        Log.d(TAG, "onPatternCellAdded");
        Log.e(TAG, LockPatternView.patternToString(pattern));
    }

    @Override
    public void onPatternDetected(List<Cell> pattern) {
        Log.d(TAG, "onPatternDetected");

        if (pattern.equals(lockPattern)) {
        	onPasswordCorrect();

        } else {
            lockPatternView.setDisplayMode(DisplayMode.Wrong);
            lockPatternView.disableInput();
            tipTV.setText(getString(R.string.password_error));
            tipTV.setTextColor(Color.RED);
            Animation shakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake);
            tipTV.startAnimation(shakeAnim);
            lockPatternView.postDelayed(new Runnable() {
				@Override
				public void run() {
					lockPatternView.clearPattern();
					lockPatternView.enableInput();
				}
			}, 1500);
        }

    }
    
    
    /**
     * 当手势正确的时候回调该方法
     */
    protected void onPasswordCorrect() {
    	
    }



}
