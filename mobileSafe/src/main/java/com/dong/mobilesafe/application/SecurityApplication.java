package com.dong.mobilesafe.application;

import android.app.Application;
import android.content.Intent;

import com.dong.mobilesafe.constant.SpKey;
import com.dong.mobilesafe.service.AppLockService;
import com.dong.mobilesafe.service.FloatWindowService;
import com.dong.mobilesafe.service.TrafficMonitorService;
import com.dong.mobilesafe.utils.APPUtils;
import com.dong.mobilesafe.utils.SharedPreferencesManager;
import com.dong.mobilesafe.utils.security.SecurityCheckManager;

/**
 * Created by liaojd on 2016/6/27.
 */
public class SecurityApplication extends Application{
    public boolean isReleaseVersion = true;
    private static SecurityApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if(isReleaseVersion) {
            SecurityCheckManager.getInstance().securityCheck(this);
        }
        APPUtils.startAppService(this);
    }

    public static SecurityApplication getInstance() {
        return instance;
    }
}
