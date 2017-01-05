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
import com.dong.mobilesafe.adapter.BlackNumberAdapter;
import com.dong.mobilesafe.adapter.CommonDialogListAdapter;
import com.dong.mobilesafe.base.BaseFragment;
import com.dong.mobilesafe.db.dao.BlackNumberDao;
import com.dong.mobilesafe.db.dao.NumberAddressQueryUtils;
import com.dong.mobilesafe.domain.BlackNumber;
import com.dong.mobilesafe.domain.DialogItem;
import com.dong.mobilesafe.utils.DialogUtils;
import com.dong.mobilesafe.utils.PhoneNumberUtil;
import com.dong.mobilesafe.utils.thread.GlobalThreadPool;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.DialogPlus.Gravity;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnItemClickListener;

public class BlackNumberFragment extends BaseFragment implements OnClickListener {
    public static final int INTERCEPT_CALL = 1;
    public static final int INTERCEPT_SMS = 2;
    public static final int INTERCEPT_ALL = 3;

    private static final int REQUEST_FROM_CALL_LOG = 1;
    private static final int REQUEST_FROM_CONTATCT = 2;
    private static final int REQUEST_FROM_SMS = 3;
    private static final int REQUEST_BY_MANUAL = 4;
    private static final Handler mHandler = new Handler(Looper.getMainLooper());

    private int[] icons = {
            R.drawable.contact, R.drawable.message_icon, R.drawable.list_history, R.drawable.manual_icon
    };


    private ListView blackNumberListView;
    private BlackNumberDao blackNumberDao;
    private Button addBlackNumber;
    private BlackNumberAdapter blackNumberAdapter;
    private List<DialogItem> items = new ArrayList<DialogItem>();


    @Override
    protected void findViewById(View rootView) {
        blackNumberListView = (ListView) rootView.findViewById(R.id.lv_callsms_safe);
        addBlackNumber = (Button) rootView.findViewById(R.id.btn_add_number);
    }


    @Override
    protected void onInitData(Bundle savedInstanceState) {
        super.onInitData(savedInstanceState);
        String[] arrs = getResources().getStringArray(R.array.dialog_title);
        for (int i = 0; i < arrs.length; i++) {
            DialogItem item = new DialogItem();
            item.setName(arrs[i]);
            item.setIcon(getResources().getDrawable(icons[i]));
            items.add(item);
        }
        blackNumberDao = new BlackNumberDao(context);
        blackNumberAdapter = new BlackNumberAdapter(context);
        blackNumberAdapter.setOndelteBlackNumberListener(onDelteBlackNumberListener);

    }

    private BlackNumberAdapter.OnDelteBlackNumberListener onDelteBlackNumberListener = new BlackNumberAdapter.OnDelteBlackNumberListener() {
        @Override
        public void OnDeleteBlackNumber(int position) {
            if (blackNumberAdapter.getCount() <=0) {
                showTip();
            }
        }
    };


    @Override
    protected void onPrepareView() {
        addBlackNumber.setOnClickListener(this);
        blackNumberListView.setAdapter(blackNumberAdapter);
        fillData();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_FROM_CALL_LOG:
                    addBlackNumber(CallLogActvity.RESULT_CALLLOGS, data);
                    break;

                case REQUEST_FROM_CONTATCT:
                    addBlackNumber(ContactActivity.RESULT_CONTATCT, data);
                    break;

                case REQUEST_FROM_SMS:
                    addBlackNumber(FromSMSActivity.RESULT_MESSAGE, data);
                    break;

                case REQUEST_BY_MANUAL:
                    addBlackNumber("manual", data);
                    break;
            }

        }
    }


    private void addBlackNumber(final String name, final Intent data) {
        GlobalThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> numbers = data.getStringArrayListExtra(name);
                for (String s : numbers) {
                    final BlackNumber blackNumber = new BlackNumber();
                    blackNumber.setNumber(s);
                    blackNumber.setMode(INTERCEPT_ALL);
                    blackNumber.setLocation(NumberAddressQueryUtils.queryNumber(getActivity(), s));
                    blackNumber.setName(PhoneNumberUtil.getNameByNumber(context, s));
                    blackNumberDao.save(blackNumber);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            blackNumberAdapter.addListBeanAtStart(blackNumber);
                            if(blackNumberAdapter.getCount() >0) {
                                hideTip();
                            }
                        }
                    });
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_number:
                showDialog();
                break;
        }

    }

    private void fillData() {
        showLoading();
        Runnable runnable = new Runnable() {
            public void run() {
                final List<BlackNumber> blackNumbers = blackNumberDao.findAll(BlackNumber.class);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        if(blackNumbers == null || blackNumbers.isEmpty()) {
                            showTip();
                        }
                        blackNumberAdapter.replaceList(blackNumbers);
                    }
                });
            }
        };
        GlobalThreadPool.getInstance().execute(runnable);
    }


    private void showDialog() {

        Holder holder = new ListHolder();
        OnItemClickListener itemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                switch (position) {
                    case 1:  //从通话记录中添加
                        startActivityForResult(
                                new Intent(context, CallLogActvity.class),
                                REQUEST_FROM_CALL_LOG);
                        break;
                    case 2: //从短信列表添加
                        startActivityForResult(
                                new Intent(context, FromSMSActivity.class),
                                REQUEST_FROM_SMS);
                        break;
                    case 3: //从联系人列表人列表添加
                        startActivityForResult(
                                new Intent(context, ContactActivity.class),
                                REQUEST_FROM_CONTATCT);
                        break;
                    case 4: //手动添加
                        startActivityForResult(
                                new Intent(context, ManualAddActivity.class),
                                REQUEST_BY_MANUAL);
                        break;
                }
                dialog.dismiss();
            }

        };
        CommonDialogListAdapter adapter = new CommonDialogListAdapter(context);
        adapter.replaceList(items);
        DialogUtils.showNoFooterDialog(context, getString(R.string.add_black_list), holder, Gravity.BOTTOM, adapter, null, itemClickListener);
    }

    @Override
    protected int onGetContentView() {

        return R.layout.fragment_black_number;
    }

}
