package com.example.gamingjr.model;

public interface AuthListener {
    void onLoginSuccess();
    void onLoginFailure(String errorMessage);
}
