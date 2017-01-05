package com.dong.mobilesafe.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dong.mobilesafe.R;
import com.dong.mobilesafe.TrafficSetupActivity;
import com.dong.mobilesafe.base.BaseFragment;
import com.dong.mobilesafe.constant.SpKey;
import com.dong.mobilesafe.db.dao.TrafficInfoDao;
import com.dong.mobilesafe.domain.TrafficInfo;
import com.dong.mobilesafe.ui.CircleScoreView;
import com.dong.mobilesafe.utils.DateUtil;
import com.dong.mobilesafe.utils.thread.GlobalThreadPool;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase.BorderPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.Legend.LegendPosition;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.XLabels.XLabelPosition;
import com.github.mikephil.charting.utils.YLabels;
import com.github.mikephil.charting.utils.YLabels.YLabelPosition;

import org.xclcharts.common.DensityUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class TrafficStatisticsFragment extends BaseFragment implements OnClickListener {

    public static final int Y_LABLE_COUNT = 10;
    private static final int WHAT_LOADING_FINISH = 1;
    private BarChart mChart;
    protected List<String> xDatas = new ArrayList<String>();
    protected List<Long> yDatas = new ArrayList<Long>();
    private TrafficInfoDao trafficInfoDao;
    private TextView trafficConsumeToday;
    private long todayMobileTraffic;
    private Button trafficSetting,trafficCorrection;
    private RelativeLayout trafficLayout;
    private TextView MobileTrafficCountTV;
    private SharedPreferences sp;
    private CircleScoreView trafficCircle;
    private int centerIndex;
    private BarDataSet barDataSet;
    private Calendar startCalendar;
    private Calendar endcCalendar;
    private View topContent;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_LOADING_FINISH:
                    setChartProperty();
                    updateView();
                    hideLoading();
                    topContent.setVisibility(View.VISIBLE);

                    break;

                default:
                    break;
            }
        }
    };


    @Override
    protected int onGetContentView() {
        return R.layout.fragment_traffic_tatistics;
    }


    protected void onInitData(Bundle savedInstanceState) {
        trafficInfoDao = new TrafficInfoDao(context);
        sp = context.getSharedPreferences(SpKey.TRAFFIC_CONFIG, Context.MODE_PRIVATE);
    }


    @Override
    protected void onPrepareView() {
        super.onPrepareView();
        trafficSetting.setOnClickListener(this);
        trafficCorrection.setOnClickListener(this);
        showLoading();
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                TrafficInfo trafficInfo = trafficInfoDao.findByDate(DateUtil.toYYYYMMDD(new Date()));
                if (trafficInfo != null) {
                    todayMobileTraffic = trafficInfo.getMobileRx() + trafficInfo.getMobileTx();
                }
                initXYlableData();
                mHandler.sendEmptyMessage(WHAT_LOADING_FINISH);

            }
        };
        GlobalThreadPool.getInstance().execute(runnable);

    }


    private void setChartProperty() {
        mChart.setDrawYValues(true);
        mChart.setDrawValueAboveBar(true);
        mChart.setDescription("");
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);
        // disable 3D
        mChart.set3DEnabled(true);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        // draw shadows for each bar that show the maximum value
        // mChart.setDrawBarShadow(true);

        mChart.setUnit("M");
        mChart.setDrawXLabels(true);
        mChart.setDrawGridBackground(false);
        mChart.setDrawHorizontalGrid(true);
        mChart.setDrawVerticalGrid(true);
        mChart.setDrawYLabels(false);
        mChart.setDrawUnitsInChart(true);
        mChart.setDrawHighlightArrow(false);
        mChart.setScaleMinima(3f, 1f);
        mChart.setScaleEnabled(false);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.centerViewPort(centerIndex, 0);
        mChart.setValueFormatter(new ValueFormatter() {
            DecimalFormat format = new DecimalFormat("#.#");

            @Override
            public String getFormattedValue(float value) {

                return format.format(value);
            }
        });


        // sets the text size of the values inside the chart
        mChart.setValueTextSize(10f);

        //设置边界
        mChart.setDrawBorder(true);
        mChart.setBorderColor(Color.GRAY);
        mChart.setBorderWidth(DensityUtil.dip2px(context, 1));
        mChart.setBorderPositions(new BorderPosition[]{BorderPosition.BOTTOM});

        //设置X轴
        XLabels xl = mChart.getXLabels();
        xl.setPosition(XLabelPosition.BOTTOM);
        xl.setSpaceBetweenLabels(0);
        xl.setCenterXLabelText(true);

        //设置y轴
        YLabels yl = mChart.getYLabels();
        yl.setLabelCount(Y_LABLE_COUNT);
        yl.setPosition(YLabelPosition.LEFT);

        setChartData();
        Legend l = mChart.getLegend();
        l.setPosition(LegendPosition.NONE);

        mChart.animateY(800);

    }


    protected void updateView() {
        if (sp.getBoolean(SpKey.KEY_IS_SETTING, false)) {
            trafficLayout.setVisibility(View.VISIBLE);
            trafficSetting.setVisibility(View.GONE);
            final int trafficCount = sp.getInt(SpKey.KEY_TRAFFIC_COUNT, 0);
            trafficConsumeToday.setText(getString(R.string.traffic_consume_today) + Formatter.formatShortFileSize(context, todayMobileTraffic));
            MobileTrafficCountTV.setText(getString(R.string.mobile_traffic_count) + trafficCount + sp.getString("unit", "M"));
            trafficCircle.setFullScore(trafficCount);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DATE, sp.getInt(SpKey.KEY_START_DATE, 1));
            if (calendar.getTime().after(new Date())) {
                calendar.add(Calendar.MONTH, -1);
            }

            long mobileTrafficCOunt = trafficInfoDao.getMobileTrafficCountByPeriod(
                    DateUtil.toYYYYMMDD(startCalendar.getTime()),
                    DateUtil.toYYYYMMDD(endcCalendar.getTime()));
            float traffic = (mobileTrafficCOunt / (1024f * 1024f));
            trafficCircle.setscores(Math.round(traffic));
        } else {
            trafficLayout.setVisibility(View.GONE);
            trafficSetting.setVisibility(View.VISIBLE);
            trafficCircle.setscores(0);
        }
        mChart.setVisibility(View.VISIBLE);
        mChart.invalidate();
    }


    public void initXYlableData() {
        xDatas.clear();
        yDatas.clear();
        startCalendar = Calendar.getInstance(Locale.CHINA);
        endcCalendar = Calendar.getInstance(Locale.CHINA);
        Calendar temCalendar = Calendar.getInstance(Locale.CHINA);

        temCalendar.set(Calendar.DAY_OF_MONTH, sp.getInt(SpKey.KEY_START_DATE, 1));
        if (temCalendar.getTime().after(new Date())) {
            endcCalendar.setTime(temCalendar.getTime());
            temCalendar.add(Calendar.MONTH, -1);
            startCalendar.setTime(temCalendar.getTime());

        } else {
            startCalendar.setTime(temCalendar.getTime());
            temCalendar.add(Calendar.MONTH, 1);
            endcCalendar.setTime(temCalendar.getTime());
        }

        temCalendar.setTime(startCalendar.getTime());
        while (temCalendar.before(endcCalendar)) {
            Date tempDate = temCalendar.getTime();
            if (DateUtils.isToday(tempDate.getTime())) {
                xDatas.add(getString(R.string.today));
                centerIndex = xDatas.size() - 5;
                if (centerIndex < 0) {
                    centerIndex = 0;
                }
            } else {
                xDatas.add(DateUtil.toMMDD(tempDate));
            }
            TrafficInfo trafficInfo = trafficInfoDao.findByDate(DateUtil.toYYYYMMDD(tempDate));
            if (trafficInfo != null) {
                yDatas.add(trafficInfo.getMobileRx() + trafficInfo.getMobileTx());
            } else {
                yDatas.add(0L);
            }

            temCalendar.add(Calendar.DATE, 1);
        }

    }


    private void setChartData() {
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < xDatas.size(); i++) {
            xVals.add(xDatas.get(i));
        }

        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
        for (int i = 0; i < yDatas.size(); i++) {
            float val = yDatas.get(i) / (1024f * 1024f);
            yVals.add(new BarEntry(val, i));
        }

        barDataSet = new BarDataSet(yVals, "data");
        barDataSet.setColor(getResources().getColor(R.color.color_traffic_bar_bg));
        barDataSet.setBarShadowColor(Color.TRANSPARENT);
        barDataSet.setBarSpacePercent(35f);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(barDataSet);

        BarData data = new BarData(xVals, dataSets);
        mChart.setData(data);
    }


    @Override
    protected void findViewById(View rootView) {
        trafficConsumeToday = (TextView) rootView.findViewById(R.id.tv_traffic_today_consume);
        trafficSetting = (Button) rootView.findViewById(R.id.btn_set_traffic);
        trafficCorrection = (Button) rootView.findViewById(R.id.btn_correction_traffic);
        trafficLayout = (RelativeLayout) rootView.findViewById(R.id.rl_traffic_info);
        MobileTrafficCountTV = (TextView) rootView.findViewById(R.id.tv_traffic_count);
        trafficCircle = (CircleScoreView) rootView.findViewById(R.id.circle_score_view);
        mChart = (BarChart) rootView.findViewById(R.id.bar_chart);
        topContent = rootView.findViewById(R.id.top_content);
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, TrafficSetupActivity.class);
        switch (v.getId()) {
            case R.id.btn_set_traffic:
                startActivityForResult(intent, Activity.RESULT_FIRST_USER);
                break;

            case R.id.btn_correction_traffic:
                final int trafficCount = sp.getInt(SpKey.KEY_TRAFFIC_COUNT, 0);
                intent.putExtra(TrafficSetupActivity.KEY_PARAM_TYPE,TrafficSetupActivity.FROM_CORRECTION);
                intent.putExtra(TrafficSetupActivity.KEY_PARAM_DAY,sp.getInt(SpKey.KEY_START_DATE, 1));
                intent.putExtra(TrafficSetupActivity.KEY_PARAM_TRAFFIC_COUNT,trafficCount);
                startActivityForResult(intent, Activity.RESULT_FIRST_USER);
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Activity.RESULT_FIRST_USER &&
                resultCode == Activity.RESULT_OK) {
            Editor editor = sp.edit();
            int trafficCount = data.getIntExtra(SpKey.KEY_TRAFFIC_COUNT, 0);
            int start = data.getIntExtra(SpKey.KEY_START_DATE, 1);
            editor.putInt(SpKey.KEY_TRAFFIC_COUNT, trafficCount);
            editor.putInt(SpKey.KEY_START_DATE, start);
            editor.putBoolean(SpKey.KEY_IS_SETTING, true);
            editor.commit();
            updateView();
        }
    }


}
