package com.dong.mobilesafe.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dong.mobilesafe.R;
import com.dong.mobilesafe.base.CommonBaseAdapter;
import com.dong.mobilesafe.bean.SlidingMenuItem;

/**
 * Created by liaojd on 2016/6/29.
 */
public class MenuSlidingAdapter extends CommonBaseAdapter<SlidingMenuItem> {
    private final static int []icons = {
            R.drawable.menu_open,
            R.drawable.info,
            R.drawable.share,
            R.drawable.good,
            R.drawable.money,
            R.drawable.about
    };

    public MenuSlidingAdapter(Context context) {
        super(context);
        String[] titles = context.getResources().getStringArray(R.array.sliding_menu_items);
        for (int i = 0; i < titles.length; i++) {
            SlidingMenuItem menuItem = new SlidingMenuItem();
            menuItem.setName(titles[i]);
            menuItem.setIcon(icons[i]);
            mList.add(menuItem);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_sliding_menu, null);
            holder = new ViewHolder();
            holder.menu_icon = (ImageView) convertView.findViewById(R.id.menu_icon);
            holder.menu_title = (TextView) convertView.findViewById(R.id.menu_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final SlidingMenuItem item = mList.get(position);
        holder.menu_title.setText(item.getName());
        holder.menu_icon.setImageResource(item.getIcon());
        return convertView;
    }

    private static class ViewHolder {
        private TextView menu_title;
        private ImageView menu_icon;
    }
}
