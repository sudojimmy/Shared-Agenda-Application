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
import com.cosin.shareagenda.activity.GroupEventsActivity;

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
        TextView tvContactName;
        ImageView ivCalendar;

        public ViewHolder(View view) {
            super(view);
            tvContactName = view.findViewById(R.id.tvContactName);
            ivCalendar = view.findViewById((R.id.ivCalendar));
        }
    }
    @Override
    public GroupContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GroupContactsAdapter.ViewHolder holder, int position) {
        holder.tvContactName.setText(contactList.get(position).getName());
        holder.ivCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Group entity = contactList.get(position);
                    Intent intent = new Intent(v.getContext(), GroupEventsActivity.class);
//                    intent.putExtra("group", (GroupEntity)entity);
                    v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
}
