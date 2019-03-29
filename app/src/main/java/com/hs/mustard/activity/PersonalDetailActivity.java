/*
package com.hs.mustard.activity;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hs.mustard.R;
import com.hs.mustard.Until.ToastUtil;
import com.hs.mustard.UrlManager;
import com.hs.mustard.adapter.PersonalList1Adapter;
import com.hs.mustard.samples.User;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


*/
/**
 * Created by hs on 2017/12/4.
 *//*


public class PersonalDetailActivity extends Fragment {
    private List<User> personalList = new ArrayList<User>();
    private View view;
    private String phone;
    private String sex;
    private String[] name;


    private TextView textView_personal_phone;
    private TextView textView_personal_name;
    private TextView textView_personal_sex;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.personal_list_view, container, false);
        textView_personal_phone = (TextView)view.findViewById(R.id.textView_personal_phone);
        textView_personal_name = (TextView)view.findViewById(R.id.textView_personal_name);
        textView_personal_sex = (TextView)view.findViewById(R.id.textView_personal_sex);
        sendRequestWithHttpClient_personal();
        textView_personal_phone.setText(phone);
        textView_personal_name.setText(name[0]);
        textView_personal_sex.setText(sex);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setContentView(R.layout.talk_list_view);
        sendRequestWithHttpClient_personal();
        //initFruits();
        PersonalList1Adapter adapter = new PersonalList1Adapter(getActivity(), R.layout.personal_item, personalList);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(FruitActivity.this, android.R.layout.simple_list_item_1, datas);
        ListView listView = (ListView)view.findViewById(R.id.list_view_personal);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = personalList.get(position);
                ToastUtil.toast(getActivity(), user.getName());
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
    public void sendRequestWithHttpClient_personal(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    ContentValues postParams = new ContentValues();
                    postParams.put("accountid", readLocal());
                    postParams.put("password", "123456");
                    String s = UrlManager.httpUrlConnectionPost("/hs/getInformationServlet", postParams);

                    JSONObject obj = new JSONObject(s);
                    phone = obj.getString("phone");
                    name[0] = obj.getString("name");
                    sex = obj.getString("sex");
                    //String imgpath = obj.getString("imgpath");
                    String real_name = obj.getString("real_name");
                    //String birthday = obj.getString("birthday");
                    String email = obj.getString("email");

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView_personal_phone.setText(phone);
                            textView_personal_name.setText(name[0]);
                            textView_personal_sex.setText(sex);
                        }
                    });
                    initFruits();
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
    
    private void initFruits() {
        personalList.clear();
        for (int i = 0; i < 1; i++) {
            User user = new User(name[i], R.drawable.default_useravatar, 1);
            personalList.add(user);
        }
    }

    public static PersonalDetailActivity newInstance(String s) {
        Bundle bundle = new Bundle();
        bundle.putString("ARGS",s);
        PersonalDetailActivity fragment = new PersonalDetailActivity();
        fragment.setArguments(bundle);
        return fragment;
    }
}
*/
