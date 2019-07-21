package com.cosin.shareagenda.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.adapter.FriendContactsAdapter;
import com.cosin.shareagenda.api.ApiClient;
import com.cosin.shareagenda.api.ApiErrorResponse;
import com.cosin.shareagenda.entity.ContactEntity;
import com.google.gson.Gson;

import java.util.ArrayList;

import types.GetFriendQueueResponse;

import static com.cosin.shareagenda.access.net.CallbackHandler.HTTP_FAILURE;
import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

public class ContactsActivity extends RefreshableActivity {

    ArrayList<ContactEntity> contactList;
    private FriendContactsAdapter conAdapter;
    RecyclerView rvContacts;
    @Override
    protected int getContentView() {
        return R.layout.activity_contacts;
    }

    @Override
    protected void loadData() {
        ApiClient.getFriendQueue(new CallbackHandler(handler));
    }

    @Override
    protected void initView() {
        super.initView();

        this.rvContacts = findViewById(R.id.rvContacts);
        this.rvContacts.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        this.rvContacts.setLayoutManager(layoutManager);
        conAdapter = new FriendContactsAdapter(this);
        this.rvContacts.setAdapter(conAdapter);

        LinearLayout ll = findViewById(R.id.llSearch);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactsActivity.this, SearchFriendsActivity.class);
                startActivity(intent);
            }
        });
    }



    @Override
    protected String titleName() {
        return "Contacts";
    }

    public void searchFriend(View view) {
        Intent intent = new Intent(ContactsActivity.this, SearchFriendsActivity.class);
        startActivity(intent);
    }

    public void getMessage(View view) {
        Intent intent = new Intent(ContactsActivity.this, FriendMessageActivity.class);
        startActivity(intent);
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message message) {
            final Gson gson = new Gson();
            switch (message.what) {
                case SUCCESS:
                    String body = (String) message.obj;
                    GetFriendQueueResponse resp = gson.fromJson(body, GetFriendQueueResponse.class);
                    conAdapter.setContactList(resp.getFriendList());
                    break;
                case HTTP_FAILURE:
                    ApiErrorResponse errorResponse = gson.fromJson((String) message.obj, ApiErrorResponse.class);
                    Toast.makeText(ContactsActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(ContactsActivity.this, (String) message.obj, Toast.LENGTH_SHORT).show();
            }
            stopRefreshing();
        }
    };
}
