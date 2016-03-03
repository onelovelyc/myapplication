package com.jason.myapplication;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private ListView listView;
    private static String mURL = "http://www.imooc.com/api/teacher?type=4&num=30";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        //开启异步任务，传入murl网址
        new MyAsyncTask().execute(mURL);

    }

    //自定义getdata方法将网络获取到的json数据进行解析，传入url地址
    private List<NewsBean> getdata(String url) {
        NewsBean newsBean;
        JSONObject jsonObject;
        List<NewsBean> beanlist = new ArrayList<>();
        try {
            //自定义readStream方法获取网络通信数据，openStream（）方法等同于url.openconnection().getInputStream()
            String jsonString = readStream(new URL(url).openStream());
            //打印获取到的json数据进行调试
            //Log.i("lyc", jsonString);

            //解析json数据，实例化JSONObject传入网络获取到的json数据jsonString
           jsonObject = new JSONObject(jsonString);
            //通过jsonArray提取里面的“data”元素的数组
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            //通过for循环将数组中的数据读取出来
            for (int i = 0; i < jsonArray.length(); i++) {
                //将数组中的数据按顺序提取
                jsonObject = jsonArray.getJSONObject(i);
                //创建NewsBean对象，用于存放数据
                newsBean = new NewsBean();
                //newsBean对象对应的属性存放“*”对应元素的内容
                newsBean.newsIconurl = jsonObject.getString("picSmall");
                newsBean.newsTitle = jsonObject.getString("name");
                newsBean.newsContent = jsonObject.getString("description");
                //将每次读到的newsBean对象存入指定泛型为NewsBean类型的List数组中
                beanlist.add(newsBean);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return beanlist;
    }
    //自定义readStream方法获取网络数据，传入InputStream输入流对象
    private String readStream(InputStream is) {
        InputStreamReader isr;
        String result = "";
        String lines = "";
        try {

            //通过InputStreamReader对象将获取到的字节流转换成utf-8格式的字符流格式
            isr = new InputStreamReader(is, "utf-8");
            //通过BufferedReader读取isr里面的内容
            BufferedReader br= new BufferedReader(isr);
            //通过while循环将数据存入result中
            while ((lines = br.readLine()) != null) {
                result += lines;

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;  //返回result，即为getdata方法中的jsonString
    }

    //定义新的AsyncTask，通过自定义的getdata（传入url地址）方法返回一个指定泛型为newsbean的list集合
    class MyAsyncTask extends AsyncTask<String, Void, List<NewsBean>> {

        @Override
        protected List<NewsBean> doInBackground(String... params) {

            return getdata(params[0]);
        }

        @Override
        //调用onPostExecute方法，用于更新UI线程，将doInBackground方法返回的newsBeans数据源配置到适配器中供listView加载
        protected void onPostExecute(List<NewsBean> newsBeans) {
            super.onPostExecute(newsBeans);
            listView.setAdapter(new newsAdapter(MainActivity.this,newsBeans));
        }
    }
}
