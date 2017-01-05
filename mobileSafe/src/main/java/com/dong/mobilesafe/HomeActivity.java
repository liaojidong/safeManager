package com.dong.mobilesafe;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.dong.mobilesafe.adapter.CommonDialogListAdapter;
import com.dong.mobilesafe.base.BaseActivity;
import com.dong.mobilesafe.domain.DialogItem;
import com.dong.mobilesafe.fragment.HomeFragment;
import com.dong.mobilesafe.fragment.SlidingMenuFragment;
import com.dong.mobilesafe.share.ShareManager;
import com.dong.mobilesafe.share.ShareParams;
import com.dong.mobilesafe.ui.CustomViewPager;
import com.dong.mobilesafe.utils.APPUtils;
import com.dong.mobilesafe.utils.DialogUtils;
import com.dong.mobilesafe.utils.MyLogger;
import com.dong.mobilesafe.utils.log.LogUtils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.GridHolder;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.dong.mobilesafe.R;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;

import butterknife.InjectView;

public class HomeActivity extends BaseActivity implements OnChangeTitleListener, SlidingMenu.OnOpenedListener, SlidingMenu.OnClosedListener, HomeFragment.OnSlidMenuClickListener, SlidingMenuFragment.OnMenuItemClickListener {

    private HomeFragment homeFragment;
    private SlidingMenu menu;

    @Override
    protected int onGetContentView() {
        return R.layout.activity_home;
    }

    @Override
    protected void onInitData() {
        super.onInitData();
        ShareManager.getInstance(this).init(this); //初始化分享管理者
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareManager.getInstance(this).unRegisterShareRespondListener(this);
    }

    @Override
    protected void onPrepareView() {
        super.onPrepareView();
        homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.tag_home_fragment));

        setEnableSlidFinish(false);
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.RIGHT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setOnOpenedListener(this);
        menu.setOnClosedListener(this);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.sliding_menu);

    }

    @Override
    public void onOpened() {
        LogUtils.jLog().e("onOpen");
        homeFragment.setMenuIcon(R.drawable.menu_open);
    }


    @Override
    public void onClosed() {
        LogUtils.jLog().e("onClosed");
        homeFragment.setMenuIcon(R.drawable.menu_close);
    }


    @Override
    public void onMenuItemClick(int postion) {
        switch (postion) {
            case 0: //小白助手

                break;
            case 1: //设备信息
                startActivity(new Intent(this, DeviceInfoActivity.class));
                break;
            case 2: //分享
                showShareDialog();
                break;
            case 3: //给个好评
                APPUtils.goAppStore(this);
                break;
            case 4: //打赏

                break;
            case 5: //关于
                startActivity(new Intent(this,AboutActivity.class));
                break;
        }
        menu.showContent();
    }

    private void showShareDialog() {
        List<DialogItem> items = initShareData();
        Holder holder = new ListHolder();
        OnItemClickListener itemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                ShareParams params = new ShareParams();
                switch (position) {
                    case 0: //微信朋友圈
                        params.setTitle(getString(R.string.app_name));
                        params.setText(getString(R.string.share_content_text));
                        params.setSiteUrl(getString(R.string.app_download_url));
                        params.setImageUrl(getString(R.string.app_logo_url));
                        ShareManager.getInstance(HomeActivity.this).share(ShareManager.SharePlatform.wechat, HomeActivity.this, params);
                        break;
                    case 1:  //微信好友
                        params.setTitle(getString(R.string.app_name));
                        params.setText(getString(R.string.share_content_text));
                        params.setFlag(SendMessageToWX.Req.WXSceneSession);
                        params.setSiteUrl(getString(R.string.app_download_url));
                        ShareManager.getInstance(HomeActivity.this).share(ShareManager.SharePlatform.wechat, HomeActivity.this, params);
                        break;
                    case 2: //QQ空间
                        params.setTitle(getString(R.string.app_name));
                        params.setText(getString(R.string.share_content_text));
                        params.setSiteUrl(getString(R.string.app_download_url));
                        params.setImageUrl(getString(R.string.app_logo_url));
                        ShareManager.getInstance(HomeActivity.this).share(ShareManager.SharePlatform.qzone, HomeActivity.this, params);
                        break;
                    case 3: //QQ好友
                        params.setTitle(getString(R.string.app_name));
                        params.setText(getString(R.string.share_content_text));
                        params.setSiteUrl(getString(R.string.app_download_url));
                        ShareManager.getInstance(HomeActivity.this).share(ShareManager.SharePlatform.qq, HomeActivity.this, params);
                        break;
                    case  4: //新浪微博
                        BitmapDrawable d = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_launcher);
                        params.setText(getString(R.string.share_content_text));
                        params.setImageBitmap(d.getBitmap());
                        params.setUrl(getString(R.string.app_download_url));
                        ShareManager.getInstance(HomeActivity.this).share(ShareManager.SharePlatform.sinaweibo, HomeActivity.this, params);
                        break;
                }
                dialog.dismiss();
            }
        };
        CommonDialogListAdapter adapter = new CommonDialogListAdapter(this);
        adapter.replaceList(items);
        DialogUtils.showOnlyContentDialog(this, holder, DialogPlus.Gravity.BOTTOM, adapter, null, itemClickListener);
    }

    @NonNull
    private List<DialogItem> initShareData() {
        List<DialogItem> items = new ArrayList<>();
        final String []titles = getResources().getStringArray(R.array.share_platforms);
        final int []icons = new int[]{
                R.drawable.ng_kazuo_icon_tuijian_wx2,
                R.drawable.ng_kazuo_icon_tuijian_wx1,
                R.drawable.ng_kazuo_icon_tuijian_qq2,
                R.drawable.ng_kazuo_icon_tuijian_qq1,
                R.drawable.ng_kazuo_icon_tuijian_wb
        };
        for(int i=0;i<titles.length;i++) {
            DialogItem item = new DialogItem();
            item.setName(titles[i]);
            item.setIcon(getResources().getDrawable(icons[i]));
            items.add(item);
        }
        return items;
    }

    @Override
    public void onMenuClick(View view) {
        menu.toggle();
    }




    @Override
    public void onChangeTitle(String title, Drawable leftDrawable,
                              Drawable rightDrawable) {
        if (!TextUtils.isEmpty(title)) {

        }
    }


    // --------------- 退出应用 start ---------------
    private boolean bExit = false;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            bExit = false;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (menu.isMenuShowing()) {
            menu.showContent();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (bExit) {
                finish();
                return true;
            }
            bExit = true;
            mHandler.sendEmptyMessageDelayed(0x100, 2000);
            showToast(getString(R.string.exit_the_application));
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    // --------------- 退出应用 end -----------------


}
