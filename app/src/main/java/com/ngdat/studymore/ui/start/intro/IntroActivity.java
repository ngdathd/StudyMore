package com.ngdat.studymore.ui.start.intro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.ngdat.studymore.R;

public class IntroActivity extends AppIntro {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(SampleSlide.newInstance(R.layout.fragment_intro1, R.id.img_intro1));
        addSlide(SampleSlide.newInstance(R.layout.fragment_intro2, R.id.img_intro2));
        addSlide(SampleSlide.newInstance(R.layout.fragment_intro3, R.id.img_intro3));
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }
}