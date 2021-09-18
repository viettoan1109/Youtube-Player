package com.hello.youtubeplayer.data.network.model;

import java.io.Serializable;

public class Video implements Serializable {

    private String urlVideo;
    private String thumnail;
    private String title;
    private String channelTitle;
    private String description;

    public Video() {
    }

    public Video(String urlVideo, String thumnail, String title, String channelTitle, String description) {
        this.urlVideo = urlVideo;
        this.thumnail = thumnail;
        this.title = title;
        this.channelTitle = channelTitle;
        this.description = description;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    public String getThumnail() {
        return thumnail;
    }

    public void setThumnail(String thumnail) {
        this.thumnail = thumnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
