package com.dong.mobilesafe.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liaojd on 2016/7/8.
 */
public abstract class BaseRecyleAdapter<VH extends RecyclerView.ViewHolder,E> extends RecyclerView.Adapter<VH> {
    protected List<E> mList = new ArrayList<>();
    protected Context context;


    public BaseRecyleAdapter(Context context) {
        this.context = context;
    }


    /**
     * 在列表头添加一条数据
     * @param e
     */
    public void addItemAtHeard(E e) {
        mList.add(0,e);
        notifyItemInserted(0);
    }


    /**
     * 添加数据列表到列表尾部
     * @param list
     */
    public void addListAtEnd(final List<E> list) {
        this.mList.addAll(list);
        notifyItemRangeInserted(mList.size(),list.size());
    }

    /**
     * 删除一条数据
     * @param e
     */
    public void removeItem(E e) {
        int position = mList.indexOf(e);
        mList.remove(e);
        notifyItemRemoved(position);
    }

    /**
     * 删除一条数据
     * @param position
     */
    public void removeItem(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 删除所有数据
     */
    public void removeAll() {
        mList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
