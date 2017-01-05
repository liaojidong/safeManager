package com.dong.mobilesafe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.dong.mobilesafe.base.BaseActivity;
import com.dong.mobilesafe.constant.SpKey;
import com.dong.mobilesafe.service.AppLockService;
import com.dong.mobilesafe.ui.LockPatternView;
import com.dong.mobilesafe.ui.LockPatternView.Cell;
import com.dong.mobilesafe.ui.LockPatternView.DisplayMode;
import com.dong.mobilesafe.utils.SharedPreferencesManager;


public class LockSetupActivity extends BaseActivity implements
        LockPatternView.OnPatternListener {

    private static final String TAG = "LockSetupActivity";
    private LockPatternView lockPatternView;
    private TextView tipTV;

    private static final int STEP_1 = 1; // 开始
    private static final int STEP_2 = 2; // 第一次设置手势完成
    private static final int STEP_3 = 3; // 按下继续按钮
    private static final int STEP_4 = 4; // 第二次设置手势完成
    private int step = -1;

    private List<Cell> choosePattern;
    private boolean confirm = false;


    @Override
    protected int onGetContentView() {
        return R.layout.activity_lock_setup;
    }


    @Override
    protected void onPrepareView() {
        super.onPrepareView();
        setEnableSlidFinish(false);
        tipTV = (TextView) findViewById(R.id.tv_tip);
        lockPatternView = (LockPatternView) findViewById(R.id.lock_pattern);

        lockPatternView.setOnPatternListener(this);
        step = STEP_1;
        updateView();
    }

    private void updateView() {
        switch (step) {
            case STEP_1:    //开始
                choosePattern = null;
                confirm = false;
                lockPatternView.enableInput();
                break;
            case STEP_2:    // 第一次设置手势完成
                tipTV.setText("再次绘制图案");
                tipTV.setTextColor(Color.WHITE);
                lockPatternView.enableInput();
                break;
            case STEP_3:
                lockPatternView.enableInput();
                break;
            case STEP_4:
                if (confirm) {
                    lockPatternView.disableInput();
                    finishSetup();
                } else {
                    tipTV.setText("与上一次绘制不一致，请重新绘制");
                    tipTV.setTextColor(Color.RED);
                    lockPatternView.setDisplayMode(DisplayMode.Wrong);
                    lockPatternView.enableInput();
                }

                break;

            default:
                break;
        }
        lockPatternView.postDelayed(new Runnable() {

            @Override
            public void run() {
                lockPatternView.clearPattern();
            }
        }, 1000);


    }


    public void finishSetup() {
        SharedPreferences preferences = getSharedPreferences(SpKey.GESTURE_CONFIG, MODE_PRIVATE);
        preferences.edit().putString(SpKey.KEY_GESTURE_APP_LOCK_PASSWORD, LockPatternView.patternToString(choosePattern)).commit();
        startActivity(AppManagerActivity.class);
        LockSetupActivity.this.finish();
        Intent watchDogIntent = new Intent(this, AppLockService.class);
        this.startService(watchDogIntent);
        SharedPreferencesManager.getInstance().putBoolean(SpKey.KEY_APP_LOCK, true);
        showToast(R.string.lock_setup_success);
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
    }

    @Override
    public void onPatternDetected(List<Cell> pattern) {
        Log.d(TAG, "onPatternDetected");

        if (pattern.size() < LockPatternView.MIN_LOCK_PATTERN_SIZE) {
            Toast.makeText(this,
                    R.string.lockpattern_recording_incorrect_too_short,
                    Toast.LENGTH_LONG).show();
            lockPatternView.setDisplayMode(DisplayMode.Wrong);
            return;
        }

        if (choosePattern == null) {
            choosePattern = new ArrayList<Cell>(pattern);
            Log.d(TAG, "choosePattern = " + choosePattern.toString());
            Log.d(TAG, "choosePattern.size() = " + choosePattern.size());
            Log.d(TAG, "choosePattern = " + Arrays.toString(choosePattern.toArray()));

            step = STEP_2;
            updateView();
            return;
        }

        Log.d(TAG, "choosePattern = " + Arrays.toString(choosePattern.toArray()));
        Log.d(TAG, "pattern = " + Arrays.toString(pattern.toArray()));

        if (choosePattern.equals(pattern)) {
            Log.d(TAG, "pattern = " + pattern.toString());
            Log.d(TAG, "pattern.size() = " + pattern.size());
            Log.d(TAG, "pattern = " + Arrays.toString(pattern.toArray()));

            confirm = true;
        } else {
            confirm = false;
        }

        step = STEP_4;
        updateView();


    }


}
