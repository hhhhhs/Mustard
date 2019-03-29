package com.hs.mustard.fragment;

import android.app.ActivityOptions;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;

import com.hs.mustard.samples.*;
import com.hs.mustard.adapter.*;
import com.hs.mustard.Until.ToastUtil;
import com.hs.mustard.R;
import com.hs.mustard.UrlManager1;
import com.hs.mustard.activity.NewsDetailActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by hs on 2017/12/4.
 */

public class FreshFragment extends Fragment {
    private List<News> newsList = new ArrayList<News>();
    private View view;
    private String[] name;
    private String[] url;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NewsListAdapter adapter;
    private News apple0 = new News("下拉刷新", R.drawable.default_useravatar);
    private News apple = new News("公益手牵手、共读一本书——记蒲公英队第二期“好书千里行”公益行动", R.drawable.default_useravatar);
    private News apple1 = new News("美宇航局利用谷歌人工智能新发现未知行星", R.drawable.default_useravatar);
    private News apple2 = new News("奶茶妹妹力压王思聪，成为500强富豪榜单最年轻女富豪", R.drawable.default_useravatar);
    private News apple3 = new News("2018好事成双，好运成对的生肖", R.drawable.default_useravatar);
    private News apple4 = new News("居民楼惊现2亿元钞票 用坏三台验钞机", R.drawable.default_useravatar);
    private News apple5 = new News("中国海军第29艘054A护卫舰下水", R.drawable.default_useravatar);
    private News apple6 = new News("美宇航局利用谷歌人工智能新发现未知行星", R.drawable.default_useravatar);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fresh_list_view, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setContentView(R.layout.talk_list_view);

        //下拉刷新
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.demo_swiperefreshlayout);
        //Log.d("swipeRefreshLayout----->", swipeRefreshLayout.toString());
        /*swipeRefreshLayout.setColorScheme(android.R.color.white,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);*/
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendRequestWithHttpClient_fresh();
                // TODO Auto-generated method stub
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        //sendRequestWithHttpClient_fresh();
                        swipeRefreshLayout.setRefreshing(false);
                        newsList.clear();
                        for (int i=0;i<29;i++){
                            apple = new News(name[i], R.drawable.default_useravatar);
                            newsList.add(apple);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, 1000);
            }
        });

        //sendRequestWithHttpClient_fresh();
        initUser();
        adapter = new NewsListAdapter(getActivity(), R.layout.fresh_item, newsList);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(FruitActivity.this, android.R.layout.simple_list_item_1, datas);
        ListView listView = (ListView)view.findViewById(R.id.list_view_fresh);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("position",Integer.toString(position));
                Log.i("id",String.valueOf(id));
                News news = newsList.get(position);
                ToastUtil.toast(getActivity(), news.getName());
                Intent intent=new Intent(getActivity(),NewsDetailActivity.class);//startActivity(intent);
                Bundle bundle = new Bundle();
                if (news.getName().equals("下拉刷新")){
                    url = new String[newsList.size()];
                    for (int i=0;i<newsList.size();i++){url[i]="http://wwww.baidu.com";}
                }
                bundle.putString("url",url[position]);
                intent.putExtras(bundle);
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
    public void sendRequestWithHttpClient_fresh(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    ContentValues postParams = new ContentValues();
                    name = new String[30];
                    url = new String[30];
                    postParams.put("type", "top");
                    postParams.put("key", "a1a755458cc22f129942b34904feb820");
                    String s = UrlManager1.httpUrlConnectionPost("/toutiao/index", postParams);
                    JSONObject obj = new JSONObject(s);
                    String r = obj.getString("result");
                    JSONObject result = new JSONObject(r);
                    String ds = result.getString("data");
                    JSONArray datas = new JSONArray(ds);
                    //obj.getJSONArray("").getJSONObject(1).getString("");
                    name = new String[datas.length()];//datas.length()
                    url = new String[datas.length()];
                    for(int i = 0; i < datas.length(); i++){
                        String d = datas.getString(i);
                        JSONObject data = new JSONObject(d);
                        name[i] = data.getString("title");
                        url[i] = data.getString("url");
                        Log.i("xxxxxxxx",name[i]);
                        Log.i("xxxxxxxx",url[i]);
                    }
                    Log.i("aaaaaaaaa",datas.length()+"");

                    /*int len = obj.length();
                    name = new String[len];
                    for(int i=0;i<len-1;i++){
                        name[i] = obj.getJSONArray("data").getString("title");
                    }*/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //initUser();
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
        //newsList.clear();
        //News apple = new News("公益手牵手、共读一本书——记蒲公英队第二期“好书千里行”公益行动", R.drawable.default_useravatar);
        //newsList.add(apple0);
        /*newsList.add(apple);
        newsList.add(apple1);
        newsList.add(apple2);
        newsList.add(apple3);
        newsList.add(apple4);
        newsList.add(apple5);
        newsList.add(apple6);*/
        for (int i = 0; i < 10; i++) {
            //Log.i("xxxxxxxx",name[i]);
            //apple = new News(name[i], R.drawable.default_useravatar);
            //newsList.add(apple0);
        }
    }


    public static FreshFragment newInstance(String s) {
        Bundle bundle = new Bundle();
        bundle.putString("ARGS",s);
        FreshFragment fragment = new FreshFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

}

