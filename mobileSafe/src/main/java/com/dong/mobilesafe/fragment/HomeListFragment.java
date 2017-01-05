package com.dong.mobilesafe.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.dong.mobilesafe.AppLockEnterActivity;
import com.dong.mobilesafe.CleanCacheActivity;
import com.dong.mobilesafe.CommunicationActivity;
import com.dong.mobilesafe.LockSetupActivity;
import com.dong.mobilesafe.LostFindActivity;
import com.dong.mobilesafe.R;
import com.dong.mobilesafe.SetupActivity;
import com.dong.mobilesafe.TaskManagerActivity;
import com.dong.mobilesafe.TrafficManagerActivity;
import com.dong.mobilesafe.adapter.HomeAdapter;
import com.dong.mobilesafe.base.BaseFragment2;
import com.dong.mobilesafe.constant.SpKey;
import com.dong.mobilesafe.db.dao.HarassCallDao;
import com.dong.mobilesafe.db.dao.RubbishSmsDao;
import com.dong.mobilesafe.utils.SharedPreferencesManager;

public class HomeListFragment extends BaseFragment2 {
    private GridView listHome;
    private HomeAdapter mAdapter;
    private EditText setupPassword;
    private EditText setupPasswordConfirm;
    private EditText enterPwd;
    private Button ok;
    private Button cancel;
    private AlertDialog dialog;
    private SharedPreferences sp;
    private RubbishSmsDao rubbishSmsDao;
    private HarassCallDao harassCallDao;
    private int unReadInfoCount = 0;

    @Override
    protected void findViewById(View rootView) {
        listHome = (GridView) rootView.findViewById(R.id.list_home);
    }


    @Override
    protected void onInitData(Bundle savedInstanceState) {
        super.onInitData(savedInstanceState);
        sp = context.getSharedPreferences(SpKey.PASSWORD_CONFIG, Context.MODE_PRIVATE);
        rubbishSmsDao = new RubbishSmsDao(context);
        harassCallDao = new HarassCallDao(context);
        unReadInfoCount = rubbishSmsDao.getUnReadCount() + harassCallDao.getUnReadCount();
        mAdapter = new HomeAdapter(context);

    }


    @Override
    public void onStart() {
        super.onStart();
        mAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onPrepareView() {
        mAdapter.setUnReadInfoCount(unReadInfoCount);
        listHome.setAdapter(mAdapter);
        mAdapter.setOnHomeListItemClickListener(new HomeAdapter.OnHomeListItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent;
                switch (position) {
                    case 0://手机防盗
                        goPhoneProtected();
                        break;
                    case 1:// 通讯卫士
                        intent = new Intent(context, CommunicationActivity.class);
                        startActivity(intent);
                        break;
                    case 2://进程管理
                        intent = new Intent(context, TaskManagerActivity.class);
                        startActivity(intent);
                        break;
                    case 3://流量统计
                        intent = new Intent(context, TrafficManagerActivity.class);
                        startActivity(intent);
                        break;
                    case 4://应用锁
                        SharedPreferences sp = context.getSharedPreferences(SpKey.GESTURE_CONFIG, Context.MODE_PRIVATE);
                        if (sp.getString(SpKey.KEY_GESTURE_APP_LOCK_PASSWORD, null) == null) {
                            startActivity(LockSetupActivity.class);
                        } else {
                            startActivity(AppLockEnterActivity.class);
                        }
                        break;
                    case 5://缓存清理
                        intent = new Intent(context, CleanCacheActivity.class);
                        startActivity(intent);
                        break;
                }

            }
        });
    }


    protected void goPhoneProtected() {
        //判断是否设置过密码
        if (SharedPreferencesManager.getInstance().getBoolean(SpKey.KEY_COMPLETE_SETUP, false)) {
            startActivity(LostFindActivity.class);
        } else {
            startActivity(SetupActivity.class);
        }

    }


    @Override
    protected int onGetContentView() {
        return R.layout.fragment_home_list;
    }


}
