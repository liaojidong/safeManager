package com.dong.mobilesafe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.dong.mobilesafe.R;
import com.dong.mobilesafe.base.BaseRecyleAdapter;
import com.dong.mobilesafe.domain.Contact;

public class ContactAdapter extends BaseRecyleAdapter<ContactAdapter.Holder, Contact> {
    private OnDataChangeListener mListener;

    public interface OnDataChangeListener {
        public void OnDateChange(boolean isChecked);
    }

    public ContactAdapter(Context context) {
        super(context);
    }

    @Override
    public ContactAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_contact, null);
        Holder hoder = new Holder(v);
        return hoder;
    }

    @Override
    public void onBindViewHolder(ContactAdapter.Holder holder, int position) {
        final Contact contact = mList.get(position);
        holder.name.setText(contact.getContactName());
        holder.contactIcon.setImageBitmap(contact.getContactPhoto());
        holder.number.setText(contact.getPhoneNumber());
        holder.checkBox.setChecked(contact.isChecked());
        holder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (contact.isChecked() != isChecked) {
                    contact.setChecked(isChecked);
                    if (mListener != null) {
                        mListener.OnDateChange(isChecked);
                    }
                }
            }
        });
    }

    static class Holder extends RecyclerView.ViewHolder {
        private TextView number;
        private TextView name;
        private CheckBox checkBox;
        private ImageView contactIcon;

        public Holder(View convertView) {
            super(convertView);
            contactIcon = (ImageView) convertView.findViewById(R.id.iv_contact_icon);
            name = (TextView) convertView.findViewById(R.id.tv_name);
            number = (TextView) convertView.findViewById(R.id.tv_phone_number);
            checkBox = (CheckBox) convertView.findViewById(R.id.cb_contact);
        }
    }

    public void setOnDataChangeListener(OnDataChangeListener mListener) {
        this.mListener = mListener;
    }

}
