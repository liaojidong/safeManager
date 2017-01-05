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
package net.tsz.afinal.http.entityhandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;

import android.util.Log;

public class StringEntityHandler {
	public static final int MAX_Size = 1*1024*1024;//1M

	public Object handleEntity(HttpEntity entity, EntityCallBack callback,String charset)throws IOException {
		if (entity == null)
			return null;
		
		
		ByteArrayOutputStream outStream = new ByteArrayOutputStream(1024);
		byte[] buffer = new byte[1024];
		long count = entity.getContentLength();
		if(count >=MAX_Size) { //防止内存溢出
			return null;
		}
		long curCount = 0;
		int len = -1;
		InputStream is = entity.getContent();
		String data = "";
		try {
			
			Log.d("StringEntityHandler", "<StringEntityHandler>");
			while ((len = is.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
				curCount += len;
				if (callback != null)
					callback.callBack(count, curCount, false);
			}
			if (callback != null)
				callback.callBack(count, curCount, true);

			data = outStream.toString("UTF-8");
		} catch (OutOfMemoryError e) {
			Log.e("error", data);
			System.gc();
			e.printStackTrace();
		}finally {
			if(outStream!=null) {
				outStream.close();
			}
			if(is != null) {
				is.close();
			}
		}
		
		return data;
	}

}
