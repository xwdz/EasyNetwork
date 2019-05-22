### EasyNetwork

![logo](./jar/logo.jpg)


[![Download](https://api.bintray.com/packages/quinnhuang/widget/EasyNetwork/images/download.svg)](https://bintray.com/quinnhuang/widget/EasyNetwork/_latestVersion)


### 前言
开发中如果依赖了第三方Jar，而第三方Jar又需要依赖其他第三方库,如果是App开发还可以接受
但是如果是SDK开发者这种情况挺尴尬的，打成Jar后可能SDK体积会变的很大，又或者并不需要引入其他第三方库有点大材小用的感觉。

`EasyNetwork`封装了一套Android原生的Http框架，提供了类`okHttp`拦截器机制,请求统一加密,响应统一解密,不需要额外引入任何库，体积小,面向SDK开发。

### Download

[Download Jar=10.16KB](./jar/EasyNetwork-1.0.0-sources.jar)


or

> compile 'com.xwdz:EasyNetwork:$DownloadLastVersion'

### 使用


#### 自定义拦截器,统一加密、添加公共参数

在您的`Application#OnCreate`添加配置

```

 EasyConfig config = new EasyConfig();
 // 添加 公共参数拦截器
 config.addIntercepts(new AppendParamsIntercept());
 // 添加 统一加密所有请求
 config.addIntercepts(new APISignatureIntercept());

 EasyNetwork.getImpl().initializeConfig(config);

```

|SimpleIntercept|
|:-:|
|[AppendParamsIntercept](https://github.com/xwdz/EasyNetwork/blob/master/app/src/main/java/com/xwdz/httpsimple/AppendGlobalParamsIntercept.java)|
|[APISignatureIntercept](https://github.com/xwdz/EasyNetwork/blob/master/app/src/main/java/com/xwdz/httpsimple/APISignatureIntercept.java)|


`EasyNetwork`内部维护了一个单例`EasyNetwork.getImpl().sendRequest(request,callback)`即可发送请求

```

 Request request = new Request.Builder()
                .url("ur")
                .post() // or get
                .tag("TAG")
                .addParam("key", "value")
                .build();
 // send Request          
 EasyNetwork.getImpl().sendRequest(request, new StringEasyCallbackImpl() {
         @Override
         public void onSuccessful(String data) {
             mLogView.setText(data);
         }
 
         @Override
         public void onFailure(Throwable error) {
             Util.Logger.w("tag", "err:" + error.toString());
             mLogView.setText(error.toString());
         }
         
         @Override
         public void onCompleted() {
         }
     });   
     
    // cancel Request
    EasyNetwork.getImpl().cancelRequest(request)               
```

### 扩展Callback
继承`BaseEasyCallbackImpl`即可，完成自己解析、解密、逻辑接口。[Simple Code](https://github.com/xwdz/EasyNetwork/blob/master/mylibrary/src/main/java/com/xwdz/http/callback/FileEasyCallbackImpl.java)




