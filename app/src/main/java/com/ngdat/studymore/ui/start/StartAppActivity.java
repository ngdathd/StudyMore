package com.ngdat.studymore.ui.start;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.ngdat.studymore.R;
import com.ngdat.studymore.base.activity.BaseActivity;
import com.ngdat.studymore.base.animation.ScreenAnimation;
import com.ngdat.studymore.base.fragment.BaseFragment;
import com.ngdat.studymore.ui.start.intro.IntroActivity;
import com.ngdat.studymore.util.Utils;

public class StartAppActivity extends BaseActivity
        implements View.OnClickListener {

    private SharedPreferences prefs = null;

    private ImageView mImgBg;

    @Override
    public int getLayout() {
        return R.layout.activity_start_app;
    }

    @Override
    public void inflateComponents() {
        prefs = getSharedPreferences("com.ngdat.studymore", MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (prefs.getBoolean("firstrun", true)) {
            Intent intent = new Intent(StartAppActivity.this, IntroActivity.class);
            startActivity(intent);
            prefs.edit().putBoolean("firstrun", false).apply();
        }
    }

    @Override
    public void findViewByIds() {
        mImgBg = (ImageView) findViewById(R.id.img_bg);
    }

    @Override
    public void initComponents() {
        mImgBg.setImageBitmap(Utils.getLargeBitmap(getResources(), R.drawable.img_background));
        findViewById(R.id.txt_hello).setVisibility(View.INVISIBLE);
        FragmentManager manager = getSupportFragmentManager();
        BaseFragment.openFragment(
                manager,
                manager.beginTransaction(),
                LoginFragment.class,
                ScreenAnimation.NONE, null, true, true);
    }

    @Override
    public void setEvents() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(LoginFragment.class.getName());
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        onBackMain();
    }
}