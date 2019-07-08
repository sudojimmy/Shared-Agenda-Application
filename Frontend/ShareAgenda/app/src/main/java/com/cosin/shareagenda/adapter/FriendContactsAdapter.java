package com.cosin.shareagenda.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.activity.WeeklyFriendActivity;
import com.cosin.shareagenda.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

public class FriendContactsAdapter extends RecyclerView.Adapter<FriendContactsAdapter.ViewHolder> {
    private List<String> contactList;

    public FriendContactsAdapter() {
        this.contactList = new ArrayList<>();
    }
    public FriendContactsAdapter(Context context, List<String> contactList){
        this.contactList = contactList;
    }

    public void setContactList(List<String> contactList) {
        this.contactList = contactList;
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

    @Override
    public void onBindViewHolder(FriendContactsAdapter.ViewHolder holder, int position) {
        holder.tvContactName.setText(contactList.get(position));
        holder.ivCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String entity = contactList.get(position);
                Intent intent = new Intent(v.getContext(), WeeklyFriendActivity.class);
                intent.putExtra("user", (UserEntity)new UserEntity("default", ""));// TODO
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
}
