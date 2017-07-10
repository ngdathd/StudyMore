package com.ngdat.studymore.service;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.ngdat.studymore.base.firebase.BaseFireBase;

public class EmailLoginServices extends BaseFireBase {

    private FirebaseAuth mAuth;

    public EmailLoginServices() {
        mAuth = getFirebaseAuth();
    }

    public void loginAccountEmail(String email, String passWord, final EmailLoginListener listener) {
        mAuth.signInWithEmailAndPassword(email, passWord)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.loginFailure(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        listener.loginSuccess();
                    }
                });
    }
}
