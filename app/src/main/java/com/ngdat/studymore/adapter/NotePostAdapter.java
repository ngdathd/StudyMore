package com.ngdat.studymore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ngdat.studymore.R;
import com.ngdat.studymore.common.Constants;
import com.ngdat.studymore.models.item.ItemNote;
import com.ngdat.studymore.models.user.UserInstance;

import java.util.ArrayList;
import java.util.List;

public class NotePostAdapter extends RecyclerView.Adapter
        implements View.OnClickListener, Constants {

    private LayoutInflater mInflater;
    private List<ItemNote> mItemPosts;
    private IPostAdapter mIPostAdapter;

    public NotePostAdapter(Context context, IPostAdapter mIPostAdapter) {
        this.mInflater = LayoutInflater.from(context);
        this.mItemPosts = new ArrayList<>();
        this.mIPostAdapter = mIPostAdapter;
    }

    public void add(ItemNote itemPost) {
        mItemPosts.add(0, itemPost);
        notifyDataSetChanged();
    }

    public boolean remove(ItemNote itemPost) {
        boolean b = mItemPosts.remove(itemPost);
        notifyDataSetChanged();
        return b;
    }

    public ItemNote get(int position) {
        return mItemPosts.get(position);
    }

    public List<ItemNote> getmItemPosts() {
        return mItemPosts;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_note, parent, false);
        return new ViewHolderPost(view, this);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (null == mItemPosts) {
            return;
        }
        final ItemNote itemPost = mItemPosts.get(position);
        final ViewHolderPost viewHolderPost = (ViewHolderPost) holder;

        viewHolderPost.txtTitle.setText(itemPost.getTitlePost());
        viewHolderPost.txtContent.setText(itemPost.getDes());
        viewHolderPost.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.child(NOTES)
                        .child(UserInstance.getInstance().getKey())
                        .child(itemPost.getId()).removeValue();
                mItemPosts.remove(itemPost);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (null == mItemPosts) {
            return 0;
        }
        return mItemPosts.size();
    }

    @Override
    public void onClick(View v) {
        IGetPosition getPosition = (IGetPosition) v.getTag();
        mIPostAdapter.onClickItem(getPosition.getPosition());
    }

    private class ViewHolderPost extends RecyclerView.ViewHolder {

        private TextView txtTitle;
        private TextView txtContent;
        private ImageButton btnDelete;


        private ViewHolderPost(View itemView, View.OnClickListener onClick) {
            super(itemView);

            txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
            txtContent = (TextView) itemView.findViewById(R.id.content_post);
            btnDelete = (ImageButton) itemView.findViewById(R.id.img_delete);

            IGetPosition getPosition = new IGetPosition() {
                @Override
                public int getPosition() {
                    return getAdapterPosition();
                }
            };
            itemView.setTag(getPosition);
            itemView.setOnClickListener(onClick);
        }
    }

    private interface IGetPosition {
        int getPosition();
    }

    public interface IPostAdapter {
        void onClickItem(int position);
    }
}