package com.cosin.shareagenda.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.dialog.DisplayAddAccountDialog;

import java.util.ArrayList;
import java.util.List;

import types.Account;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolder> {
    private List<String> memberList;
    private Context context;

    public GroupListAdapter(Context context) {
        this.context = context;
        memberList = new ArrayList<>();
    }

    public GroupListAdapter(List<String> memberList, Context context) {
        this.context = context;
        this.memberList = memberList;
    }

    public void setMemberList(List<String> memberList) {
        this.memberList = memberList;
        notifyDataSetChanged();
    }

    // todo

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView viewName;
        TextView viewDescription;

        public ViewHolder(View view) {
            super(view);
            viewName = view.findViewById(R.id.nameTextView);
            viewDescription = view.findViewById(R.id.description);
        }
    }

    public Context getContext(){
        return context;
    }

    private GroupListAdapter getSearchFriendAdapter(){
        return this;
    }

    @Override
    public GroupListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grouplist_row, parent, false);

        final GroupListAdapter.ViewHolder viewHolder =
                new GroupListAdapter.ViewHolder(view);

        return viewHolder;
        // TODO different from groupContactsAdapter
    }

//    public void removeElementFromContactList(int position){
//        memberList.remove(position);
//        notifyItemRemoved(position);
//        notifyDataSetChanged();
//
//    }

    @Override
    public void onBindViewHolder(GroupListAdapter.ViewHolder viewHolder, int position) {
        String memberAccountId = memberList.get(position);
        Account account;
        viewHolder.viewName.setText(memberList.get(position));

        viewHolder.viewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String candidateId = memberList.get(position).getAccountId();
                new DisplayAddAccountDialog(
                        getContext(),
                        candidateId,
                        position,
                        getSearchFriendAdapter() ).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }
}
