package com.dong.mobilesafe.base;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;

import com.dong.mobilesafe.domain.TaskInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用基础适配器
 *
 * @param <E>
 * @author Jesse
 */
public abstract class CommonBaseAdapter<E> extends BaseAdapter {
    protected ViewHolderClick<E> holderClick;
    protected List<E> mList;
    protected Context context;
    protected LayoutInflater mInflater;

    public interface ViewHolderClick<E> {
        public void onViewClick(View view, E e, int position);
    }

    /**
     * 点击事件抽象方法
     *
     * @param holderClick
     */
    public void setOnHolderClick(ViewHolderClick<E> holderClick) {
        this.holderClick = holderClick;
    }

    public CommonBaseAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        mList = new ArrayList<E>();
    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        if (position >= mList.size()) return null;
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 添加数据列表到列表头部
     *
     * @param list
     */
    public void addListAtStart(final List<E> list) {
        for (E e : list) {
            if (!mList.contains(e)) {
                mList.add(0, e);
                notifyDataSetChanged();
            }
        }
    }

    public void replaceBean(final int position, final E e) {
        CommonBaseAdapter.this.mList.remove(position);
        notifyDataSetChanged();
        CommonBaseAdapter.this.mList.add(position, e);
        notifyDataSetChanged();
    }

    public void replaceBean(E e) {
        int index = -1;
        for (int i = 0; i < mList.size(); i++) {
            if (e.equals(mList.get(i))) {
                index = i;
                break;
            }
        }
        if (index == -1) return;
        replaceBean(index, e);
    }


    /**
     * 添加数据列表到列表尾部
     *
     * @param list
     */
    public void addListAtEnd(final List<E> list) {
        CommonBaseAdapter.this.mList.addAll(list);
        notifyDataSetChanged();
    }


    /**
     * 添加单个元素到列表头
     *
     * @param e
     */
    public void addListBeanAtStart(final E e) {

        if (e != null && !mList.contains(e)) {
            mList.add(0, e);
            notifyDataSetChanged();
        }


    }

    /**
     * 添加单个元素到列表尾
     *
     * @param e
     */
    public void addListBeanAtEnd(final E e) {
        mList.add(e);
        notifyDataSetChanged();
    }

    /**
     * 替换ListView数据
     *
     * @param list
     */
    public void replaceList(final List<E> list) {
        if (list == null) return;
        mList = list;
        notifyDataSetChanged();
    }

    /**
     * 删除ListView所有数据
     */
    public void removeAll() {

        if (mList != null) {
            mList.clear();
            notifyDataSetChanged();
        }


    }

    /**
     * 删除ListView指定位置的数据
     */
    public void remove(final E e) {

        if (e != null) {
            mList.remove(e);
            notifyDataSetChanged();
        }
    }

    /**
     * 删除ListView指定位置的数据
     *
     * @param position
     */
    public void remove(final int position) {

        if (position >= 0 && position <= mList.size() && mList != null) {
            mList.remove(position);
            notifyDataSetChanged();
        }


    }


    public void update(final E e) {

        for (int i = 0; i < mList.size(); i++) {
            E o = mList.get(i);
            if (o.equals(e)) {
                mList.remove(i);
                notifyDataSetChanged();
                mList.add(i, e);
                notifyDataSetChanged();
            }
        }


    }

    /**
     * 在指定位置添加数据
     *
     * @param e
     * @param position
     */
    public void addAtPosition(final E e, final int position) {

        if (e != null) {
            mList.add(position, e);
            notifyDataSetChanged();
        }
    }


    public void removeAll(List<TaskInfo> list) {
        list.clear();
        notifyDataSetChanged();
    }

    /**
     * 跳转方法
     *
     * @param intent
     */
    protected void startActivity(Intent intent) {
        context.startActivity(intent);
    }

    public List<E> getAllItems() {
        return mList;
    }
}