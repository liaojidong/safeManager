package com.dong.mobilesafe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.dong.mobilesafe.base.BaseFragment;
import com.dong.mobilesafe.constant.SpKey;
import com.dong.mobilesafe.ui.SettingItemView;
import com.dong.mobilesafe.utils.SharedPreferencesManager;

import net.tsz.afinal.annotation.view.ViewInject;

import butterknife.InjectView;
import butterknife.OnClick;

public class Setup2Fragment extends BaseFragment {
    /**
     * 读取手机sim的信息
     */
    private TelephonyManager tm;
    private SharedPreferences sp;
    private OnNextPageListener mListener;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof OnNextPageListener) {
            mListener = (OnNextPageListener) activity;
        }else {
            throw new RuntimeException("must be implement OnNextPageListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);

    }



    @OnClick(R.id.btn_next_step)
    public void nextStep(View view) {
        final String serialNumber = tm.getSimSerialNumber();
        if(TextUtils.isEmpty(serialNumber)) {
            showToast(R.string.please_insert_sim);
            return;
        }
        SharedPreferencesManager.getInstance().putString(SpKey.KEY_SIM_SERAL_NUMBER,serialNumber);
        mListener.onNextPage();
    }

    @Override
    protected int onGetContentView() {
        return R.layout.activity_setup2;
    }

}
