package com.ngdat.studymore.ui.primary;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ngdat.studymore.R;
import com.ngdat.studymore.adapter.FragmentAdapter;
import com.ngdat.studymore.base.fragment.BaseFragment;

public class PrimaryFragment extends BaseFragment {

    @Override
    public int getLayout() {
        return R.layout.fragment_primary;
    }

    @Override
    public void inflateComponents() {

    }

    @Override
    public void findViewByIds() {

    }

    @Override
    public void initComponents() {
        initViewPager();
    }

    private void initViewPager() {
        ViewPager viewPager = (ViewPager) getView().findViewById(R.id.viewpager_primary);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                View view = getBaseActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getBaseActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) getView().findViewById(R.id.tablayout_primary);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private void setupViewPager(ViewPager viewPager) {
        FragmentAdapter adapter = new FragmentAdapter(getBaseActivity().getSupportFragmentManager());
        adapter.addFragment(new FindDiscussFragment(), "THẢO LUẬN");
        adapter.addFragment(new FindSupportFragment(), "TÌM HỖ TRỢ");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void setEvents() {

    }

    @Override
    public int getIdLayout() {
        return R.id.content_main;
    }
}