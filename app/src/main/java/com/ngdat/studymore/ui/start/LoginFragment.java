package com.ngdat.studymore.ui.start;

import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentManager;

import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ngdat.studymore.R;
import com.ngdat.studymore.base.animation.ScreenAnimation;
import com.ngdat.studymore.base.fragment.BaseFragment;
import com.ngdat.studymore.common.Constants;
import com.ngdat.studymore.models.user.User;
import com.ngdat.studymore.models.user.UserInstance;
import com.ngdat.studymore.service.EmailLoginListener;
import com.ngdat.studymore.service.EmailLoginServices;
import com.ngdat.studymore.service.FacebookLoginServices;
import com.ngdat.studymore.ui.main.MainActivity;
import com.ngdat.studymore.util.Utils;

public class LoginFragment extends BaseFragment
        implements View.OnClickListener, Constants, Animation.AnimationListener {

    private AppCompatButton mBtnLoginEmail;
    private AppCompatButton mBtnLoginFb;
    private AppCompatButton mBtnRegister;
    private AppCompatButton mBtnForgetPass;
    private TextInputEditText mEdtEmail;
    private TextInputEditText mEdtPass;

    private EmailLoginServices emailLoginServices;
    private FacebookLoginServices facebookLoginServices;
    private CallbackManager callbackManager;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String email;
    private String passWord;

    private boolean isFinishLoadData;
    private boolean isFinishAnimation;

    @Override
    public int getLayout() {
        return R.layout.fragment_start_login;
    }

    @Override
    public void inflateComponents() {

    }

    @Override
    public void findViewByIds() {
        mBtnLoginEmail = (AppCompatButton) getView().findViewById(R.id.btn_email_login);
        mBtnLoginFb = (AppCompatButton) getView().findViewById(R.id.btn_fb_custom_login_button);
        mBtnRegister = (AppCompatButton) getView().findViewById(R.id.btn_register);
        mBtnForgetPass = (AppCompatButton) getView().findViewById(R.id.btn_forget_pass);
        mEdtEmail = (TextInputEditText) getView().findViewById(R.id.edt_email);
        mEdtPass = (TextInputEditText) getView().findViewById(R.id.edt_pass);
    }

    @Override
    public void initComponents() {
        emailLoginServices = new EmailLoginServices();
        facebookLoginServices = new FacebookLoginServices();
        callbackManager = CallbackManager.Factory.create();

        init();
    }

    private void init() {
        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (null != firebaseUser) {
                    getBaseActivity().findViewById(R.id.fragment_start_login).setVisibility(View.INVISIBLE);
                    getBaseActivity().findViewById(R.id.txt_hello).startAnimation(
                            AnimationUtils.loadAnimation(getBaseActivity(), R.anim.anim_txt));

                    Animation animation = AnimationUtils.loadAnimation(getBaseActivity(), R.anim.anim_img);
                    animation.setAnimationListener(LoginFragment.this);
                    getBaseActivity().findViewById(R.id.img_logo).startAnimation(
                            animation);
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    reference.child(USERS)
                            .child(firebaseUser.getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User user = dataSnapshot.getValue(User.class);
                                    if (user != null) {
                                        UserInstance.getInstance().setName(user.getName());
                                        UserInstance.getInstance().setEmail(user.getEmail());
                                        UserInstance.getInstance().setUid(user.getUid());
                                        UserInstance.getInstance().setKey(firebaseUser.getUid());
                                        LoginManager.getInstance().unregisterCallback(callbackManager);
                                        isFinishLoadData = true;
                                        if (isFinishAnimation) {
                                            startActivity(new Intent(getBaseActivity(), MainActivity.class));
                                            getActivity().finish();
                                        }
                                    } else {
                                        isFinishLoadData = true;
                                        if (isFinishAnimation) {
                                            LoginManager.getInstance().logOut();
                                            FirebaseAuth.getInstance().signOut();
                                            startActivity(new Intent(getBaseActivity(), StartAppActivity.class));
                                            getActivity().finish();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                }
            }
        };
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        isFinishAnimation = true;
        if (isFinishLoadData) {
            startActivity(new Intent(getBaseActivity(), MainActivity.class));
            getActivity().finish();
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            auth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setEvents() {
        mBtnLoginEmail.setOnClickListener(this);
        mBtnLoginFb.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
        mBtnForgetPass.setOnClickListener(this);
    }

    @Override
    public int getIdLayout() {
        return R.id.activity_start_app;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_email_login: {
                if (checkInputData()) {
                    getBaseActivity().findViewById(R.id.txt_hello).startAnimation(
                            AnimationUtils.loadAnimation(getBaseActivity(), R.anim.anim_txt));
                    getBaseActivity().findViewById(R.id.img_logo).startAnimation(
                            AnimationUtils.loadAnimation(getBaseActivity(), R.anim.anim_img));
                    getBaseActivity().findViewById(R.id.fragment_start_login).setVisibility(View.INVISIBLE);

                    emailLoginServices.loginAccountEmail(email, passWord, new EmailLoginListener() {
                        @Override
                        public void loginSuccess() {

                        }

                        @Override
                        public void loginFailure(String message) {
                            getBaseActivity().findViewById(R.id.fragment_start_login).setVisibility(View.VISIBLE);
                            getBaseActivity().findViewById(R.id.img_logo).clearAnimation();
                            getBaseActivity().findViewById(R.id.txt_hello).clearAnimation();
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            break;
            case R.id.btn_fb_custom_login_button: {
                facebookLoginServices.loginAccountFacebook(this.getBaseActivity(), callbackManager);
            }
            break;
            case R.id.btn_register: {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                BaseFragment.openFragment(
                        manager,
                        manager.beginTransaction(),
                        RegisterFragment.class,
                        ScreenAnimation.OPEN_FULL, null, true, true);
            }
            break;
            case R.id.btn_forget_pass: {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                BaseFragment.openFragment(
                        manager,
                        manager.beginTransaction(),
                        RegisterFragment.class,
                        ScreenAnimation.OPEN_FULL, null, true, true);
            }
            break;
            default:
                break;
        }
    }

    private boolean checkInputData() {
        if (Utils.isNotEmpty(mEdtEmail) && Utils.isNotEmpty(mEdtPass)) {
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
                }
            }
            return true;
        } else {
            return false;
        }
    }
}