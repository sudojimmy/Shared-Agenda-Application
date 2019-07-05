package com.cosin.shareagenda.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.activity.GroupEventsActivity;
import com.cosin.shareagenda.activity.WeeklyActivity;
import com.cosin.shareagenda.activity.WeeklyFriendActivity;
import com.cosin.shareagenda.entity.ContactEntity;
import com.cosin.shareagenda.entity.GroupEntity;
import com.cosin.shareagenda.entity.UserEntity;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
    private List<ContactEntity> contactList;

    public ContactsAdapter(Context context, List<ContactEntity> contactList){
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
    public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ContactsAdapter.ViewHolder holder, int position) {
        holder.tvContactName.setText(contactList.get(position).getName());
        holder.ivCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactEntity entity = contactList.get(position);
                if ("UserEntity".equals(entity.getClass().getSimpleName())) {
                    Intent intent = new Intent(v.getContext(), WeeklyFriendActivity.class);
                    intent.putExtra("user", (UserEntity)entity);
                    v.getContext().startActivity(intent);
                } else {
                    Intent intent = new Intent(v.getContext(), GroupEventsActivity.class);
                    intent.putExtra("group", (GroupEntity)entity);
                    v.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
}
