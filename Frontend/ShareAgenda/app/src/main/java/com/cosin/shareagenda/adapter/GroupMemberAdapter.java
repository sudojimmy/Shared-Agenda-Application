package com.cosin.shareagenda.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.entity.VO_Member;

import java.util.List;

public class GroupMemberAdapter extends RecyclerView.Adapter<GroupMemberAdapter.ViewHolder> {
    private List<VO_Member> members;

    public GroupMemberAdapter(List<VO_Member> members) {
        this.members = members;
    }

    public void setMembers(List<VO_Member> members) {
        this.members = members;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        LinearLayout linearLayout;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.tvGroupContactName);
            linearLayout = view.findViewById(R.id.llCreateGroupItem);
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
        viewHolder.textView.setText(member.getMember());
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int position = viewHolder.getAdapterPosition();
                VO_Member member = members.get(position);
                if (member.isElected()) {
                    member.setElected(false);
                    ((LinearLayout)v).setBackgroundColor(0x00000000);
                } else {
                    member.setElected(true);
                    ((LinearLayout)v).setBackgroundColor(0xaa88ffaa);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return members.size();
    }
}
