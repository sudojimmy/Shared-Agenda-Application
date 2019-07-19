package com.cosin.shareagenda.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.api.ApiClient;
import com.cosin.shareagenda.api.ApiErrorResponse;
import com.cosin.shareagenda.dialog.DisplayAddAccountDialog;
import com.cosin.shareagenda.dialog.DisplayFriendAccountDialog;
import com.cosin.shareagenda.dialog.DisplayFriendRequestAccountDialog;
import com.cosin.shareagenda.model.Model;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import types.Account;
import types.GetFriendQueueResponse;

import static com.cosin.shareagenda.access.net.CallbackHandler.HTTP_FAILURE;
import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolder> {
    private List<Account> memberList = new ArrayList<>();
    private List<Account> myFriendList;
    private Context context;
    private String ownerId;

    public GroupListAdapter(Context context) {
        this.context = context;
        updateInformation();
    }

    public void setMemberList(List<Account> memberList) {
        Account owner = null;
        int removeIndex = -1;

        for (Account member: memberList) {
            if(member.getAccountId().equals(ownerId)) {
                owner = member;
                removeIndex = memberList.indexOf(member);
                break;
            }
        }
        memberList.remove(removeIndex);
        memberList.add(0, owner);

        this.memberList = memberList;
        notifyDataSetChanged();
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void updateInformation(){
        ApiClient.getFriendQueue(new CallbackHandler(handler));
    }

    private void setMyFriendList(List<Account> myFriendList) {
        this.myFriendList = myFriendList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView profileImage;
        private final TextView nameTextView;
        private final TextView description;
        private final TextView selfIdentity;
        private final RelativeLayout relativeLayout;

        public ViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.nameTextView);
            description = view.findViewById(R.id.description);
            selfIdentity = view.findViewById(R.id.you);
            relativeLayout = view.findViewById(R.id.group_list_row_id);
            profileImage = view.findViewById(R.id.imageView);
        }
    }

    public Context getContext(){
        return context;
    }

    @Override
    public GroupListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grouplist_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GroupListAdapter.ViewHolder holder, int position) {
        Account memberAccount = memberList.get(position);
        String memberAccountId = memberAccount.getAccountId();
        holder.nameTextView.setText(memberAccount.getNickname());

        String identity = "";
        if (memberAccountId.equals(Model.model.getUser().getAccountId())) {
            identity += "(YOU)";
        }
        if (memberAccountId.equals(ownerId)) {
            identity += "(OWNER)";
        }
        holder.selfIdentity.setText(identity);


        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show the group member profile
                if (memberAccountId.equals(Model.model.getUser().getAccountId())) {
                    // oneself
                    new DisplayFriendRequestAccountDialog(
                            getContext(),
                            memberAccount,
                            position).show();
                } else if (myFriendList.contains(memberAccount)) {
                    // has been friend
                    // may delete him/her
                    new DisplayFriendAccountDialog(
                            getContext(),
                            memberAccount,
                            position,
                            GroupListAdapter.this).show();
                } else {
                    // not friend && may want to add
                    new DisplayAddAccountDialog(
                            getContext(),
                            memberAccount,
                            position,
                            GroupListAdapter.this).show();
                }
            }
        });

        holder.description.setText(memberAccount.getDescription());

        if (memberAccount.getProfileImageUrl() != null) {
            Picasso.with(getContext())
                    .load(memberAccount.getProfileImageUrl())
                    .centerInside()
                    .fit()
                    .into(holder.profileImage);
        }

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
