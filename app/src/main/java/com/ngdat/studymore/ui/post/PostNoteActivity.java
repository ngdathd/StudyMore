package com.ngdat.studymore.ui.post;

import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ngdat.studymore.R;
import com.ngdat.studymore.base.activity.BaseActivity;
import com.ngdat.studymore.common.Constants;
import com.ngdat.studymore.models.item.ItemNote;
import com.ngdat.studymore.models.user.UserInstance;
import com.ngdat.studymore.util.Utils;

import java.util.Calendar;

public class PostNoteActivity extends BaseActivity
        implements Constants {

    private Toolbar mToolbarNote;
    private TextInputEditText mTitle;
    private TextInputEditText mDes;

    @Override
    public int getLayout() {
        return R.layout.activity_note;
    }

    @Override
    public void inflateComponents() {

    }

    @Override
    public void findViewByIds() {
        mToolbarNote = (Toolbar) findViewById(R.id.toolbar_note);
        mTitle = (TextInputEditText) findViewById(R.id.edt_title);
        mDes = (TextInputEditText) findViewById(R.id.edt_des);
    }

    @Override
    public void initComponents() {
        setSupportActionBar(mToolbarNote);

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
        if (!Utils.isNotEmpty(mTitle)) {
            mTitle.setError("Mời bạn nhập tiêu đề");
            return false;
        }
        if (!Utils.isNotEmpty(mDes)) {
            mDes.setError("Mời bạn mô tả bài viết");
            return false;
        }

        String title = mTitle.getText().toString().trim();
        String des = mDes.getText().toString().trim();

//        upload itemPost lên Firebase
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        String postId = reference.child(NOTES).push().getKey();
        ItemNote itemPost = new ItemNote(title, des, postId,
                Calendar.getInstance().get(Calendar.DAY_OF_YEAR),
                Calendar.getInstance().get(Calendar.YEAR));
        reference.child(NOTES)
                .child(UserInstance.getInstance().getKey())
                .child(postId).setValue(itemPost);
        return true;
    }
}
