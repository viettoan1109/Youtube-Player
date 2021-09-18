package com.hello.youtubeplayer.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SnippetCommentLow {

    @SerializedName("videoId")
    @Expose
    private String videoId;
    @SerializedName("topLevelComment")
    @Expose
    private TopLevelComment topLevelComment;
    @SerializedName("canReply")
    @Expose
    private Boolean canReply;
    @SerializedName("totalReplyCount")
    @Expose
    private Integer totalReplyCount;
    @SerializedName("isPublic")
    @Expose
    private Boolean isPublic;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public TopLevelComment getTopLevelComment() {
        return topLevelComment;
    }

    public void setTopLevelComment(TopLevelComment topLevelComment) {
        this.topLevelComment = topLevelComment;
    }

    public Boolean getCanReply() {
        return canReply;
    }

    public void setCanReply(Boolean canReply) {
        this.canReply = canReply;
    }

    public Integer getTotalReplyCount() {
        return totalReplyCount;
    }

    public void setTotalReplyCount(Integer totalReplyCount) {
        this.totalReplyCount = totalReplyCount;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }
}
