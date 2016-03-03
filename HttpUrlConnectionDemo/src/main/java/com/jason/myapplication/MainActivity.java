package com.jason.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.Handler;



public class MainActivity extends Activity {
    Button bt_getdata;
    TextView tv_content;

    public static final int SHOW_RESULT = 0;
    //新建handler对象调用handleMessage方法处理返回的数据
   private  Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //通过wsitch判断Message标识
            switch (msg.what) {
                case SHOW_RESULT:
                    //取出msg中的obj对象并转换成字符串
                    String text = (String) msg.obj;
                    //更新UI
                    tv_content.setText(text);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_getdata = (Button) findViewById(R.id.getdata);
        tv_content = (TextView) findViewById(R.id.content);

        bt_getdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdata();
            }
        });

    }
    //通过HttpURLConnection方式获取网络数据
    public void getdata() {
        //开启子线程进行耗时操作
       new Thread(new Runnable() {
           @Override
           public void run() {
               HttpURLConnection connection = null;
               try {
                   URL url = new URL("http://www.baidu.com");
                   //通过url.openConnection打开网络连接
                   connection = (HttpURLConnection) url.openConnection();
                   //设置请求方式为POST
                   connection.setRequestMethod("POST");
                   OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                   osw.write("username=admin&password=123456"); //具体看服务器端提供的访问方法
                   //设置请求方式为GET，设置连接超时、读取超时都为8000毫秒
                  // connection.setRequestMethod("GET");
                   connection.setConnectTimeout(8000);
                   connection.setReadTimeout(8000);
                   InputStream is = connection.getInputStream();    //获取输入流
                   //通过InputStreamReader将输入流从字节流转换成utf-8字符流
                   InputStreamReader isr = new InputStreamReader(is,"utf-8");
                   //将isr放入缓存读取区进行读取
                   BufferedReader br = new BufferedReader(isr);
                   //创建可变字符长度result字符串变量
                   StringBuilder result = new StringBuilder();
                   String line = "";
                   //循环读取缓存读取区br中的内容并存入result变量中
                   while ((line = br.readLine()) != null) {
                       result.append(line);
                   }
                   //关闭输入流等等
                   is.close();
                   isr.close();
                   br.close();
                   //创建Message对象用于从子线程传递数据给UI线程
                   Message message = new Message();
                   message.what = SHOW_RESULT;      //设置message标识
                   message.obj = result.toString();     //设置message内容
                   handler.sendMessage(message);    //通过handler发送message
               } catch (IOException e) {
                   e.printStackTrace();
               }finally {
                   if (connection != null) {
                       connection.disconnect();     //断开连接
                   }
               }
           }
       }).start();      //开启子线程
    }
}
