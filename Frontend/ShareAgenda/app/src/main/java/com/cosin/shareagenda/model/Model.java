package com.cosin.shareagenda.model;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import types.Account;

public class Model {
    public static Model model = new Model();

    private Account user = null;
    private GoogleSignInAccount googleSignInAccount = null;

    public void setUser(Account user) {
        this.user = user;
    }

    public Account getUser() {
        return user;
    }

    public GoogleSignInAccount getGoogleSignInAccount() {
        return googleSignInAccount;
    }

    public void setGoogleSignInAccount(GoogleSignInAccount googleSignInAccount) {
        this.googleSignInAccount = googleSignInAccount;
    }
}
