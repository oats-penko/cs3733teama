package com.teama.login;


/**
 * Created by aliss on 11/11/2017.
 */
public class SystemUser {
    // holds the login credentials for an admin/staff user for verification
    private LoginInfo login;
    private AccessType access;
    // private int userID; // may add later

    // set to default
    public SystemUser() {
        login = null;
        access = AccessType.GUEST;
    }

    // returned  by login database if login successful
    public SystemUser(LoginInfo l, AccessType a) {
        login = l;
        access = a;
    }

    public String getUsername() {
        return login.getUsername();
    }

    public void setUsername(String uname) {
        login.setUsername(uname);
    }

    public String getPassword() {
        return login.getPassword();
    }

    public void setPassword(String pw) {
        login.setPassword(pw);
    }

    public AccessType getAccess() {
        return access;
    }

    public LoginInfo getLoginInfo() {
        return login;
    }

    public void setLoginInfo(LoginInfo l) {
        this.login = l;
    }

    public void setAccess(AccessType a) {
        this.access = a;
    }
}
