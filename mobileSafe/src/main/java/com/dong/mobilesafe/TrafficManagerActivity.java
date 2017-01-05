package com.dong.mobilesafe;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.dong.mobilesafe.base.BaseTitleActivity;
import com.dong.mobilesafe.fragment.AppFireWallFragment;
import com.dong.mobilesafe.fragment.TrafficRankingFragment;
import com.dong.mobilesafe.fragment.TrafficStatisticsFragment;
import com.dong.mobilesafe.ui.CustomViewPager;
import com.dong.mobilesafe.ui.LazyViewPager;
import com.dong.mobilesafe.utils.MyLogger;

import java.util.LinkedList;
import java.util.List;

public class TrafficManagerActivity extends BaseTitleActivity implements OnClickListener, OnCheckedChangeListener {
    private CustomViewPager mViewPager;
    private RadioGroup bottomBar;
    private TrafficPagerAdapter mAdapter;
    private int curCheckId = R.id.rb_traffic_statistics;
    private List<Fragment> fragments = new LinkedList<Fragment>();
    private TrafficRankingFragment rankingFragment;
    private TrafficStatisticsFragment statisticsFragment;
    private AppFireWallFragment appFireWallFragment;

    @Override
    protected int onGetContentView() {
        return R.layout.activity_traffi_manager;
    }


    @Override
    protected void onInitData() {
        super.onInitData();
        rankingFragment = new TrafficRankingFragment();
        statisticsFragment = new TrafficStatisticsFragment();
        appFireWallFragment = new AppFireWallFragment();
        fragments.add(statisticsFragment);
        fragments.add(rankingFragment);
        fragments.add(appFireWallFragment);
        mAdapter = new TrafficPagerAdapter(getSupportFragmentManager());
    }


    @Override
    protected void onPrepareView() {
        super.onPrepareView();
        setEnableSlidFinish(false);
        setBarTitle(R.string.traffic_statistics);
        mViewPager = (CustomViewPager) findViewById(R.id.viewpager);
        bottomBar = (RadioGroup) findViewById(R.id.rg_bottom_bar);

        mViewPager.setAdapter(mAdapter);
        mViewPager.setScrollable(false);
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setCurrentItem(0);
        mViewPager.setOnPageChangeListener(pageChangeListener);
        bottomBar.setOnCheckedChangeListener(this);
        bottomBar.check(curCheckId);
    }

    private LazyViewPager.OnPageChangeListener pageChangeListener = new LazyViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    setBarTitle(R.string.traffic_statistics);
                    break;
                case 1:
                    setBarTitle(R.string.traffic_rank);
                    break;
                case 2:
                    setBarTitle(R.string.fire_wall);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    class TrafficPagerAdapter extends FragmentPagerAdapter {

        public TrafficPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            MyLogger.jLog().d("getItem " + arg0);
            return fragments.get(arg0);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_traffic_statistics:
                mViewPager.setCurrentItem(0, false);
                break;

            case R.id.rb_traffic_ranking:
                mViewPager.setCurrentItem(1, false);
                break;

            case R.id.rb_traffic_speed:
                mViewPager.setCurrentItem(2, false);
                break;
        }
        curCheckId = checkedId;
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
    }


    @Override
    public void onClick(View v) {
        startActivity(TrafficSettingActivity.class);
    }


}
