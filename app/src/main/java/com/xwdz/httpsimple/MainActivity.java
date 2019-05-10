package com.xwdz.httpsimple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.xwdz.http.EasyNetwork;
import com.xwdz.http.Util;
import com.xwdz.http.callback.FileEasyCallbackImpl;
import com.xwdz.http.core.Request;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private TextView mLogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLogView = findViewById(R.id.logText);


        findViewById(R.id.get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testGET();
            }
        });


        findViewById(R.id.post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyNetwork.getImpl().cancelRequest("TAG");
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        EasyNetwork.getImpl().cancelRequest("TAG");
    }


    public void testGET() {
        Request request = new Request.Builder()
                .url("http://shouji.360tpcdn.com/150707/2ef5e16e0b8b3135aa714ad9b56b9a3d/com.happyelements.AndroidAnimal_25.apk")
                .method(Request.Method.GET)
//                .addParam("key", "10926a9165054566b6df6a8410e45f08")
//                .addParam("id", "0bd50c2f6e7f47d9a803d95011b8fe9c")
                .tag("TAG")
                .build();

        EasyNetwork.getImpl().sendRequest(request,
                new FileEasyCallbackImpl(
                        Util.getDownloaderDir(this, "easy").getAbsolutePath(),
                        "test.apk") {

                    @Override
                    public void onProgressUpdated(int currentLength, int totalLength, int percent) {
                        mLogView.setText("" + percent + "%");
                    }

                    @Override
                    public void onSuccessful(File file) {
                        mLogView.setText(file.getAbsolutePath());
                    }

                    @Override
                    public void onFailure(Throwable error) {
                        mLogView.setText(error.toString());
                    }
                });

//        EasyNetwork.getImpl()
//                .sendRequest(request, new StringEasyCallbackImpl() {
//                    @Override
//                    public void onSuccessful(String data) {
//                        mLogView.setText(data);
//                    }
//
//                    @Override
//                    public void onFailure(Throwable error) {
//                        super.onFailure(error);
//                        Util.Logger.w("tag", "err:" + error.toString());
//                        mLogView.setText(error.toString());
//                    }
//                });
    }
}
