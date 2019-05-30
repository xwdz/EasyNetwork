### EasyNetwork

![logo](./jar/logo.jpg)


[![DownloadLastVersion](https://api.bintray.com/packages/quinnhuang/widget/EasyNetwork/images/download.svg)](https://bintray.com/quinnhuang/widget/EasyNetwork/_latestVersion)


#### 如果用的还行希望点个star,我会继续维护下去.

### 前言
开发中如果依赖了第三方Jar，而第三方Jar又需要依赖其他第三方库,如果是App开发还可以接受
但是如果是SDK开发者这种情况挺尴尬的，打成Jar后可能SDK体积会变的很大，又或者并不需要引入其他第三方库有点大材小用的感觉。

`EasyNetwork`封装了一套Android原生的Http框架，提供了类`okHttp`拦截器机制,请求统一加密,响应统一解密,不需要额外引入任何库，体积小,强烈建议SDK开发者引入jar包.

### Download

[强烈建议下载jar包依赖 | Click download jar](https://bintray.com/quinnhuang/widget/EasyNetwork#files)


### 使用


#### 自定义拦截器,统一加密、添加公共参数

`EasyNetwork.getImpl().initializeConfig()`必须传一个`EasyNetworkConfig(Context)`实例

在您的`Application#OnCreate`初始化各种配置.

```
// Request拦截器
public interface InterceptRequest {

    Request onInterceptRequest(Request request);

}
public class App extends Application {

    @Override
    public void onCreate() {
      super.onCreate();
      
        EasyNetworkConfig config = new EasyNetworkConfig(this);
        // 1. 如若不需要使用各种自定义配置直接初始即可
        EasyNetwork.getImpl().initializeConfig(config); 
        // 2. 添加各种配置
        // 添加 公共参数拦截器
        // config.addIntercepts(new AppendGlobalParamsIntercept());
        // 添加 统一加密所有请求
        // config.addIntercepts(new APISignatureIntercept());
        // config.setRetryCount(5) 
        EasyNetwork.getImpl().initializeConfig(config);
}

```

|SimpleIntercept|
|:-:|
|[AppendParamsIntercept](https://github.com/xwdz/EasyNetwork/blob/master/app/src/main/java/com/xwdz/httpsimple/AppendGlobalParamsIntercept.java)|
|[APISignatureIntercept](https://github.com/xwdz/EasyNetwork/blob/master/app/src/main/java/com/xwdz/httpsimple/APISignatureIntercept.java)|


**EasyNetworkConfig**

|属性|说明|
|:-:|:-:|
|`setRetryCount(int)`|失败的重试次数|
|`setOpenRetry(boolean)`|是否需要打开重试功能|
|`setRetryIntervalMillis(long)`|每次重试的间隔时间|
|`addIntercepts(InterceptRequest)`|自定义的各种拦截器|

  // 省略部分属性


`EasyNetwork`内部维护了一个单例`EasyNetwork.getImpl().sendRequest(request,callback)`即可发送请求

```

 Request request = new Request.Builder()
                .url("ur")
                .post() // or get
                .tag("TAG")
                .addHeader("key","value")
                .addParam("key", "value")
                .build();
 // send Request          
 EasyNetwork.getImpl().sendRequest(request, new StringEasyCallbackImpl() {
         @Override
         public void onSuccessful(String data) {
             mLogView.setText(data);
         }
 
         @Override
         public void onFailure(EasyCall easyCall,Throwable error) {
              super.onFailure(easyCall, error);
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


### ChangedLog

#### `1.0.2`
- 添加自动请求重定向机制
- 添加自动重试机制
- `IBaseEasyCallback#onResponse()#onFailure()`新增参数`EasyCall`类,`EasyCall`目前仅仅包装了一些请求信息.
`






