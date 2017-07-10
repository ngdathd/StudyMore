package com.ngdat.studymore.service;

public interface EmailRegisterListener {
    void registerSuccess();

    void registerFailure(String message);
}