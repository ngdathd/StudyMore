package com.ngdat.studymore.service;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.ngdat.studymore.base.firebase.BaseFireBase;
import com.ngdat.studymore.common.Constants;
import com.ngdat.studymore.models.user.User;
import com.ngdat.studymore.models.user.UserInfor;

public class EmailRegisterServices extends BaseFireBase
        implements Constants {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Activity mActivity;

    public EmailRegisterServices(Activity activity) {
        mActivity = activity;
        mAuth = getFirebaseAuth();
        mDatabase = getDatabaseReference();
    }

    public void registerAccount(final String email, String passWord, final EmailRegisterListener listener) {
        mAuth.createUserWithEmailAndPassword(email, passWord)
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final FirebaseUser firebaseUser = task.getResult().getUser();
                            if (firebaseUser != null) {
                                firebaseUser.sendEmailVerification().addOnCompleteListener(mActivity, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            User user = new User();
                                            user.setUid(firebaseUser.getUid());
                                            user.setName(email.split("[@]")[0]);
                                            user.setEmail(firebaseUser.getEmail());
                                            createAccountInDatabase(user, new EmailRegisterListener() {
                                                @Override
                                                public void registerSuccess() {
                                                    mAuth.signOut();
                                                    listener.registerSuccess();
                                                }

                                                @Override
                                                public void registerFailure(String message) {
                                                    listener.registerFailure(message);
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.registerFailure(e.getMessage());
            }
        });
    }

    public void createAccountInDatabase(final User user, final EmailRegisterListener listener) {
        mDatabase.child(USERS)
                .child(user.getUid())
                .setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.registerSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.registerFailure(e.getMessage());
            }
        });

        mDatabase.child(PERSONAL)
                .child(user.getUid())
                .setValue(new UserInfor("", "", "", "", "", "", "", user.getUid()));
    }
}