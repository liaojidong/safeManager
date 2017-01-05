package com.dong.mobilesafe.base;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.LayoutParams;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.dong.mobilesafe.R;
import com.dong.mobilesafe.ui.LazyViewPager;
import com.dong.mobilesafe.ui.ScrollerLinearLayout;
import com.dong.mobilesafe.ui.SlideViewPager;
import com.dong.mobilesafe.utils.DensityUtil;

import java.util.LinkedHashMap;
import java.util.Set;



public abstract class PageViewActivity extends BaseTitleActivity implements OnCheckedChangeListener,LazyViewPager.OnPageChangeListener {
	
	private static final int DEFAULT_INTERVAL_SPACE = 1;
	private static final int DEFAULT_SELECT = 0;
	private int tabHorizonDividerBg;
	private int tabLineBg;
	protected SlideViewPager mviewPager;
	protected RadioGroup radioGroup;
	protected ViewGroup contentView;
	private Fragment[] fragments;
	private int tabCount;
	protected ScrollerLinearLayout tabLineLayout;
	private int tabSelect;
	
	
	@Override
	protected void onInitData() {
		super.onInitData();
		tabHorizonDividerBg = getResources().getColor(R.color.primary_blue);
		tabLineBg = getResources().getColor(R.color.tab_line_bg);
		tabSelect = DEFAULT_SELECT;
	}
	
	@Override
	protected int onGetContentView() {
		return 0;
	}


	
	@Override
	protected View getContentView() {
		initView();
		return contentView;
	}

	protected void initView() {
		initContentView();
		initRadioGroupView();
		initTabInditor();
		initDriverLine();
		initViewPager();
		
		initFragment(setFragment());
		mviewPager.setAdapter(new TabFragmentPagerAdapter(getSupportFragmentManager()));
		mviewPager.setCurrentItem(0);
		radioGroup.setOnCheckedChangeListener(this);
		mviewPager.setOnPageChangeListener(this);
		tabLineLayout.scroollToPosition(tabSelect);
	}
	
	
	private void initViewPager() {
		mviewPager = new SlideViewPager(this);
		mviewPager.setOffscreenPageLimit(0);
		mviewPager.setFadingEdgeLength(0);
		mviewPager.setId(R.id.ID_VIEWPAGER);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW, R.id.ID_DRIVER_LINE);
		contentView.addView(mviewPager, params);
		
	}



	private void initDriverLine() {
		View division = new View(this);
		division.setId(R.id.ID_DRIVER_LINE);
		division.setBackgroundColor(getResources().getColor(R.color.primary_blue));
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,1);
		params.addRule(RelativeLayout.BELOW,R.id.ID_TAB_LAYOUT);
		contentView.addView(division, params);
	}




	private void initTabInditor() {
		tabLineLayout = new ScrollerLinearLayout(this);
		tabLineLayout.setId(R.id.ID_TAB_LAYOUT);
		tabLineLayout.setBackgroundColor(tabLineBg);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		tabLineLayout.setOrientation(LinearLayout.VERTICAL);
		params.addRule(RelativeLayout.BELOW, R.id.ID_RG_NAV_CONTENT);
		contentView.addView(tabLineLayout, params);
		
	}



	private void initRadioGroupView() {
		radioGroup = new RadioGroup(this);
		radioGroup.setId(R.id.ID_RG_NAV_CONTENT);
		radioGroup.setBackgroundColor(getResources().getColor(R.color.primary_blue));
		radioGroup.setGravity(Gravity.CENTER);
		RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
				RadioGroup.LayoutParams.MATCH_PARENT,DensityUtil.dip2px(this,36));
		radioGroup.setOrientation(RadioGroup.HORIZONTAL);
		contentView.addView(radioGroup, params);
	}



	private void initContentView() {
		contentView = new RelativeLayout(this);
		contentView.setVisibility(View.VISIBLE);
	}



	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId){
		mviewPager.setCurrentItem(checkedId, true);
	}
	
	

	@Override
	public void onPageSelected(int position) {
		tabLineLayout.scroollToPosition(position);
		radioGroup.check(position);
	}

	
	@Override  
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		tabLineLayout.move(position, positionOffsetPixels);
	}

	@Override
	public void onPageScrollStateChanged(int position) {
		
	}
	
	

	private void initFragment(LinkedHashMap<String, Fragment> hashMap) {
		Set<String> set = hashMap.keySet();
		Object[] keys = set.toArray();
		tabCount = hashMap.size();
		tabLineLayout.initTabLine(tabCount);
		fragments = new Fragment[set.size()];
		for (int i = 0; i < keys.length; i++) {
			final String tabName = keys[i].toString();
			createRadioButton(i,tabName);
			Fragment fragment = hashMap.get(tabName);
			fragments[i] = fragment;

			if (i != (keys.length - 1)) { //增加分割线
				View view = new View(this);
				view.setBackgroundColor(tabHorizonDividerBg);
				LayoutParams viewParams = new LayoutParams(DensityUtil.dip2px(this,DEFAULT_INTERVAL_SPACE), LayoutParams.MATCH_PARENT);
				view.setLayoutParams(viewParams);
				radioGroup.addView(view);
			}
		}
		radioGroup.check(tabSelect);
	}
	
	
	
	private void createRadioButton(int id,String tabName) {
		RadioButton radioButton = new RadioButton(this);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 
				LayoutParams.MATCH_PARENT);
		radioButton.setText(tabName);
		radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
		radioButton.setPadding(0, 0, 0, 0);
		radioButton.setClickable(true);
		radioButton.setId(id);
		radioButton.setBackgroundColor(getResources().getColor(R.color.primary_blue));
		radioButton.setTextColor(Color.WHITE);
		radioButton.setSingleLine(true);
		radioButton.setGravity(Gravity.CENTER);
		radioButton.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
		params.weight = 1;
		radioButton.setLayoutParams(params);
		radioGroup.addView(radioButton);
	}



	/**
	 * ViewPager 数据适配器
	 * 
	 */
	public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

		public TabFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return fragments[position];
		}

		@Override
		public int getCount() {
			return fragments.length;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
		}
	}
	
	
	
	protected abstract LinkedHashMap<String, Fragment> setFragment();
	



}
