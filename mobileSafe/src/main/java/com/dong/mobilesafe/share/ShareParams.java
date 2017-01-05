package com.dong.mobilesafe.share;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;

import java.io.ByteArrayOutputStream;

public class ShareParams {

	/**
	 * 待分享的文本
	 */
	private String text;
	/**
	 * 待分享的本地图片。如果目标平台使用客户端分享，此路径不可以在/data/data下面
	 */
	private String imagePath;
	/**
	 * 待分享的文件路径。这个用在Dropbox和Wechat中
	 */
	private String filePath;
	/**
	 * 分享内容的标题
	 */
	private String title;
	/**
	 * 待分享的网络图片
	 */
	private String imageUrl;
	/**
	 * 对分享内容的评价。区别于text，评论一般共应用的用户自己填写，部分平台支持此字段
	 */
	private String comment;
	/**
	 * 分享内容标题的链接地址
	 */
	private String titleUrl;
	/**
	 * 分享内容的url、在微信和易信中也使用为视频文件地址
	 */
	private String url;
	/**
	 * 邮箱地址或者短信电话号码，一般在邮箱或者短信中使用
	 */
	private String address; 
	
	/**
	 *  QQ空间的字段，标记分享应用的名称
	 */
	private String site; 
	/**
	 * QQ空间的字段，标记分享应用的网页地址
	 */
	private String siteUrl;
	/**
	 * 微信和易信的字段，各类分享内容中的图片bitmap对象，可以替代imagePath或者imageUrl
	 */
	private byte[] imageData;



	private int flag = SendMessageToWX.Req.WXSceneTimeline;

	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getTitleUrl() {
		return titleUrl;
	}

	public void setTitleUrl(String titleUrl) {
		this.titleUrl = titleUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getSiteUrl() {
		return siteUrl;
	}

	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}

	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}

	public void setImageBitmap(Bitmap bitmap) {
		if (bitmap != null) {
			this.imageData = bmpToByteArray(bitmap, false);
		}
	}

	private static byte[] bmpToByteArray(final Bitmap bmp,
			final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
