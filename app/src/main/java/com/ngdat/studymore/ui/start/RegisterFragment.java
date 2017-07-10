package com.ngdat.studymore.ui.start;

import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ngdat.studymore.R;
import com.ngdat.studymore.base.fragment.BaseFragment;
import com.ngdat.studymore.service.EmailRegisterListener;
import com.ngdat.studymore.service.EmailRegisterServices;
import com.ngdat.studymore.util.Utils;

public class RegisterFragment extends BaseFragment
        implements View.OnClickListener {

    private static final String TAG = RegisterFragment.class.getSimpleName();

    private AppCompatButton mBtnRegister;
    private AppCompatButton mBtnBack;
    private TextInputEditText mEdtEmail;
    private TextInputEditText mEdtPass;
    private TextInputEditText mEdtConfirmPass;

    private String email;
    private String passWord;
    private EmailRegisterServices emailRegisterServices;

    @Override
    public int getLayout() {
        return R.layout.fragment_start_register;
    }

    @Override
    public void inflateComponents() {

    }

    @Override
    public void findViewByIds() {
        mBtnRegister = (AppCompatButton) getView().findViewById(R.id.btn_email_register);
        mBtnBack = (AppCompatButton) getView().findViewById(R.id.btn_back_register);
        mEdtEmail = (TextInputEditText) getView().findViewById(R.id.edt_email_register);
        mEdtPass = (TextInputEditText) getView().findViewById(R.id.edt_pass_register);
        mEdtConfirmPass = (TextInputEditText) getView().findViewById(R.id.edt_pass_register1);
    }

    @Override
    public void initComponents() {
        emailRegisterServices = new EmailRegisterServices(this.getBaseActivity());
    }

    @Override
    public void setEvents() {
        mBtnRegister.setOnClickListener(this);
        mBtnBack.setOnClickListener(this);
    }

    @Override
    public int getIdLayout() {
        return R.id.activity_start_app;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_email_register: {
                Log.i(TAG, "onClick: btn_email_register");
                if (checkInputData()) {
                    showProgressDialog(getResources().getString(R.string.loading));
                    emailRegisterServices.registerAccount(email, passWord, new EmailRegisterListener() {
                        @Override
                        public void registerSuccess() {
                            hideProgressDialog();
                            mEdtEmail.setText("");
                            mEdtPass.setText("");
                            mEdtConfirmPass.setText("");
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle(getResources().getString(R.string.notification));
                            builder.setMessage(getResources().getString(R.string.verifiation));
                            builder.setIcon(R.mipmap.ic_launcher);
                            builder.create().show();
                        }

                        @Override
                        public void registerFailure(String message) {
                            hideProgressDialog();
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            break;
            case R.id.btn_back_register: {
                Log.i(TAG, "onClick: btn_back_register");
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.popBackStack();
            }
            break;
            default:
                break;
        }
    }

    private boolean checkInputData() {
        if (Utils.isNotEmpty(mEdtEmail) && Utils.isNotEmpty(mEdtPass) && Utils.isNotEmpty(mEdtConfirmPass)) {
            email = mEdtEmail.getText().toString().trim();
            passWord = mEdtPass.getText().toString().trim();
            if (!Utils.isEmailValid(email)) {
                mEdtEmail.requestFocus();
                mEdtEmail.setError(getResources().getString(R.string.email_error));
                return false;
            } else {
                if (passWord.length() < 6) {
                    mEdtPass.requestFocus();
                    mEdtPass.setError(getResources().getString(R.string.pass_error));
                    return false;
                } else {
                    if (!mEdtConfirmPass.getText().toString().trim().equals(passWord)) {
                        mEdtConfirmPass.requestFocus();
                        mEdtConfirmPass.setError(getResources().getString(R.string.passconfirm_error));
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }
}