package com.dong.mobilesafe.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.dong.mobilesafe.CallLogActvity;
import com.dong.mobilesafe.ContactActivity;
import com.dong.mobilesafe.FromSMSActivity;
import com.dong.mobilesafe.ManualAddActivity;
import com.dong.mobilesafe.R;
import com.dong.mobilesafe.adapter.CommonDialogListAdapter;
import com.dong.mobilesafe.adapter.WhiteNumberAdapter;
import com.dong.mobilesafe.base.BaseFragment;
import com.dong.mobilesafe.db.dao.NumberAddressQueryUtils;
import com.dong.mobilesafe.db.dao.WhiteNumberDao;
import com.dong.mobilesafe.domain.DialogItem;
import com.dong.mobilesafe.domain.WhiteNumber;
import com.dong.mobilesafe.utils.DialogUtils;
import com.dong.mobilesafe.utils.PhoneNumberUtil;
import com.dong.mobilesafe.utils.thread.GlobalThreadPool;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.DialogPlus.Gravity;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnItemClickListener;

public class WhiteNumberFragment extends BaseFragment implements OnClickListener {
    private static final int REQUEST_FROM_CALL_LOG = 1;
    private static final int REQUEST_FROM_CONTATCT = 2;
    private static final int REQUEST_FROM_SMS = 3;
    private static final int REQUEST_BY_MANUAL = 4;

    private int[] icons = {
            R.drawable.contact, R.drawable.message_icon, R.drawable.list_history, R.drawable.manual_icon
    };

    private Button addNumberBtn;
    private WhiteNumberDao whiteNumberDao;
    private ListView mListView;
    private WhiteNumberAdapter mAdapter;
    private static final Handler mHandler = new Handler(Looper.getMainLooper());
    private List<DialogItem> items = new ArrayList<DialogItem>();

    @Override
    protected void findViewById(View rootView) {
        addNumberBtn = (Button) rootView.findViewById(R.id.btn_add_number);
        mListView = (ListView) rootView.findViewById(R.id.lv_white_number);
    }


    @Override
    protected void onInitData(Bundle savedInstanceState) {
        super.onInitData(savedInstanceState);
        mAdapter = new WhiteNumberAdapter(context);
        mAdapter.setOnDeleteWhiteNumberListener(deleteWhiteNumberListener);
        whiteNumberDao = new WhiteNumberDao(context);
        String[] arrs = getResources().getStringArray(R.array.dialog_title);
        for (int i = 0; i < arrs.length; i++) {
            DialogItem item = new DialogItem();
            item.setName(arrs[i]);
            item.setIcon(getResources().getDrawable(icons[i]));
            items.add(item);
        }
    }

    private WhiteNumberAdapter.OnDeleteWhiteNumberListener deleteWhiteNumberListener = new WhiteNumberAdapter.OnDeleteWhiteNumberListener() {
        @Override
        public void OnDeleteWhiteNumber(int position) {
            if (mAdapter.getCount() <= 0) {
                showTip();
            }
        }
    };


    @Override
    protected void onPrepareView() {
        super.onPrepareView();
        mListView.setAdapter(mAdapter);
        addNumberBtn.setOnClickListener(this);
        fillData();
    }


    private void fillData() {
        showLoading();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<WhiteNumber> whiteNumbers = whiteNumberDao.findAll(WhiteNumber.class);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        if (whiteNumbers == null || whiteNumbers.isEmpty()) {
                            showTip();
                        }
                        mAdapter.replaceList(whiteNumbers);
                    }
                });
            }
        };
        GlobalThreadPool.getInstance().execute(runnable);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_number:
                showDialog();
                break;

            default:
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_FROM_CALL_LOG:
                    addWhiteNumber("callLogs", data);
                    break;

                case REQUEST_FROM_CONTATCT:
                    addWhiteNumber("contacts", data);
                    break;

                case REQUEST_FROM_SMS:
                    addWhiteNumber("messages", data);
                    break;
                case REQUEST_BY_MANUAL:
                    addWhiteNumber("manual", data);
                    break;
            }

        }
    }


    private void addWhiteNumber(final String name, final Intent data) {
        GlobalThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> numbers = data.getStringArrayListExtra(name);
                for (String s : numbers) {
                    final WhiteNumber whiteNumber = new WhiteNumber();
                    whiteNumber.setNumber(s);
                    whiteNumber.setName(PhoneNumberUtil.getNameByNumber(context, s));
                    whiteNumber.setLocation(NumberAddressQueryUtils.queryNumber(getActivity(), s));
                    whiteNumberDao.save(whiteNumber);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.addListBeanAtStart(whiteNumber);
                            if (mAdapter.getCount() > 0) {
                                hideTip();
                            }
                        }
                    });
                }
            }
        });

    }


    private void showDialog() {

        Holder holder = new ListHolder();
        OnItemClickListener itemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                switch (position) {
                    case 1:
                        startActivityForResult(
                                new Intent(context, CallLogActvity.class),
                                REQUEST_FROM_CALL_LOG);
                        break;

                    case 2:
                        startActivityForResult(
                                new Intent(context, FromSMSActivity.class),
                                REQUEST_FROM_SMS);
                        break;
                    case 3:
                        startActivityForResult(
                                new Intent(context, ContactActivity.class),
                                REQUEST_FROM_CONTATCT);
                        break;
                    case 4:
                        Intent intent = new Intent(context, ManualAddActivity.class);
                        intent.putExtra(ManualAddActivity.PARAM_FROM, ManualAddActivity.FROM_WHITE_NUMBER);
                        startActivityForResult(intent, REQUEST_BY_MANUAL);
                        break;
                }
                dialog.dismiss();
            }
        };
        CommonDialogListAdapter adapter = new CommonDialogListAdapter(context);
        adapter.replaceList(items);
        DialogUtils.showNoFooterDialog(context, getString(R.string.add_white_list), holder, Gravity.BOTTOM, adapter, null, itemClickListener);
    }


    @Override
    protected int onGetContentView() {
        return R.layout.fragment_white_number;
    }

}
