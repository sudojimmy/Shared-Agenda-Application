package com.cosin.shareagenda.model;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import types.Account;
import types.Group;

public class Model {
    public static Model model = new Model();

    private Account user = null;
    private Group currentGroup = null;
    private GoogleSignInAccount googleSignInAccount = null;
    private boolean isLoggedIn = false;

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

    private String currentTermStart;
    private String currentTermEnd;

    public String getCurrentTermStart() {
        return currentTermStart;
    }

    public void setCurrentTermStart(String currentTermStart) {
        this.currentTermStart = currentTermStart;
    }

    public String getCurrentTermEnd() {
        return currentTermEnd;
    }

    public void setCurrentTermEnd(String currentTermEnd) {
        this.currentTermEnd = currentTermEnd;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }
}
