package com.dong.mobilesafe.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dong.mobilesafe.R;
import com.dong.mobilesafe.base.CommonBaseAdapter;
import com.dong.mobilesafe.bean.DateSelectItem;

/**
 * Created by liaojd on 2016/7/10.
 */
public class DateSelectorAdapter extends CommonBaseAdapter<DateSelectItem> {


    public DateSelectorAdapter(Context context) {
        super(context);
        for (int i = 1; i <= 31; i++) {
            DateSelectItem item = new DateSelectItem();
            item.setDate(i + "");
            if (i == 1) {
                item.setSelected(true);
            } else {
                item.setSelected(false);
            }
            mList.add(item);
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setText(mList.get(i).getDate());
        if (mList.get(i).isSelected()) {
            textView.setBackgroundColor(context.getResources().getColor(R.color.primary_selected_blue));
        } else {
            textView.setBackgroundColor(context.getResources().getColor(R.color.primary_blue));
        }
        textView.setPadding(0, 20, 0, 20);

        return textView;
    }

    public void setSelectedDate(int position) {
        if (position > 31) return;
        for (int i = 0; i < mList.size(); i++) {
            DateSelectItem item = mList.get(i);
            if (i == position) {
                item.setSelected(true);
            } else {
                item.setSelected(false);
            }
        }
        notifyDataSetChanged();
    }


}
