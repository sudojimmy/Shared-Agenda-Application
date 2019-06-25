package com.cosin.shareagenda.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.model.ApiClient;
import com.cosin.shareagenda.model.Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.http.HttpStatusCodes;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
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
                ApiClient.createAccount(name, description, new SigninCallback());
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

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            String errmsg;
            switch (message.what) {
                case HttpStatusCodes.STATUS_CODE_BAD_REQUEST:
                    errmsg = "Invalid Field!";
                    break;
                case HttpStatusCodes.STATUS_CODE_CONFLICT:
                    errmsg = "Already Exist!";
                    break;
                default:
                    errmsg = (String) message.obj;
            }
            Toast.makeText(SignUpActivity.this, errmsg, Toast.LENGTH_SHORT).show();
        }
    };

    class SigninCallback implements Callback {
        @Override
        public void onFailure(Call call, IOException e) {
            Message msg = handler.obtainMessage();
            msg.obj = e.getMessage();
            handler.sendMessage(msg);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            int rc = response.code();
            if (!HttpStatusCodes.isSuccess(rc)) {
                Message msg = handler.obtainMessage(rc);
                msg.obj = rc + response.message();
                handler.sendMessage(msg);
                return;
            }

            String body = response.body().string();

            final Gson gson = new Gson();
            CreateAccountResponse resp = gson.fromJson(body, CreateAccountResponse.class);

            Model.model.setUser(new Account()
                    .withAccountId(resp.getAccountId())
                    .withCalendarId(resp.getCalendarId())
                    .withDescription(etDescription.getText().toString())
                    .withMessageQueueId(resp.getMessageQueueId())
                    .withNickname(etNickName.getText().toString()));

            Intent intent = new Intent(SignUpActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
