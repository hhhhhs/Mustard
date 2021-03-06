package com.hs.mustard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hs.mustard.R;
import com.hs.mustard.samples.User;

import java.util.List;

public class FindListAdapter extends ArrayAdapter<User> {

    private int resourceId;
    ImageView findImage;
    TextView findName;

    public FindListAdapter(Context context, int textViewResourceId, List<User> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position); // 获取当前项的User实例
        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.findImage = (ImageView) view.findViewById (R.id.find_image);
            viewHolder.findName = (TextView) view.findViewById (R.id.find_name);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
            //View中的setTag（Object）表示给View添加一个格外的数据，以后可以用getTag()将这个数据取出来。
            //可以用在多个Button添加一个监听器，每个Button都设置不同的setTag。
            // 这个监听器就通过getTag来分辨是哪个Button 被按下。
            }
        viewHolder.findImage.setImageResource(user.getImageId());
        viewHolder.findName.setText(user.getName());
        return view;
    }

    class ViewHolder {
        ImageView findImage;
        TextView findName;
    }
}