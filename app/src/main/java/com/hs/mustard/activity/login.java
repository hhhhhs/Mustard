package com.hs.mustard.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hs.mustard.*;
import com.hs.mustard.samples.*;
import com.hs.mustard.Until.SysApplication;
import com.hs.mustard.Until.*;
import com.hs.mustard.UrlManager;

import org.json.JSONObject;

public class login extends AppCompatActivity {
    private EditText editText_accountid;
    private EditText editText_password;
    private Button button_login;
    private TextView textView_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SysApplication.getInstance().addActivity(login.this);//加入activity列表

        getSupportActionBar().hide();//隐藏标题

        editText_accountid = (EditText) findViewById(R.id.editText_accountid);
        editText_password = (EditText) findViewById(R.id.editText_password);
        button_login = (Button) findViewById(R.id.button_login);
        textView_root = (TextView) findViewById(R.id.textView_root);

        editText_accountid.setSaveEnabled(false);
        editText_accountid.addTextChangedListener(new TextWatcher(){
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editText_accountid.getText().toString().length()>=1){
                    ColorStateList cl = new ColorStateList(new int[][]{new int[0]}, new int[]{0xff6bc056});//BackgroundTint
                    button_login.setBackgroundTintList(cl);
                }else{
                    ColorStateList c2 = new ColorStateList(new int[][]{new int[0]}, new int[]{0xffd6d7d7});//BackgroundTint
                    button_login.setBackgroundTintList(c2);
                }
            }
            public void afterTextChanged(Editable s) {
                //
            }
        });

        //从注册页接收just_id值
        Bundle bundle_register = this.getIntent().getExtras();
        if(bundle_register!=null) {
            String this_id = bundle_register.getString("just_id");
            //Log.i("qqqqqqqqqqqqqqqqqqqq",this_id);
            //String this_ps = bundle_register.getString("just_ps");
            //Integer.parseInt(this_id.toString())
            //findViewById(R.id.editText_accountid).setVisibility(View.GONE);
            ((EditText) findViewById(R.id.editText_accountid)).setText(this_id);
            //editText_accountid.postInvalidate();
            //editText_password.setText(this_ps);
            //Log.i("xxxxxxxxxxxxxxxxxxxx",this_id);
        }

        //接收id值
        Bundle bundle_login = this.getIntent().getExtras();
        if(bundle_login!=null) {
            String accountid = bundle_login.getString("accountid");
            String password = bundle_login.getString("password");
            editText_accountid.setText(accountid);
            editText_password.setText(password);
            //Typeface.createFromAsset(getAssets() , "fonts/timesi.ttf")
        }
    }

    //登陆后跳转至home页
    public void button_login_onClick(View view){
        String s1 = editText_accountid.getText().toString();
        String s2 = editText_password.getText().toString();
        if(s1.length()>0 && s2.length()>0)
        {
            if(s1.length() == 11)
            {
                sendRequestWithHttpClient_login();
            }
            else{
                ToastUtil.toast(login.this, "只能输入手机号哦(•̀o•́)ง");
            }
        }
        else{
            ToastUtil.toast(login.this, "好像少了点什么(〟-_・)ﾝ?");
        }
    }


    /*//退出
    long onBackPressed_startTime = 0;
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - onBackPressed_startTime) >= 1000) {
            ToastUtil.toast(login.this, "要离开了么( •̥́ ˍ •̀ू )");
            onBackPressed_startTime = currentTime;
        } else {
            login.this.finish();
        }
    }*/



    //跳转至注册页
    public void textView_login_onClick(View view){
        view.setSaveEnabled(false);
        Intent intent_login_register=new Intent(login.this,register.class);
        startActivity(intent_login_register);
    }

    //root
    public void textView_root_onClick(View view){
        String code = "13601350078".trim();
        SharedPreferences.Editor editor = getSharedPreferences("lock", MODE_PRIVATE).edit();
        editor.putString("code", code);
        editor.commit();
        Intent intent_root=new Intent(login.this,MainActivity.class);
        startActivity(intent_root);
        login.this.finish();
    }

    //服务端通信
    private void sendRequestWithHttpClient_login(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    ContentValues postParams = new ContentValues();
                    postParams.put("accountid", editText_accountid.getText().toString());
                    postParams.put("password", editText_password.getText().toString());
                    String s = UrlManager.httpUrlConnectionPost("/hs/LoginServlet", postParams);

                    //JSONObject obj = new JSONObject(s);
                    /*String phone = obj.getString("phone");
                    String name = obj.getString("name");
                    String sex = obj.getString("sex");
                    String imgpath = obj.getString("imgpath");
                    String real_name = obj.getString("real_name");
                    String birthday = obj.getString("birthday");
                    String email = obj.getString("email");*/
                    //boolean login_success = obj.getBoolean("login_success");


                    //if (login_success){
                    if(s.startsWith("login_success")){
                        //MainActivity.this.finish();
                        Looper.prepare();
                        saveLocal();
                        ToastUtil.toast(login.this, "成功啦٩(๑^o^๑)۶");
                        Intent intent=new Intent(login.this,MainActivity.class);startActivity(intent);
                        login.this.finish();
                        Looper.loop();
                    }else if (s.startsWith("login_fail_wrongCheck")){
                        Looper.prepare();
                        ToastUtil.toast(login.this, "好像不太对哦( ๑ŏ ﹏ ŏ๑ )");
                        Looper.loop();
                    }
                    Thread.sleep(2000);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }



    //存本地
    public void saveLocal(){
        //步骤1：获取输入值
        //trim()方法返回调用字符串对象的一个副本，但是所有起始和结尾的空格都被删除了，例子如下：String s="    Hello World      ".trim();就是把"Hello World"放入s中
        String code = editText_accountid.getText().toString().trim();
        //步骤2-1：创建一个SharedPreferences.Editor接口对象，lock表示要写入的XML文件名，MODE_WORLD_WRITEABLE写操作
        SharedPreferences.Editor editor = getSharedPreferences("lock", MODE_PRIVATE).edit();
        //步骤2-2：将获取过来的值放入文件
        editor.putString("code", code);
        //步骤3：提交
        editor.commit();
    }



}
