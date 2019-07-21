package com.cosin.shareagenda.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.api.ApiClient;
import com.cosin.shareagenda.api.ApiErrorResponse;
import com.cosin.shareagenda.model.Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import types.Account;
import types.CreateAccountResponse;

import static com.cosin.shareagenda.access.net.CallbackHandler.HTTP_FAILURE;
import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

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
                ApiClient.createAccount(name, description, new CallbackHandler(handler));
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
                        finish();
                    }
                });
            }
        });
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            final Gson gson = new Gson();
            switch (message.what) {
                case SUCCESS:
                    String body = (String) message.obj;
                    CreateAccountResponse resp = gson.fromJson(body, CreateAccountResponse.class);

                    Model.model.setUser(new Account()
                            .withAccountId(resp.getAccountId())
                            .withCalendarId(resp.getCalendarId())
                            .withDescription(etDescription.getText().toString())
                            .withMessageQueueId(resp.getMessageQueueId())
                            .withNickname(etNickName.getText().toString()));

                    Model.model.setLoggedIn(true);
                    Intent intent = new Intent(SignUpActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case HTTP_FAILURE:
                    ApiErrorResponse errorResponse = gson.fromJson((String) message.obj, ApiErrorResponse.class);
                    Toast.makeText(SignUpActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(SignUpActivity.this, (String) message.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };
}
