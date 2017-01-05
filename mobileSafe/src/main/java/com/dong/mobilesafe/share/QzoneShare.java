package com.dong.mobilesafe.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.dong.mobilesafe.utils.log.LogUtils;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

/**
 * 用于QQ空间分享
 *
 * @author Jesse
 */
public class QzoneShare extends Share implements IUiListener {
    private Tencent mTencent;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private int shareType = com.tencent.connect.share.QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT;

    @Override
    public void initParams(Context context, ShareCofig param) {
        mTencent = Tencent.createInstance(param.getAppid(), context);
    }

    @Override
    public void sendRequest(final Activity activity, ShareParams message) {
        final Bundle params = new Bundle();
        if (message != null) {
            params.putInt(com.tencent.connect.share.QzoneShare.SHARE_TO_QZONE_KEY_TYPE, shareType);
            params.putString(com.tencent.connect.share.QzoneShare.SHARE_TO_QQ_TITLE, message.getTitle());
            params.putString(com.tencent.connect.share.QzoneShare.SHARE_TO_QQ_SUMMARY, message.getText());
            //app分享不支持传目标链接
            params.putString(com.tencent.connect.share.QzoneShare.SHARE_TO_QQ_TARGET_URL, message.getSiteUrl());
            ArrayList<String> imageUrls = new ArrayList<String>();
            imageUrls.add(message.getImageUrl());
            params.putStringArrayList(com.tencent.connect.share.QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);


        }
        // QZone分享要在主线程做
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                mTencent.shareToQzone(activity, params, QzoneShare.this);

            }
        });

    }

    @Override
    public void handleRespond(Intent intent, Object handle) {

    }

    @Override
    public void onCancel() {
        LogUtils.jLog().e("onCancel");
    }

    @Override
    public void onComplete(Object arg0) {
        LogUtils.jLog().e("onComplete");
    }

    @Override
    public void onError(UiError arg0) {
        LogUtils.jLog().e("onError:" + arg0.errorMessage);
    }

}
