package com.dong.mobilesafe.utils.security;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证工具类
 * 
 * @author Sam
 *
 */
public class ValidateUtil {

	private static final String TAG = ValidateUtil.class.getSimpleName();

	/**
	 * 验证图片是否符合分享规则
	 * @param imgUrl 图片后缀仅支持JPEG、GIF、PNG格式
	 * @return
	 */
	public static boolean validateImgUrl(String imgUrl) {
		// 分享到新浪微博。要上传的图片，仅支持JPEG、GIF、PNG格式，图片大小小于5M
		if (imgUrl.endsWith(".jpeg") || imgUrl.endsWith(".gif") 
				|| imgUrl.endsWith(".png") || imgUrl.endsWith(".JPEG") 
				|| imgUrl.endsWith(".GIF") || imgUrl.endsWith(".PNG") ) {
			
			return true;
		}
		return false;
	}
	
	/**
	 * 验证手机号码是否合法
	 * @param number 需要做验证的手机号码
	 * @return 返回true表示合法，false表示非法
	 */
	public static boolean validateMobilePhone(String number) {
		/*
		 * 国家号码段分配如下：
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186
		 * 电信：133、153、180、189、（1349卫通）
		 * 186/187/188是4G号码
		 * 注：手机号码必须是11位数
		 */
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9]))\\d{8}$");// 获得一个匹配模式
		Matcher m = p.matcher(number);// 获取一个匹配规则
		boolean matches = m.matches();// 开始匹配
		return matches;
	}
	
	/**
	 * 检查用户名是否合法
	 * @param name
	 * @return
	 */
	public static boolean validateUserName(String name) {
		Pattern p = Pattern.compile(".{1,12}$");// 获得一个匹配模式
		Matcher m = p.matcher(name);// 获取一个匹配规则
		return m.matches();// 开始匹配

	}
}
