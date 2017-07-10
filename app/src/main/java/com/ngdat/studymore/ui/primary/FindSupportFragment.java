package com.ngdat.studymore.ui.primary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ngdat.studymore.R;
import com.ngdat.studymore.adapter.ItemPostAdapter;
import com.ngdat.studymore.base.fragment.BaseFragment;
import com.ngdat.studymore.common.Constants;
import com.ngdat.studymore.common.Interactor;
import com.ngdat.studymore.models.item.ItemPost;
import com.ngdat.studymore.ui.post.PostActivity;
import com.ngdat.studymore.ui.post.PostDetailActivity;
import com.ngdat.studymore.util.Action1;
import com.ngdat.studymore.util.Utils;

public class FindSupportFragment extends BaseFragment
        implements View.OnClickListener, ItemPostAdapter.IPostAdapter, Constants, Action1<ItemPost>, ValueEventListener, ChildEventListener {

    private DatabaseReference databaseReference;

    private ItemPostAdapter mAdapter;
    private FloatingActionButton mfab2;
    private RelativeLayout mRlSearch2;
    private ImageButton mBtnChoose2;
    private ImageButton mBtnClean2;
    private EditText mEdtSearch2;
    private String key;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ItemPostAdapter(getContext(), this);

//        Load các itemPost có trong "POSTS"
        databaseReference = FirebaseDatabase.getInstance().getReference().child(POSTS);
//        databaseReference.addValueEventListener(this);
        databaseReference.addChildEventListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_find_support;
    }

    @Override
    public void inflateComponents() {

    }

    @Override
    public void findViewByIds() {
        mfab2 = (FloatingActionButton) getView().findViewById(R.id.fab_create_find_support);
        mRlSearch2 = (RelativeLayout) getView().findViewById(R.id.rl_search2);
        mBtnChoose2 = (ImageButton) getView().findViewById(R.id.btn_choose2);
        mBtnClean2 = (ImageButton) getView().findViewById(R.id.btn_clean2);
        mEdtSearch2 = (EditText) getView().findViewById(R.id.edt_search2);
    }

    @Override
    public void initComponents() {
        initRecyclerView();
        Interactor.getInstance().registerActionRemoveItemPost(this);
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(false);

        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.rc_post_find_support);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void setEvents() {
        mfab2.setOnClickListener(this);
        mBtnChoose2.setOnClickListener(this);
        mBtnClean2.setOnClickListener(this);
    }

    @Override
    public int getIdLayout() {
        return 0;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.fab_create_find_support: {
                Intent intent = new Intent(this.getBaseActivity(), PostActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.btn_clean2: {
                View view = getBaseActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getBaseActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                mRlSearch2.setVisibility(View.GONE);
                mAdapter.getmItemPosts().clear();
//                databaseReference.addValueEventListener(this);
                databaseReference.addChildEventListener(this);
            }
            break;
            case R.id.btn_choose2: {
                View view = getBaseActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getBaseActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                key = mEdtSearch2.getText().toString().trim();
                mAdapter.getmItemPosts().clear();
                databaseReference = FirebaseDatabase.getInstance().getReference().child(POSTS);
                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        ItemPost itemPost = dataSnapshot.getValue(ItemPost.class);
                        if (!itemPost.getFee().equals("") &&
                                (itemPost.getUserName().contains(key)
                                        || itemPost.getLocation().contains(key)
                                        || itemPost.getPhoneNumber().contains(key)
                                        || itemPost.getTitlePost().contains(key)
                                        || itemPost.getDes().contains(key))) {
                            mAdapter.add(itemPost);
                        }
                        mAdapter.initCheckBookMark();
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
                databaseReference = FirebaseDatabase.getInstance().getReference().child(POSTS);
                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        ItemPost itemPost = dataSnapshot.getValue(ItemPost.class);
                        if (!itemPost.getFee().equals("") &&
                                (Utils.checkToday(itemPost.getDay(), itemPost.getYear()))) {
                            mAdapter.add(itemPost);
                        }
                        mAdapter.initCheckBookMark();
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
                databaseReference = FirebaseDatabase.getInstance().getReference().child(POSTS);
                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        ItemPost itemPost = dataSnapshot.getValue(ItemPost.class);
                        if (!itemPost.getFee().equals("") &&
                                (Utils.checkYesterday(itemPost.getDay(), itemPost.getYear()))) {
                            mAdapter.add(itemPost);
                        }
                        mAdapter.initCheckBookMark();
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
                databaseReference = FirebaseDatabase.getInstance().getReference().child(POSTS);
                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        ItemPost itemPost = dataSnapshot.getValue(ItemPost.class);
                        if (!itemPost.getFee().equals("") &&
                                (Utils.checkLastWeek(itemPost.getDay(), itemPost.getYear()))) {
                            mAdapter.add(itemPost);
                        }
                        mAdapter.initCheckBookMark();
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
//                databaseReference.addValueEventListener(this);
                databaseReference.addChildEventListener(this);
                return true;
            }
            case R.id.action_search: {
                mRlSearch2.setVisibility(View.VISIBLE);
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

    //    ChildEventListener
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        ItemPost itemPost = dataSnapshot.getValue(ItemPost.class);
        if (!itemPost.getFee().equals("")) {
            mAdapter.add(itemPost);
        }
        mAdapter.initCheckBookMark();
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

    //    ValueEventListener
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot d :
                dataSnapshot.getChildren()) {
            ItemPost itemPost = d.getValue(ItemPost.class);
            if (!itemPost.getFee().equals("")) {
                mAdapter.add(itemPost);
            }
        }
        mAdapter.initCheckBookMark();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onDestroyView() {
        Interactor.getInstance().unregisterActionRemoveItemPost(this);
        super.onDestroyView();
    }

    @Override
    public void call(ItemPost itemPost) {
        int index = 0;
        for (ItemPost post : mAdapter.getmItemPosts()) {
            if (post.getPostID().equals(itemPost.getPostID())) {
                post.setCheck(false);
                mAdapter.notifyItemChanged(index);
                return;
            }
            index++;
        }
    }
}
