package com.dong.mobilesafe.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Vibrator;
import com.dong.mobilesafe.R;
import com.dong.mobilesafe.utils.thread.GlobalThreadPool;

/**
 * Created by Administrator on 2016/2/19 0019.
 */
public class WaveNotifyManager {
    // 默认消息音乐
    private static final int DEFAULT_BACKGROUND_MUSIC = R.raw.wave;
    private SoundPool soundPool;
    private static WaveNotifyManager instance;
    private int soundId;
    private Context context;
    private Vibrator vibrator;
    private Object lock = new Object();
    private boolean loadComplete = false;


    private WaveNotifyManager(Context context) {
        this.context = context;
        initVibrator();
        initSoundPool();
    }

    /**
     * 初始化震动器
     */
    private void initVibrator() {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }


    private void initSoundPool() {
        GlobalThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                /**
                 * 21版本后，SoundPool的创建发生很大改变
                 */
                //判断系统sdk版本，如果版本超过21，调用第一种
                if (Build.VERSION.SDK_INT >= 21) {
                    SoundPool.Builder builder = new SoundPool.Builder();
                    builder.setMaxStreams(1);//传入音频数量
                    //AudioAttributes是一个封装音频各种属性的方法
                    AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
                    attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);//设置音频流的合适的属性
                    builder.setAudioAttributes(attrBuilder.build());//加载一个AudioAttributes
                    soundPool = builder.build();
                } else {
                    soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
                }
                //load的返回值是一个int类的值：音频的id，在SoundPool的play()方法中加入这个id就能播放这个音频
                soundId = soundPool.load(context, DEFAULT_BACKGROUND_MUSIC, 5);
                soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                        synchronized (lock) {
                            loadComplete = true;
                            lock.notifyAll();
                        }
                    }
                });
            }
        });
    }


    public static WaveNotifyManager getInstance(Context context) {
        if (instance == null) {
            synchronized (WaveNotifyManager.class) {
                if (instance == null) {
                    instance = new WaveNotifyManager(context);
                }
            }
        }
        return instance;
    }

    /**
     * 通过声音通知
     */
    public void notifyByVoice() {
        GlobalThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    while (!loadComplete) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    soundPool.play(soundId, 1, 1, 0, 0, 1);
                }

            }
        });

    }

    /**
     * 通过震动进行通知
     */
    public void notifyByShake() {
        vibrator.vibrate(500);//振动时间
    }


    /**
     * 通过多种方式进行通知
     */
    public void notifyByAll() {
        notifyByVoice();
        notifyByShake();
    }



}
