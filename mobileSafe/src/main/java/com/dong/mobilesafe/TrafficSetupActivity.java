package com.dong.mobilesafe;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.codetroopers.betterpickers.numberpicker.NumberPickerBuilder;
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.dong.mobilesafe.adapter.DateSelectorAdapter;
import com.dong.mobilesafe.base.BaseTitleActivity;
import com.dong.mobilesafe.bean.DateSelectItem;
import com.dong.mobilesafe.constant.SpKey;
import com.dong.mobilesafe.utils.log.LogUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

import butterknife.InjectView;

public class TrafficSetupActivity extends BaseTitleActivity {
    public final static String KEY_PARAM_DAY = "start_day";
    public final static String KEY_PARAM_TRAFFIC_COUNT = "traffic_coutn";
    public final static String KEY_PARAM_TYPE = "setup_type";
    public final static short FROM_SETUP = 0;
    public final static short FROM_CORRECTION = 1;

    @InjectView(R.id.rg_traffic_number)
    RadioGroup rg_traffic_number;
    @InjectView(R.id.rb_traffic_other)
    RadioButton rb_traffic_other;
    @InjectView(R.id.tv_traffic_count)
    TextView tv_traffic_count;
    @InjectView(R.id.gv_start_date)
    GridView gl_start_date;
    @InjectView(R.id.tv_traffic_start)
    TextView tv_traffic_start;

    private DateSelectorAdapter dateSelectorAdapter;
    private int startDay = 1;
    private int trafficCount = 50;
    private short fromType = FROM_SETUP;

    @Override
    protected int onGetContentView() {
        return R.layout.activity_setup_setting;
    }

    @Override
    protected void onInitData() {
        super.onInitData();
        Intent intent = getIntent();
        startDay = intent.getIntExtra(KEY_PARAM_DAY, 1);
        trafficCount = intent.getIntExtra(KEY_PARAM_TRAFFIC_COUNT, 50);
        fromType = intent.getShortExtra(KEY_PARAM_TYPE, FROM_SETUP);
    }

    private OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rb_traffic_50:
                    trafficCount = 50;
                    rg_traffic_number.check(R.id.rb_traffic_50);
                    tv_traffic_count.setText(getString(R.string.traffic_number_per_month, 50));
                    break;
                case R.id.rb_traffic_100:
                    trafficCount = 100;
                    rg_traffic_number.check(R.id.rb_traffic_100);
                    tv_traffic_count.setText(getString(R.string.traffic_number_per_month, 100));
                    break;
                case R.id.rb_traffic_300:
                    trafficCount = 300;
                    rg_traffic_number.check(R.id.rb_traffic_300);
                    tv_traffic_count.setText(getString(R.string.traffic_number_per_month, 300));
                    break;
                case R.id.rb_traffic_500:
                    trafficCount = 500;
                    rg_traffic_number.check(R.id.rb_traffic_500);
                    tv_traffic_count.setText(getString(R.string.traffic_number_per_month, 500));
                    break;
                case R.id.rb_traffic_other:
                    rg_traffic_number.check(R.id.rb_traffic_other);
                    showPickTrafficDialog();
                    break;
            }
        }
    };

    private void showPickTrafficDialog() {
        NumberPickerBuilder npb = new NumberPickerBuilder()
                .setFragmentManager(getSupportFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment)
                .setLabelText("M");
        npb.addNumberPickerDialogHandler(new NumberPickerDialogFragment.NumberPickerDialogHandlerV2() {
            @Override
            public void onDialogNumberSet(int reference, BigInteger number, double decimal, boolean isNegative, BigDecimal fullNumber) {
                LogUtils.jLog().e("number = " + number.toString());
                tv_traffic_count.setText(getString(R.string.traffic_number_per_month, number.toString()));
                rb_traffic_other.setText(number.toString() + "M");
                trafficCount = number.intValue();

            }
        });
        npb.show();
    }


    @Override
    protected void onPrepareView() {
        super.onPrepareView();
        if (fromType == FROM_SETUP) {
            setBarTitle(getString(R.string.traffic_setting));
        } else {
            setBarTitle(getString(R.string.traffic_correction));
        }

        setRightText(getString(R.string.complete), new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataVerify()) {
                    Intent data = new Intent();
                    data.putExtra(SpKey.KEY_TRAFFIC_COUNT, trafficCount);
                    data.putExtra(SpKey.KEY_START_DATE, startDay);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });
        checkTrafficNumber();
        tv_traffic_count.setText(getString(R.string.traffic_number_per_month, trafficCount));
        tv_traffic_start.setText(getString(R.string.start_date, startDay));
        for (int i = 0; i < rg_traffic_number.getChildCount(); i++) {
            View v = rg_traffic_number.getChildAt(i);
            v.setOnClickListener(clickListener);
        }
        dateSelectorAdapter = new DateSelectorAdapter(this);
        gl_start_date.setAdapter(dateSelectorAdapter);
        gl_start_date.setOnItemClickListener(onItemClickListener);
        dateSelectorAdapter.setSelectedDate(startDay-1);
    }

    private void checkTrafficNumber() {
        switch (trafficCount) {
            case 50:
                rg_traffic_number.check(R.id.rb_traffic_50);
                break;
            case 100:
                rg_traffic_number.check(R.id.rb_traffic_100);
                break;
            case 300:
                rg_traffic_number.check(R.id.rb_traffic_300);
                break;
            case 500:
                rg_traffic_number.check(R.id.rb_traffic_500);
                break;
            default:
                rg_traffic_number.check(R.id.rb_traffic_other);
                break;
        }

    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            DateSelectItem item = (DateSelectItem) gl_start_date.getItemAtPosition(i);
            startDay = Integer.parseInt(item.getDate());
            setSelectDay(i);
        }
    };

    private void setSelectDay(int i) {
        tv_traffic_start.setText(getString(R.string.start_date, startDay));
        dateSelectorAdapter.setSelectedDate(i);
    }


    private boolean dataVerify() {
        if (trafficCount <= 0) {
            showToast("流量要大于0");
            return false;
        }
        if (startDay <= 0) {
            showToast("计费日要大于0");
            return false;
        }
        if (startDay > 31) {
            showToast("计费日要小于31");
            return false;
        }
        return true;
    }

}
