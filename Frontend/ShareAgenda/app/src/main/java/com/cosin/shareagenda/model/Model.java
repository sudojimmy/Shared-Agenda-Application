package com.cosin.shareagenda.model;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import types.Account;
import types.Group;

public class Model {
    public static Model model = new Model();

    private Account user = null;
    private Group currentGroup = null;
    private GoogleSignInAccount googleSignInAccount = null;

    public void setUser(Account user) {
        this.user = user;
    }

    public Account getUser() {
        return user;
    }

    public void setCurrentGroup(Group group) {
        this.currentGroup = group;
    }

    public Group getCurrentGroup() {
        return currentGroup;
    }

    public GoogleSignInAccount getGoogleSignInAccount() {
        return googleSignInAccount;
    }

    public void setGoogleSignInAccount(GoogleSignInAccount googleSignInAccount) {
        this.googleSignInAccount = googleSignInAccount;
    }
}
