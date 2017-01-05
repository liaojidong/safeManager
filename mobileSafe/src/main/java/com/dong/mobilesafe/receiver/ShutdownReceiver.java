package com.dong.mobilesafe.receiver;

import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dong.mobilesafe.db.dao.TrafficInfoDao;
import com.dong.mobilesafe.domain.TrafficInfo;
import com.dong.mobilesafe.utils.DateUtil;
import com.dong.mobilesafe.utils.MyLogger;

public class ShutdownReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
			MyLogger.jLog().d("接收到关机广播");
			TrafficInfoDao dao = new TrafficInfoDao(context);
			String id = DateUtil.transformYYYYMMDD(new Date()).getTime()+"";
			TrafficInfo info = dao.findById(id,TrafficInfo.class);
			if(info != null) {
				info.setBootMobileRx(0L);
				info.setBootMobileTx(0L);
				info.setBootRxCount(0L);
				info.setBootTxCount(0L);
				dao.update(info);
			}
	}

}
