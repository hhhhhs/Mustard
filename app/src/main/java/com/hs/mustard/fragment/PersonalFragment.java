package com.hs.mustard.fragment;

import android.app.ActivityOptions;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hs.mustard.R;
import com.hs.mustard.Until.ToastUtil;
import com.hs.mustard.UrlManager;
import com.hs.mustard.samples.User;
import com.hs.mustard.adapter.PersonalList1Adapter;
import com.hs.mustard.adapter.PersonalList2Adapter;
import com.hs.mustard.activity.*;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.makeramen.roundedimageview.RoundedImageView.TAG;


/**
 * Created by hs on 2017/12/4.
 */

public class PersonalFragment extends Fragment {
    private List<User> personalList = new ArrayList<User>();
    private List<User> personalList2 = new ArrayList<User>();
    private View view;
    private String name;
    private ImageView imageView_net;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.personal_list_view, container, false);


        sendRequestWithHttpClient_personal();
        initUser();
        PersonalList1Adapter adapter = new PersonalList1Adapter(getActivity(), R.layout.personal_item, personalList);
        ListView listView = (ListView)view.findViewById(R.id.list_view_personal);
        listView.setAdapter(adapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = personalList.get(position);
                //ToastUtil.toast(getActivity(), user.getName());
                sendRequestWithHttpClient_personal_detail();
            }
        });


        PersonalList2Adapter adapter2 = new PersonalList2Adapter(getActivity(), R.layout.personal2_item, personalList2);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(FruitActivity.this, android.R.layout.simple_list_item_1, datas);
        ListView listView2 = (ListView)view.findViewById(R.id.list_view_personal2);
        listView2.setAdapter(adapter2);

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = personalList2.get(position);

                if(user.getName().equals("设置")){
                    Intent intent=new Intent(getActivity(),personalDetialActivity.class);//startActivity(intent);
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                }else if(user.getName().equals("个人资料")){
                    sendRequestWithHttpClient_personal_detail();
                }else if(user.getName().equals("大学信息")){
                    ToastUtil.toast(getActivity(), user.getName());
                }else if(user.getName().equals("兴趣")){
                    ToastUtil.toast(getActivity(), user.getName());
                }else if(user.getName().equals("我的新鲜事")){
                    ToastUtil.toast(getActivity(), user.getName());
                }else if(user.getName().equals("好友印象")){
                    ToastUtil.toast(getActivity(), user.getName());
                }else if(user.getName().equals("向朋友推荐芥末")){
                    ToastUtil.toast(getActivity(), user.getName());
                }
            }
        });

        /*//在线图片
        imageView_net = (ImageView)view.findViewById(R.id.textView_personal_image);
        ViewUtils.inject(getActivity());// 注入view和事件
        initView();*/
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    //服务端通信
    private void sendRequestWithHttpClient_personal_detail(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    ContentValues postParams = new ContentValues();
                    postParams.put("accountid", readLocal());
                    postParams.put("password", "123456");
                    String s = UrlManager.httpUrlConnectionPost("/hs/getInformationServlet", postParams);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //
                        }
                    });
                    JSONObject obj = new JSONObject(s);
                    String phone = obj.getString("phone");
                    String name = obj.getString("name");
                    String sex = obj.getString("sex");
                    //String imgpath = obj.getString("imgpath");
                    String real_name = obj.getString("real_name");
                    //String birthday = obj.getString("birthday");
                    String email = obj.getString("email");

                    Looper.prepare();
                    //saveLocal();
                    ToastUtil.toast(getActivity(), phone+name+sex+real_name+email);
                    Looper.loop();
                    Thread.sleep(2000);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initView() {
        // TODO Auto-generated method stub
        BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
        // 加载网络图片
        bitmapUtils.display(imageView_net,
                "http://09.imgmini.eastday.com/mobile/20171219/20171219073347_d41d8cd98f00b204e9800998ecf8427e_1_mwpm_03200403.jpg");

        // 加载本地图片(路径以/开头， 绝对路径)
        // bitmapUtils.display(imageView, "/sdcard/test.jpg");

        // 加载assets中的图片(路径以assets开头)
        // bitmapUtils.display(imageView, "assets/img/wallpaper.jpg");

    }

    /**
     * 加载本地图片
     * http://bbs.3gstdy.com
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
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
    private void sendRequestWithHttpClient_personal(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    ContentValues postParams = new ContentValues();
                    postParams.put("accountid", readLocal());
                    postParams.put("password", "123456");
                    String s = UrlManager.httpUrlConnectionPost("/hs/getInformationServlet", postParams);

                    JSONObject obj = new JSONObject(s);
                    name = obj.getString("phone");

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //
                        }
                    });
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
        personalList.clear();
        personalList2.clear();
        User user = new User(name, R.drawable.touxiang2, 1);
        personalList.add(user);
        User user1 = new User("aaa", R.drawable.apple_pic, 2);
        //personalList.add(user1);
        User userX1 = new User("个人资料", R.drawable.ic_account_box_black_24dp, 0);
        personalList2.add(userX1);
        User userX2 = new User("大学信息", R.drawable.ic_account_balance_black_24dp, 0);
        personalList2.add(userX2);
        User userX3 = new User("兴趣", R.drawable.ic_favorite_black_24dp_red, 0);
        personalList2.add(userX3);
        User userX4 = new User("我的新鲜事", R.drawable.ic_toys_black_24dp, 0);
        personalList2.add(userX4);
        User userX5 = new User("好友印象", R.drawable.ic_loyalty_black_24dp, 0);
        personalList2.add(userX5);
        User userX6 = new User("向朋友推荐芥末", R.drawable.ic_thumb_up_black_24dp, 0);
        personalList2.add(userX6);
        User userX7 = new User("设置", R.drawable.ic_brightness_low_black_24dp, 0);
        personalList2.add(userX7);
        //personalList.add(user1);
        /*ViewUtils.inject(getActivity());// 注入view和事件
        initView();*/
    }

    public static PersonalFragment newInstance(String s) {
        Bundle bundle = new Bundle();
        bundle.putString("ARGS",s);
        PersonalFragment fragment = new PersonalFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
