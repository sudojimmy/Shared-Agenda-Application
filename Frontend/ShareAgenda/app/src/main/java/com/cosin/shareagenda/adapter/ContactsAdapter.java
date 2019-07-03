package com.cosin.shareagenda.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.entity.UserEntity;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
    private List<UserEntity> contactList;

    public ContactsAdapter(Context context, List<UserEntity> contactList){
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
        holder.tvContactName.setText(contactList.get(position).getNickname());
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
}
