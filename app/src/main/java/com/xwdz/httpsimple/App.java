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
        config.addIntercepts(new CommParamsIntercept());
        config.addIntercepts(new SignatureIntercept());
//
        EasyNetwork.getImpl().initConfig(config);

    }
}
