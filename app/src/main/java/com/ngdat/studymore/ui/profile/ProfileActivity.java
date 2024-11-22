package com.ngdat.studymore.ui.profile;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.ngdat.studymore.R;
import com.ngdat.studymore.adapter.FragmentAdapter;
import com.ngdat.studymore.base.activity.BaseActivity;
import com.ngdat.studymore.common.Constants;
import com.ngdat.studymore.models.user.UserInstance;
import com.ngdat.studymore.util.Utils;

public class ProfileActivity extends BaseActivity
        implements Constants {

    private static final String TAG = ProfileActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private SimpleDraweeView avatarFace;

    @Override
    public int getLayout() {
        return R.layout.activity_profile;
    }

    @Override
    public void inflateComponents() {
        Fresco.initialize(this);
    }

    @Override
    public void findViewByIds() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        avatarFace = (SimpleDraweeView) findViewById(R.id.img_avatar);
    }

    @Override
    public void initComponents() {
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setupComponents();
        initViewPager();
    }

    private void setupComponents() {
        mCollapsingToolbar.setTitle(UserInstance.getInstance().getName());
        if (UserInstance.getInstance().getKey().equals(UserInstance.getInstance().getUid())) {
            int t = (int) UserInstance.getInstance().getName().charAt(0);
            avatarFace.setImageResource(Utils.setImgBackground(t));
        } else {
            avatarFace.setImageURI(Utils.getLinkAvatar(
                    UserInstance.getInstance().getUid(),
                    WIDTH_SCREEN * 2,
                    (int) Utils.convertDpToPixel(256) * 2));
        }
    }

    private void initViewPager() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public void setEvents() {

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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new TimelineFragment(), "TIMELINE");
        adapter.addFragment(new PersonalFragment(), "PERSONAL");
        viewPager.setAdapter(adapter);
    }
}