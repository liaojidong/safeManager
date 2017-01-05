package com.dong.mobilesafe.utils;
import java.io.BufferedReader;
import java.io.File;    
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;    
    
import android.content.Context;    
import android.content.pm.IPackageDataObserver;    
import android.content.pm.PackageManager;    
import android.os.Environment;    
import android.os.RemoteException;    
import android.os.StatFs;    
    
/**   
 * 1.功能相当于,点击了 应用程序信息 里面的 清楚缓存按钮，而非 清除数据   
 *    
 * 2.功能相当于,删除了/data/data/packageName/cache 文件夹里面所有的东西   
 *    
 * 3.需要权限 <uses-permission android:name="android.permission.CLEAR_APP_CACHE" />   
 */    
public class CacheClearHelper {    
    
    public static void clearCache(Context context) {    
    
        try {    
            PackageManager packageManager = context.getPackageManager();    
            Method localMethod = packageManager.getClass().getMethod("freeStorageAndNotify", Long.TYPE,    
                    IPackageDataObserver.class);    
               
            localMethod.invoke(packageManager, (Long.MAX_VALUE-1L), new IPackageDataObserver.Stub() {    
    
                @Override    
                public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {    
                	System.out.println("packageName ="+packageName+" successed = " + succeeded);
                }    
            });    
        } catch (Exception e) {    
            e.printStackTrace();    
        }    
    }    
    
       
}    
