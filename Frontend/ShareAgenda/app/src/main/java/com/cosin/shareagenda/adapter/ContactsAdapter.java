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

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
    private List<UserEntity> contactList;
    private Context context;

    public ContactsAdapter(Context context, List<UserEntity> contactList){
        this.contactList = contactList;
        this.context = context;
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
        holder.ivCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), WeeklyFriendActivity.class);
                intent.putExtra("user", contactList.get(position));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
}
