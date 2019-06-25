package com.cosin.shareagenda.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.model.ApiClient;
import com.cosin.shareagenda.model.Model;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.api.client.http.HttpStatusCodes;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import types.Account;
import types.GetAccountResponse;

import static com.cosin.shareagenda.config.SystemConfig.SERVER_CLIENT_ID;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "AndroidClarified";
    public static  GoogleSignInClient googleSignInClient;
    private SignInButton googleSignInButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        googleSignInButton = findViewById(R.id.sign_in_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(SERVER_CLIENT_ID)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount alreadyloggedAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (alreadyloggedAccount != null) {
            Toast.makeText(this, "Already Logged In", Toast.LENGTH_SHORT).show();
            onLoggedIn(alreadyloggedAccount);
        } else {
            Log.d(TAG, "Not logged in");
        }
    }
    private void onLoggedIn(GoogleSignInAccount googleSignInAccount) {
        Model.model.setGoogleSignInAccount(googleSignInAccount);

        ApiClient.getAccount(googleSignInAccount.getEmail(), new SigninCallback());
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            if (message.what == HttpStatusCodes.STATUS_CODE_NOT_FOUND) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
                return;
            }
            Toast.makeText(MainActivity.this, (String)message.obj, Toast.LENGTH_SHORT).show();
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
            GetAccountResponse resp = gson.fromJson(body, GetAccountResponse.class);
            Model.model.setUser(new Account()
                    .withAccountId(resp.getAccountId())
                    .withCalendarId(resp.getCalendarId())
                    .withDescription(resp.getDescription())
                    .withMessageQueueId(resp.getMessageQueueId())
                    .withNickname(resp.getNickname()));

            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 101:
                    try {
                        // The Task returned from this call is always completed, no need to attach
                        // a listener.
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        onLoggedIn(account);
                    } catch (ApiException e) {
                        // The ApiException status code indicates the detailed failure reason.
                        Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
                    }
                    break;
            }
        } else {
            Toast.makeText(this, "Request Cdoe: "+requestCode+"  Result Code:" + resultCode, Toast.LENGTH_SHORT).show(); // TODO remove this
        }
    }
}
