package com.dong.mobilesafe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.dong.mobilesafe.base.BaseActivity;
import com.dong.mobilesafe.base.BaseFragment;
import com.dong.mobilesafe.constant.SpKey;
import com.dong.mobilesafe.utils.SharedPreferencesManager;
import com.dong.mobilesafe.utils.security.ValidateUtil;

import butterknife.InjectView;
import butterknife.OnClick;

public class Setup3Fragment extends BaseFragment {
    @InjectView(R.id.et_setup3_phone)
    EditText et_setup3_phone;
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
    protected int onGetContentView() {
        return R.layout.activity_setup3;
    }


    @Override
    protected void onPrepareView() {
        super.onPrepareView();
    }

    @OnClick(R.id.btn_next_step)
    public void nextStep() {
        final String phoneNumber = et_setup3_phone.getText().toString().trim();
        if(TextUtils.isEmpty(phoneNumber)) {
            showToast(R.string.please_input_phone_number);
            return;
        }
        if(!ValidateUtil.validateMobilePhone(phoneNumber)) {
            showToast(R.string.please_input_correct_phone_number);
            return;
        }
        SharedPreferencesManager.getInstance().putString(SpKey.key_safe_number,phoneNumber);
        mListener.onNextPage();
    }

}
