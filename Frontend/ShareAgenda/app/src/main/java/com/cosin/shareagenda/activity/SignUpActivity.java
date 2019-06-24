package com.cosin.shareagenda.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.model.ApiClient;
import com.cosin.shareagenda.model.ApiException;
import com.cosin.shareagenda.model.Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import types.Account;
import types.CreateAccountResponse;

public class SignUpActivity extends AppCompatActivity {
    private EditText etNickName;
    private EditText etDescription;
    private Button btnSignUpSubmit;
    private Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etNickName = findViewById(R.id.etNickName);
        etDescription = findViewById(R.id.etDescription);
        btnSignUpSubmit = findViewById(R.id.btnSignUp);
        btnBack = findViewById(R.id.btnBack);

        Activity activity = this;
        btnSignUpSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivityForResult(signInIntent, 101); // TODO follow pattern
                String name = etNickName.getText().toString();
                String description = etDescription.getText().toString();
                CreateAccountResponse resp = null;
                try {
                    resp = ApiClient.createAccount(name, description);
                } catch (ApiException e) {
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Model.model.setUser(new Account()
                        .withAccountId(resp.getAccountId())
                        .withCalendarId(resp.getCalendarId())
                        .withDescription(description)
                        //.withMessageQueueId(resp.get)
                        .withNickname(name));

                Intent intent = new Intent(activity, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //On Succesfull signout we navigate the user back to LoginActivity
                        Intent intent = new Intent(activity, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}
