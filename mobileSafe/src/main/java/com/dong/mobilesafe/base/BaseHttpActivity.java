package com.dong.mobilesafe.base;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import com.dong.mobilesafe.LoginActivity;
import com.dong.mobilesafe.R;
import com.dong.mobilesafe.utils.MyLogger;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View.OnClickListener;



/**
 * 网络请求基类
 * 
 */
public abstract class BaseHttpActivity extends BaseActivity implements OnClickListener {
	private static final boolean isPrintResultJson = true;
	
	public static final int REQUEST_CODE = 8888;
	private boolean isShowDialog = false;
	protected String host;
	protected FinalHttp finalHttp;
	

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		request();
	}
	
	
	@Override
	protected void onInitData() {
		super.onInitData();
		host = getString(R.string.host);
		if(TextUtils.isEmpty(host)) {
			throw new RuntimeException("host不能为空！");
		}
		finalHttp = new FinalHttp(BaseHttpActivity.this, true);
	}
	
	
	/**
	 * post请求方法 停止加载条,失败之后显示重新加载或设置网络 这些父类都处理了 子类不需要在处理
	 * 
	 * @param urlId
	 *            请求地址只需要传后面的就可以了 host不需要传
	 * @param jsonObject
	 *            请求参数封装的json对象
	 */
	protected void post(int urlId, JSONObject jsonObject) {
		post(urlId, jsonObject, 0, true);
	}

	/**
	 * post请求方法 停止加载条,失败之后显示重新加载或设置网络 这些父类都处理了 子类不需要在处理 这种是弹对话框的形式
	 * 
	 * @param urlId
	 *            请求地址只需要传后面的就可以了 host不需要传
	 * @param jsonObject
	 *            请求参数封装的json对象
	 * @param isShowLoading
	 *            是否显示全屏的加载条
	 */
	protected void postDialog(int urlId, JSONObject jsonObject, final boolean isShowLoading) {
		post(urlId, jsonObject, 0, isShowLoading);
	}

	/**
	 * post请求方法 停止加载条,失败之后显示重新加载或设置网络 这些父类都处理了 子类不需要在处理 这种是弹对话框的形式
	 * 
	 * @param urlId
	 *            请求地址只需要传后面的就可以了 host不需要传
	 * @param jsonObject
	 *            请求参数封装的json对象
	 * @param requestCode
	 *            请求码 用来区分一个activity中有多个请求
	 * @param isShowLoading
	 *            是否显示全屏的加载条
	 */
	protected void postDialog(int urlId, JSONObject jsonObject, int requestCode, final boolean isShowLoading) {
		isShowDialog = true;
		post(urlId, jsonObject, requestCode, isShowLoading);
	}

	/**
	 * post请求方法 停止加载条,失败之后显示重新加载或设置网络 这些父类都处理了 子类不需要在处理 这种是弹对话框的形式
	 * 
	 * @param urlId
	 *            请求地址只需要传后面的就可以了 host不需要传
	 * @param jsonObject
	 *            请求参数封装的json对象
	 * @param isShowLoading
	 *            是否显示全屏的加载条
	 */
	protected void post(int urlId, JSONObject jsonObject, final boolean isShowLoading) {
		post(urlId, jsonObject, 0, isShowLoading);
	}

	
	/**
	 * 上传文件
	 * @param urlId
	 *            请求地址只需要传后面的就可以了 host不需要传
	 * @param jsonObject
	 *            请求参数封装的json对象
	 * @param isShowLoading
	 *            是否显示全屏的加载条
	 */
	protected void postFile(int urlId, AjaxParams params, final boolean isShowLoading, final int requestCode) {
		finalHttp.post(host + getString(urlId), params, new AjaxCallBack<String>() {

			@Override
			public void onStart() {
		
			}

			@Override
			public void onSuccess(String json) {
				if (!TextUtils.isEmpty(json)) {
					json = json.replace(":null,", ":\"\",");
				}
				if (isPrintResultJson) {
					MyLogger.jLog().d("result = "+json);
				}
				
				// 没有登录
				if (!TextUtils.isEmpty(json) && json.contains("\"code\":\"401\"")) {
					loginActivity();
				} else {
					BaseHttpActivity.this.onSuccess(json, requestCode);
				}
				isShowDialog = false;
			}


			
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				BaseHttpActivity.this.onFailure();
			}
			
		});

	}

	/**
	 * post请求方法 停止加载条,失败之后显示重新加载或设置网络 这些父类都处理了 子类不需要在处理
	 * 
	 * @param urlId
	 *            请求地址只需要传后面的就可以了 host不需要传
	 * @param jsonObject
	 *            请求参数封装的json对象
	 */
	protected void post(int urlId, JSONObject jsonObject, int tag) {
		post(urlId, jsonObject, tag, true);
	}

	protected void postDefault(int urlId, JSONObject jsonObject, final int requestCode) {
		post(urlId, jsonObject, requestCode, false);
	};

	/**
	 * post请求方法 停止加载条,失败之后显示重新加载或设置网络 这些父类都处理了 子类不需要在处理 这种是中间有加载条的形式
	 * 
	 * @param urlId
	 *            请求地址只需要传后面的就可以了 host不需要传
	 * @param jsonObject
	 *            请求参数封装的json对象
	 * @param requestCode
	 *            请求码 用来区分一个activity中有多个请求
	 * @param isShowLoading
	 *            是否显示全屏的加载条
	 */
	protected void post(int urlId, JSONObject jsonObject, final int requestCode, final boolean isShowLoading) {
		if (TextUtils.isEmpty(host) || urlId == 0) {
			throw new RuntimeException("host或请求的url为空！");
		}
		if (isShowLoading) {
							// 显示加载条
		}
		AjaxParams params = new AjaxParams();
		if (jsonObject != null) {
			params.put("requestParams", jsonObject.toString());
		}
		finalHttp.post(host + getString(urlId), params, new AjaxCallBack<String>() {

			@Override
			public void onStart() {
				
				if (isShowLoading) {
					if (isShowDialog) {
						
					} else {
						showLoading();
					}
				}
			}

			@Override
			public void onSuccess(String json) {
				if (!TextUtils.isEmpty(json)) { 
					json = json.replace(":null", ":\"\"");
				}
				if (isPrintResultJson) {
					MyLogger.jLog().d("result = "+json);
				}
				if (isShowLoading) {
					if (isShowDialog) {
						
					} else {
						hideLoading();
					}
				}
				// 没有登录
				if (!TextUtils.isEmpty(json) && json.contains("\"code\":\"401\"")) {
					loginActivity();
				} else {
					BaseHttpActivity.this.onSuccess(json, requestCode);
				}
				isShowDialog = false;
			}
			
			
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				BaseHttpActivity.this.onFailure();
			}
		});
	}

	
	/**
	 * 网络请求失败，回调方法
	 */
	public abstract void onFailure();

	
	/**
	 * 开启登录界面，并要求返回结果
	 */
	protected void loginActivity() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivityForResult(intent, REQUEST_CODE);
	}


	/**
	 * 请求网络构造参数
	 */
	protected abstract void request();

	/**
	 * 网络请求数据成功之后回调方法
	 * 
	 * @param json
	 * @param requestCode
	 */
	protected abstract void onSuccess(String json, int requestCode);

	
	/**
	 * 加载数据失败之后点击重新加载会调用此方法
	 */
	protected abstract void reLoad();

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (REQUEST_CODE == requestCode && resultCode == RESULT_OK) {
			loginCallBack();
		} else {
			onActivityResultCallBack(requestCode, resultCode, data);
		}
	}

	/**
	 * 开启Activity时，除登陆之外的，其他回调接口
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	protected void onActivityResultCallBack(int requestCode, int resultCode,Intent data) {
		
	}

	/**
	 * 登陆成功回调方法
	 */
	protected void loginCallBack() {
		
	}
}
