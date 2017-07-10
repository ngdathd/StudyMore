package com.ngdat.studymore.ui.post;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ngdat.studymore.R;
import com.ngdat.studymore.base.activity.BaseActivity;
import com.ngdat.studymore.common.Constants;
import com.ngdat.studymore.util.Utils;

public class PostDetailActivity extends BaseActivity
        implements View.OnClickListener, Constants {

    private static final String TAG = PostDetailActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private SimpleDraweeView avatarFace;
    private TextView txtTitle;
    private TextView txtPhone;
    private TextView txtLocal;
    private TextView txtFee;
    private TextView txtDes;

    private Intent receiverIntent;

    @Override
    public int getLayout() {
        return R.layout.activity_post_detail;
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
        txtTitle = (TextView) findViewById(R.id.txt_title1);
        txtPhone = (TextView) findViewById(R.id.txt_phone_number);
        txtLocal = (TextView) findViewById(R.id.txt_location);
        txtFee = (TextView) findViewById(R.id.txt_fee);
        txtDes = (TextView) findViewById(R.id.content_post_detail);
    }

    @Override
    public void initComponents() {
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setupComponents();
    }

    private void setupComponents() {
        receiverIntent = getIntent();
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

        txtTitle.setText(receiverIntent.getStringExtra(KEY_TITLE));
        txtPhone.setText(receiverIntent.getStringExtra(KEY_PHONE));
        txtLocal.setText(receiverIntent.getStringExtra(KEY_LOCAL));
        if (receiverIntent.getStringExtra(KEY_FEE).equals("")) {
            findViewById(R.id.rl_fee).setVisibility(View.GONE);
            findViewById(R.id.txt_line).setVisibility(View.GONE);
        } else {
            txtFee.setText(receiverIntent.getStringExtra(KEY_FEE));
        }
        txtDes.setText(receiverIntent.getStringExtra(KEY_DES));
    }

    @Override
    public void setEvents() {
        avatarFace.setOnClickListener(this);
        findViewById(R.id.img_phone).setOnClickListener(this);
        findViewById(R.id.img_message).setOnClickListener(this);
        findViewById(R.id.img_directions).setOnClickListener(this);
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
            case R.id.img_post_detail: {
                Intent intent = new Intent(PostDetailActivity.this, UserDetailActivity.class);
                intent.putExtra(KEY_USER_ID, receiverIntent.getStringExtra(KEY_USER_ID));
                intent.putExtra(KEY_USER_KEY, receiverIntent.getStringExtra(KEY_USER_KEY));
                intent.putExtra(KEY_USER_NAME, receiverIntent.getStringExtra(KEY_USER_NAME));
                Log.i(TAG, "onClick: " + receiverIntent.getStringExtra(KEY_USER_NAME));
                startActivity(intent);
            }
            break;
            case R.id.img_phone: {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + txtPhone.getText().toString()));
                startActivity(intent);
            }
            break;
            case R.id.img_message: {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("sms:" + txtPhone.getText().toString()));
                startActivity(intent);
            }
            break;
            case R.id.img_directions: {
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
