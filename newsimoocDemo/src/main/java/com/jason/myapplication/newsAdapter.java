package com.jason.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/28.
 */
public class newsAdapter extends BaseAdapter {

    List<NewsBean> mlist = new ArrayList<>();
    LayoutInflater inflater;

    public newsAdapter(Context context, List<NewsBean> list) {
        mlist = list;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        NewsBean item = mlist.get(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.items, null);
            viewHolder = new ViewHolder();
            //新建viewHolder加载布局中的控件
            viewHolder.newsicon = (ImageView) convertView.findViewById(R.id.icon);
            viewHolder.newsTitle = (TextView) convertView.findViewById(R.id.title);
            viewHolder.newsContent = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //viewHolder加载数据源中对应的数据
        viewHolder.newsicon.setImageResource(R.mipmap.ic_launcher);
        viewHolder.newsTitle.setText(item.newsTitle);
        viewHolder.newsContent.setText(item.newsContent);


        return convertView;
    }

    class ViewHolder {
        ImageView newsicon;
        TextView newsTitle;
        TextView newsContent;
    }
}
