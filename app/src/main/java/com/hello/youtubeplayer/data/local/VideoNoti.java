package com.hello.youtubeplayer.data.local;

public class VideoNoti {
    private String image;
    private String title;
    private String channel;

    public VideoNoti(String image, String title, String channel) {
        this.image = image;
        this.title = title;
        this.channel = channel;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
