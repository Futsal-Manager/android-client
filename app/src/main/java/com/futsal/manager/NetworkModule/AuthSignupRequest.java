package com.futsal.manager.NetworkModule;

/**
 * Created by stories2 on 2017. 3. 12..
 */

public class AuthSignupRequest {
    String username, password, team;

    public void SetUsername(String username) {
        this.username = username;
    }

    public void SetPassword(String password) {
        this.password = password;
    }

    public void SetTeam(String team) {
        this.team = team;
    }

    public String GetUsername() {
        return username;
    }

    public String GetPassword() {
        return password;
    }

    public String GetTeam() {
        return team;
    }
}
