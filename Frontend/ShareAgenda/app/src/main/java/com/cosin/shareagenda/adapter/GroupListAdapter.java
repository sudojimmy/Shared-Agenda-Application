package com.cosin.shareagenda.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.api.ApiClient;
import com.cosin.shareagenda.api.ApiErrorResponse;
import com.cosin.shareagenda.dialog.DisplayFriendRequestAccountDialog;
import com.cosin.shareagenda.model.Model;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import types.Account;
import types.GetFriendQueueResponse;

import static com.cosin.shareagenda.access.net.CallbackHandler.HTTP_FAILURE;
import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolder> {
    private List<Account> memberList;
    private List<String> myFriendList;
    private Context context;

    public GroupListAdapter(Context context) {
        this.context = context;
        memberList = new ArrayList<>();

        ApiClient.getFriendQueue(Model.model.getUser().getAccountId()
                , new CallbackHandler(handler));
    }

//    public GroupListAdapter(List<Account> memberList, Context context) {
//        this.context = context;
//        this.memberList = memberList;
//    }

    public void setMemberList(List<Account> memberList) {
        this.memberList = memberList;
        notifyDataSetChanged();
    }

    public void setMyFriendList(List<String> myFriendList) {
        this.myFriendList = myFriendList;
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
        Account memberAccount = memberList.get(position);
        viewHolder.viewName.setText(memberAccount.getNickname());

        viewHolder.viewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show the group member profile
                String memberAccountId = memberAccount.getAccountId();
                if (myFriendList.contains(memberAccountId)) {
                    // has been friend
                    // may delete him/her

                    //TODO just test purpose WRONG!!!
                    DisplayFriendRequestAccountDialog displayFriendRequestAccountDialog =
                            new DisplayFriendRequestAccountDialog(
                                    getContext(),
                                    memberAccountId,
                                    position);
                    displayFriendRequestAccountDialog.show();

//                    DisplayFriendAccountDialog displayAccountRequestDialog =
//                            new DisplayFriendAccountDialog(
//                                    getContext(),
//                                    memberAccountId,
//                                    position,
//                                    getG);
//                    displayAccountRequestDialog.show();

                } else {
                    // not friend && may want to add
                    DisplayFriendRequestAccountDialog displayFriendRequestAccountDialog =
                            new DisplayFriendRequestAccountDialog(
                                    getContext(),
                                    memberAccountId,
                                    position);
                    displayFriendRequestAccountDialog.show();
                }


//                String candidateId = memberList.get(position).getAccountId();
//                new DisplayAddAccountDialog(
//                        getContext(),
//                        candidateId,
//                        position,
//                        getSearchFriendAdapter() ).show();
            }
        });

        viewHolder.viewDescription.setText(memberAccount.getDescription());


    }


    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message message) {
            final Gson gson = new Gson();
            switch (message.what) {
                case SUCCESS:
                    String body = (String) message.obj;
                    GetFriendQueueResponse resp = gson.fromJson(body, GetFriendQueueResponse.class);
                    setMyFriendList(resp.getFriendList());
                    break;
                case HTTP_FAILURE:
                    ApiErrorResponse errorResponse = gson.fromJson((String) message.obj, ApiErrorResponse.class);
                    Toast.makeText(context, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(context, (String) message.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public int getItemCount() {
        return memberList.size();
    }
}
