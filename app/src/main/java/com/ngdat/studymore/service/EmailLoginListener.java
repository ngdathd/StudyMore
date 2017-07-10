package com.ngdat.studymore.service;

public interface EmailLoginListener {
    void loginSuccess();

    void loginFailure(String message);
}