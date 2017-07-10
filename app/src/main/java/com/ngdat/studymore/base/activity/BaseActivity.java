package com.ngdat.studymore.base.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ngdat.studymore.base.IViewMain;
import com.ngdat.studymore.base.fragment.BaseFragment;

public abstract class BaseActivity extends AppCompatActivity
        implements IViewMain {

    private ProgressDialog mProgressDialog;
    protected boolean mIsDestroy;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        mIsDestroy = false;

        inflateComponents();
        findViewByIds();
        initComponents();
        setEvents();
    }

    @Override
    public void onBackPressed() {
        onBackRoot();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        mIsDestroy = true;
        super.onDestroy();
    }

    public final void onBackMain() {
        super.onBackPressed();
    }

    //  implements IViewMain
    @Override
    public void showProgressDialog(String message) {
        if (mIsDestroy) {
            return;
        }
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(message);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.show();
    }

    //  implements IViewMain
    @Override
    public void hideProgressDialog() {
        if (mIsDestroy) {
            return;
        }
        if (mProgressDialog == null) {
            return;
        }
        mProgressDialog.hide();
    }

    //  implements IViewMain
    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    //  implements IViewMain
    @Override
    public void showMessage(@StringRes int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    //  implements IViewMain
    @Override
    public void onBackRoot() {
        BaseFragment baseFragment = BaseFragment.getCurrentFragment(getSupportFragmentManager());
        if (null != baseFragment) {
            baseFragment.onBackRoot();
        }
    }

    public boolean isDestroyActivity() {
        return mIsDestroy;
    }
}