package com.dong.mobilesafe.utils.security;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2016/6/13 0013.
 */
public class SecurityCheckManager {

    private static SecurityCheckManager instance;


    private SecurityCheckManager() {
    }


    public static SecurityCheckManager getInstance() {
        if (instance == null) {
            synchronized (SecurityCheckManager.class) {
                if (instance == null) {
                    instance = new SecurityCheckManager();
                }
            }
        }
        return instance;
    }


    /**
     * 是否运行在模拟器中
     *
     * @return true表示运行在模拟器中，否则运行在真机中
     */
    private boolean isRunningInEmulator() {
        boolean qemuKernel = false;
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("getprop ro.kernel.qemu");
            os = new DataOutputStream(process.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            os.flush();
            process.waitFor();
            qemuKernel = (Integer.valueOf(in.readLine()) == 1);
        } catch (Exception e) {
            qemuKernel = false;
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return qemuKernel;
    }


    /**
     * 检查签名是否有误，防止恶意修改签名
     *
     * @return true签名有误，否则签名正确
     */
    private boolean isSignatureError(Context context) {
        boolean isError = false;
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(),PackageManager.GET_SIGNATURES);
            Signature[] signatures = packageInfo.signatures;
            int sig = signatures[0].hashCode();
            if(sig != -2057429939) {
                isError = true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return isError;
    }

    /**
     * 检查软件是否可以调试
     * @return
     */
    private boolean isDebuggable(Context context) {
        boolean debuggable = false;
        if((context.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
            debuggable = true;
        }
        return debuggable;
    }


    /**
     * 对APP进行安全检查，如果发现运行在模拟器上、签名被修改和应用能够被调试
     * 则程序会自动关闭
     * @param context
     */
    public void securityCheck(final Context context) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                // Moves the current Thread into the background
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                if(isRunningInEmulator()) {
                    killMyself();
                    return;
                }
                if(isSignatureError(context)) {
                    killMyself();
                    return;
                }
                if(isDebuggable(context)) {
                    killMyself();
                    return;
                }
            }
        };
        new Thread(r).start();
    }


    /**
     * 杀死自己的进程
     */
    private void killMyself() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }




}
