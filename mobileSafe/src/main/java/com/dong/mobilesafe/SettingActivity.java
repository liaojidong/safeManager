package com.dong.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;

import com.dong.mobilesafe.base.BaseActivity;
import com.dong.mobilesafe.base.BaseTitleActivity;
import com.dong.mobilesafe.constant.SpKey;
import com.dong.mobilesafe.service.AddressService;
import com.dong.mobilesafe.service.AutoCleanService;
import com.dong.mobilesafe.service.CallSmsSafeService;
import com.dong.mobilesafe.service.FloatWindowService;
import com.dong.mobilesafe.service.TrafficMonitorService;
import com.dong.mobilesafe.service.AppLockService;
import com.dong.mobilesafe.ui.SettingItemView;
import com.dong.mobilesafe.ui.SettingItemView.onOnOffListener;
import com.dong.mobilesafe.ui.SettingNextItemView;
import com.dong.mobilesafe.utils.ServiceUtils;
import com.dong.mobilesafe.utils.SharedPreferencesManager;

public class SettingActivity extends BaseTitleActivity implements onOnOffListener {

    // 设置是否开启自动更新
    private SettingItemView updateItem;
    private SharedPreferences sp;

    // 设置是否开启显示归属地
    private SettingItemView addressItem;
    private Intent showAddress;

    //黑名单拦截设置
    private SettingItemView callsmsSafe;

    //程序锁看门狗设置
    private SettingItemView watchdogItem;
    private SettingItemView floatWindowItem;
    private SettingItemView trafficMonitor;
    private SettingItemView cb_auto_clean;

    //设置归属地显示框背景
    private SettingNextItemView scv_changebg;
    private String[] items = {"半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿"};


    @Override
    protected int onGetContentView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onPrepareView() {
        super.onPrepareView();
        setBarTitle(R.string.setting);
        cb_auto_clean = (SettingItemView) this.findViewById(R.id.cb_auto_clean);
        // 初始设置是否开启自动更新
        updateItem = (SettingItemView) this.findViewById(R.id.siv_update);
        // 设置号码归属地显示控件
        addressItem = (SettingItemView) this.findViewById(R.id.siv_show_address);
        //黑名单拦截设置
        callsmsSafe = (SettingItemView) this.findViewById(R.id.siv_callsms_safe);
        //程序锁设置
        watchdogItem = (SettingItemView) this.findViewById(R.id.siv_watchdog);
        //设置号码归属地的背景
        scv_changebg = (SettingNextItemView) this.findViewById(R.id.sniv_changebg);
        //流量监控设置
        floatWindowItem = (SettingItemView) this.findViewById(R.id.siv_float_window);

        trafficMonitor = (SettingItemView) this.findViewById(R.id.siv_traffic_monitor);

        boolean update = sp.getBoolean(SpKey.KEY_UPDATE, false);
        updateItem.setChecked(update);


        showAddress = new Intent(this, AddressService.class);
        boolean isAddressServiceRunning = ServiceUtils.isServiceRunning(this, AddressService.class.getName());
        addressItem.setChecked(isAddressServiceRunning);

        boolean isFloatWindowServiceRunning = ServiceUtils.isServiceRunning(this, FloatWindowService.class.getName());
        floatWindowItem.setChecked(isFloatWindowServiceRunning);


        boolean isTrafficMonitorServiceRunning = ServiceUtils.isServiceRunning(this, TrafficMonitorService.class.getName());
        trafficMonitor.setChecked(isTrafficMonitorServiceRunning);


        updateItem.setOnOnOffListener(this);
//			scv_changebg.setOnOnOffListener(this);
        watchdogItem.setOnOnOffListener(this);
        addressItem.setOnOnOffListener(this);
        callsmsSafe.setOnOnOffListener(this);
        floatWindowItem.setOnOnOffListener(this);
        trafficMonitor.setOnOnOffListener(this);
        cb_auto_clean.setOnOnOffListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        showAddress = new Intent(this, AddressService.class);

        boolean isAddressServiceRunning = ServiceUtils.isServiceRunning(this, AddressService.class.getName());
        addressItem.setChecked(isAddressServiceRunning);

        boolean iscallSmsServiceRunning = ServiceUtils.isServiceRunning(this, CallSmsSafeService.class.getName());
        callsmsSafe.setChecked(iscallSmsServiceRunning);


        if (SharedPreferencesManager.getInstance().getBoolean(SpKey.KEY_AUTO_KILL_PROCESS, false)) {
            boolean isAutoCleanRunning = ServiceUtils.isServiceRunning(this, AutoCleanService.class.getName());
            if (!isAutoCleanRunning) {
                startService(AutoCleanService.class);
            }
            cb_auto_clean.setChecked(true);
        }

        if (SharedPreferencesManager.getInstance().getBoolean(SpKey.KEY_APP_LOCK, false)) {
            boolean iswatchdogServiceRunning = ServiceUtils.isServiceRunning(this, AppLockService.class.getName());
            if (!iswatchdogServiceRunning) {
                startService(AppLockService.class);
            }
            watchdogItem.setChecked(true);
        }

        if (SharedPreferencesManager.getInstance().getBoolean(SpKey.KEY_FLOAT_WINDOW, false)) {
            boolean isFloatWindowServiceRunning = ServiceUtils.isServiceRunning(this, FloatWindowService.class.getName());
            if (!isFloatWindowServiceRunning) {
                startService(FloatWindowService.class);
            }
            floatWindowItem.setChecked(true);
        }


    }


    @Override
    protected void onInitData() {
        super.onInitData();
        sp = this.getSharedPreferences(SpKey.SETTING_CONFIG, MODE_PRIVATE);
    }

    @Override
    public void onChange(View view, boolean isCheced) {
        final Editor editor = sp.edit();
        switch (view.getId()) {

            case R.id.siv_update:
                editor.putBoolean(SpKey.KEY_UPDATE, isCheced);
                break;

            case R.id.siv_show_address:
                addressItem.setChecked(isCheced);
                this.stopService(showAddress);

                break;
            case R.id.siv_callsms_safe:
                Intent callSmsSafeIntent = new Intent(this, CallSmsSafeService.class);
                if (isCheced) {
                    this.startService(callSmsSafeIntent);
                } else {
                    this.stopService(callSmsSafeIntent);

                }
                break;
            case R.id.siv_watchdog:
                SharedPreferencesManager.getInstance().putBoolean(SpKey.KEY_APP_LOCK, isCheced);
                Intent watchDogIntent = new Intent(this, AppLockService.class);
                if (isCheced) {
                    // 选择状态
                    this.startService(watchDogIntent);
                } else {
                    // 变为非选中状态
                    this.stopService(watchDogIntent);
                }
                break;

            case R.id.sniv_changebg:

                break;
            case R.id.siv_float_window:
                SharedPreferencesManager.getInstance().putBoolean(SpKey.KEY_FLOAT_WINDOW, isCheced);
                if (isCheced) {
                    startService(FloatWindowService.class);
                } else {
                    stopService(FloatWindowService.class);
                }
                break;

            case R.id.siv_traffic_monitor:
                if (isCheced) {
                    startService(TrafficMonitorService.class);
                } else {
                    stopService(TrafficMonitorService.class);
                }
                break;
            case R.id.cb_auto_clean:
                SharedPreferencesManager.getInstance().putBoolean(SpKey.KEY_AUTO_KILL_PROCESS,isCheced);
                cb_auto_clean.setChecked(isCheced);
                if (isCheced) {
                    startService(AutoCleanService.class);
                } else {
                    stopService(AutoCleanService.class);
                }
                break;

        }
        editor.commit();

    }


}
