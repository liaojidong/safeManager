package com.dong.mobilesafe.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;


public class StringUtils {

	
	/**
	 * 是否是空的字符串
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		if(str == null || "".equals(str)){
			return true;
		}
		return false;
	}
	
	/**
	 * 是否是非空的字符串
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str){
		if(str != null && !"".equals(str)){
			return true;
		}
		return false;
	}
	

    /**
     * 获得汉语拼音首字母
     * @param str
     * @return
     */
    public static String getAlpha(String str)
    {
        if (str == null)
        {
            return "#";
        }

        if (str.trim().length() == 0)
        {
            return "#";
        }

        char c = str.trim().substring(0, 1).charAt(0);
        // 正则表达式，判断首字母是否是英文字母  
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c + "").matches())
        {
            return (c + "").toUpperCase();
        }
        else
        {
            return "#";
        }
    }

    /**
     * 根据日期解析出当前星期几
     * @param data
     * @return
     */
    public static String dayForWeek(Date data)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        String dayForWeek = null;
        switch (c.get(Calendar.DAY_OF_WEEK))
        {
            case 1:
                dayForWeek = "日";
                break;
            case 2:
                dayForWeek = "一";
                break;
            case 3:
                dayForWeek = "二";
                break;
            case 4:
                dayForWeek = "三";
                break;
            case 5:
                dayForWeek = "四";
                break;
            case 6:
                dayForWeek = "五";
                break;
            case 7:
                dayForWeek = "六";
                break;
            default:
                break;
        }
        return dayForWeek;
    }

    /**
     * 截取日期
     * @param time   日期格式:yyyy-MM-dd HH:mm:ss
     * @param length 从左截取个数
     * @return
     */
    public static String getSubTime(String time, int length)
    {
        String newSubTime = "";
        if (StringUtils.isNotEmpty(time))
        {
            newSubTime = time.substring(0, length);
        }
        return newSubTime;
    }

    /** 
    * 日期转换成字符串 
    * @param date  
    * @return str 
    */
    public static String DateToStr(Date date, String format)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String str = sdf.format(date);
        return str;
    }

    /** 
    * 字符串转换成日期 
    * @param str 
    * @return date 
    */
    public static Date strToDate(String str, String format)
    {

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try
        {
            date = sdf.parse(str);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return date;
    }
    

    /**
     * 打电话
     * @param context
     * @param tel
     */
    public static void call(Context context, String tel)
    {
        if (StringUtils.isNotEmpty(tel))
        {
        	String sz = "tel:" + tel;
        	Uri uri = Uri.parse(sz);
        	Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        	context.startActivity(intent);
        }
    }

    /**
     * 格式化价格
     * @return
     */
    public static String formatPrice(String price)
    {
        String newprice = "0.00";
        if (StringUtils.isNotEmpty(price))
        {
            DecimalFormat df = new DecimalFormat("#######0.00");
            newprice = df.format(Double.valueOf(price));
        }
        return newprice;
    }

    /**
     * 格式化价格
     * @return
     */
    public static String formatPrice(Double price)
    {
        String newprice = "0.00";
        DecimalFormat df = new DecimalFormat("#######0.00");
        newprice = df.format(price);
        return newprice;
    }

    /** 
     * 字符串转换成日期 (默认格式：yyyy-MM-dd HH:mm:ss)
     * @param str 
     * @return date 
     */
    public static Date strToDate(String str)
    {
        if (StringUtils.isNotEmpty(str))
        {
            return strToDate(str, "yyyy-MM-dd HH:mm:ss");
        }
        return null;
    }

    /**
     * 比较时间大小
     * @param firsttime
     * @param secondtime
     * @return true:fristtime>secondtime,false:fristtime<=secondtime
     */
    public static Boolean isgt(String firsttime, String secondtime)//is greater than
    {
        Boolean isFlag = false;
        Date firstdate = strToDate(firsttime);
        Date seconddate = strToDate(secondtime);
        if (firstdate.getTime() > seconddate.getTime())
        {
            isFlag = true;
        }
        return isFlag;

    }

    /**
     * 截取字符串（超过一定长度加...）
     * @param text
     * @param len 个数
     * @return
     */
    public static String getEllipsisString(String text, int len)
    {
        String newString = text;
        if (StringUtils.isNotEmpty(text) && text.length() > len)
        {
            newString = text.subSequence(0, len - 1) + "...";
        }
        return newString;
    }
    
    /**
     * 不够2位用0补齐
     * @param str
     * @return
     */
    public static String LeftPad_Tow_Zero(int str)
    {
        java.text.DecimalFormat format = new java.text.DecimalFormat("00");
        return format.format(str);

    }
    
    /**
     * 转换大小写
     * @param str
     * @return
     */
    public static String getCnString(String str)
    {
        Double n = StringToDouble(str);
        String fraction[] = { "角", "分" };
        String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
        String unit[][] = { { "元", "万", "亿" }, { "", "拾", "佰", "仟" } };

        String head = n < 0 ? "负" : "";
        n = Math.abs(n);

        String s = "";
        for (int i = 0; i < fraction.length; i++)
        {
            s += (digit[(int) (Math.floor(n * 10 * Math.pow(10, i)) % 10)] + fraction[i]).replaceAll("(零.)+", "");
        }
        if (s.length() < 1)
        {
            s = "整";
        }
        int integerPart = (int) Math.floor(n);

        for (int i = 0; i < unit[0].length && integerPart > 0; i++)
        {
            String p = "";
            for (int j = 0; j < unit[1].length && n > 0; j++)
            {
                p = digit[integerPart % 10] + unit[1][j] + p;
                integerPart = integerPart / 10;
            }
            s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i] + s;
        }
        return head + s.replaceAll("(零.)*零元", "元").replaceFirst("(零.)+", "").replaceAll("(零.)+", "零").replaceAll("^整$", "零元整");
    }
    
    /**
     * String转Double
     * @param str
     * @return
     */
    public static Double StringToDouble(String str)
    {

        if (StringUtils.isEmpty(str))
        {
            return 0.0;
        }
        try
        {
            return Double.valueOf(str);
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        return 0.0;
    }
    
    
    public static String formatPhoneNumber(String number) {
    	
    	return number.replaceFirst("^\\+86", "").trim();
    }
    
    
    
}
