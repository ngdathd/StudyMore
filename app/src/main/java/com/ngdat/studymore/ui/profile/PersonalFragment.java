package com.ngdat.studymore.ui.profile;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ngdat.studymore.R;
import com.ngdat.studymore.base.fragment.BaseFragment;
import com.ngdat.studymore.common.Constants;
import com.ngdat.studymore.models.user.UserInfor;
import com.ngdat.studymore.models.user.UserInstance;

public class PersonalFragment extends BaseFragment
        implements View.OnClickListener, Constants {

    private static final String TAG = PersonalFragment.class.getSimpleName();

    private TextView mPhone;
    private TextView mEmail;
    private TextView mLocation;
    private TextView mBirthday;
    private TextView mGender;
    private TextView mEdu;
    private TextView mFavor;

    private AppCompatButton mBtnEdit;

    @Override
    public int getLayout() {
        return R.layout.fragment_personal;
    }

    @Override
    public void inflateComponents() {

    }

    @Override
    public void findViewByIds() {
        mPhone = (TextView) getView().findViewById(R.id.txt_phone_number1);
        mEmail = (TextView) getView().findViewById(R.id.txt_email1);
        mLocation = (TextView) getView().findViewById(R.id.txt_location1);
        mBirthday = (TextView) getView().findViewById(R.id.txt_birthday1);
        mGender = (TextView) getView().findViewById(R.id.txt_gender);
        mEdu = (TextView) getView().findViewById(R.id.txt_edu);
        mFavor = (TextView) getView().findViewById(R.id.txt_favor);
        mBtnEdit = (AppCompatButton) getView().findViewById(R.id.btn_edit_profile);
        setFonts();
    }

    @Override
    public void initComponents() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(PERSONAL);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i(TAG, "onChildAdded: " + dataSnapshot);
                UserInfor userInfor = dataSnapshot.getValue(UserInfor.class);
                if (UserInstance.getInstance().getKey().equals(userInfor.getKey())) {
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
                if (UserInstance.getInstance().getKey().equals(userInfor.getKey())) {
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
        mBtnEdit.setOnClickListener(this);
        getView().findViewById(R.id.img_phone1).setOnClickListener(this);
        getView().findViewById(R.id.img_message1).setOnClickListener(this);
        getView().findViewById(R.id.img_email1).setOnClickListener(this);
        getView().findViewById(R.id.img_directions1).setOnClickListener(this);
    }

    @Override
    public int getIdLayout() {
        return 0;
    }

    private void setFonts() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
        mPhone.setTypeface(tf);
        mEmail.setTypeface(tf);
        mLocation.setTypeface(tf);
        mBirthday.setTypeface(tf);
        mGender.setTypeface(tf);
        mEdu.setTypeface(tf);
        mFavor.setTypeface(tf);

        tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Medium.ttf");
        TextView t = (TextView) getView().findViewById(R.id.txt_header_phone);
        t.setTypeface(tf);
        t = (TextView) getView().findViewById(R.id.txt_header_email);
        t.setTypeface(tf);
        t = (TextView) getView().findViewById(R.id.txt_header_location);
        t.setTypeface(tf);
        t = (TextView) getView().findViewById(R.id.txt_header_personal);
        t.setTypeface(tf);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_edit_profile: {
                startActivity(new Intent(getContext(), EditProfileActivity.class));
            }
            break;
            case R.id.img_phone1: {
                Log.i(TAG, "onClick: phone");
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mPhone.getText().toString()));
                startActivity(intent);
            }
            break;
            case R.id.img_message1: {
                Log.i(TAG, "onClick: message");
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