package com.cosin.shareagenda.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.entity.VO_Member;

import java.util.List;

import types.Account;

public class GroupMemberAdapter extends RecyclerView.Adapter<GroupMemberAdapter.ViewHolder> {
    private List<VO_Member> members;
    private Context context;

    public GroupMemberAdapter(List<VO_Member> members, Context context) {
        this.members = members;
        this.context = context;
    }

    public void setMembers(List<VO_Member> members) {
        this.members = members;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        LinearLayout linearLayout;
        CheckBox checkBox;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.tvGroupContactName);
            linearLayout = view.findViewById(R.id.llCreateGroupItem);
            checkBox = view.findViewById(R.id.checkBox);
        }
    }

    @Override
    public GroupMemberAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.create_group_item, parent, false);
        final GroupMemberAdapter.ViewHolder viewHolder = new GroupMemberAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        VO_Member member = members.get(position);
        Account memberAccount = member.getMember();

        String nicknameAddEmail = memberAccount.getNickname() + "(" + memberAccount.getAccountId() + ")";
        viewHolder.textView.setText(nicknameAddEmail);
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int position = viewHolder.getAdapterPosition();
                VO_Member member = members.get(position);
                if (member.isElected()) {
                    member.setElected(false);
                    viewHolder.checkBox.setChecked(false);
                    ((LinearLayout)v).setBackgroundColor(0x00000000);
                } else {
                    // selected
                    member.setElected(true);
                    viewHolder.checkBox.setChecked(true);
                    ((LinearLayout)v).setBackgroundColor(
                            ContextCompat.getColor(context, R.color.light_blue));
                }
            }
        });

        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View checkboxview) {
                //int position = viewHolder.getAdapterPosition();
                VO_Member member = members.get(position);
                View v = viewHolder.linearLayout;

                if (!viewHolder.checkBox.isChecked()) {
                    member.setElected(false);
                    viewHolder.checkBox.setChecked(false);

                    ((LinearLayout)v).setBackgroundColor(0x00000000);
                } else {
                    // selected
                    member.setElected(true);
                    viewHolder.checkBox.setChecked(true);

                    ((LinearLayout)v).setBackgroundColor(
                            ContextCompat.getColor(context, R.color.light_blue));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return members.size();
    }
}
