package com.dong.mobilesafe;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.dong.mobilesafe.base.BaseTitleActivity;
import com.dong.mobilesafe.ui.CustomViewPager;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

public class SetupActivity extends BaseTitleActivity implements OnNextPageListener{
    protected SharedPreferences sp;
    private List<Fragment> fragments = new ArrayList<>();
    @InjectView(R.id.vp_setup)
    ViewPager vp_setup;
    @InjectView(R.id.indicator)
    CirclePageIndicator mIndicator;
    private SetupAdapter mAdapter;


    @Override
    protected void onInitData() {
        super.onInitData();
        sp = getSharedPreferences("config", MODE_PRIVATE);
        mAdapter = new SetupAdapter(getSupportFragmentManager());
        fragments.add(new Setup1Fragment());
        fragments.add(new Setup2Fragment());
        fragments.add(new Setup3Fragment());
        fragments.add(new Setup4Fragment());
    }

    @Override
    protected int onGetContentView() {
        return R.layout.activity_setup;
    }

    @Override
    protected void onPrepareView() {
        super.onPrepareView();
        setEnableSlidFinish(false);
        setBarTitle(R.string.protected_setup);
        vp_setup.setAdapter(mAdapter);
        mIndicator.setViewPager(vp_setup);
    }

    @Override
    public void onNextPage() {
        vp_setup.setCurrentItem(vp_setup.getCurrentItem()+1,true);
    }

    private class SetupAdapter extends FragmentPagerAdapter {


        public SetupAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
