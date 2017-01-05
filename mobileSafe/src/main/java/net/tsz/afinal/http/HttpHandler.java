/**
 * Copyright (c) 2012-2013, Michael Yang 杨福海 (www.yangfuhai.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.tsz.afinal.http;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownHostException;

import net.tsz.afinal.core.AsyncTask;
import net.tsz.afinal.http.entityhandler.EntityCallBack;
import net.tsz.afinal.http.entityhandler.FileEntityHandler;
import net.tsz.afinal.http.entityhandler.StringEntityHandler;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

import com.dong.mobilesafe.utils.MyLogger;



import android.os.SystemClock;


public class  HttpHandler  <T> extends  AsyncTask<Object, Object, Object> implements EntityCallBack{

	private final AbstractHttpClient client;
	private final HttpContext context;
	
	private final StringEntityHandler mStrEntityHandler = new StringEntityHandler();
	private final FileEntityHandler mFileEntityHandler = new FileEntityHandler();
	
	private final AjaxCallBack<T> callback;
	
	private int executionCount = 0;
	private String targetUrl = null; //下载的路径
	private boolean isResume = false; //是否断点续传
	private String charset;
	private long count;

	public HttpHandler(AbstractHttpClient client, HttpContext context, AjaxCallBack<T> callback,String charset) {
		this.client = client;
		this.context = context;
		this.callback = callback;
		this.charset = charset;
	}


	private void makeRequestWithRetries(HttpUriRequest request) throws IOException {
		if(isResume && targetUrl!= null){
			File downloadFile = new File(targetUrl);
			long fileLen = 0;
			if(downloadFile.isFile() && downloadFile.exists()){
				fileLen = downloadFile.length();
			}
			request.setHeader("Range", "bytes="+fileLen+"-"+count);
			MyLogger.jLog().d("range"+"bytes = "+fileLen+" counnt = "+count);
		}
		
		boolean retry = true;
		IOException cause = null;
		HttpRequestRetryHandler retryHandler = client.getHttpRequestRetryHandler();
		while (retry) {
			try {
				if (!isCancelled()) {
					HttpResponse response = client.execute(request, context);
					if (!isCancelled()) {
						handleResponse(response);
					} 
				}
				return;
			} catch (UnknownHostException e) {
				publishProgress(UPDATE_FAILURE,e,0,"unknownHostException：can't resolve host");
				return;
			} catch (IOException e) {
				cause = e;
				retry = retryHandler.retryRequest(cause, ++executionCount,context);
			} catch (NullPointerException e) {
				// HttpClient 4.0.x 之前的一个bug
				// http://code.google.com/p/android/issues/detail?id=5255
				cause = new IOException("NPE in HttpClient" + e.getMessage());
				retry = retryHandler.retryRequest(cause, ++executionCount,context);
			}catch (Exception e) {
				cause = new IOException("Exception" + e.getMessage());
//				retry = retryHandler.retryRequest(cause, ++executionCount,context);
			}
		}
		if(cause!=null)
			throw cause;
		else
			throw new IOException("未知网络错误");
	}

	@Override
	protected Object doInBackground(Object... params) {
		if(params!=null && params.length == 3){
			targetUrl = String.valueOf(params[1]);
			isResume = (Boolean) params[2];
		}
		try {
			publishProgress(UPDATE_START); // 开始
			HttpUriRequest request = (HttpUriRequest)params[0];
			if(isResume) {
				count = getDownloadFileSize(request.getURI().toString());
				MyLogger.jLog().d("count = "+count);
			}
			makeRequestWithRetries(request);
			
		} catch (IOException e) {
			publishProgress(UPDATE_FAILURE,e,0,e.getMessage()); // 结束
		}

		return null;
	}

	private final static int UPDATE_START = 1;
	private final static int UPDATE_LOADING = 2;
	private final static int UPDATE_FAILURE = 3;
	private final static int UPDATE_SUCCESS = 4;

	@SuppressWarnings("unchecked")
	@Override
	protected void onProgressUpdate(Object... values) {
		int update = Integer.valueOf(String.valueOf(values[0]));
		switch (update) {
		case UPDATE_START:
			if(callback!=null)
				callback.onStart();
			break;
		case UPDATE_LOADING:
			if(callback!=null)
				callback.onLoading(Long.valueOf(String.valueOf(values[1])),Long.valueOf(String.valueOf(values[2])));
			break;
		case UPDATE_FAILURE:
			if(callback!=null)
				callback.onFailure((Throwable)values[1],(Integer)values[2],(String)values[3]);
			break;
		case UPDATE_SUCCESS:
			if(callback!=null)
				callback.onSuccess((T)values[1]);
			break;
		default:
			break;
		}
		super.onProgressUpdate(values);
	}
	
	public boolean isStop() {
		return mFileEntityHandler.isStop();
	}


	/**
	 * @param stop 停止下载任务
	 */
	public void stop() {
		mFileEntityHandler.setStop(true);
	} 

	private void handleResponse(HttpResponse response) {
		StatusLine status = response.getStatusLine();
		if (status.getStatusCode() >= 300) {
			String errorMsg = "response status error code:"+status.getStatusCode();
			if(status.getStatusCode() == 416 && isResume){
				errorMsg += " \n maybe you have download complete.";
				publishProgress(UPDATE_SUCCESS,new File(targetUrl));
			}else {
				publishProgress(UPDATE_FAILURE,new HttpResponseException(status.getStatusCode(), status.getReasonPhrase()),status.getStatusCode() ,errorMsg);
			}
		
		} else {
			try {
				HttpEntity entity = response.getEntity();
				Object responseBody = null;
				if (entity != null) {
					time = SystemClock.uptimeMillis();
					if(targetUrl != null){
						responseBody = mFileEntityHandler.handleEntity(entity,this,targetUrl,isResume,count);
						
					}
					else{
						responseBody = mStrEntityHandler.handleEntity(entity,this,charset);
					}
						
				}
				publishProgress(UPDATE_SUCCESS,responseBody);
				
			} catch (Exception e) {
				publishProgress(UPDATE_FAILURE,e,0,e.getMessage());
			}
			
		}
	}
	
	
	private long time;
	@Override
	public void callBack(long count, long current,boolean mustNoticeUI) {
		if(callback!=null && callback.isProgress()){
			if(mustNoticeUI){
				publishProgress(UPDATE_LOADING,count,current);
			}else{
				long thisTime = SystemClock.uptimeMillis();
				if(thisTime - time >= callback.getRate()){
					time = thisTime ;
					publishProgress(UPDATE_LOADING,count,current);
				}
			}
		}
	}
	
	public long getDownloadFileSize(String target) {
		HttpURLConnection conn = null;
		long lenth = 0;
		try {
			URL url = new URL(target);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5 * 1000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty(
					"Accept",
					"image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			conn.setRequestProperty("Accept-Language", "zh-CN");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty(
					"User-Agent",
					"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.connect();
			if (conn.getResponseCode() == 200) {
				lenth = conn.getContentLength();
}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			conn.disconnect();
		}
		return lenth;
}
	

}