/*

{
        "reason": "成功的返回",
        "result": {
        "stat": "1",
        "data": [
        {
        "uniquekey": "ac0b3b52931237fc8e4f11f1aeeb7146",
        "title": "公益手牵手、共读一本书——记蒲公英队第二期“好书千里行”公益行动",
        "date": "2017-12-18 12:20",
        "category": "头条",
        "author_name": "环球网",
        "url": "http://mini.eastday.com/mobile/171218122005543.html",
        "thumbnail_pic_s": "http://01.imgmini.eastday.com/mobile/20171218/20171218122005_bd23a5127b8ce4c1c84396325d79ed00_2_mwpm_03200403.jpg",
        "thumbnail_pic_s02": "http://01.imgmini.eastday.com/mobile/20171218/20171218122005_bd23a5127b8ce4c1c84396325d79ed00_3_mwpm_03200403.jpg",
        "thumbnail_pic_s03": "http://01.imgmini.eastday.com/mobile/20171218/20171218122005_bd23a5127b8ce4c1c84396325d79ed00_4_mwpm_03200403.jpg"
        },
        {
        "uniquekey": "0e6fecd424a6f55f26e129854aa6ea17",
        "title": "百岁侏儒女子，同嫁两个老公，老公年龄更吓人！",
        "date": "2017-12-18 11:45",
        "category": "头条",
        "author_name": "兔儿姐",
        "url": "http://mini.eastday.com/mobile/171218114529285.html",
        "thumbnail_pic_s": "http://02.imgmini.eastday.com/mobile/20171218/20171218_77869f1e99e0d1bab3b16d39bb6f9e2f_mwpm_03200403.jpg",
        "thumbnail_pic_s02": "http://02.imgmini.eastday.com/mobile/20171218/20171218_ec4d00eb4d2b7b9ad2cbde2be8b2c26f_mwpm_03200403.jpg",
        "thumbnail_pic_s03": "http://02.imgmini.eastday.com/mobile/20171218/20171218_15085812c4be237dc7fe24ef061d8d25_mwpm_03200403.jpg"
        },
        {
        "uniquekey": "328e971759bfbf216bd01e201535da78",
        "title": "美宇航局利用谷歌人工智能新发现未知行星",
        "date": "2017-12-18 11:29",
        "category": "头条",
        "author_name": "环球网",
        "url": "http://mini.eastday.com/mobile/171218112900387.html",
        "thumbnail_pic_s": "http://01.imgmini.eastday.com/mobile/20171218/20171218112900_1fe2525a1cfb764221d45435278950c1_1_mwpm_03200403.jpg"
        },
        {
        "uniquekey": "b1d672296dac7a42b605ee6357969af1",
        "title": "母亲拼下性命生下孩子，家人却将他低价卖出",
        "date": "2017-12-18 11:14",
        "category": "头条",
        "author_name": "奇闻达人",
        "url": "http://mini.eastday.com/mobile/171218111421620.html",
        "thumbnail_pic_s": "http://07.imgmini.eastday.com/mobile/20171218/20171218_59f16996e063589a424e363081db5b8b_cover_mwpm_03200403.jpg",
        "thumbnail_pic_s02": "http://07.imgmini.eastday.com/mobile/20171218/20171218_e35f82f7b6c4fb3feb4e8f3b2cc22975_cover_mwpm_03200403.jpg",
        "thumbnail_pic_s03": "http://07.imgmini.eastday.com/mobile/20171218/20171218_4d4031190ccb69e9ffd44ab3ef1c2c33_cover_mwpm_03200403.jpg"
        },
        {
        "uniquekey": "e74855f7e19963fe4accfe97156decc7",
        "title": "刚刚，朝鲜罕见出席安理会放重大消息：美国已被吓破胆我们会自卫",
        "date": "2017-12-18 11:13",
        "category": "头条",
        "author_name": "第一军情",
        "url": "http://mini.eastday.com/mobile/171218111336842.html",
        "thumbnail_pic_s": "http://06.imgmini.eastday.com/mobile/20171218/20171218_4eaa255973217cb001cfbd18861b0b0b_cover_mwpm_03200403.jpg",
        "thumbnail_pic_s02": "http://06.imgmini.eastday.com/mobile/20171218/20171218_c0c647e6e445eed47961bc68b38b8576_cover_mwpm_03200403.jpg"
        },
        {
        "uniquekey": "02615aa33315b41850a61648a2f20b71",
        "title": "港媒:\"不尊重国歌事件\"系预谋 媒体假扮亲友入场",
        "date": "2017-12-18 11:11",
        "category": "头条",
        "author_name": "海外网",
        "url": "http://mini.eastday.com/mobile/171218111116618.html",
        "thumbnail_pic_s": "http://07.imgmini.eastday.com/mobile/20171218/20171218111116_e2d8a03a561c210510f189cd5b755e78_1_mwpm_03200403.jpg"
        },
        {
        "uniquekey": "874de0270eb089d5db23ae078d4951a4",
        "title": "张朋受邀参加滑雪明星赛 展现运动魅力",
        "date": "2017-12-18 11:08",
        "category": "头条",
        "author_name": "环球网",
        "url": "http://mini.eastday.com/mobile/171218110831205.html",
        "thumbnail_pic_s": "http://07.imgmini.eastday.com/mobile/20171218/20171218110831_32e8661e249fe19c092dee7804dde76a_1_mwpm_03200403.jpg"
        },
        {
        "uniquekey": "15708765807336c790603f1432248fc8",
        "title": "这是当下澳大利亚最大的丑闻！1.5万儿童被性侵，神父和教师最多",
        "date": "2017-12-18 11:08",
        "category": "头条",
        "author_name": "环球网",
        "url": "http://mini.eastday.com/mobile/171218110830271.html",
        "thumbnail_pic_s": "http://05.imgmini.eastday.com/mobile/20171218/20171218110830_2c5830d8ab5754968b06b8c11350789a_1_mwpm_03200403.jpg",
        "thumbnail_pic_s02": "http://05.imgmini.eastday.com/mobile/20171218/20171218110830_2c5830d8ab5754968b06b8c11350789a_2_mwpm_03200403.jpg",
        "thumbnail_pic_s03": "http://05.imgmini.eastday.com/mobile/20171218/20171218110830_2c5830d8ab5754968b06b8c11350789a_3_mwpm_03200403.jpg"
        },
        {
        "uniquekey": "9e3fc87104a620f8e045ab6012afe507",
        "title": "曾风华绝代的她洗尽铅华，55岁复出主持节目，场场令人潸然泪下",
        "date": "2017-12-18 11:08",
        "category": "头条",
        "author_name": "部落酋长",
        "url": "http://mini.eastday.com/mobile/171218110813588.html",
        "thumbnail_pic_s": "http://04.imgmini.eastday.com/mobile/20171218/20171218110813_6448533ab3862ef439a3d017d607d2be_6_mwpm_03200403.jpg",
        "thumbnail_pic_s02": "http://04.imgmini.eastday.com/mobile/20171218/20171218110813_6448533ab3862ef439a3d017d607d2be_5_mwpm_03200403.jpg",
        "thumbnail_pic_s03": "http://04.imgmini.eastday.com/mobile/20171218/20171218110813_6448533ab3862ef439a3d017d607d2be_1_mwpm_03200403.jpg"
        },
        {
        "uniquekey": "ac48e4e945389b340362cf4e065e88b5",
        "title": "女生在火车卧铺睡觉，半夜被男子做了这件事，男子称只为寻求刺激",
        "date": "2017-12-18 10:56",
        "category": "头条",
        "author_name": "爱影播客",
        "url": "http://mini.eastday.com/mobile/171218105644343.html",
        "thumbnail_pic_s": "http://01.imgmini.eastday.com/mobile/20171218/20171218_b777f25f557956dd3a926c644113f988_cover_mwpm_03200403.jpg",
        "thumbnail_pic_s02": "http://01.imgmini.eastday.com/mobile/20171218/20171218_23a649952def5afef48e6f8fb69fdf25_cover_mwpm_03200403.jpg",
        "thumbnail_pic_s03": "http://01.imgmini.eastday.com/mobile/20171218/20171218_debd31026516614c52f2650ec5023ab2_cover_mwpm_03200403.jpg"
        },
        {
        "uniquekey": "1222a6ac8a78019e9313b3d6b6047000",
        "title": "2018年好运连连没事偷着乐的四大生肖",
        "date": "2017-12-18 10:56",
        "category": "头条",
        "author_name": "恋星座",
        "url": "http://mini.eastday.com/mobile/171218105642254.html",
        "thumbnail_pic_s": "http://02.imgmini.eastday.com/mobile/20171218/20171218_859a93725ab0a45665c698b0ba6babe8_cover_mwpm_03200403.jpg",
        "thumbnail_pic_s02": "http://02.imgmini.eastday.com/mobile/20171218/20171218_80512815ea8d8bf689552ebb712618de_cover_mwpm_03200403.jpg",
        "thumbnail_pic_s03": "http://02.imgmini.eastday.com/mobile/20171218/20171218_71584981cdfdc3c3be5a1920c0087578_cover_mwpm_03200403.jpg"
        },
        {
        "uniquekey": "b53fb829c56ed8c11ed20799290c7d95",
        "title": "一小时打遍全球！中国多项太空技术已大幅度领先美国",
        "date": "2017-12-18 10:45",
        "category": "头条",
        "author_name": "利刃军事",
        "url": "http://mini.eastday.com/mobile/171218104523354.html",
        "thumbnail_pic_s": "http://00.imgmini.eastday.com/mobile/20171218/20171218_e630b83a135efb284e522b41e4d82641_cover_mwpm_03200403.jpg",
        "thumbnail_pic_s02": "http://00.imgmini.eastday.com/mobile/20171218/20171218_f762f5d07111aaaeb7997eb1d2bb2cdb_cover_mwpm_03200403.jpg",
        "thumbnail_pic_s03": "http://00.imgmini.eastday.com/mobile/20171218/20171218_cd712a1e60c30dea91d5e6e890fd8b70_cover_mwpm_03200403.jpg"
        },
        {
        "uniquekey": "9b0e6d933cf6f622c4de97b28dd7b454",
        "title": "这对夫妻的墓葬很是邪门，每次被挖必有异样，千年来没有人敢进去",
        "date": "2017-12-18 10:39",
        "category": "头条",
        "author_name": "山川文社",
        "url": "http://mini.eastday.com/mobile/171218103943476.html",
        "thumbnail_pic_s": "http://01.imgmini.eastday.com/mobile/20171218/20171218_7016d87387c661e019cbaf11fd86a99a_cover_mwpm_03200403.jpg",
        "thumbnail_pic_s02": "http://01.imgmini.eastday.com/mobile/20171218/20171218_c93a041601af927374baeb5413b47b32_cover_mwpm_03200403.jpg",
        "thumbnail_pic_s03": "http://01.imgmini.eastday.com/mobile/20171218/20171218_65d3fa85e3a55dbd1a424b974dcd6ec1_cover_mwpm_03200403.jpg"
        },
        {
        "uniquekey": "c74263034c7dcd1fbafeeb6faf6188f9",
        "title": "国外一个国家，美女成灾，结婚不要豪车豪宅，可男人却不太敢娶！",
        "date": "2017-12-18 10:37",
        "category": "头条",
        "author_name": "柠檬论车",
        "url": "http://mini.eastday.com/mobile/171218103749495.html",
        "thumbnail_pic_s": "http://09.imgmini.eastday.com/mobile/20171218/20171218_43515aa4c015c7e478a1da26a02255c1_cover_mwpm_03200403.jpg",
        "thumbnail_pic_s02": "http://09.imgmini.eastday.com/mobile/20171218/20171218_6131f9d13de14eed262472cda27ee161_cover_mwpm_03200403.jpg",
        "thumbnail_pic_s03": "http://09.imgmini.eastday.com/mobile/20171218/20171218_c82f51568e5ce53b804087fca907918e_cover_mwpm_03200403.jpg"
        },
        {
        "uniquekey": "e318bb6cbacee2613e2294424e3e8158",
        "title": "现实版《人民的名义》：居民楼惊现2亿元现金，3辆运钞车才装走",
        "date": "2017-12-18 10:07",
        "category": "头条",
        "author_name": "杀生丸",
        "url": "http://mini.eastday.com/mobile/171218100732758.html",
        "thumbnail_pic_s": "http://00.imgmini.eastday.com/mobile/20171218/20171218_8169d322765ada8323ab5f2c40247d7c_mwpm_03200403.jpg",
        "thumbnail_pic_s02": "http://00.imgmini.eastday.com/mobile/20171218/20171218_66b49ed3f7e4b89f1477e3c6e03e152f_mwpm_03200403.jpg",
        "thumbnail_pic_s03": "http://00.imgmini.eastday.com/mobile/20171218/20171218_57172c698c161cfa11779ebbc4b0ad6a_mwpm_03200403.jpg"
        },
        {
        "uniquekey": "a15b0b3878f8000d4e3b50ba87a3ad20",
        "title": "汪莹纯任蚌埠市委书记 张岳峰任马鞍山市委书记",
        "date": "2017-12-18 09:57",
        "category": "头条",
        "author_name": "人民网",
        "url": "http://mini.eastday.com/mobile/171218095713577.html",
        "thumbnail_pic_s": "http://02.imgmini.eastday.com/mobile/20171218/20171218095713_7c8d2a782c09c24fb5b49a9556fd38bd_1_mwpm_03200403.jpg",
        "thumbnail_pic_s02": "http://02.imgmini.eastday.com/mobile/20171218/20171218095713_244d2fae07a9f1f62073026a88c2a656_2_mwpm_03200403.jpg"
        },
        {
        "uniquekey": "0aefe4515fc1965b7e3fd6883da2cb55",
        "title": "雷绍业任湖南省怀化市政府副市长、代理市长",
        "date": "2017-12-18 09:57",
        "category": "头条",
        "author_name": "人民网",
        "url": "http://mini.eastday.com/mobile/171218095713430.html",
        "thumbnail_pic_s": "http://04.imgmini.eastday.com/mobile/20171218/20171218095713_8968c2f05f6c89ab6ab0d4522c034092_1_mwpm_03200403.jpg"
        },
        {
        "uniquekey": "b8890064262d795cfcbb2b28f5d536e7",
        "title": "白萝卜和它是绝配，做法简单营养丰富，冬天来一碗全身暖呼呼的",
        "date": "2017-12-18 09:56",
        "category": "头条",
        "author_name": "三棵树",
        "url": "http://mini.eastday.com/mobile/171218095643113.html",
        "thumbnail_pic_s": "http://00.imgmini.eastday.com/mobile/20171218/20171218_37aba36b7d8916141fdfe4ce628f866c_cover_mwpm_03200403.jpg",
        "thumbnail_pic_s02": "http://00.imgmini.eastday.com/mobile/20171218/20171218_861b7bb14f98c341a532cc4a20abc7a5_cover_mwpm_03200403.jpg",
        "thumbnail_pic_s03": "http://00.imgmini.eastday.com/mobile/20171218/20171218_408eb06d26b532c70d7c187c51236b4a_cover_mwpm_03200403.jpg"
        },
        {
        "uniquekey": "7c2f21d527c628161164065a88b718f2",
        "title": "中国海军第29艘054A护卫舰下水",
        "date": "2017-12-18 09:54",
        "category": "头条",
        "author_name": "中国网",
        "url": "http://mini.eastday.com/mobile/171218095445286.html",
        "thumbnail_pic_s": "http://00.imgmini.eastday.com/mobile/20171218/20171218095445_cbe8f460485c61533b5a687bd91074bf_8_mwpm_03200403.jpg",
        "thumbnail_pic_s02": "http://00.imgmini.eastday.com/mobile/20171218/20171218095445_44f43d5e42f6021ca9de2b1737a7c347_7_mwpm_03200403.jpg",
        "thumbnail_pic_s03": "http://00.imgmini.eastday.com/mobile/20171218/20171218095445_d357167fb998e3222c66b79e5153f804_4_mwpm_03200403.jpg"
        },
        {
        "uniquekey": "db240668b27a3561dc1411e4201e6d23",
        "title": "一棵白菜挽救整个冬天！最美味的白菜吃法都在这了！",
        "date": "2017-12-18 09:53",
        "category": "头条",
        "author_name": "美食in味道",
        "url": "http://mini.eastday.com/mobile/171218095306891.html",
        "thumbnail_pic_s": "http://09.imgmini.eastday.com/mobile/20171218/20171218095306_81a4391d92bc39e4578d15ec8a47b2ea_19_mwpm_03200403.jpg",
        "thumbnail_pic_s02": "http://09.imgmini.eastday.com/mobile/20171218/20171218095306_81a4391d92bc39e4578d15ec8a47b2ea_13_mwpm_03200403.jpg",
        "thumbnail_pic_s03": "http://09.imgmini.eastday.com/mobile/20171218/20171218095306_81a4391d92bc39e4578d15ec8a47b2ea_5_mwpm_03200403.jpg"
        },
        {
        "uniquekey": "1d8d3fefbc6e8480fc946a99d4059d95",
        "title": "这国曾宣布禁止出售航母给中国,如今主动寻求中国合作,决不答应",
        "date": "2017-12-18 09:46",
        "category": "头条",
        "author_name": "当兵女神",
        "url": "http://mini.eastday.com/mobile/171218094635020.html",
        "thumbnail_pic_s": "http://04.imgmini.eastday.com/mobile/20171218/20171218_6068b91eb457c59783d7ea0cf2521270_cover_mwpm_03200403.jpg",
        "thumbnail_pic_s02": "http://04.imgmini.eastday.com/mobile/20171218/20171218_51a5d739bcbafe6632aad8315311098d_cover_mwpm_03200403.jpg",
        "thumbnail_pic_s03": "http://04.imgmini.eastday.com/mobile/20171218/20171218_b9cc5f148832879fea31c335523b1740_cover_mwpm_03200403.jpg"
        },
        {
        "uniquekey": "3ba8eadff4d476cba41e10b90a174d1b",
        "title": "美德州现惊悚“尸体农场” 70具腐尸装铁笼暴晒防秃鹰啄食",
        "date": "2017-12-18 09:46",
        "category": "头条",
        "author_name": "环球网",
        "url": "http://mini.eastday.com/mobile/171218094629544.html",
        "thumbnail_pic_s": "http://05.imgmini.eastday.com/mobile/20171218/20171218094629_865b2254272e28718d91a2153a674630_2_mwpm_03200403.jpg",
        "thumbnail_pic_s02": "http://05.imgmini.eastday.com/mobile/20171218/20171218094629_865b2254272e28718d91a2153a674630_4_mwpm_03200403.jpg",
        "thumbnail_pic_s03": "http://05.imgmini.eastday.com/mobile/20171218/20171218094629_865b2254272e28718d91a2153a674630_1_mwpm_03200403.jpg"
        },
        {
        "uniquekey": "af5c945234e29e4d04bcd7b2c3632987",
        "title": "2018年一到好日子马上来的三个生肖，财神给力，财运更是了得",
        "date": "2017-12-18 09:46",
        "category": "头条",
        "author_name": "小易日记",
        "url": "http://mini.eastday.com/mobile/171218094621277.html",
        "thumbnail_pic_s": "http://07.imgmini.eastday.com/mobile/20171218/20171218_dccec0229d5d2a913a0e42e59dfdf033_cover_mwpm_03200403.jpg",
        "thumbnail_pic_s02": "http://07.imgmini.eastday.com/mobile/20171218/20171218_098fb5e224b75cef7d830c2d74b2de90_cover_mwpm_03200403.jpg",
        "thumbnail_pic_s03": "http://07.imgmini.eastday.com/mobile/20171218/20171218_26a1a2bde67c7c4874a6dfa95c94f050_cover_mwpm_03200403.jpg"
        },
        {
        "uniquekey": "9ce8b541857cc4d8ed56b8355a291a46",
        "title": "为什么有人找不到对象，心理学今年最新研究，很多人都还不知道！",
        "date": "2017-12-18 09:30",
        "category": "头条",
        "author_name": "考拉情感",
        "url": "http://mini.eastday.com/mobile/171218093026084.html",
        "thumbnail_pic_s": "http://01.imgmini.eastday.com/mobile/20171218/20171218093026_eae19d311696674f970077e67b5af565_2_mwpm_03200403.jpg",
        "thumbnail_pic_s02": "http://01.imgmini.eastday.com/mobile/20171218/20171218093026_eae19d311696674f970077e67b5af565_1_mwpm_03200403.jpg",
        "thumbnail_pic_s03": "http://01.imgmini.eastday.com/mobile/20171218/20171218093026_eae19d311696674f970077e67b5af565_3_mwpm_03200403.jpg"
        },
        {
        "uniquekey": "aa03240a78216d90b4c909988137d051",
        "title": "张闻天怎样处理群体性事件",
        "date": "2017-12-18 09:30",
        "category": "头条",
        "author_name": "人民网",
        "url": "http://mini.eastday.com/mobile/171218093024124.html",
        "thumbnail_pic_s": "http://04.imgmini.eastday.com/mobile/20171218/20171218093024_7f993f2271268116bcadfe99e01a78f9_1_mwpm_03200403.jpg"
        },
        {
        "uniquekey": "cf7538518cf6bafccb6ed76b09e40650",
        "title": "女大学生深夜打车遗落苹果8，司机竟然让他花钱买！",
        "date": "2017-12-18 09:27",
        "category": "头条",
        "author_name": "听闻时光",
        "url": "http://mini.eastday.com/mobile/171218092721608.html",
        "thumbnail_pic_s": "http://08.imgmini.eastday.com/mobile/20171218/20171218_b15813c5b2aa954ab79c13aa72b5582b_mwpm_03200403.jpg"
        },
        {
        "uniquekey": "f278441de1a8c19dcc519195f4826975",
        "title": "居民楼惊现2亿元钞票 用坏三台验钞机",
        "date": "2017-12-18 09:25",
        "category": "头条",
        "author_name": "紫羽",
        "url": "http://mini.eastday.com/mobile/171218092534208.html",
        "thumbnail_pic_s": "http://05.imgmini.eastday.com/mobile/20171218/20171218_f23247dea04fc8febdfff30268501c79_mwpm_03200403.jpg",
        "thumbnail_pic_s02": "http://05.imgmini.eastday.com/mobile/20171218/20171218_e844774e3cf6059a5d89b1d09f227a13_mwpm_03200403.jpg",
        "thumbnail_pic_s03": "http://05.imgmini.eastday.com/mobile/20171218/20171218_553a4db531b8b6ab732be85f038e6ce0_mwpm_03200403.jpg"
        },
        {
        "uniquekey": "7ce73eb0f29b394c1aac4338237bc1d7",
        "title": "奶茶妹妹力压王思聪，成为500强富豪榜单最年轻女富豪",
        "date": "2017-12-18 09:19",
        "category": "头条",
        "author_name": "巫婆说",
        "url": "http://mini.eastday.com/mobile/171218091940218.html",
        "thumbnail_pic_s": "http://05.imgmini.eastday.com/mobile/20171218/20171218091940_102fe695f632ee48f272d52810646429_2_mwpm_03200403.jpg",
        "thumbnail_pic_s02": "http://05.imgmini.eastday.com/mobile/20171218/20171218091940_102fe695f632ee48f272d52810646429_3_mwpm_03200403.jpg",
        "thumbnail_pic_s03": "http://05.imgmini.eastday.com/mobile/20171218/20171218091940_102fe695f632ee48f272d52810646429_1_mwpm_03200403.jpg"
        },
        {
        "uniquekey": "9d24995918702677e1c9d1796642fea9",
        "title": "2018好事成双，好运成对的生肖",
        "date": "2017-12-18 09:17",
        "category": "头条",
        "author_name": "星座运势",
        "url": "http://mini.eastday.com/mobile/171218091749264.html",
        "thumbnail_pic_s": "http://09.imgmini.eastday.com/mobile/20171218/20171218_9a6b47c318d32eb65b49dd03e1e9d39c_cover_mwpm_03200403.jpg",
        "thumbnail_pic_s02": "http://09.imgmini.eastday.com/mobile/20171218/20171218_5c5cc9b3d8be4f91716e74bcda2ee8a8_cover_mwpm_03200403.jpg",
        "thumbnail_pic_s03": "http://09.imgmini.eastday.com/mobile/20171218/20171218_37f12adecbe22a40b7cc39de66958333_cover_mwpm_03200403.jpg"
        },
        {
        "uniquekey": "5e538f9d957c2e8ef55cbb0b3168df91",
        "title": "与欧盟“离婚官司”还没完，英国就想和中国“度蜜月”？",
        "date": "2017-12-18 09:16",
        "category": "头条",
        "author_name": "观察者网",
        "url": "http://mini.eastday.com/mobile/171218091656442.html",
        "thumbnail_pic_s": "http://08.imgmini.eastday.com/mobile/20171218/20171218091656_305ee0e29393bff7942afb26e3202b00_2_mwpm_03200403.jpg",
        "thumbnail_pic_s02": "http://08.imgmini.eastday.com/mobile/20171218/20171218091656_305ee0e29393bff7942afb26e3202b00_3_mwpm_03200403.jpg",
        "thumbnail_pic_s03": "http://08.imgmini.eastday.com/mobile/20171218/20171218091656_305ee0e29393bff7942afb26e3202b00_1_mwpm_03200403.jpg"
        }
        ]
        },
        "error_code": 0
        }
*/
