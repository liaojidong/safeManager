package com.dong.mobilesafe;

import android.widget.TextView;

import com.dong.mobilesafe.base.BaseTitleActivity;
import com.dong.mobilesafe.utils.APPUtils;

import butterknife.InjectView;

/**
 * @author snubee
 * @Description:TODO关于页面
 * @Date 2015-9-30
 */
public class AboutActivity extends BaseTitleActivity {
    @InjectView(R.id.tv_user_deal)
    TextView tv_user_deal;// 用户协议
    @InjectView(R.id.tv_ico_version)
    TextView tv_version;// 版本


    @Override
    protected int onGetContentView() {
        return R.layout.activity_about;
    }

    @Override
    protected void onPrepareView() {
        super.onPrepareView();
        setBarTitle(R.string.about);
        String version = APPUtils.getVersion(this, true);
        tv_version.setText(version);
    }

}
