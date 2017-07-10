package com.ngdat.studymore.ui.profile;

import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ngdat.studymore.R;
import com.ngdat.studymore.base.activity.BaseActivity;
import com.ngdat.studymore.common.Constants;
import com.ngdat.studymore.models.user.UserInfor;
import com.ngdat.studymore.models.user.UserInstance;
import com.ngdat.studymore.util.Utils;

public class EditProfileActivity extends BaseActivity
        implements Constants {

    private Toolbar mToolbarPost;

    private TextInputEditText mPhone;
    private TextInputEditText mEmail;
    private TextInputEditText mLocation;
    private TextInputEditText mBirthday;
    private TextInputEditText mGender;
    private TextInputEditText mEdu;
    private TextInputEditText mFavor;

    @Override
    public int getLayout() {
        return R.layout.activity_edit_profile;
    }

    @Override
    public void inflateComponents() {

    }

    @Override
    public void findViewByIds() {
        mToolbarPost = (Toolbar) findViewById(R.id.toolbar_edit_profile);
        mPhone = (TextInputEditText) findViewById(R.id.edt_phone_profile);
        mEmail = (TextInputEditText) findViewById(R.id.edt_email_profile);
        mLocation = (TextInputEditText) findViewById(R.id.edt_local_profile);
        mBirthday = (TextInputEditText) findViewById(R.id.edt_birthday_profile);
        mGender = (TextInputEditText) findViewById(R.id.edt_gender_profile);
        mEdu = (TextInputEditText) findViewById(R.id.edt_education_profile);
        mFavor = (TextInputEditText) findViewById(R.id.edt_favor_profile);
    }

    @Override
    public void initComponents() {
        setSupportActionBar(mToolbarPost);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void setEvents() {

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
                this.finish();
                return true;
            }
            case R.id.action_complete: {
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
        if (!Utils.isNotEmpty(mPhone)) {
            mPhone.setError("Mời bạn nhập số điện thoại");
            return false;
        }
        if (!Utils.isNotEmpty(mEmail)) {
            mEmail.setError("Mời bạn nhập email");
            return false;
        }
        if (!Utils.isNotEmpty(mLocation)) {
            mLocation.setError("Mời bạn nhập địa chỉ");
            return false;
        }
        if (!Utils.isNotEmpty(mBirthday)) {
            mBirthday.setError("Mời bạn nhập ngày sinh");
            return false;
        }
        if (!Utils.isNotEmpty(mGender)) {
            mGender.setError("Mời bạn nhập giới tính");
            return false;
        }
        if (!Utils.isNotEmpty(mEdu)) {
            mEdu.setError("Mời bạn nhập trình độ học vẫn");
            return false;
        }
        if (!Utils.isNotEmpty(mFavor)) {
            mFavor.setError("Mời bạn nhập sở thích");
            return false;
        }

        String phone = mPhone.getText().toString().trim();
        String email = mEmail.getText().toString().trim();
        String local = mLocation.getText().toString().trim();
        String birthday = mBirthday.getText().toString().trim();
        String gender = mGender.getText().toString().trim();
        String edu = mEdu.getText().toString().trim();
        String favor = mFavor.getText().toString().trim();

//        upload userInfor lên Firebase
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        UserInfor userInfor = new UserInfor(
                phone, email, local, birthday, gender, edu, favor, UserInstance.getInstance().getKey());
        reference.child(PERSONAL)
                .child(UserInstance.getInstance().getKey())
                .setValue(userInfor);
        return true;
    }
}
