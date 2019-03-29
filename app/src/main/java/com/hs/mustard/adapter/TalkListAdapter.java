package com.hs.mustard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hs.mustard.*;
import com.hs.mustard.samples.*;
import java.util.List;

public class TalkListAdapter extends ArrayAdapter<Talk> {

    private int resourceId;
    ImageView talkImage;
    TextView talkName;
    TextView talk_time;

    public TalkListAdapter(Context context, int textViewResourceId, List<Talk> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Talk talk = getItem(position); // 获取当前项的Talk实例
        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.talkImage = (ImageView) view.findViewById (R.id.talk_image);
            viewHolder.talkName = (TextView) view.findViewById (R.id.talk_name);
            viewHolder.talk_time = (TextView) view.findViewById (R.id.talk_time);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
            //View中的setTag（Object）表示给View添加一个格外的数据，以后可以用getTag()将这个数据取出来。
            //可以用在多个Button添加一个监听器，每个Button都设置不同的setTag。
            // 这个监听器就通过getTag来分辨是哪个Button 被按下。
            }
        viewHolder.talkImage.setImageResource(talk.getImageId());
        viewHolder.talkName.setText(talk.getName());
        viewHolder.talk_time.setText(talk.getTime());
        return view;
    }

    class ViewHolder {
        ImageView talkImage;
        TextView talkName;
        TextView talk_time;
    }
}