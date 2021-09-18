package com.hello.youtubeplayer.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopLevelComment {
    @SerializedName("snippet")
    @Expose
    private SnippetCommentHight snippet;

    public SnippetCommentHight getSnippet() {
        return snippet;
    }

    public void setSnippet(SnippetCommentHight snippet) {
        this.snippet = snippet;
    }
}
