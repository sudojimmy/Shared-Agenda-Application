package com.cosin.shareagenda.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.model.Model;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends MainTitleActivity {
    public static final String GOOGLE_ACCOUNT = "google_account";
    private TextView profileName, profileEmail, profileUserId, profileDescription, profileNickname;
    private ImageView profileImage;
    private Button signOut;

    @Override
    protected void initView() {
        super.initView();
        profileName = findViewById(R.id.profile_text);
        profileEmail = findViewById(R.id.profile_email);
        profileUserId = findViewById(R.id.profile_user_id);
        profileImage = findViewById(R.id.profile_image);
        profileNickname = findViewById(R.id.profile_nickname);
        profileDescription = findViewById(R.id.profile_description);
        signOut = findViewById(R.id.sign_out);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          /*
          Sign-out is initiated by simply calling the googleSignInClient.signOut API. We add a
          listener which will be invoked once the sign out is the successful
           */
                MainActivity.googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //On Succesfull signout we navigate the user back to LoginActivity
                        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
        });

        setDataOnView();
    }

    private void setDataOnView() {
        GoogleSignInAccount googleSignInAccount = Model.model.getGoogleSignInAccount();

        Picasso.with(this).load(googleSignInAccount.getPhotoUrl()).centerInside().fit().into(profileImage);

        profileName.setText(googleSignInAccount.getDisplayName());
        profileEmail.setText(googleSignInAccount.getEmail());
        profileUserId.setText(googleSignInAccount.getId());
        profileDescription.setText(Model.model.getUser().getDescription());
        profileNickname.setText(Model.model.getUser().getNickname());

        System.out.println(googleSignInAccount.getIdToken());
    }

    @Override
    protected  String titleName() {
        return getResources().getString(R.string.title_profile);
    }

    @Override
    protected void loadContentView() {
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.activity_profile, coordinatorLayout);
    }

    @Override
    protected void loadData() {
        //
    }

}
