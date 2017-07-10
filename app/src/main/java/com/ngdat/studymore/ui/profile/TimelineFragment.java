package com.ngdat.studymore.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ngdat.studymore.R;
import com.ngdat.studymore.adapter.ItemPostAdapter;
import com.ngdat.studymore.adapter.TimelinePostAdapter;
import com.ngdat.studymore.base.fragment.BaseFragment;
import com.ngdat.studymore.common.Constants;
import com.ngdat.studymore.models.item.ItemPost;
import com.ngdat.studymore.models.user.UserInstance;
import com.ngdat.studymore.ui.post.PostDetailActivity;

public class TimelineFragment extends BaseFragment
        implements Constants, TimelinePostAdapter.IPostAdapter {

    private TimelinePostAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new TimelinePostAdapter(getContext(), this);

//        Load các itemPost có trong "POSTS"
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(POSTS);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ItemPost itemPost = dataSnapshot.getValue(ItemPost.class);
                if (UserInstance.getInstance().getUid().equals(itemPost.getUserID())) {
                    mAdapter.add(itemPost);
                }
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

    @Override
    public int getLayout() {
        return R.layout.fragment_timeline;
    }

    @Override
    public void inflateComponents() {

    }

    @Override
    public void findViewByIds() {

    }

    @Override
    public void initComponents() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(false);

        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.rc_timeline);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void setEvents() {

    }

    @Override
    public int getIdLayout() {
        return R.id.activity_profile;
    }

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
}