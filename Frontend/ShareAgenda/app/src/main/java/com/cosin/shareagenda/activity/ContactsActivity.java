package com.cosin.shareagenda.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.adapter.ContactsAdapter;
import com.cosin.shareagenda.entity.UserEntity;

import java.util.ArrayList;

public class ContactsActivity extends MainTitleActivity {

    ArrayList<UserEntity> contactList;

    @Override
    protected int getContentView() {
        return R.layout.activity_contacts;
    }

    @Override
    protected void loadData() {
        contactList = new ArrayList<UserEntity>();
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
        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.rvContacts);
        rvContacts.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvContacts.setLayoutManager(layoutManager);
        ContactsAdapter conAdapter = new ContactsAdapter(this, contactList);
        rvContacts.setAdapter(conAdapter);
    }

    @Override
    protected String titleName() {
        return "Contacts";
    }
}
