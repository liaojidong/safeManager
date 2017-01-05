package com.dong.mobilesafe.utils.Process;

import android.app.ActivityManager;
import android.content.Context;

import com.dong.mobilesafe.utils.root.RootUtil;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by liaojd on 2016/6/30.
 */
public class ProcessKiller {
    private static final boolean root = RootUtil.isDeviceRooted();

    /**
     * 杀死进程
     *
     * @param context
     * @param pkgName 包名
     */
    public static void killProcess(Context context, String pkgName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        am.killBackgroundProcesses(pkgName);
        if (root) {
            forceStopAPK(pkgName);
        }
    }

    private static void forceStopAPK(String pkgName) {
        Process sh = null;
        DataOutputStream os = null;
        try {
            sh = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(sh.getOutputStream());
            final String Command = "am force-stop " + pkgName + "\n";
            os.writeBytes(Command);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
