package com.dong.mobilesafe.utils.log;

import android.util.Log;


import com.dong.mobilesafe.application.SecurityApplication;

import java.util.Hashtable;

public class LogUtils  
{  
    private final static boolean logEnable = !SecurityApplication.getInstance().isReleaseVersion;

    public final static String tag = "[xiaobai]";
    private final static int                    logLevel        = Log.VERBOSE;  
    private static Hashtable<String, LogUtils>    sLoggerTable    = new Hashtable<String, LogUtils>();  
    private String                              devName;  
      
    private static LogUtils                     jlog;  

    private static final String                 JESSE           = "@jesse@ ";
      
    /**
     * 根据�?��者名称创建Logger对象�?
     * @param devName
     */
    private LogUtils(String devName)  
    {  
        this.devName = devName;  
    }  
      
    /** 
     *  根据�?��者名称创建一个Logger对象�?
     * @param devName �?��者名�?
     * @return 
     */  
    @SuppressWarnings("unused")  
    private static LogUtils getLogger(String devName)  
    {  
        LogUtils classLogger = (LogUtils) sLoggerTable.get(devName);  
        if(classLogger == null)  
        {  
            classLogger = new LogUtils(devName);  
            sLoggerTable.put(devName, classLogger);  
        }  
        return classLogger;  
    }  
      
    /** 
     * Purpose:Mark user one 
     * @return 
     */  
    public static LogUtils jLog()  
    {  
        if(jlog == null)  
        {  
            jlog = new LogUtils(JESSE);  
        }  
        return jlog;  
    }

      
    /** 
     * Get The Current Function Name 
     * @return 
     */  
    private String getFunctionName()  
    {  
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();  
        if(sts == null)  
        {  
            return null;  
        }  
        for(StackTraceElement st : sts)  
        {  
            if(st.isNativeMethod())  
            {  
                continue;  
            }  
            if(st.getClassName().equals(Thread.class.getName()))  
            {  
                continue;  
            }  
            if(st.getClassName().equals(this.getClass().getName()))  
            {  
                continue;  
            }  
            return devName + "[ " + Thread.currentThread().getName() + " : "  
                    + st.getFileName() + " : " + st.getLineNumber() + " "  
                    + st.getMethodName() + " ]";  
        }  
        return null;  
    }  
      
    /** 
     * The Log Level:i 
     * @param str 
     */  
    public void i(Object str)  
    {  
        if(logEnable)
        {  
            if(logLevel <= Log.INFO)  
            {  
                String name = getFunctionName();  
                if(name != null)  
                {  
                    Log.i(tag, name + " - " + str);  
                }  
                else  
                {  
                    Log.i(tag, str.toString());  
                }  
            }  
        }  
          
    }  
      
    /** 
     * The Log Level:d 
     * @param str 
     */  
    public void d(Object str)  
    {  
        if(logEnable)
        {  
            if(logLevel <= Log.DEBUG)  
            {  
                String name = getFunctionName();  
                if(name != null)  
                {  
                    Log.d(tag, name + " - " + str);  
                }  
                else  
                {  
                    Log.d(tag, str.toString());  
                }  
            }  
        }  
    }  
      
    /** 
     * The Log Level:V 
     * @param str 
     */  
    public void v(Object str)  
    {  
        if(logEnable)
        {  
            if(logLevel <= Log.VERBOSE)  
            {  
                String name = getFunctionName();  
                if(name != null)  
                {  
                    Log.v(tag, name + " - " + str);  
                }  
                else  
                {  
                    Log.v(tag, str.toString());  
                }  
            }  
        }  
    }  
      
    /** 
     * The Log Level:w 
     * @param str 
     */  
    public void w(Object str)  
    {  
        if(logEnable)
        {  
            if(logLevel <= Log.WARN)  
            {  
                String name = getFunctionName();  
                if(name != null)  
                {  
                    Log.w(tag, name + " - " + str);  
                }  
                else  
                {  
                    Log.w(tag, str.toString());  
                }  
            }  
        }  
    }  
      
    /** 
     * The Log Level:e 
     * @param str 
     */  
    public void e(Object str)  
    {  
        if(logEnable)
        {  
            if(logLevel <= Log.ERROR)  
            {  
                String name = getFunctionName();  
                if(name != null)  
                {  
                    Log.e(tag, name + " - " + str);  
                }  
                else  
                {  
                    Log.e(tag, str.toString());  
                }  
            }  
        }  
    }  
      
    /** 
     * The Log Level:e 
     * @param ex 
     */  
    public void e(Exception ex)  
    {  
        if(logEnable)
        {  
            if(logLevel <= Log.ERROR)  
            {  
                Log.e(tag, "error", ex);  
            }  
        }  
    }  
      
    /** 
     * The Log Level:e 
     * @param log 
     * @param tr 
     */  
    public void e(String log, Throwable tr)  
    {  
        if(logEnable)
        {  
            String line = getFunctionName();  
            Log.e(tag, "{Thread:" + Thread.currentThread().getName() + "}"  
                    + "[" + devName + line + ":] " + log + "\n", tr);  
        }  
    }  
}  
