package com.dong.mobilesafe.utils;

import java.util.Hashtable;

import android.util.Log;

public class MyLogger  
{  
    private final static boolean                logFlag         = true;  
      
    public final static String                  tag             = "[ADSDK]";  
    private final static int                    logLevel        = Log.VERBOSE;  
    private static Hashtable<String, MyLogger>    sLoggerTable    = new Hashtable<String, MyLogger>();  
    private String                              devName;  
      
    private static MyLogger                     jlog;  

    private static final String                 JESSE           = "@jesse@ ";  
      
    /**
     * 鏍规嵁寮?彂鑰呭悕绉板垱寤篖ogger瀵硅薄銆?
     * @param devName
     */
    private MyLogger(String devName)  
    {  
        this.devName = devName;  
    }  
      
    /** 
     *  鏍规嵁寮?彂鑰呭悕绉板垱寤轰竴涓狶ogger瀵硅薄銆?
     * @param devName 寮?彂鑰呭悕绉?
     * @return 
     */  
    @SuppressWarnings("unused")  
    private static MyLogger getLogger(String devName)  
    {  
        MyLogger classLogger = (MyLogger) sLoggerTable.get(devName);  
        if(classLogger == null)  
        {  
            classLogger = new MyLogger(devName);  
            sLoggerTable.put(devName, classLogger);  
        }  
        return classLogger;  
    }  
      
    /** 
     * Purpose:Mark user one 
     * @return 
     */  
    public static MyLogger jLog()  
    {  
        if(jlog == null)  
        {  
            jlog = new MyLogger(JESSE);  
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
        if(logFlag)  
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
        if(logFlag)  
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
        if(logFlag)  
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
        if(logFlag)  
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
        if(logFlag)  
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
        if(logFlag)  
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
        if(logFlag)  
        {  
            String line = getFunctionName();  
            Log.e(tag, "{Thread:" + Thread.currentThread().getName() + "}"  
                    + "[" + devName + line + ":] " + log + "\n", tr);  
        }  
    }  
}  
