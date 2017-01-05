package com.dong.mobilesafe.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;



public class HttpUtil { 
	public static final String REQUEST_PARAMS = "requestParams";

	
	public static final int STATE_INVALIDE_NETWORK = -1;  //无网络
	public static final int STATE_UNKNOWN_NEWORK = 0;    //未知网络
	public static final int STATE_MOBILE_NETWORK = 1;    //移动网络
	public static final int STATE_WIFI_NETWORK = 2;      //wifi网络
	public static final int STATE_ETHERNET_NETWORK = 3;  //以太网

	
	
	
	
	
	public static boolean isCompleteURL(String url) {
		if(StringUtils.isEmpty(url)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^(http://|https://).+");
		Matcher matcher = pattern.matcher(url);
		if(matcher.matches()) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * 检查网络是否可用
	 *  
	 * @param context
	 * @return true：网络可用 false：网络不可用
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager mgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] info = mgr.getAllNetworkInfo();
		if (info != null) {
			for (int i = 0; i < info.length; i++) {
				if (info[i].getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 获取设备IP地址
	 * @param context
	 * @return
	 */
	public String getDeviceIp(Context context) {
		String ipAddress = "";
		switch (getNetWorkState(context)) {
		case STATE_WIFI_NETWORK:
			   WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);  
		        //判断wifi是否开启  
		        if (!wifiManager.isWifiEnabled()) {  
		        	wifiManager.setWifiEnabled(true);    
		        }  
		        WifiInfo wifiInfo = wifiManager.getConnectionInfo();       
		        ipAddress =intToIp(wifiInfo.getIpAddress()) ; 
			break;

		case STATE_MOBILE_NETWORK:
		       try  
		        {  
		            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)  
		            {  
		               NetworkInterface intf = en.nextElement();  
		               for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)  
		               {  
		                   InetAddress inetAddress = enumIpAddr.nextElement();  
		                   if (!inetAddress.isLoopbackAddress())  
		                   {  
		                	   ipAddress =  inetAddress.getHostAddress().toString();  
		                   }  
		               }  
		           }  
		        }  
		        catch (SocketException e) {  
		            e.printStackTrace();
		        }  
			break;
			
		}
		return ipAddress;
	}
	
	
	/**
	 * 将整数转换为ip地址
	 * @param i
	 * @return
	 */
	 private String intToIp(int i) {       
         
         return (i & 0xFF ) + "." +       
       ((i >> 8 ) & 0xFF) + "." +       
       ((i >> 16 ) & 0xFF) + "." +       
       ( i >> 24 & 0xFF) ;  
    }   
	
	/**
	 * 获取网络状态
	 * @param context
	 * @return
	 */
	public static int getNetWorkState(Context context) {
		
		   ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 

	        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo(); 
	        if(null == networkInfo){ 
	            return STATE_INVALIDE_NETWORK; 
	        } 
	        
	        int nType = networkInfo.getType(); 

	        if(nType==ConnectivityManager.TYPE_MOBILE){ 
	        	
	        	return STATE_MOBILE_NETWORK;
	        	
	        } else if(nType==ConnectivityManager.TYPE_WIFI){ 
	        	
	        	return STATE_WIFI_NETWORK;
	        	
	        } else if(nType == ConnectivityManager.TYPE_ETHERNET) {
	        	return STATE_ETHERNET_NETWORK;
	        }
		
		return STATE_UNKNOWN_NEWORK;
		
	}
	
	

	/**
	 * 获取运营商名字
	 * @param context
	 * @return
	 */
	public static String getOperatorName(Context context) {
		   String ProvidersName = "";
		    TelephonyManager telephonyManager = (TelephonyManager) context.
		    					getSystemService(Context.TELEPHONY_SERVICE);
	        String IMSI = telephonyManager.getSubscriberId();
	        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
	        MyLogger.jLog().d("IMSI = "+IMSI);
	        if(StringUtils.isEmpty(IMSI)) {
	        	return ProvidersName;
	        }
	        
	        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
	            ProvidersName = "移动";
	        } else if (IMSI.startsWith("46001")) {
	            ProvidersName = "联通";
	        } else if (IMSI.startsWith("46003")) {
	            ProvidersName = "电信";
	        }else {
	        	ProvidersName = "其他";
	        }
	        return ProvidersName;

	}
	
	
	
	

}
