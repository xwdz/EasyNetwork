package com.xwdz.httpsimple;

import android.app.Application;

import com.xwdz.http.EasyNetwork;
import com.xwdz.http.EasyNetworkConfig;

/**
 * @author xingwei.huang (xwdz9989@gmail.com)
 * @since v1.0.0
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        EasyNetworkConfig config = new EasyNetworkConfig();
        config.addIntercepts(new AppendGlobalParamsIntercept());
//        config.addIntercepts(new APISignatureIntercept());
        EasyNetwork.getImpl().initializeConfig(config);


//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("11")
//                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .build();
//
//        retrofit.create(FeedApi.class).request("0")
//                .enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<String> call, Throwable t) {
//
//                    }
//                });



    }
}
