package com.ngdat.studymore.common;

import android.content.res.Resources;

public interface Constants {
    int WIDTH_SCREEN = Resources.getSystem().getDisplayMetrics().widthPixels;
    int HEIGHT_SCREEN = Resources.getSystem().getDisplayMetrics().heightPixels;
    String USERS = "USERS";
    String POSTS = "POSTS";
    String NOTES = "NOTES";
    String PERSONAL = "PERSONAL";
    String BOOKMARK = "BOOKMARK";

    String KEY_USER_ID = "KEY_USER_ID";
    String KEY_USER_KEY = "KEY_USER_KEY";
    String KEY_USER_NAME = "KEY_USER_NAME";
    String KEY_LOCAL = "KEY_LOCAL";
    String KEY_PHONE = "KEY_PHONE";
    String KEY_TITLE = "KEY_TITLE";
    String KEY_DES = "KEY_DES";
    String KEY_FEE = "KEY_FEE";
    String KEY_FREE = "KEY_FREE";
}