package com.cosin.shareagenda.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.activity.NewCalendarActivity;
import com.cosin.shareagenda.dialog.DisplayAccountRequestDialog;

import java.util.ArrayList;
import java.util.List;

import types.Account;

public class FriendContactsAdapter extends RecyclerView.Adapter<FriendContactsAdapter.ViewHolder> {
    private List<String> contactList;
    

    public FriendContactsAdapter(Context context) {
        this.contactList = new ArrayList<>();
    }
    public FriendContactsAdapter(Context context, List<String> contactList){
        this.contactList = contactList;
    }

    public void setContactList(List<String> contactList) {
        this.contactList = contactList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvContactName;
        ImageView ivCalendar;

        public ViewHolder(View view) {
            super(view);
            tvContactName = view.findViewById(R.id.tvContactName);
            ivCalendar = view.findViewById((R.id.ivCalendar));
        }
    }
    @Override
    public FriendContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item,parent,false);
        return new ViewHolder(v);
    }

    private FriendContactsAdapter getFriendContactsAdapter(){
        return this;
    }

    @Override
    public void onBindViewHolder(FriendContactsAdapter.ViewHolder holder, int position) {
        holder.tvContactName.setText(contactList.get(position));
        holder.tvContactName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String entity = contactList.get(position);
                String accountId = contactList.get(position);
                new DisplayAccountRequestDialog(getFriendContactsAdapter(), accountId).show();
            }
        });


        holder.ivCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String entity = contactList.get(position);
                Intent intent = new Intent(v.getContext(), NewCalendarActivity.class);
                intent.putExtra(NewCalendarActivity.CALENDAR_TARGET_ID, entity);
                intent.putExtra(NewCalendarActivity.CALENDAR_ACTIVITY_TYPE, NewCalendarActivity.FRIEND_CALENDAR);
                intent.putExtra(NewCalendarActivity.CALENDAR_ACTIVITY_TITLE, entity); // TODO change to use nickname, backend /getFriendQueue return Account
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
}
