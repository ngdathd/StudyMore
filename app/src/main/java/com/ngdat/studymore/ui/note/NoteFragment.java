package com.ngdat.studymore.ui.note;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ngdat.studymore.R;
import com.ngdat.studymore.adapter.NotePostAdapter;
import com.ngdat.studymore.base.fragment.BaseFragment;
import com.ngdat.studymore.common.Constants;
import com.ngdat.studymore.models.item.ItemNote;
import com.ngdat.studymore.models.user.UserInstance;
import com.ngdat.studymore.ui.post.PostNoteActivity;
import com.ngdat.studymore.util.Utils;

public class NoteFragment extends BaseFragment
        implements View.OnClickListener, Constants, ChildEventListener, NotePostAdapter.IPostAdapter {

    private static final String TAG = NoteFragment.class.getSimpleName();

    private FloatingActionButton mFabAdd;

    private DatabaseReference databaseReference;

    private NotePostAdapter mAdapter;
    private RelativeLayout mRlSearch2;
    private ImageButton mBtnChoose2;
    private ImageButton mBtnClean2;
    private EditText mEdtSearch2;
    private String key;
    private ItemNote itemNote;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new NotePostAdapter(getContext(), this);

        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(NOTES)
                .child(UserInstance.getInstance().getKey());
        databaseReference.addChildEventListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_note;
    }

    @Override
    public void inflateComponents() {

    }

    @Override
    public void findViewByIds() {
        mFabAdd = (FloatingActionButton) getView().findViewById(R.id.fab_add);
        mRlSearch2 = (RelativeLayout) getView().findViewById(R.id.rl_search1);
        mBtnChoose2 = (ImageButton) getView().findViewById(R.id.btn_choose1);
        mBtnClean2 = (ImageButton) getView().findViewById(R.id.btn_clean1);
        mEdtSearch2 = (EditText) getView().findViewById(R.id.edt_search1);
    }

    @Override
    public void initComponents() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(false);

        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.rc_note);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }


    @Override
    public void setEvents() {
        mFabAdd.setOnClickListener(this);
        mBtnChoose2.setOnClickListener(this);
        mBtnClean2.setOnClickListener(this);
    }

    @Override
    public int getIdLayout() {
        return R.id.content_main;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.fab_add: {
                Intent intent = new Intent(this.getBaseActivity(), PostNoteActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.btn_clean1: {
                View view = getBaseActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getBaseActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                mRlSearch2.setVisibility(View.GONE);
                mAdapter.getmItemPosts().clear();
                databaseReference.addChildEventListener(this);
            }
            break;
            case R.id.btn_choose1: {
                View view = getBaseActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getBaseActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                key = mEdtSearch2.getText().toString().trim();
                mAdapter.getmItemPosts().clear();
                databaseReference = FirebaseDatabase.getInstance().getReference().child(NOTES).child(UserInstance.getInstance().getKey());
                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        ItemNote itemPost = dataSnapshot.getValue(ItemNote.class);
                        if ((itemPost.getTitlePost().contains(key) || itemPost.getDes().contains(key))) {
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
                        ItemNote itemPost = dataSnapshot.getValue(ItemNote.class);
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
                        ItemNote itemPost = dataSnapshot.getValue(ItemNote.class);
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
                        ItemNote itemPost = dataSnapshot.getValue(ItemNote.class);
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
                mRlSearch2.setVisibility(View.VISIBLE);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClickItem(int position) {

    }

    //    ChildEventListener
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        itemNote = dataSnapshot.getValue(ItemNote.class);
        mAdapter.add(itemNote);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        itemNote = dataSnapshot.getValue(ItemNote.class);
        boolean b = mAdapter.remove(itemNote);
        mAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Đã xóa ghi chú", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onChildRemoved: " + mAdapter.getmItemPosts().size() + b);
//        databaseReference.addChildEventListener(this);
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
