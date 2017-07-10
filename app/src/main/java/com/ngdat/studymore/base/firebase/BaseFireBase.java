package com.ngdat.studymore.base.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public abstract class BaseFireBase {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;

    protected DatabaseReference getDatabaseReference() {
        if (null == mDatabase) {
            return FirebaseDatabase.getInstance().getReference();
        } else {
            return mDatabase;
        }
    }

    protected FirebaseAuth getFirebaseAuth() {
        if (null == mAuth) {
            return FirebaseAuth.getInstance();
        } else {
            return mAuth;
        }
    }

    protected StorageReference getStorageReference() {
        if (null == mStorageRef) {
            return FirebaseStorage.getInstance().getReference();
        } else {
            return mStorageRef;
        }
    }
}