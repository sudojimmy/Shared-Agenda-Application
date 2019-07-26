package com.cosin.shareagenda.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.api.ApiClient;
import com.cosin.shareagenda.api.ApiErrorResponse;
import com.cosin.shareagenda.model.Model;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.http.HttpStatusCodes;
import com.google.gson.Gson;

import types.Account;
import types.GetAccountResponse;

import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;
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
//            Toast.makeText(this, "Already Logged In, expired: " + alreadyloggedAccount.isExpired(), Toast.LENGTH_SHORT).show();
            if (alreadyloggedAccount.isExpired()) {
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
            } else {
                onLoggedIn(alreadyloggedAccount);
            }
        } else {
            Log.d(TAG, "Not logged in");
        }
    }
    private void onLoggedIn(GoogleSignInAccount googleSignInAccount) {
        Model.model.setGoogleSignInAccount(googleSignInAccount);

        ApiClient.getAccount(googleSignInAccount.getEmail(), new CallbackHandler(handler));
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            final Gson gson = new Gson();
            Intent intent;
            switch (message.what) {
                case SUCCESS:
                    String body = (String) message.obj;
                    GetAccountResponse resp = gson.fromJson(body, GetAccountResponse.class);
                    Model.model.setUser(new Account()
                            .withAccountId(resp.getAccountId())
                            .withCalendarId(resp.getCalendarId())
                            .withDescription(resp.getDescription())
                            .withMessageQueueId(resp.getMessageQueueId())
                            .withNickname(resp.getNickname()));

                    Model.model.setLoggedIn(true);
                    intent = new Intent(MainActivity.this, ProfileActivity.class);
                    break;
                case CallbackHandler.HTTP_FAILURE:
                    ApiErrorResponse errorResponse = gson.fromJson((String) message.obj, ApiErrorResponse.class);
                    if (errorResponse.getStatus() == HttpStatusCodes.STATUS_CODE_NOT_FOUND) {
                        intent = new Intent(MainActivity.this, SignUpActivity.class);
                    } else {
                        Toast.makeText(MainActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;
                default:
                    Toast.makeText(MainActivity.this, (String)message.obj, Toast.LENGTH_SHORT).show();
                    return;
            }
            startActivity(intent);
            finish();
        }
    };

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
            Toast.makeText(this, "Result Code:" + resultCode, Toast.LENGTH_SHORT).show(); // TODO remove this
        }
    }
}
