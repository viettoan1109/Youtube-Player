package com.hello.youtubeplayer.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemComment {
    private String etag;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("snippet")
    @Expose
    private SnippetCommentLow snippet;

    public ItemComment(String etag, String id, SnippetCommentLow snippet) {
        this.etag = etag;
        this.id = id;
        this.snippet = snippet;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SnippetCommentLow getSnippet() {
        return snippet;
    }

    public void setSnippet(SnippetCommentLow snippet) {
        this.snippet = snippet;
    }
}
