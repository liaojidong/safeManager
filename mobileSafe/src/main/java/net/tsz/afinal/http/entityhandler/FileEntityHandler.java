package net.tsz.afinal.http.entityhandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;

import com.dong.mobilesafe.exception.FileIncompleteException;
import com.dong.mobilesafe.exception.NetworkDataException;
import com.dong.mobilesafe.utils.MyLogger;

import android.text.TextUtils;




public class FileEntityHandler {
	
	private boolean mStop = false;
	private static final int BUFFER_SIZE = 1024;
	

	public boolean isStop() {
		return mStop;
	}



	public void setStop(boolean stop) {
		this.mStop = stop;
	}


	public Object handleEntity(HttpEntity entity, EntityCallBack callback,String target,boolean isResume,long count) throws IOException {
		MyLogger.jLog().d("target = "+target);
		
		if (TextUtils.isEmpty(target) || target.trim().length() == 0)
			return null;

		File targetFile = new File(target);

		if (!targetFile.exists()) {
			targetFile.createNewFile();
		}

		if(mStop){
			return null;
		}
		long current = 0;
		FileOutputStream os = null;
		try {
			if (isResume) {
				current = targetFile.length();
				os = new FileOutputStream(targetFile, true);
			} else {
				os = new FileOutputStream(targetFile);
			}

			InputStream input = entity.getContent();
			if (current == count ) {
				return targetFile;
			}
			long downloadSize = entity.getContentLength();
			
			if(downloadSize != (count - current)) {
				throw new NetworkDataException("network data exception");
			}
			
			if (mStop) {
				return null;
			}
			int readLen = 0;
			byte[] buffer = new byte[BUFFER_SIZE];
			MyLogger.jLog().d("下载前 current = "+current+" count = "+ count);
			while (!mStop && !(current >= count)
					&& ((readLen = input.read(buffer, 0, BUFFER_SIZE)) != -1)) {//未全部读取
				os.write(buffer, 0, readLen);
				current += readLen;
				callback.callBack(count, current, false);
			}
			callback.callBack(count, current, true);
			if (mStop && current < count) { 	//用户主动停止
				throw new IOException("user stop download thread");
			}
			if(current > count) {  //文件已经损坏
				targetFile.delete();
				throw new FileIncompleteException("The file has been damaged");
			}
			
		}finally {
			MyLogger.jLog().d("下载后 current = "+current+" count = "+ count);
			if(os!=null) {
				os.flush();
				os.close();
			}
			
		}
		if(current > 0 && current == count) {
			return targetFile;
		}else {
			return null;
		}
	}
	
	
	


}
