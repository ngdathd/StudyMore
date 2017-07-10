package com.ngdat.studymore.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ngdat.studymore.R;
import com.ngdat.studymore.common.Constants;
import com.ngdat.studymore.common.Interactor;
import com.ngdat.studymore.models.item.ItemPost;
import com.ngdat.studymore.models.user.UserInstance;
import com.ngdat.studymore.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class BookmarkPostAdapter extends RecyclerView.Adapter
        implements View.OnClickListener, Constants {

    private LayoutInflater mInflater;
    private List<ItemPost> mItemPosts;
    private IPostAdapter mIPostAdapter;

    public BookmarkPostAdapter(Context context, IPostAdapter mIPostAdapter) {
        this.mInflater = LayoutInflater.from(context);
        this.mItemPosts = new ArrayList<>();
        this.mIPostAdapter = mIPostAdapter;
    }

    public void add(ItemPost itemPost) {
        mItemPosts.add(0, itemPost);
        notifyDataSetChanged();
    }

    public boolean remove(ItemPost itemPost) {
        boolean b = mItemPosts.remove(itemPost);
        notifyDataSetChanged();
        return b;
    }

    public ItemPost get(int position) {
        return mItemPosts.get(position);
    }

    public List<ItemPost> getmItemPosts() {
        return mItemPosts;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_post, parent, false);
        return new ViewHolderPost(view, this);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (null == mItemPosts) {
            return;
        }
        final ItemPost itemPost = mItemPosts.get(position);
        final ViewHolderPost viewHolderPost = (ViewHolderPost) holder;
        viewHolderPost.imgAvatar
                .setBackgroundResource(Utils.setImgAvatar((int) itemPost.getUserName().charAt(0)));
        viewHolderPost.imgAvatar
                .setImageURI(Utils.getLinkAvatar(itemPost.getUserID(),
                        (int) Utils.convertDpToPixel(48) * 2,
                        (int) Utils.convertDpToPixel(48) * 2));
        viewHolderPost.txtUsername.setText(itemPost.getUserName());
        viewHolderPost.txtPhone.setText(itemPost.getPhoneNumber());
        viewHolderPost.txtLocation.setText(itemPost.getLocation());
        viewHolderPost.txtTitle.setText(itemPost.getTitlePost());
        viewHolderPost.txtContent.setText(itemPost.getDes());
        viewHolderPost.toggleButton.setChecked(true);
        viewHolderPost.toggleButton.setTag(position);
        viewHolderPost.toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.child(BOOKMARK)
                        .child(UserInstance.getInstance().getKey())
                        .child(itemPost.getPostID()).removeValue();
                mItemPosts.remove(itemPost);
                Interactor.getInstance().removeItemPost(itemPost);
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
        private SimpleDraweeView imgAvatar;
        private TextView txtUsername;
        private TextView txtLocation;
        private TextView txtPhone;
        private TextView txtTitle;
        private TextView txtContent;
        private ToggleButton toggleButton;

        private ViewHolderPost(View itemView, View.OnClickListener onClick) {
            super(itemView);
            imgAvatar = (SimpleDraweeView) itemView.findViewById(R.id.img_avatar_item);
            txtUsername = (TextView) itemView.findViewById(R.id.txt_username);
            txtPhone = (TextView) itemView.findViewById(R.id.txt_phone_number);
            txtLocation = (TextView) itemView.findViewById(R.id.txt_location);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
            txtContent = (TextView) itemView.findViewById(R.id.content_post);
            toggleButton = (ToggleButton) itemView.findViewById(R.id.img_bookmark);

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