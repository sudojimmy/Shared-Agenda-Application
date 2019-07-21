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
import com.cosin.shareagenda.activity.GroupEventMessagesActivity;
import com.cosin.shareagenda.activity.GroupMembersActivity;
import com.cosin.shareagenda.activity.NewCalendarActivity;
import com.cosin.shareagenda.model.Model;

import java.util.ArrayList;
import java.util.List;

import types.Group;

public class GroupContactsAdapter extends RecyclerView.Adapter<GroupContactsAdapter.ViewHolder> {
    private List<Group> contactList = new ArrayList<>();

    public GroupContactsAdapter(Context context, List<Group> contactList){
        this.contactList = contactList;
    }

    public GroupContactsAdapter(Context context){}

    public void setContactList(List<Group> contactList) {
        this.contactList = contactList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivGroupEvent;
        TextView tvContactName;
        ImageView ivCalendar;

        public ViewHolder(View view) {
            super(view);
            tvContactName = view.findViewById(R.id.tvContactName);
            ivCalendar = view.findViewById((R.id.ivCalendar));
            ivGroupEvent = view.findViewById((R.id.ivGroupEvent));
        }
    }
    @Override
    public GroupContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.group_contact_item, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GroupContactsAdapter.ViewHolder holder, int position) {
        holder.tvContactName.setText(contactList.get(position).getName());
        holder.tvContactName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo
                Group entity = contactList.get(position);
                Intent intent = new Intent(v.getContext(), GroupMembersActivity.class);
                intent.putExtra(GroupMembersActivity.GROUP_ID, entity.getGroupId());
                intent.putExtra(GroupMembersActivity.GROUP_NAME, entity.getName());
                Model.model.setCurrentGroup(entity);
                v.getContext().startActivity(intent);
            }
        });

        holder.ivCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Group entity = contactList.get(position);
                Intent intent = new Intent(v.getContext(), NewCalendarActivity.class);
                intent.putExtra(NewCalendarActivity.CALENDAR_TARGET_ID, entity.getGroupId());
                intent.putExtra(NewCalendarActivity.CALENDAR_ACTIVITY_TYPE, NewCalendarActivity.GROUP_CALENDAR);
                intent.putExtra(NewCalendarActivity.CALENDAR_ACTIVITY_TITLE, entity.getName());
                Model.model.setCurrentGroup(entity);
                v.getContext().startActivity(intent);
            }
        });

        holder.ivGroupEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Group entity = contactList.get(position);
                Model.model.setCurrentGroup(entity);
                Intent intent = new Intent(v.getContext(), GroupEventMessagesActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
}
