package com.hs.mustard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hs.mustard.*;
import com.hs.mustard.samples.News;

import java.util.List;

public class NewsListAdapter extends ArrayAdapter<News> {

    private int resourceId;
    ImageView newsImage;
    TextView newsName;

    public NewsListAdapter(Context context, int textViewResourceId, List<News> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        News news = getItem(position); // 获取当前项的News实例
        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.newsImage = (ImageView) view.findViewById (R.id.fresh_image);
            viewHolder.newsName = (TextView) view.findViewById (R.id.fresh_name);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
            //View中的setTag（Object）表示给View添加一个格外的数据，以后可以用getTag()将这个数据取出来。
            //可以用在多个Button添加一个监听器，每个Button都设置不同的setTag。
            // 这个监听器就通过getTag来分辨是哪个Button 被按下。
            }
        viewHolder.newsImage.setImageResource(news.getImageId());
        viewHolder.newsName.setText(news.getName());
        return view;
    }

    class ViewHolder {
        ImageView newsImage;
        TextView newsName;
    }
}