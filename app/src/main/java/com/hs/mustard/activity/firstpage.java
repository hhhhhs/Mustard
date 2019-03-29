package com.hs.mustard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.hs.mustard.*;
import com.hs.mustard.Until.SysApplication;

import java.util.Timer;
import java.util.TimerTask;

public class firstpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);//去标题
        setContentView(R.layout.welcome);//设置当前视图为main
        getSupportActionBar().hide();//隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        //final Intent intent_login = new Intent(firstpage.this, login.class); //下一步转向login
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent_login = new Intent(firstpage.this, MainActivity.class); //下一步转向login
                startActivity(intent_login); //执行意图
                SysApplication.getInstance().addActivity(firstpage.this);
                firstpage.this.finish();
            }
        };
        timer.schedule(task, 1000); //3秒后跳转，这里根据自己需要设定时间
    }
}
