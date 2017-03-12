package com.futsal.manager.NetworkModule;

/**
 * Created by stories2 on 2017. 3. 12..
 */

public class AuthLoginRequest {
    String username, password;

    public void SetUsername(String username) {
        this.username = username;
    }

    public void SetPassword(String password) {
        this.password = password;
    }

    public String GetUsername() {
        return username;
    }

    public String GetPassword() {
        return password;
    }
}