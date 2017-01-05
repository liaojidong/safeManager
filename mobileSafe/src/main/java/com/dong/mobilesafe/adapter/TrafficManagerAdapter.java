package com.dong.mobilesafe.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dong.mobilesafe.R;
import com.dong.mobilesafe.base.CommonBaseAdapter;
import com.dong.mobilesafe.domain.AppInfo;

public class TrafficManagerAdapter extends CommonBaseAdapter<AppInfo> {
    private int type;

    public TrafficManagerAdapter(Context context, int type) {
        super(context);
        this.type = type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.list_item_traffic_info, null);
            mHolder = new ViewHolder();
            mHolder.appIcon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
            mHolder.appName = (TextView) convertView.findViewById(R.id.tv_app_name);
            mHolder.mobileTraffic = (TextView) convertView.findViewById(R.id.tv_traffic_mobile_count);
            mHolder.wifiTraffic = (TextView) convertView.findViewById(R.id.tv_traffic_rx_count);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        AppInfo appinfo = mList.get(position);
        mHolder.appIcon.setImageDrawable(appinfo.getIcon());
        mHolder.appName.setText(appinfo.getName());
        switch (type) {
            case R.id.rb_statistics_count:
                mHolder.mobileTraffic.setText(appinfo.getMobileTraffic() == 0 ? 0 + "KB" : Formatter.formatShortFileSize(context, appinfo.getMobileTraffic()));
                mHolder.wifiTraffic.setText(appinfo.getWifiTraffic() == 0 ? 0 + "KB" : Formatter.formatShortFileSize(context, appinfo.getWifiTraffic()));
                break;

            case R.id.rb_statistics_today:
                mHolder.mobileTraffic.setText(appinfo.getTodayMobileTraffic() == 0 ? 0 + "KB" : Formatter.formatShortFileSize(context, appinfo.getTodayMobileTraffic()));
                mHolder.wifiTraffic.setText(appinfo.getTodayWifiTraffic() == 0 ? 0 + "KB" : Formatter.formatShortFileSize(context, appinfo.getTodayWifiTraffic()));
                break;
        }

        return convertView;
    }


    public void setType(int type) {
        if (this.type != type) {
            this.type = type;
            notifyDataSetChanged();
        }
    }


    class ViewHolder {
        ImageView appIcon;
        TextView appName, mobileTraffic, wifiTraffic;
    }

}
