package com.ngdat.studymore.ui.post;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ngdat.studymore.R;
import com.ngdat.studymore.base.activity.BaseActivity;
import com.ngdat.studymore.common.Constants;
import com.ngdat.studymore.models.item.ItemPost;
import com.ngdat.studymore.models.user.UserInfor;
import com.ngdat.studymore.models.user.UserInstance;
import com.ngdat.studymore.util.Utils;

import java.util.Calendar;

public class PostActivity extends BaseActivity
        implements Constants {

    private static final String TAG = PostActivity.class.getSimpleName();

    private Toolbar mToolbarPost;
    private TextInputEditText mTitle;
    private TextInputEditText mDes;
    private TextInputEditText mLocal;
    private TextInputEditText mPhone;
    private TextInputEditText mFee;

    private boolean isFree = false;
    private int countPost = 0;
    private boolean isFirst = true;

    @Override
    public int getLayout() {
        return R.layout.activity_post;
    }

    @Override
    public void inflateComponents() {

    }

    @Override
    public void findViewByIds() {
        mToolbarPost = (Toolbar) findViewById(R.id.toolbar_post);
        mTitle = (TextInputEditText) findViewById(R.id.edt_title);
        mDes = (TextInputEditText) findViewById(R.id.edt_des);
        mLocal = (TextInputEditText) findViewById(R.id.edt_local);
        mPhone = (TextInputEditText) findViewById(R.id.edt_phone);
        mFee = (TextInputEditText) findViewById(R.id.edt_fee);
    }

    @Override
    public void initComponents() {
        setSupportActionBar(mToolbarPost);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent receiverIntent = getIntent();
        if (null != receiverIntent.getStringExtra(KEY_FREE)) {
            isFree = true;
            mToolbarPost.setTitle("Thảo luận");
        } else {
            mToolbarPost.setTitle("Tìm hỗ trợ");
        }
    }

    @Override
    public void setEvents() {
        if (isFree) {
            findViewById(R.id.edt_fee1).setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home: {
                Log.i(TAG, "onOptionsItemSelected: back");
                this.finish();
                return true;
            }
            case R.id.action_complete: {
                Log.i(TAG, "onOptionsItemSelected: action_complete");
                if (commit()) {
                    finish();
                }
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean commit() {
        if (!Utils.isNotEmpty(mTitle)) {
            mTitle.setError("Mời bạn nhập tiêu đề");
            return false;
        }
        if (!Utils.isNotEmpty(mDes)) {
            mDes.setError("Mời bạn mô tả bài viết");
            return false;
        }
        if (!Utils.isNotEmpty(mLocal)) {
            mLocal.setError("Mời bạn nhập địa chỉ");
            return false;
        }
        if (!Utils.isNotEmpty(mPhone)) {
            mPhone.setError("Mời bạn nhập số điện thoại");
            return false;
        }
        if (!isFree && !Utils.isNotEmpty(mFee)) {
            mFee.setError("Mời bạn nhập học phí");
            return false;
        }

        String title = mTitle.getText().toString().trim();
        String des = mDes.getText().toString().trim();
        String local = mLocal.getText().toString().trim();
        String phone = mPhone.getText().toString().trim();
        String fee = mFee.getText().toString().trim();
        if (isFree) {
            fee = "";
        }

//        upload itemPost lên Firebase
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        String postId = reference.child(POSTS).push().getKey();
        ItemPost itemPost = new ItemPost(
                UserInstance.getInstance().getUid(),
                UserInstance.getInstance().getKey(),
                UserInstance.getInstance().getName(),
                postId, local, phone, title, des, fee,
                Calendar.getInstance().get(Calendar.DAY_OF_YEAR),
                Calendar.getInstance().get(Calendar.YEAR));
        reference.child(POSTS).child(postId).setValue(itemPost);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ItemPost itemPost = dataSnapshot.getValue(ItemPost.class);
                if (UserInstance.getInstance().getUid().equals(itemPost.getUserID())) {
                    countPost++;
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Log.i(TAG, "onChildAdded1: " + countPost);

        reference.child(PERSONAL);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserInfor userInfor = dataSnapshot.getValue(UserInfor.class);
                if (UserInstance.getInstance().getKey().equals(userInfor.getKey())) {
                    isFirst = false;
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.i(TAG, "onChildAdded2: " + isFirst);
        return true;
    }
}