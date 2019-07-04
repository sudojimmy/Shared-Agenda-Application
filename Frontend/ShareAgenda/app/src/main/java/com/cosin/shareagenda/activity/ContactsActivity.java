package com.cosin.shareagenda.activity;

<<<<<<< HEAD
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
=======
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
>>>>>>> 976fb93b2bfed9b299239c58a3db8716e9bbaed3

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
<<<<<<< HEAD
        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.rvContacts);
        rvContacts.setHasFixedSize(true);

=======

        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.rvContacts);
        rvContacts.setHasFixedSize(true);
>>>>>>> 976fb93b2bfed9b299239c58a3db8716e9bbaed3
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvContacts.setLayoutManager(layoutManager);
        ContactsAdapter conAdapter = new ContactsAdapter(this, contactList);
        rvContacts.setAdapter(conAdapter);
<<<<<<< HEAD
=======

        LinearLayout ll = findViewById(R.id.llSearch);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactsActivity.this, SearchFriendsActivity.class);
                startActivity(intent);
            }
        });
>>>>>>> 976fb93b2bfed9b299239c58a3db8716e9bbaed3
    }

    @Override
    protected String titleName() {
        return "Contacts";
    }
}
