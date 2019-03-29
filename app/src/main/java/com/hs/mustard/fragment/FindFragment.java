package com.hs.mustard.fragment;

import android.app.ActivityOptions;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.hs.mustard.R;
import com.hs.mustard.samples.User;
import com.hs.mustard.adapter.FindListAdapter;
import com.hs.mustard.Until.ToastUtil;
import com.hs.mustard.UrlManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by hs on 2017/12/4.
 */

public class FindFragment extends Fragment {
    private List<User> findList = new ArrayList<User>();
    private View view;
    private String[] name;
    private Button button_find_a;
    private Button button_find_b;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.find_list_view, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setContentView(R.layout.talk_list_view);
        sendRequestWithHttpClient_find();
        initUser();
        FindListAdapter adapter = new FindListAdapter(getActivity(), R.layout.find_item, findList);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(FruitActivity.this, android.R.layout.simple_list_item_1, datas);
        ListView listView = (ListView)view.findViewById(R.id.list_view_find);
        listView.setAdapter(adapter);

        /*//设置颜色图层
        button_find_a = (Button)view.findViewById(R.id.button_find_a);
        button_find_b = (Button)view.findViewById(R.id.button_find_b);
        ColorStateList cl = new ColorStateList(new int[][]{new int[0]}, new int[]{0xff6bc056});//BackgroundTint
        button_find_a.setBackgroundTintList(cl);
        button_find_b.setBackgroundTintList(cl);*/


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = findList.get(position);
                ToastUtil.toast(getActivity(), user.getName());
                //Intent intent=new Intent(getActivity(),MainActivity1.class);//startActivity(intent);
                //startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
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
    public void sendRequestWithHttpClient_find(){
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
        findList.clear();
        for (int i = 0; i < 20; i++) {
            //Log.i("xxxxxxxx",name[i]);//这里可以用服务器传来的数据
            User apple = new User("黄硕", R.drawable.touxiang2, 0);
            findList.add(apple);
        }
    }


    public static FindFragment newInstance(String s) {
        Bundle bundle = new Bundle();
        bundle.putString("ARGS",s);
        FindFragment fragment = new FindFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
