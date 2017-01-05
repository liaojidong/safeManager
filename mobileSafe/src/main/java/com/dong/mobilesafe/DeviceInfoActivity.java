package com.dong.mobilesafe;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.dong.mobilesafe.base.BaseActivity;
import com.dong.mobilesafe.base.BaseTitleActivity;
import com.dong.mobilesafe.utils.CpuInfoUtils;
import com.dong.mobilesafe.utils.DeviceInfoUtils;

public class DeviceInfoActivity extends BaseTitleActivity {
    private ExpandableListView mListView;

    @Override
    protected int onGetContentView() {
        return R.layout.activity_device_info;
    }

    @Override
    protected void onPrepareView() {
        super.onPrepareView();
        setBarTitle(R.string.device_info);
        mListView = (ExpandableListView) findViewById(R.id.elv_device_info);
        mListView.setAdapter(new DeviceInfoAdapter(this));
    }

    private class DeviceInfoAdapter extends BaseExpandableListAdapter {
        String[] groups;
        String[][] children;
        Context context;


        public DeviceInfoAdapter(Context context) {
            this.context = context;
            DisplayMetrics dm = getResources().getDisplayMetrics();
            double physicalSize = (Math.sqrt(Math.pow(dm.widthPixels,2)+Math.pow(dm.heightPixels,2)))/(160*dm.density);
            groups = getResources().getStringArray(R.array.device_info_grounds);
            children = new String[][]{
                    {"设备名称：" + Build.DEVICE, "系统版本：" + Build.VERSION.RELEASE, "SDK版本：" + Build.VERSION.SDK_INT, "设备型号：" + Build.MODEL,"生产厂家: "+Build.MANUFACTURER,"基带版本: "+Build.VERSION.INCREMENTAL},
                    {"cup架构："+ Build.CPU_ABI,"cup名字：" + CpuInfoUtils.getCpuName(), "cpu最大频率：" + CpuInfoUtils.getMaxCpuFreq()+" HZ", "cpu最小频率：" + CpuInfoUtils.getMinCpuFreq()+" HZ", "cpu个数：" + CpuInfoUtils.getCpuCount()+"核"},
                    {"屏幕分辨率：" + dm.heightPixels + " × " + dm.widthPixels , "屏幕物理尺寸:"+String.format("%.2f", physicalSize)+"英寸", "屏幕密度:" + dm.densityDpi + "/英寸",},
                    {"MAC:"+ DeviceInfoUtils.getWifiMac(context),"IMEI:"+DeviceInfoUtils.getIMEI(context),"Android ID:"+DeviceInfoUtils.getAndroidId(context)}

            };
        }


        @Override
        public int getGroupCount() {
            return groups.length;
        }

        @Override
        public int getChildrenCount(int i) {
            return children[i].length;
        }

        @Override
        public Object getGroup(int i) {
            return groups[i];
        }

        @Override
        public Object getChild(int i, int i1) {
            return children[i][i1];
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder groupHolder = null;
            if (convertView == null)
            {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_device_ground, null);
                groupHolder = new GroupHolder();
                groupHolder.tv_ground_name = (TextView)convertView.findViewById(R.id.tv_ground_name);
                convertView.setTag(groupHolder);
            }
            else
            {
                groupHolder = (GroupHolder)convertView.getTag();
            }

            groupHolder.tv_ground_name.setText(groups[groupPosition]);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ViewHolder itemHolder = null;
            if (convertView == null)
            {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_device_info, null);
                itemHolder = new ViewHolder();
                itemHolder.tv_info = (TextView)convertView.findViewById(R.id.tv_info);
                convertView.setTag(itemHolder);
            }
            else
            {
                itemHolder = (ViewHolder)convertView.getTag();
            }
            itemHolder.tv_info.setText(children[groupPosition][childPosition]);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }

    private static class ViewHolder {
        TextView tv_info;
    }

    private static class GroupHolder {
       TextView tv_ground_name;
    }
}
