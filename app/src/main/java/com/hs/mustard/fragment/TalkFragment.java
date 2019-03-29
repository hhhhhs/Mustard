package com.hs.mustard.fragment;

import android.app.ActivityOptions;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hs.mustard.*;
import com.hs.mustard.samples.*;
import com.hs.mustard.adapter.*;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import com.hs.mustard.Until.*;
import com.hs.mustard.ui.activity.MainActivity1;
/**
 * Created by hs on 2017/12/4.
 */

public class TalkFragment extends Fragment {
    private List<Talk> talkList = new ArrayList<Talk>();
    private View view;
    private String phone;
    //private String name;
    private String sex;
    private String[] name;
    private int hour;
    private int minute;
    private int second;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.talk_list_view, container, false);
        Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        hour = t.hour; // 0-23
        minute = t.minute;
        second = t.second;
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setContentView(R.layout.talk_list_view);
        sendRequestWithHttpClient_talk();
        initUser();
        TalkListAdapter adapter = new TalkListAdapter(getActivity(), R.layout.talk_item, talkList);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(FruitActivity.this, android.R.layout.simple_list_item_1, datas);
        ListView listView = (ListView)view.findViewById(R.id.list_view_talk);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Talk talk = talkList.get(position);
                ToastUtil.toast(getActivity(), talk.getName());
                Intent intent=new Intent(getActivity(),MainActivity1.class);//startActivity(intent);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
            }
        });
    }

    //读本地
    public String readLocal(){
        //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getActivity().getSharedPreferences("lock", MODE_PRIVATE);
        //步骤2：获取文件中的值
        String value = read.getString("code", "");
        if(value.isEmpty()){
            return null;
        }
        return value;
    }


    //服务端通信
    public void sendRequestWithHttpClient_talk(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    ContentValues postParams = new ContentValues();
                    postParams.put("accountid", readLocal());
                    postParams.put("password", "123456");
                    String s = UrlManager.httpUrlConnectionPost("/hs/getTalkServlet", postParams);
                    JSONObject obj = new JSONObject(s);
                    int len = obj.length();
                    name = new String[len];
                    for(int i=0;i<len-1;i++){
                        //name = obj.getString("name");
                        name[i] = obj.getString("name"+i);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                    //handle
                    //initUser();
                    Looper.prepare();
                    //saveLocal();
                    //ToastUtil.toast(MainActivity.this, phone+name+sex+real_name+email);
                    Looper.loop();
                    Thread.sleep(2000);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initUser() {
        talkList.clear();
        for (int i = 0; i < 30; i++) {
            //Log.i("xxxxxxxx",name[i]);
            Talk apple = new Talk("黄硕", R.drawable.touxiang2, hour+":"+minute+":"+second);
            talkList.add(apple);
        }
    }


    public static TalkFragment newInstance(String s) {
        Bundle bundle = new Bundle();
        bundle.putString("ARGS", s);
        TalkFragment fragment = new TalkFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

}