package com.ngdat.studymore.ui.main;

import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.ngdat.studymore.R;
import com.ngdat.studymore.base.activity.BaseActivity;
import com.ngdat.studymore.common.Constants;
import com.ngdat.studymore.models.user.UserInstance;
import com.ngdat.studymore.ui.bookmark.BookmarkFragment;
import com.ngdat.studymore.ui.note.NoteFragment;
import com.ngdat.studymore.ui.primary.PrimaryFragment;
import com.ngdat.studymore.ui.profile.ProfileActivity;
import com.ngdat.studymore.ui.start.StartAppActivity;
import com.ngdat.studymore.util.Utils;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, Constants {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbarMain;
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mToggle;
    private View mHeaderView;

    private SimpleDraweeView avatarUser;
    private TextView nameUser;
    private TextView emailUser;
    private MenuItem menuItem;

    private PrimaryFragment primaryFragment;
    private BookmarkFragment bookmarkFragment;
    private NoteFragment noteFragment;

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void inflateComponents() {
        Fresco.initialize(this);
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
    }

    @Override
    public void findViewByIds() {
        mToolbarMain = (Toolbar) findViewById(R.id.toolbar_main);
        mDrawer = (DrawerLayout) findViewById(R.id.activity_main);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        avatarUser = (SimpleDraweeView) mHeaderView.findViewById(R.id.img_avatar_circle);
        nameUser = (TextView) mHeaderView.findViewById(R.id.txt_name);
        emailUser = (TextView) mHeaderView.findViewById(R.id.txt_email);

        Menu menu = mNavigationView.getMenu();
        menuItem = menu.findItem(R.id.nav_home);
    }

    @Override
    public void initComponents() {
        setSupportActionBar(mToolbarMain);

        mToggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbarMain,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mToggle.syncState();

        mNavigationView.addHeaderView(mHeaderView);

        setupComponents();

        primaryFragment = new PrimaryFragment();
        bookmarkFragment = new BookmarkFragment();
        noteFragment = new NoteFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content_main, noteFragment)
                .hide(noteFragment)
                .add(R.id.content_main, bookmarkFragment)
                .hide(bookmarkFragment)
                .add(R.id.content_main, primaryFragment)
                .commit();
        Toast.makeText(this, getString(R.string.msg_login_success), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setEvents() {
        mDrawer.addDrawerListener(mToggle);
        mNavigationView.setNavigationItemSelectedListener(this);
        avatarUser.setOnClickListener(this);

        Typeface tf = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Medium.ttf");
        nameUser.setTypeface(tf);
        tf = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Regular.ttf");
        emailUser.setTypeface(tf);
    }

    private void setupComponents() {
        nameUser.setText(UserInstance.getInstance().getName());
        emailUser.setText(UserInstance.getInstance().getEmail());
        menuItem.setTitle(UserInstance.getInstance().getName());
        if (UserInstance.getInstance().getKey().equals(UserInstance.getInstance().getUid())) {
            int t = (int) UserInstance.getInstance().getName().charAt(0);
            avatarUser.setImageResource(Utils.setImgAvatar(t));
        } else {
            avatarUser.setImageURI(Utils.getLinkAvatar(
                    UserInstance.getInstance().getUid(),
                    (int) Utils.convertDpToPixel(72) * 2,
                    (int) Utils.convertDpToPixel(72) * 2));
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_primary: {
                mToolbarMain.setTitle("Trang chủ");

                if (primaryFragment.isVisible()) {
                    break;
                }
                getSupportFragmentManager().beginTransaction()
                        .hide(noteFragment)
                        .hide(bookmarkFragment)
                        .show(primaryFragment)
                        .commit();
            }
            break;
            case R.id.nav_home: {
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.nav_bookmark: {
                mToolbarMain.setTitle("Bài viết đánh dấu");

                if (bookmarkFragment.isVisible()) {
                    break;
                }
                getSupportFragmentManager().beginTransaction()
                        .hide(noteFragment)
                        .hide(primaryFragment)
                        .show(bookmarkFragment)
                        .commit();
            }
            break;
            case R.id.nav_note: {
                mToolbarMain.setTitle("Ghi chú");

                if (noteFragment.isVisible()) {
                    break;
                }
                getSupportFragmentManager().beginTransaction()
                        .hide(bookmarkFragment)
                        .hide(primaryFragment)
                        .show(noteFragment)
                        .commit();

            }
            break;
            case R.id.nav_logout: {
                Log.i(TAG, "onNavigationItemSelected: nav_logout");
                LoginManager.getInstance().logOut();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, StartAppActivity.class));
                finish();
            }
            break;
            default:
                break;
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.img_avatar_circle: {
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
            }
            break;
            default:
                break;
        }
    }
}