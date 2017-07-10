package com.ngdat.studymore.ui.post;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ngdat.studymore.R;
import com.ngdat.studymore.base.activity.BaseActivity;
import com.ngdat.studymore.common.Constants;
import com.ngdat.studymore.models.user.UserInfor;
import com.ngdat.studymore.util.Utils;

public class UserDetailActivity extends BaseActivity
        implements View.OnClickListener, Constants {

    private static final String TAG = UserDetailActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private SimpleDraweeView avatarFace;

    private TextView mPhone;
    private TextView mEmail;
    private TextView mLocation;
    private TextView mBirthday;
    private TextView mGender;
    private TextView mEdu;
    private TextView mFavor;

    @Override
    public int getLayout() {
        return R.layout.activity_user_detail;
    }

    @Override
    public void inflateComponents() {
        Fresco.initialize(this);
    }

    @Override
    public void findViewByIds() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_post_detail);
        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        avatarFace = (SimpleDraweeView) findViewById(R.id.img_post_detail);
        mPhone = (TextView) findViewById(R.id.txt_phone_number1);
        mEmail = (TextView) findViewById(R.id.txt_email1);
        mLocation = (TextView) findViewById(R.id.txt_location1);
        mBirthday = (TextView) findViewById(R.id.txt_birthday1);
        mGender = (TextView) findViewById(R.id.txt_gender);
        mEdu = (TextView) findViewById(R.id.txt_edu);
        mFavor = (TextView) findViewById(R.id.txt_favor);
        setFonts();
    }

    private void setFonts() {
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        mPhone.setTypeface(tf);
        mEmail.setTypeface(tf);
        mLocation.setTypeface(tf);
        mBirthday.setTypeface(tf);
        mGender.setTypeface(tf);
        mEdu.setTypeface(tf);
        mFavor.setTypeface(tf);

        tf = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
        TextView t = (TextView) findViewById(R.id.txt_header_phone);
        t.setTypeface(tf);
        t = (TextView) findViewById(R.id.txt_header_email);
        t.setTypeface(tf);
        t = (TextView) findViewById(R.id.txt_header_location);
        t.setTypeface(tf);
        t = (TextView) findViewById(R.id.txt_header_personal);
        t.setTypeface(tf);
    }

    @Override
    public void initComponents() {
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final Intent receiverIntent = getIntent();
        Log.i(TAG, "initComponents: " + receiverIntent.getStringExtra(KEY_USER_NAME));
        mCollapsingToolbar.setTitle(receiverIntent.getStringExtra(KEY_USER_NAME));
        if (receiverIntent.getStringExtra(KEY_USER_KEY).equals(receiverIntent.getStringExtra(KEY_USER_ID))) {
            avatarFace.setImageResource(
                    Utils.setImgBackground((int) receiverIntent.getStringExtra(KEY_USER_NAME).charAt(0)));
        } else {
            avatarFace.setImageURI(Utils.getLinkAvatar(
                    receiverIntent.getStringExtra(KEY_USER_ID),
                    WIDTH_SCREEN * 2,
                    (int) Utils.convertDpToPixel(256) * 2));
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(PERSONAL);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserInfor userInfor = dataSnapshot.getValue(UserInfor.class);
                if (receiverIntent.getStringExtra(KEY_USER_KEY).equals(userInfor.getKey())) {
                    mPhone.setText(userInfor.getPhone());
                    mEmail.setText(userInfor.getEmail());
                    mLocation.setText(userInfor.getLocal());
                    mBirthday.setText(userInfor.getBirthday());
                    mGender.setText(userInfor.getGender());
                    mEdu.setText(userInfor.getEdu());
                    mFavor.setText(userInfor.getFavor());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                UserInfor userInfor = dataSnapshot.getValue(UserInfor.class);
                if (receiverIntent.getStringExtra(KEY_USER_KEY).equals(userInfor.getKey())) {
                    mPhone.setText(userInfor.getPhone());
                    mEmail.setText(userInfor.getEmail());
                    mLocation.setText(userInfor.getLocal());
                    mBirthday.setText(userInfor.getBirthday());
                    mGender.setText(userInfor.getGender());
                    mEdu.setText(userInfor.getEdu());
                    mFavor.setText(userInfor.getFavor());
                }
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
    }

    @Override
    public void setEvents() {
        findViewById(R.id.img_phone1).setOnClickListener(this);
        findViewById(R.id.img_message1).setOnClickListener(this);
        findViewById(R.id.img_email1).setOnClickListener(this);
        findViewById(R.id.img_directions1).setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home: {
                this.finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.img_phone1: {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mPhone.getText().toString()));
                startActivity(intent);
            }
            break;
            case R.id.img_message1: {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("sms:" + mPhone.getText().toString()));
                startActivity(intent);
            }
            break;
            case R.id.img_email1: {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{mEmail.getText().toString()});
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email client:"));
            }
            break;
            case R.id.img_directions1: {
                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse("http://maps.google.com"));
                intent.setData(Uri.parse("geo:21.1,105.51?z=10"));
                startActivity(intent);
            }
            break;
            default:
                break;
        }
    }
}
