package com.cosin.shareagenda.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.adapter.ContactsAdapter;
import com.cosin.shareagenda.entity.ContactEntity;
import com.cosin.shareagenda.entity.UserEntity;

import java.util.ArrayList;

public class ContactsActivity extends MainTitleActivity {

    ArrayList<ContactEntity> contactList;

    @Override
    protected int getContentView() {
        return R.layout.activity_contacts;
    }

    @Override
    protected void loadData() {
        contactList = new ArrayList<ContactEntity>();
        contactList.add(new UserEntity("1", "Jimmy"));
        contactList.add(new UserEntity("2", "Alice"));
        contactList.add(new UserEntity("3", "Ricki"));
        contactList.add(new UserEntity("4", "florie"));
        contactList.add(new UserEntity("1", "Jimmy"));
        contactList.add(new UserEntity("2", "Alice"));
        contactList.add(new UserEntity("3", "Ricki"));
        contactList.add(new UserEntity("4", "florie"));
        contactList.add(new UserEntity("1", "Jimmy"));
        contactList.add(new UserEntity("2", "Alice"));
        contactList.add(new UserEntity("3", "Ricki"));
        contactList.add(new UserEntity("4", "florie"));
    }

    @Override
    protected void initView() {
        super.initView();

        RecyclerView rvContacts = findViewById(R.id.rvContacts);
        rvContacts.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvContacts.setLayoutManager(layoutManager);
        ContactsAdapter conAdapter = new ContactsAdapter(this, contactList);
        rvContacts.setAdapter(conAdapter);

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
}
