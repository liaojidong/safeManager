package com.dong.mobilesafe.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dong.mobilesafe.R;
import com.dong.mobilesafe.SettingActivity;
import com.dong.mobilesafe.adapter.MenuSlidingAdapter;
import com.dong.mobilesafe.base.BaseFragment;

import butterknife.InjectView;
import butterknife.OnClick;

import static android.widget.AdapterView.*;

public class SlidingMenuFragment extends BaseFragment implements OnItemClickListener{
	@InjectView(R.id.lv_sliding_menu)
	ListView mListView;
	private MenuSlidingAdapter mAdapter;
	private OnMenuItemClickListener mMenuItemClickListener;



	public interface OnMenuItemClickListener {
		public void onMenuItemClick(int postion);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(activity instanceof OnMenuItemClickListener) {
			mMenuItemClickListener = (OnMenuItemClickListener) activity;
		}else {
			throw new RuntimeException("must be implement OnMenuItemClickListener ！");
		}
	}

	@Override
	protected void onInitData(Bundle savedInstanceState) {
		super.onInitData(savedInstanceState);
		mAdapter = new MenuSlidingAdapter(getActivity());
	}

	@Override
	protected int onGetContentView() {
		return R.layout.sliding_menu_list;
	}



	@Override
	protected void onPrepareView() {
		super.onPrepareView();
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}

	@OnClick(R.id.ll_setting)
	void goSettingCenter(View v) {
		startActivity(new Intent(getActivity(), SettingActivity.class));
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
		mMenuItemClickListener.onMenuItemClick(i);
	}
}
