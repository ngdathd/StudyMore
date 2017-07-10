package com.ngdat.studymore.common;

import com.ngdat.studymore.models.item.ItemPost;
import com.ngdat.studymore.util.Action1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hdt on 07/07/2017.
 */

public class Interactor {
    private static Interactor instance = new Interactor();
    private List<Action1<ItemPost>> actionRemovePost;
    private List<Action1<ItemPost>> actionRemovePostFromFind;

    private Interactor() {
        actionRemovePost = new ArrayList<>();
        actionRemovePostFromFind = new ArrayList<>();
    }

    public static Interactor getInstance() {
        return instance;
    }

    public void removeItemPost(ItemPost itemPost) {
        for (Action1<ItemPost> action : actionRemovePost) {
            action.call(itemPost);
        }
    }

    public void changeItemPostFromFind(ItemPost itemPost) {
        for (Action1<ItemPost> action : actionRemovePostFromFind) {
            action.call(itemPost);
        }
    }

    public void registerActionRemoveItemPost(Action1<ItemPost> itemPostAction) {
        actionRemovePost.add(itemPostAction);
    }

    public void unregisterActionRemoveItemPost(Action1<ItemPost> itemPostAction) {
        actionRemovePost.remove(itemPostAction);
    }
    public void registerActionRemoveItemPostFromFind(Action1<ItemPost> itemPostAction) {
        actionRemovePostFromFind.add(itemPostAction);
    }

    public void unregisterActionRemoveItemPostFromFind(Action1<ItemPost> itemPostAction) {
        actionRemovePostFromFind.remove(itemPostAction);
    }
}
