package com.dong.mobilesafe;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.dong.mobilesafe.adapter.ScanCacheAdapter;
import com.dong.mobilesafe.base.BaseTitleActivity;
import com.dong.mobilesafe.ui.AnimRelativeLayout;
import com.dong.mobilesafe.ui.clearn.ExplosionField;
import com.dong.mobilesafe.utils.CacheManager;
import com.dong.mobilesafe.utils.CacheManager.OnCleanListener;
import com.dong.mobilesafe.utils.CacheManager.OnScanListener;
import com.dong.mobilesafe.utils.thread.GlobalThreadPool;

import butterknife.InjectView;
import butterknife.OnClick;

public class CleanCacheActivity extends BaseTitleActivity implements OnScanListener, OnCleanListener {

    @InjectView(R.id.scan_list)
    RecyclerView mListView;
    @InjectView(R.id.tv_garbage_count)
    TextView garbageCountTV;
    @InjectView(R.id.top_content)
    AnimRelativeLayout top_content;
    @InjectView(R.id.btn_clear_cache)
    CircularProgressButton btn_clear_cache;
    @InjectView(R.id.iv_scan)
    ImageView iv_scan;
    @InjectView(R.id.iv_smile)
    ImageView iv_smile;

    ExplosionField explosionField;

    private CacheManager cacheManager;
    private ScanCacheAdapter mAdapter;
    private PackageManager pm;
    private long cacheCount;
    private boolean isCleanFinish = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        explosionField = new ExplosionField(this);
        cacheManager.scanCache();
    }

    @Override
    protected void onInitData() {
        super.onInitData();
        pm = getPackageManager();
        mAdapter = new ScanCacheAdapter(this);
        cacheManager = new CacheManager(this, this, this);
    }

    @Override
    protected void onPrepareView() {
        super.onPrepareView();
        setBarTitle(R.string.clean_cache);
        mListView.setLayoutManager(new LinearLayoutManager(this));
        mListView.setItemAnimator(new DefaultItemAnimator());
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected int onGetContentView() {
        return R.layout.activity_clean_cache;
    }

    @OnClick(R.id.btn_clear_cache)
    public void clearAll(View view) {
        if (isCleanFinish) {
            this.finish();
        } else {
            cacheManager.clearAllCache();
            showProgress();
        }

    }

    private void showProgress() {
        GlobalThreadPool.getInstance().execute(new Runnable() {
            int progress = 0;

            @Override
            public void run() {
                while (progress < 100) {
                    progress += 1;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btn_clear_cache.setProgress(progress);
                        }
                    });
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onCleanFinish() {
        mAdapter.removeAll();
        btn_clear_cache.setProgress(100);
        explosionField.explode(garbageCountTV, new ExplosionField.OnExplodeListener() {
            @Override
            public void onExplodeFinish() {
                showSmile();
            }
        });
        isCleanFinish = true;


    }

    private void showSmile() {
        iv_smile.setVisibility(View.VISIBLE);
        iv_smile.animate().alpha(1.0f).setDuration(800).start();
    }

    public class CacheApp {
        public long cacheSize;
        public String appName;
        public Drawable icon;
    }

    @Override
    public void onScanCacheSize(PackageStats pStats) {
        try {
            CacheApp app = new CacheApp();
            app.cacheSize = pStats.cacheSize + pStats.externalCacheSize;
            if (app.cacheSize > 0) {
                cacheCount = cacheCount + app.cacheSize;
                garbageCountTV.setText(Formatter.formatShortFileSize(this, cacheCount));
                PackageInfo info = pm.getPackageInfo(pStats.packageName, 0);
                app.appName = info.applicationInfo.loadLabel(pm).toString();
                app.icon = info.applicationInfo.loadIcon(pm);
                ImageView imageView = new ImageView(this);
                imageView.setImageDrawable(app.icon);
                top_content.startImageView(imageView);
                mAdapter.addItemAtHeard(app);
                mListView.smoothScrollToPosition(0);
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onScanStart(int scanCount) {
        startScanAnim();
    }

    @Override
    public void onScanning(int progress, PackageInfo packInfo) {

    }

    @Override
    public void onScanFinish() {
        showCleanCacheBtn();
        stopScanAnim();
    }

    ObjectAnimator scanAnim;
    public void startScanAnim(){
        scanAnim = ObjectAnimator.ofFloat(iv_scan, "translationX", -iv_scan.getWidth(),top_content.getWidth());
        scanAnim.setDuration(2500);
        scanAnim.setRepeatCount(-1);
        scanAnim.setInterpolator(new LinearInterpolator());
        scanAnim.setRepeatMode(ObjectAnimator.INFINITE);
        scanAnim.start();
    }

    public void stopScanAnim() {
        iv_scan.postDelayed(new Runnable() {
            @Override
            public void run() {
                iv_scan.animate().alpha(0).setDuration(200).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        iv_scan.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                }).start();
            }
        },600);

    }

    private void showCleanCacheBtn() {
        btn_clear_cache.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom);
        anim.setInterpolator(new AnticipateOvershootInterpolator());
        btn_clear_cache.startAnimation(anim);
    }


}  


