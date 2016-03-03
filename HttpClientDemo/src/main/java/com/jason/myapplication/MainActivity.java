package com.jason.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;


public class MainActivity extends Activity {
    TextView tv;
    Button bt;
    public static final int SHOW_DATA = 0;

  private   Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_DATA:
                    String response = (String) msg.obj;
                    tv.setText(response);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = (Button) findViewById(R.id.button);
        tv = (TextView) findViewById(R.id.textView);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestWithHttpClient();
            }
        });
    }
    private void sendRequestWithHttpClient() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //获取httpclient对象
                HttpClient httpclient = new DefaultHttpClient();
                    //设置访问方式为GET，并传入网址
                HttpGet httpget = new HttpGet("http://www.baidu.com");
                    //通过httpclient对象执行httpget对象，返回httpresponse对象
                    HttpResponse httpresponse = httpclient.execute(httpget);
                    //请求和响应都成功了
                    if (httpresponse.getStatusLine().getStatusCode() == 200) {
                        //创建httpresponse实体
                        HttpEntity entity = httpresponse.getEntity();
                        //通过EntityUtils将entity转换成字符串对象
                        String response = EntityUtils.toString(entity, "utf-8");
                        //将数据存入message中并发送出去
                        Message message = new Message();
                        message.what = SHOW_DATA;
                        message.obj = response;
                        handler.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    }
