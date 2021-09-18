package com.hello.youtubeplayer.data.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoYoutube {

    private List<ItemsBean> items;


    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }


    public static class ItemsBean {
        @SerializedName("id")
        private String id;

        @SerializedName("snippet")
        private Snippet snippet;

        @SerializedName("contentDetails")
        private ContentDetails contentDetails;

        @SerializedName("statistics")
        private Statistics statistics;

        public ItemsBean() {
        }

        public ItemsBean(String id, Snippet snippet, ContentDetails contentDetails, Statistics statistics) {
            this.id = id;
            this.snippet = snippet;
            this.contentDetails = contentDetails;
            this.statistics = statistics;
        }

        public String getId() {
            return id;
        }

        public Snippet getSnippet() {
            return snippet;
        }

        public static class Snippet {
            @SerializedName("publishedAt")
            private String publishedAt;

            @SerializedName("channelId")
            private String channelId;

            @SerializedName("title")
            private String title;

            @SerializedName("description")
            private String description;

            @SerializedName("thumbnails")
            private Thumbnails thumbnails;

            @SerializedName("channelTitle")
            private String channelTitle;

            @SerializedName("categoryId")
            private String categoryId;

            @SerializedName("liveBroadcastContent")
            private String liveBroadcastContent;

            public Snippet(String publishedAt, String channelId, String title, String description, Thumbnails thumbnails, String channelTitle, String categoryId, String liveBroadcastContent) {
                this.publishedAt = publishedAt;
                this.channelId = channelId;
                this.title = title;
                this.description = description;
                this.thumbnails = thumbnails;
                this.channelTitle = channelTitle;
                this.categoryId = categoryId;
                this.liveBroadcastContent = liveBroadcastContent;
            }

            public Snippet() {
            }

            public String getPublishedAt() {
                return publishedAt;
            }

            public String getChannelId() {
                return channelId;
            }

            public String getTitle() {
                return title;
            }

            public String getDescription() {
                return description;
            }

            public Thumbnails getThumbnails() {
                return thumbnails;
            }

            public String getChannelTitle() {
                return channelTitle;
            }

            public String getCategoryId() {
                return categoryId;
            }

            public String getLiveBroadcastContent() {
                return liveBroadcastContent;
            }


            public static class Thumbnails {
                @SerializedName("high")
                private High high;

                public Thumbnails() {
                }

                public Thumbnails(High high) {
                    this.high = high;
                }

                public High getHigh() {
                    return high;
                }

                public static class High {
                    @SerializedName("url")
                    private String url;

                    public High(String url) {
                        this.url = url;
                    }

                    public High() {

                    }

                    public String getUrl() {
                        return url;
                    }
                }
            }

        }

        public static class Statistics {
            @SerializedName("viewCount")
            private String viewCount;
            @SerializedName("likeCount")
            private String likeCount;
            @SerializedName("dislikeCount")
            private String dislikeCount;
            @SerializedName("commentCount")
            private String commentCount;

            public Statistics() {
            }

            public Statistics(String viewCount, String likeCount, String dislikeCount, String commentCount) {
                this.viewCount = viewCount;
                this.likeCount = likeCount;
                this.dislikeCount = dislikeCount;
                this.commentCount = commentCount;
            }

            public String getViewCount() {
                return viewCount;
            }


            public String getLikeCount() {
                return likeCount;
            }

            public String getDislikeCount() {
                return dislikeCount;
            }

            public String getCommentCount() {
                return commentCount;
            }
        }

        public static class ContentDetails {
            @SerializedName("duration")
            private String duration;

            public ContentDetails() {
            }

            public ContentDetails(String duration) {
                this.duration = duration;
            }

            public String getDuration() {
                return duration;
            }
        }

        public ContentDetails getContentDetails() {
            return contentDetails;
        }

        public Statistics getStatistics() {
            return statistics;
        }

    }
}
