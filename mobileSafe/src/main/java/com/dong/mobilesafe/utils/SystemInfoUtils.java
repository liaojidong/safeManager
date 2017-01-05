package com.dong.mobilesafe.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Pattern;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

/**
 * 系统信息的工具类
 *
 */
public class SystemInfoUtils {
	/**
	 * 获取正在运行的进程的数量
	 * @param context 上下文
	 * @return
	 */
	public static int getRunningProcessCount(Context context){
		//PackageManager //包管理器 相当于程序管理器。静态的内容。
		//ActivityManager  进程管理器。管理的手机的活动信息。动态的内容。
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> infos = am.getRunningAppProcesses();
		return infos.size();
	}
	/**
	 * 获取手机可用的剩余内存
	 * @param context 上下文
	 * @return
	 */
	public static long getAvailMem(Context context){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		am.getMemoryInfo(outInfo);
		return outInfo.availMem;
	} 
	/**
	 * 获取手机可用的总内存
	 * @param context 上下文
	 * @return long byte
	 */
	public static long getTotalMem(Context context){
//		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//		MemoryInfo outInfo = new MemoryInfo();
//		am.getMemoryInfo(outInfo);
//		return outInfo.totalMem;
		try {
			File file = new File("/proc/meminfo");
			FileInputStream fis = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line = br.readLine();
			//MemTotal:         513000 kB
			StringBuilder sb = new StringBuilder();
			for(char c: line.toCharArray()){
				if(c>='0'&&c<='9'){
					sb.append(c);
				}
			}
			return Long.parseLong(sb.toString())*1024;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
		
	
	
	/**
	 *  获取CPU最大频率（单位KHZ）
	 *   "/system/bin/cat" 命令行
     "   /sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" 存储最大频率的文件的路径
	 */
       public static String getMaxCpuFreq() {
               String result = "";
               ProcessBuilder cmd;
               try {
                       String[] args = { "/system/bin/cat",
                                       "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" };
                       cmd = new ProcessBuilder(args);
                       Process process = cmd.start();
                       InputStream in = process.getInputStream();
                       byte[] re = new byte[24];
                       while (in.read(re) != -1) {
                               result = result + new String(re);
                       }
                       in.close();
               } catch (IOException ex) {
                       ex.printStackTrace();
                       result = "N/A";
               }
               return result.trim();
       }
       
       
       
       /**
        *  获取CPU最小频率（单位KHZ）
        * @return
        */
       public static String getMinCpuFreq() {
               String result = "";
               ProcessBuilder cmd;
               try {
                       String[] args = { "/system/bin/cat",
                                       "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq" };
                       cmd = new ProcessBuilder(args);
                       Process process = cmd.start();
                       InputStream in = process.getInputStream();
                       byte[] re = new byte[24];
                       while (in.read(re) != -1) {
                               result = result + new String(re);
                       }
                       in.close();
               } catch (IOException ex) {
                       ex.printStackTrace();
                       result = "N/A";
               }
               return result.trim();
       }
       
       
       /**
        * 获取cup当前频率（单位KHZ）
        * @return
        */
       public static String getCurCpuFreq() {
           String result = "N/A";
           BufferedReader br = null;
           try {
               br = new BufferedReader(new FileReader(
                       "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq"));
               String text = br.readLine();
               result = text.trim();
           } catch (FileNotFoundException e) {
                   e.printStackTrace();
           } catch (IOException e) {
                   e.printStackTrace();
           }finally {
        	   if(br != null) {
        		   try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	   }
           }
           return result;
   }
       
       
       public static int getCpuNumCores() {
    	    class CpuFilter implements FileFilter {
    	        @Override
    	        public boolean accept(File pathname) {
    	            //Check if filename is "cpu", followed by a single digit number
    	            if(Pattern.matches("cpu[0-9]", pathname.getName())) {
    	                return true;
    	            }
    	            return false;
    	        }      
    	    }
    	    try {
    	        //Get directory containing CPU info
    	        File dir = new File("/sys/devices/system/cpu/");
    	        //Filter to only list the devices we care about
    	        File[] files = dir.listFiles(new CpuFilter());
    	        //Return the number of cores (virtual CPU devices)
    	        return files.length;
    	    } catch(Exception e) {
    	    	e.printStackTrace();
    	        return 1;
    	    }
    	}
       
       
       
	
	
}
