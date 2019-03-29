package com.hs.mustard.activity;

import android.app.ActivityOptions;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.TextBadgeItem;
import com.ashokvarma.bottomnavigation.ShapeBadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.hs.mustard.*;
import com.hs.mustard.Until.*;
import com.hs.mustard.fragment.*;

import java.util.ArrayList;
import java.util.List;

import com.hs.lib.CardConfig;
import com.hs.lib.CardItemTouchHelperCallback;
import com.hs.lib.CardLayoutManager;
import com.hs.lib.OnSwipeListener;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;

import org.json.JSONObject;

import static com.ashokvarma.bottomnavigation.ShapeBadgeItem.SHAPE_OVAL;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    //private ActivityStyleBinding bind;
    //private List<Integer> list = new ArrayList<>();

    private BottomNavigationBar mBottomNavigationBar;
    private FreshFragment freshFragment;
    private FindFragment findFragment;
    private HobbyFragment hobbyFragment;
    private TalkFragment talkFragment;
    private PersonalFragment personalFragment;

    private TextBadgeItem badgeItem;
    private ShapeBadgeItem badgeItem1;
    private TextBadgeItem badgeItem2;
    private TextBadgeItem badgeItem3;
    private TextBadgeItem badgeItem4;
    private int num = 0;
    private ImageView mIconView;
    private ImageView mIconView2;
    private ImageView imageView_net;

    private TextView textView_personal_information;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ToastUtil.toast(MainActivity.this, "下拉刷新(•̀o•́)ง");
        getSupportActionBar().hide();//隐藏标题
        SysApplication.getInstance().addActivity(MainActivity.this);//加入activity列表

        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

        /*** the setting for BadgeItem ***/

        badgeItem = new TextBadgeItem();
        if (num < 1) {
            badgeItem.hide();
        } else {
            badgeItem.setHideOnSelect(false)
                    .setText(String.valueOf(num))
                    .setBackgroundColorResource(R.color.colorRed)
                    .setBorderWidth(0);
        }

        badgeItem1 = new ShapeBadgeItem();
        badgeItem1.setHideOnSelect(false)
                .setShape(SHAPE_OVAL)
                .setShapeColorResource(R.color.colorRed)
                .setSizeInPixels(30,30);

        badgeItem2 = new TextBadgeItem();
        badgeItem2.setHideOnSelect(false)
                .setText("2")
                .setBackgroundColorResource(R.color.colorRed)
                .setBorderWidth(0);

        badgeItem3 = new TextBadgeItem();
        badgeItem3.setHideOnSelect(false)
                .setText("3")
                .setBackgroundColorResource(R.color.colorRed)
                .setBorderWidth(0);

        badgeItem4 = new TextBadgeItem();
        badgeItem4.setHideOnSelect(false)
                .setText("4")
                .setBackgroundColorResource(R.color.colorRed)
                .setBorderWidth(0);


        /*** the setting for BottomNavigationBar ***/

        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        //mBottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        //mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        mBottomNavigationBar.setBarBackgroundColor(R.color.colorWhite);//set background color for navigation bar
        mBottomNavigationBar.setInActiveColor(R.color.colorGrayDark);//unSelected icon color
        BottomNavigationItem bottomNavigationItem0 = new BottomNavigationItem(R.drawable.ic_star_black_24dp, R.string.title_fresh)
                .setInactiveIcon(ContextCompat.getDrawable(this,R.drawable.ic_star_border_black_24dp))
                .setBadgeItem(badgeItem);
        BottomNavigationItem bottomNavigationItem1 = new BottomNavigationItem(R.drawable.ic_people_black_24dp, R.string.title_find)
                .setInactiveIcon(ContextCompat.getDrawable(this,R.drawable.ic_people_outline_black_24dp))
                .setBadgeItem(badgeItem1);
        BottomNavigationItem bottomNavigationItem2 = new BottomNavigationItem(R.drawable.ic_favorite_black_24dp, R.string.title_hobby)
                .setInactiveIcon(ContextCompat.getDrawable(this,R.drawable.ic_favorite_border_black_24dp))
                /*.setBadgeItem(badgeItem2)*/;
        BottomNavigationItem bottomNavigationItem3 = new BottomNavigationItem(R.drawable.ic_chat_bubble_black_24dp, R.string.title_talk)
                .setInactiveIcon(ContextCompat.getDrawable(this,R.drawable.ic_chat_bubble_outline_black_24dp))
                .setBadgeItem(badgeItem3);
        BottomNavigationItem bottomNavigationItem4 = new BottomNavigationItem(R.drawable.ic_person_black_24dp, R.string.title_personal)
                .setInactiveIcon(ContextCompat.getDrawable(this,R.drawable.ic_person_outline_black_24dp))
                .setBadgeItem(badgeItem4);
        mBottomNavigationBar.addItem(bottomNavigationItem0)
                .addItem(bottomNavigationItem1)
                .addItem(bottomNavigationItem2)
                .addItem(bottomNavigationItem3)
                .addItem(bottomNavigationItem4)
                .setFirstSelectedPosition(0)
                .initialise();

        mBottomNavigationBar.setTabSelectedListener(this);
        setDefaultFragment();


        //获取 bar 的 所对应的子 view 控件，方便扩展动画
        LayoutInflater inflater = LayoutInflater.from(this);
        View parentView = inflater.inflate(com.ashokvarma.bottomnavigation.R.layout.bottom_navigation_bar_container, mBottomNavigationBar, true);
        LinearLayout mTabContainer = (LinearLayout) parentView.findViewById(com.ashokvarma.bottomnavigation.R.id.bottom_navigation_bar_item_container);
        //绑定图标
        mIconView = (ImageView) mTabContainer.getChildAt(0).findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_icon);
        //mIconView2 = (ImageView) mTabContainer.getChildAt(2).findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_icon);


    }

    /**
     * set the default fragment
     */
    private void setDefaultFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        freshFragment = freshFragment.newInstance("First Fragment");
        transaction.replace(R.id.content, freshFragment).commit();
    }
    /**
     * set the default fragment
     */
    private void setDefaultFragment1() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        personalFragment = personalFragment.newInstance("Fifth Fragment");
        transaction.replace(R.id.content, personalFragment).commit();
    }


    /**
     * 设置tab数字提示加缩放动画
     */
    private void setBadgeNum(int num) {
        badgeItem.setText(String.valueOf(num));
        if (num < 1) {
            badgeItem.hide();
        } else {
            badgeItem.show();
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_view_badge);
            //ObjectAnimator.ofFloat(mIconView, "translationX", 2000, 0).start();
            mIconView.startAnimation(animation);
        }
    }


    //服务端通信
    private void sendRequestWithHttpClient_personal(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    ContentValues postParams = new ContentValues();
                    postParams.put("accountid", "13601350078");
                    postParams.put("password", "123456");
                    String s = UrlManager.httpUrlConnectionPost("/hs/getInformationServlet", postParams);
                    runOnUiThread(new Runnable() {
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
                    ToastUtil.toast(MainActivity.this, phone+name+sex+real_name+email);
                    Looper.loop();
                    Thread.sleep(2000);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /*//+1
    public void button_fresh_onClick(View view){
        view.setSaveEnabled(false);
        sendRequestWithHttpClient_personal();
        setBadgeNum(++num);
        mBottomNavigationBar.hide();//隐藏
        if(num>1){mBottomNavigationBar.show();}//显示
    }*/

    public void button_addFriends_onClick(View view){
        view.setSaveEnabled(false);
        sendRequestWithHttpClient_personal();
        //ToastUtil.toast(MainActivity.this, "");
    }

    public void button_del_onClick(View view){
        view.setSaveEnabled(false);
        ToastUtil.toast(MainActivity.this, "移除");
    }

    @Override
    public void onTabSelected(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (position) {
            case 0:
                if (freshFragment == null) {
                    freshFragment = FreshFragment.newInstance("First Fragment");
                }
                if(ifExisted()){
                    ToastUtil.toast(MainActivity.this, "下拉刷新(•̀o•́)ง");
                    transaction.replace(R.id.content, freshFragment);
                    //Intent intent=new Intent(this,NewsDetailActivity.class);//startActivity(intent);
                    //startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                }else{
                    //切换回按钮
                    Intent intent=new Intent(MainActivity.this,login.class);startActivity(intent);
                }
                break;
            case 1:
                if (findFragment == null) {
                    findFragment = FindFragment.newInstance("Second Fragment");
                }
                if(ifExisted()){
                    transaction.replace(R.id.content, findFragment);
                }else{
                    //切换回按钮
                    Intent intent=new Intent(MainActivity.this,login.class);startActivity(intent);
                }
                break;
            case 2:
                if (hobbyFragment == null) {
                    hobbyFragment = HobbyFragment.newInstance("Third Fragment");
                }
                if(ifExisted()){
                    transaction.replace(R.id.content, hobbyFragment);
                    //Intent intent=new Intent(MainActivity.this,slipActivity.class);//startActivity(intent);
                    //startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                }else{
                    //切换回按钮
                    Intent intent=new Intent(MainActivity.this,login.class);startActivity(intent);
                }
                break;
            case 3:
                if (talkFragment == null) {
                    talkFragment = TalkFragment.newInstance("Forth Fragment");
                }
                if(ifExisted()){
                    transaction.replace(R.id.content,talkFragment);
                }else{
                    //切换回按钮
                    Intent intent=new Intent(MainActivity.this,login.class);startActivity(intent);
                }
                break;
            case 4:
                if (personalFragment == null) {
                    personalFragment = PersonalFragment.newInstance("Fifth Fragment");
                }
                if(ifExisted()){
                    transaction.replace(R.id.content, personalFragment);
                    ///////////////////
                    /*//在线图片
                    imageView_net = (ImageView)findViewById(R.id.textView_personal_image);
                    ViewUtils.inject(this);// 注入view和事件
                    initView();*/
                }else{
                    //切换回按钮
                    Intent intent=new Intent(MainActivity.this,login.class);startActivity(intent);
                }
                break;
            default:
                if (freshFragment == null) {
                    freshFragment = FreshFragment.newInstance("First Fragment");
                }
                transaction.replace(R.id.content, freshFragment);
                break;
        }
        transaction.commit();

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    private void initView() {
        // TODO Auto-generated method stub
        BitmapUtils bitmapUtils = new BitmapUtils(this);
        // 加载网络图片
        bitmapUtils.display(imageView_net,
                "http://09.imgmini.eastday.com/mobile/20171219/20171219073347_d41d8cd98f00b204e9800998ecf8427e_1_mwpm_03200403.jpg");

        // 加载本地图片(路径以/开头， 绝对路径)
        // bitmapUtils.display(imageView, "/sdcard/test.jpg");

        // 加载assets中的图片(路径以assets开头)
        // bitmapUtils.display(imageView, "assets/img/wallpaper.jpg");
    }


    //退出
    long onBackPressed_startTime = 0;
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - onBackPressed_startTime) >= 1000) {
            ToastUtil.toast(MainActivity.this, "要离开了么( •̥́ ˍ •̀ू )");
            onBackPressed_startTime = currentTime;
        } else {
            MainActivity.this.finish();
            SysApplication.getInstance().exit();//关闭所有activity
        }
    }

    //本地是否已存在
    public boolean ifExisted(){
        //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getSharedPreferences("lock", MODE_PRIVATE);
        //步骤2：获取文件中的值
        String value = read.getString("code", "");
        if(!value.isEmpty()){
            return true;
        }
        //ToastUtil.toast(getApplicationContext(), "口令为："+value);
        return false;
    }

    //删除
    public void del(){
        SharedPreferences del= getSharedPreferences("lock", MODE_PRIVATE);
        SharedPreferences.Editor editor = del.edit();
        editor.remove("code");
        editor.commit();
    }

    //注销登录
    public void ccc(View view){
        del();
        view.setSaveEnabled(false);
        Intent ccc = new Intent(MainActivity.this,MainActivity.class);
        startActivity(ccc);
        //home.this.finish();
    }





    /*private void initView() {
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new MyAdapter());
        CardItemTouchHelperCallback cardCallback = new CardItemTouchHelperCallback(recyclerView.getAdapter(), list);
        cardCallback.setOnSwipedListener(new OnSwipeListener<Integer>() {

            @Override
            public void onSwiping(RecyclerView.ViewHolder viewHolder, float ratio, int direction) {
                MyAdapter.MyViewHolder myHolder = (MyAdapter.MyViewHolder) viewHolder;
                viewHolder.itemView.setAlpha(1 - Math.abs(ratio) * 0.2f);
                if (direction == CardConfig.SWIPING_LEFT) {
                    myHolder.dislikeImageView.setAlpha(Math.abs(ratio));
                } else if (direction == CardConfig.SWIPING_RIGHT) {
                    myHolder.likeImageView.setAlpha(Math.abs(ratio));
                } else {
                    myHolder.dislikeImageView.setAlpha(0f);
                    myHolder.likeImageView.setAlpha(0f);
                }
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, Integer o, int direction) {
                MyAdapter.MyViewHolder myHolder = (MyAdapter.MyViewHolder) viewHolder;
                viewHolder.itemView.setAlpha(1f);
                myHolder.dislikeImageView.setAlpha(0f);
                myHolder.likeImageView.setAlpha(0f);
                Toast.makeText(MainActivity.this, direction == CardConfig.SWIPED_LEFT ? "swiped left" : "swiped right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipedClear() {
                Toast.makeText(MainActivity.this, "data clear", Toast.LENGTH_SHORT).show();
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                }, 3000L);
            }

        });
        final ItemTouchHelper touchHelper = new ItemTouchHelper(cardCallback);
        final CardLayoutManager cardLayoutManager = new CardLayoutManager(recyclerView, touchHelper);
        recyclerView.setLayoutManager(cardLayoutManager);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    private void initData() {
        list.add(R.drawable.img_avatar_01);
        list.add(R.drawable.img_avatar_02);
        list.add(R.drawable.img_avatar_03);
        list.add(R.drawable.img_avatar_04);
        list.add(R.drawable.img_avatar_05);
        list.add(R.drawable.img_avatar_06);
        list.add(R.drawable.img_avatar_07);
    }

    private class MyAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ImageView avatarImageView = ((MyViewHolder) holder).avatarImageView;
            avatarImageView.setImageResource(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView avatarImageView;
            ImageView likeImageView;
            ImageView dislikeImageView;

            MyViewHolder(View itemView) {
                super(itemView);
                avatarImageView = (ImageView) itemView.findViewById(R.id.iv_avatar);
                likeImageView = (ImageView) itemView.findViewById(R.id.iv_like);
                dislikeImageView = (ImageView) itemView.findViewById(R.id.iv_dislike);
            }

        }
    }*/



}
