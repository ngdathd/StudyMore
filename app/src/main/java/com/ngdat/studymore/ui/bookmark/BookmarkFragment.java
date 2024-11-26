package com.ngdat.studymore.ui.bookmark;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ngdat.studymore.R;
import com.ngdat.studymore.adapter.BookmarkPostAdapter;
import com.ngdat.studymore.base.fragment.BaseFragment;
import com.ngdat.studymore.common.Constants;
import com.ngdat.studymore.common.Interactor;
import com.ngdat.studymore.models.item.ItemPost;
import com.ngdat.studymore.models.user.UserInstance;
import com.ngdat.studymore.ui.post.PostDetailActivity;
import com.ngdat.studymore.util.Action1;
import com.ngdat.studymore.util.Utils;

import java.util.List;

public class BookmarkFragment extends BaseFragment
        implements View.OnClickListener, Constants, ChildEventListener, BookmarkPostAdapter.IPostAdapter, Action1<ItemPost> {

    private static final String TAG = BookmarkFragment.class.getSimpleName();

    private DatabaseReference databaseReference;

    private BookmarkPostAdapter mAdapter;
    private RelativeLayout mRlSearch3;
    private ImageButton mBtnChoose3;
    private ImageButton mBtnClean3;
    private EditText mEdtSearch3;
    private String key;
    private ItemPost itemPost;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new BookmarkPostAdapter(getContext(), this);

        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(BOOKMARK)
                .child(UserInstance.getInstance().getKey());
        databaseReference.addChildEventListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_bookmark;
    }

    @Override
    public void inflateComponents() {

    }

    @Override
    public void findViewByIds() {
        mRlSearch3 = (RelativeLayout) getView().findViewById(R.id.rl_search3);
        mBtnChoose3 = (ImageButton) getView().findViewById(R.id.btn_choose3);
        mBtnClean3 = (ImageButton) getView().findViewById(R.id.btn_clean3);
        mEdtSearch3 = (EditText) getView().findViewById(R.id.edt_search3);
    }

    @Override
    public void initComponents() {
        initRecyclerView();
        Interactor.getInstance().registerActionRemoveItemPostFromFind(this);
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(false);

        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.rc_bookmark);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void setEvents() {
        mBtnChoose3.setOnClickListener(this);
        mBtnClean3.setOnClickListener(this);
    }

    @Override
    public int getIdLayout() {
        return R.id.content_main;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_clean3: {
                View view = getBaseActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getBaseActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                mRlSearch3.setVisibility(View.GONE);
                mAdapter.getmItemPosts().clear();
                databaseReference.addChildEventListener(this);
            }
            break;
            case R.id.btn_choose3: {
                View view = getBaseActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getBaseActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                key = mEdtSearch3.getText().toString().trim();
                mAdapter.getmItemPosts().clear();
                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        ItemPost itemPost = dataSnapshot.getValue(ItemPost.class);
                        if ((itemPost.getUserName().contains(key)
                                || itemPost.getLocation().contains(key)
                                || itemPost.getPhoneNumber().contains(key)
                                || itemPost.getTitlePost().contains(key)
                                || itemPost.getDes().contains(key))) {
                            mAdapter.add(itemPost);
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            break;
            default:
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_today: {
                mAdapter.getmItemPosts().clear();
                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        ItemPost itemPost = dataSnapshot.getValue(ItemPost.class);
                        if ((Utils.checkToday(itemPost.getDay(), itemPost.getYear()))) {
                            mAdapter.add(itemPost);
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                return true;
            }
            case R.id.action_yesterday: {
                mAdapter.getmItemPosts().clear();
                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        ItemPost itemPost = dataSnapshot.getValue(ItemPost.class);
                        if ((Utils.checkYesterday(itemPost.getDay(), itemPost.getYear()))) {
                            mAdapter.add(itemPost);
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                return true;
            }
            case R.id.action_week: {
                mAdapter.getmItemPosts().clear();
                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        ItemPost itemPost = dataSnapshot.getValue(ItemPost.class);
                        if ((Utils.checkLastWeek(itemPost.getDay(), itemPost.getYear()))) {
                            mAdapter.add(itemPost);
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                return true;
            }
            case R.id.action_all: {
                mAdapter.getmItemPosts().clear();
                databaseReference.addChildEventListener(this);
                return true;
            }
            case R.id.action_search: {
                mRlSearch3.setVisibility(View.VISIBLE);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //    ItemPostAdapter.IPostAdapter
    @Override
    public void onClickItem(int position) {
        ItemPost itemPost = mAdapter.get(position);
        Intent intent = new Intent(getContext(), PostDetailActivity.class);
        intent.putExtra(KEY_USER_ID, itemPost.getUserID());
        intent.putExtra(KEY_USER_KEY, itemPost.getUserKey());
        intent.putExtra(KEY_USER_NAME, itemPost.getUserName());
        intent.putExtra(KEY_LOCAL, itemPost.getLocation());
        intent.putExtra(KEY_PHONE, itemPost.getPhoneNumber());
        intent.putExtra(KEY_TITLE, itemPost.getTitlePost());
        intent.putExtra(KEY_DES, itemPost.getDes());
        intent.putExtra(KEY_FEE, itemPost.getFee());
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        Interactor.getInstance().unregisterActionRemoveItemPostFromFind(this);
        super.onDestroyView();
    }

    //    ChildEventListener
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        itemPost = dataSnapshot.getValue(ItemPost.class);
        mAdapter.add(itemPost);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        itemPost = dataSnapshot.getValue(ItemPost.class);
        boolean b = mAdapter.remove(itemPost);
        mAdapter.notifyDataSetChanged();
        if (getContext() != null) {
            Toast.makeText(getContext(), "Đã xóa đánh dấu bài viết", Toast.LENGTH_SHORT).show();
        }
        Log.i(TAG, "onChildRemoved: " + mAdapter.getmItemPosts().size() + b);
//        databaseReference.addChildEventListener(this);
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void call(ItemPost itemPost) {
        if ( mAdapter.getmItemPosts() == null) {
            return;
        }
        for ( int i = 0; i<mAdapter.getmItemPosts().size(); i++ ) {
            ItemPost post = mAdapter.getmItemPosts().get(i);
            if ( post.getPostID().equals(itemPost.getPostID())) {
                mAdapter.getmItemPosts().remove(i);
                mAdapter.notifyItemRemoved(i);
                break;
            }
        }

    }
}