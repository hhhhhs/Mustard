/*
package com.hs.mustard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hs.mustard.Until.ToastUtil;
import com.hs.mustard.fragment.*;

public class home extends AppCompatActivity {

    //private ActivityStyleBinding bind;

    private TextView mTextMessage;

    private Fragment f1,f2,f3,f4,f5;
    private FragmentManager manager;
    private FragmentTransaction transaction;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            manager = getSupportFragmentManager();
            transaction = manager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_fresh:
                    //mTextMessage.setText(R.string.title_fresh);
                    //setContentView(R.layout.fragment_fresh);
                    if(ifExisted()){
                        hideFragment(transaction);
                        f1 = new freshFragment();
                        transaction.replace(R.id.content, f1);
                        transaction.commit();
                    }else{
                        item.setChecked(true);//选中当前按钮
                        Intent intent=new Intent(home.this,login.class);startActivity(intent);
                    }
                    return true;
                case R.id.navigation_find:
                    //mTextMessage.setText(R.string.title_find);
                    //setContentView(R.layout.fragment_find);
                    if(ifExisted()){
                        hideFragment(transaction);
                        f2 = new findFragment();
                        transaction.replace(R.id.content, f2);
                        transaction.commit();
                    }else{
                        Intent intent=new Intent(home.this,login.class);startActivity(intent);
                    }
                    return true;
                case R.id.navigation_hobby:
                    //mTextMessage.setText(R.string.title_hobby);
                    //setContentView(R.layout.fragment_hobby);
                    if(ifExisted()){
                        hideFragment(transaction);
                        f3 = new hobbyFragment();
                        transaction.replace(R.id.content, f3);
                        transaction.commit();
                    }else{
                        Intent intent=new Intent(home.this,login.class);startActivity(intent);
                    }
                    return true;
                case R.id.navigation_talk:
                    //mTextMessage.setText(R.string.title_talk);
                    //setContentView(R.layout.fragment_talk);
                    if(ifExisted()){
                        hideFragment(transaction);
                        f4 = new talkFragment();
                        transaction.replace(R.id.content, f4);
                        transaction.commit();
                    }else{
                        Intent intent=new Intent(home.this,login.class);startActivity(intent);
                    }
                    return true;
                case R.id.navigation_personal:
                    //mTextMessage.setText(R.string.title_personal);
                    //setContentView(R.layout.fragment_personal);
                    if(ifExisted()){
                        hideFragment(transaction);
                        f5 = new personalFragment();
                        transaction.replace(R.id.content, f5);
                        transaction.commit();
                    }else{
                        Intent intent=new Intent(home.this,login.class);startActivity(intent);
                    }
                    return true;
            }
            return false;
        }

        //public void onMenuModeChange(MenuBuilder menu) {}

    };


     */
/** 改变Fragment
     * *//*


public void changeFragment(Fragment fff, Fragment freshFragment()){
        hideFragment(transaction);
        f1 = new freshFragment();
        transaction.replace(R.id.content, f1);
        transaction.commit();
    }


     */
/** 去除（隐藏）所有的Fragment
     * *//*


    private void hideFragment(FragmentTransaction transaction) {
        if (f1 != null) {
            //transaction.hide(f1);隐藏方法也可以实现同样的效果
            transaction.remove(f1);
        }
        if (f2 != null) {
            //transaction.hide(f2);
            transaction.remove(f2);
        }
        if (f3 != null) {
            //transaction.hide(f3);
            transaction.remove(f3);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        SysApplication.getInstance().addActivity(home.this);//加入activity列表

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        f1 = new freshFragment();
        transaction.replace(R.id.content, f1);
        transaction.commit();

        //mTextMessage = (TextView) findViewById(R.id.message);
        //bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    //退出
    long onBackPressed_startTime = 0;
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - onBackPressed_startTime) >= 1000) {
            ToastUtil.toast(home.this, "要离开了么( •̥́ ˍ •̀ू )");
            onBackPressed_startTime = currentTime;
        } else {
            home.this.finish();
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
        Intent ccc = new Intent(home.this,home.class);
        startActivity(ccc);
        //home.this.finish();
    }

}






*/
