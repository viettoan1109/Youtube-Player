package com.hello.youtubeplayer.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommentYoutube {

    @SerializedName("items")
    @Expose
    List<ItemComment> itemComments = null;

    public CommentYoutube(List<ItemComment> itemComments) {
        this.itemComments = itemComments;
    }

    public List<ItemComment> getItemComments() {
        return itemComments;
    }

    public void setItemComments(List<ItemComment> itemComments) {
        this.itemComments = itemComments;
    }
}
