package com.dong.mobilesafe;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts.Photo;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.dong.mobilesafe.base.BaseTitleActivity;
import com.dong.mobilesafe.utils.thread.GlobalThreadPool;

import org.kymjs.contacts.HanziToPinyin;
import org.kymjs.contacts.bean.Contact;
import org.kymjs.contacts.widget.SideBar;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

public class ContactActivity extends BaseTitleActivity implements SideBar
        .OnTouchingLetterChangedListener, TextWatcher {
    public static final String RESULT_CONTATCT = "contacts";
    private static final String[] PHONES_PROJECTION = new String[]{
            Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID};
    protected static final int WHAT_LOADING_FINISH = 1;
    private List<Contact> contacts = new ArrayList<Contact>();
    @InjectView(R.id.school_friend_member)
    ListView mListView;
    private TextView mFooterView;

    /**
     * 联系人显示名称
     **/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;

    /**
     * 电话号码
     **/
    private static final int PHONES_NUMBER_INDEX = 1;

    /**
     * 头像ID
     **/
    private static final int PHONES_PHOTO_ID_INDEX = 2;

    /**
     * 联系人的ID
     **/
    private static final int PHONES_CONTACT_ID_INDEX = 3;


    private org.kymjs.contacts.ContactAdapter mAdapter;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_LOADING_FINISH:
                    mFooterView.setText(contacts.size() + "位联系人");
                    mAdapter = new org.kymjs.contacts.ContactAdapter(mListView, contacts);
                    mListView.setAdapter(mAdapter);
                    hideLoading();
                    break;

                default:
                    break;
            }

        }
    };

    @Override
    protected int onGetContentView() {
        return org.kymjs.contacts.R.layout.activity_my_contact;
    }

    @Override
    protected void onInitData() {
        super.onInitData();
    }


    @Override
    protected void onPrepareView() {
        super.onPrepareView();
        setBarTitle(R.string.contact);
        SideBar mSideBar = (SideBar) findViewById(R.id.school_friend_sidrbar);
        TextView mDialog = (TextView) findViewById(R.id.school_friend_dialog);
        EditText mSearchInput = (EditText) findViewById(R.id.school_friend_member_search_input);

        mSideBar.setTextView(mDialog);
        mSideBar.setOnTouchingLetterChangedListener(this);
        mSearchInput.addTextChangedListener(this);

        // 给listView设置adapter
        mFooterView = (TextView) View.inflate(this, R.layout.item_list_contact_count, null);
        mListView.addFooterView(mFooterView);
        mListView.setOnItemClickListener(itemClickListener);

    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view,int position, long id) {
            Contact contact = mAdapter.getItem(position);
            complete(contact);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLoading();
        GlobalThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                getPhoneContacts();
                mHandler.sendEmptyMessage(WHAT_LOADING_FINISH);
            }
        });
    }


    /**
     * 得到手机通讯录联系人信息
     **/
    private void getPhoneContacts() {
        ContentResolver resolver = getContentResolver();

        // 获取手机联系人
        Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                Contact contact = new Contact();
                //得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                //当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                //得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);

                //得到联系人ID
                Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);

                //得到联系人头像ID
                Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);

                //得到联系人头像Bitamp
                Bitmap contactPhoto = null;

                //photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
                if (photoid > 0) {
                    Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);
                    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
                    contactPhoto = BitmapFactory.decodeStream(input);
                }
                contact.setContactid(contactid);
                contact.setName(contactName);
                contact.setContactPhoto(contactPhoto);
                contact.setPhoneNumber(phoneNumber);
                contact.setPinyin(HanziToPinyin.getPinYin(contact.getName()));
                contacts.add(contact);
            }
            phoneCursor.close();
        }
    }

    public void complete(Contact contact) {
        Intent data = new Intent();
        ArrayList<String> results = new ArrayList<String>();
        results.add(contact.getPhoneNumber());
        data.putStringArrayListExtra(RESULT_CONTATCT,results);
        setResult(RESULT_OK, data);
        finish();
    }


    @Override
    public void onTouchingLetterChanged(String s) {
        int position = 0;
        // 该字母首次出现的位置
        if (mAdapter != null) {
            position = mAdapter.getPositionForSection(s.charAt(0));
        }
        if (position != -1) {
            mListView.setSelection(position);
        } else if (s.contains("#")) {
            mListView.setSelection(0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        ArrayList<Contact> temp = new ArrayList<>(contacts);
        for (Contact data : contacts) {
            if (data.getName().contains(s) || data.getPinyin().contains(s)) {

            } else {
                temp.remove(data);
            }
        }
        if (mAdapter != null) {
            mAdapter.refresh(temp);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }


}
