package com.dong.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.dong.mobilesafe.base.BaseActivity;
import com.dong.mobilesafe.constant.FilePath;
import com.dong.mobilesafe.service.TrafficMonitorService;
import com.dong.mobilesafe.utils.APPUtils;
import com.dong.mobilesafe.utils.AnimationUtils;
import com.dong.mobilesafe.utils.AppManager;
import com.dong.mobilesafe.utils.StreamTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.InjectView;

public class SplashActivity extends BaseActivity {

    protected static final String TAG = "SplashActivity";
    protected static final int SHOW_UPDATE_DIALOG = 0;
    protected static final int ENTER_HOME = 1;
    protected static final int URL_ERROR = 2;
    protected static final int NETWORK_ERROR = 3;
    protected static final int JSON_ERROR = 4;
    private final static int WAIT_DURATION = 2500; //进入主程序等待的时间
    private final static String PREFERENCE_NAME_SHORTCUT = "shortcut";

    private TextView tv_splash_version;
    private String description;
    @InjectView(R.id.iv_splash_logo)
    ImageView iv_splash_logo;
    /**
     * 新版本的下载地址
     */
    private String apkurl;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, TrafficMonitorService.class));
        //创建快捷图标
        installShortCut();
        //拷贝数据库
        copyDB(FilePath.FILE_NAME_ADDRESS);
        copyDB(FilePath.FILE_NAME_ANTIVIRUS);

        delayEnterHome();
        //进入动画
        startAnim();
    }

    @Override
    public boolean onEnableStatusBarTint() {
        return false;
    }

    @Override
    protected void onInitData() {
        super.onInitData();
        sp = this.getSharedPreferences("config", MODE_PRIVATE);
    }


    private void delayEnterHome() {
            //自动升级已经关闭
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //进入主页面
                    enterHome();
                }
            }, WAIT_DURATION);


    }

    private boolean isUpdate() {
        return sp.getBoolean("update", false);

    }

    @Override
    protected int onGetContentView() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onPrepareView() {
        super.onPrepareView();
        AnimationUtils.playFrameAnim(R.drawable.splash_anim, iv_splash_logo);
        tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
        tv_splash_version.setText(APPUtils.getVersion(this, true));
    }

    private void startAnim() {
        AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
        aa.setDuration(500);
        findViewById(R.id.rl_root_splash).startAnimation(aa);
    }


    /**
     * 创建快捷图标
     */
    private void installShortCut() {
        boolean shortcut = sp.getBoolean(PREFERENCE_NAME_SHORTCUT, false);
        if (shortcut)
            return;
        Editor editor = sp.edit();
        //发送广播的意图，要创建快捷图标了
        Intent intent = new Intent();
        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        //快捷方式  要包含3个重要的信息 1，名称 2.图标 3.干什么事情
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, AppManager.getInstance(this).getAppName());
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
        //桌面点击图标对应的意图。
        Intent shortcutIntent = new Intent();
        shortcutIntent.setAction(Intent.ACTION_MAIN);
        shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        shortcutIntent.setClassName(getPackageName(), SplashActivity.class.getName());
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        sendBroadcast(intent);
        editor.putBoolean(PREFERENCE_NAME_SHORTCUT, true);
        editor.commit();
    }


    private void copyDB(String filename) {
        //只要你拷贝了一次，我就不要你再拷贝了
        try {
            File file = FilePath.getDiskFileDir(this, filename);
            if (file.exists() && file.length() > 0) {
                Log.i(TAG, "正常了，就不需要拷贝了");
            } else {
                InputStream is = getAssets().open(filename);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                is.close();
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_UPDATE_DIALOG:// 显示升级的对话框
                    Log.i(TAG, "显示升级的对话框");
                    break;
                case ENTER_HOME: // 进入主页面
                    enterHome();
                    break;

                case URL_ERROR:  // URL错误
                    enterHome();
                    showToast("URL错误");

                    break;

                case NETWORK_ERROR: // 网络异常
                    enterHome();
                    showToast("网络异常");
                    break;

                case JSON_ERROR:  // JSON解析出错
                    enterHome();
                    showToast("JSON解析出错");
                    break;

            }
        }

    };

    /**
     * 检查是否有新版本，如果有就升级
     */
    private void checkUpdate() {

        new Thread() {
            public void run() {
                // URLhttp://192.168.1.254:8080/updateinfo.html

                Message mes = Message.obtain();
                long startTime = System.currentTimeMillis();
                try {

                    URL url = new URL(getString(R.string.serverurl));
                    // 联网
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(4000);
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        // 联网成功
                        InputStream is = conn.getInputStream();
                        // 把流转成String
                        String result = StreamTools.readFromStream(is);
                        Log.i(TAG, "联网成功了" + result);
                        // json解析
                        JSONObject obj = new JSONObject(result);
                        // 得到服务器的版本信息
                        String version = (String) obj.get("version");

                        description = (String) obj.get("description");
                        apkurl = (String) obj.get("apkurl");

                        // 校验是否有新版本
                        if (AppManager.getInstance(SplashActivity.this)
                                .getAppInfo().getVersion().equals(version)) {
                            // 版本一致，没有新版本，进入主页面
                            mes.what = ENTER_HOME;
                        } else {
                            // 有新版本，弹出一升级对话框
                            mes.what = SHOW_UPDATE_DIALOG;

                        }

                    }

                } catch (MalformedURLException e) {
                    mes.what = URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    mes.what = NETWORK_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                    mes.what = JSON_ERROR;
                } finally {

                    long endTime = System.currentTimeMillis();
                    // 我们花了多少时间
                    long dTime = endTime - startTime;
                    // 2000
                    if (dTime < 2000) {
                        try {
                            Thread.sleep(2000 - dTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    handler.sendMessage(mes);
                }

            }

            ;
        }.start();

    }


    protected void enterHome() {
        startActivity(HomeActivity.class);
        this.finish();

    }

}
