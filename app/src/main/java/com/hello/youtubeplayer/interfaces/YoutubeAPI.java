package com.hello.youtubeplayer.interfaces;

import com.hello.youtubeplayer.data.network.model.ChannelYoutube;
import com.hello.youtubeplayer.data.network.model.CommentYoutube;
import com.hello.youtubeplayer.data.network.model.SOAnswersResponse;
import com.hello.youtubeplayer.data.network.model.Snippet;
import com.hello.youtubeplayer.data.network.model.VideoYoutube;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface YoutubeAPI {
    @GET("search/")
    Call<SOAnswersResponse> getAnswers(@Query("key") String key,
                                       @Query("part") String part,
                                       @Query("q") String search,
                                       @Query("type") String type,
                                       @Query("maxResults") int maxResult
    );


    @GET("videos?part=snippet,contentDetails,statistics&chart=mostPopular")
    Call<VideoYoutube> getVideoYoutube(@Query("maxResults") int maxResults,
                                       @Query("key") String key,
                                       @Query("regionCode") String regionCode
    );

    @GET("videos?part=snippet,statistics")
    Call<VideoYoutube> getVideoDetail(@Query("id") String videoId,
                                      @Query("key") String key
    );


    @GET("channels?part=snippet&part=statistics")
    Call<ChannelYoutube> getChannelYoutube(@Query("id") String id,
                                           @Query("key") String key
    );

    @GET("commentThreads?part=snippet")
    Call<CommentYoutube> getCommentYoutube(@Query("maxResults") int maxResults,
                                           @Query("videoId") String videoId,
                                           @Query("key") String key);

}
