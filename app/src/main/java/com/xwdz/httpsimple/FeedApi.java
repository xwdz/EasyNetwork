package com.xwdz.httpsimple;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author 黄兴伟 (xwdz9989@gamil.com)
 * @since 2018/9/20
 */
public interface FeedApi {

    @GET("message/list?")
    Call<String> request(@Query("pageSize") String pageSize);
}
