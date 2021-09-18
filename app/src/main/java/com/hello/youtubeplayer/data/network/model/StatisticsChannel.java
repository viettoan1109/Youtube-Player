package com.hello.youtubeplayer.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatisticsChannel {
    @SerializedName("subscriberCount")
    @Expose
    private String subscriberCount;

    public String getSubscriberCount() {
        return subscriberCount;
    }
}
