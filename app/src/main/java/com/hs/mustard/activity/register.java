package com.hs.mustard.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hs.mustard.*;
import com.hs.mustard.Until.SysApplication;
import com.hs.mustard.Until.ToastUtil;
import com.hs.mustard.UrlManager;

public class register extends AppCompatActivity {

    private EditText editText_register_accountid;
    private Button button_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();//隐藏标题

        SysApplication.getInstance().addActivity(register.this);//加入activity列表

        editText_register_accountid = (EditText) findViewById(R.id.editText_register_accountid);
        button_register = (Button) findViewById(R.id.button_register);

        editText_register_accountid.addTextChangedListener(new TextWatcher(){
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editText_register_accountid.getText().toString().length()>=1){
                    ColorStateList c3 = new ColorStateList(new int[][]{new int[0]}, new int[]{0xff6bc056});//BackgroundTint
                    button_register.setBackgroundTintList(c3);
                }else{
                    ColorStateList c4 = new ColorStateList(new int[][]{new int[0]}, new int[]{0xffd6d7d7});//BackgroundTint
                    button_register.setBackgroundTintList(c4);
                }
            }
            public void afterTextChanged(Editable s) {
                //
            }
        });

        //接收id值
        Bundle bundle_register = this.getIntent().getExtras();
        if(bundle_register!=null) {
            String register_id = bundle_register.getString("register_id");
            editText_register_accountid.setText(register_id);
        }
    }

    //后退按钮
    public void textView_register_back_onClick(View view){
        register.this.finish();
    }

    //注册后跳转至登录
    public void button_register_onClick(View view){
        view.setSaveEnabled(false);
        //sendRequestWithHttpClient_register();//向服务器发数据
        //用 Bundle 携带数据 跳转
        final String just_id = editText_register_accountid.getText().toString();

        if(just_id.length()>0)
        {
            if(just_id.length() == 11)
            {
                //sendRequestWithHttpClient_register();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            ContentValues postParams = new ContentValues();
                            postParams.put("register_accountid", editText_register_accountid.getText().toString());
                            String ss = UrlManager.httpUrlConnectionPost("/hs/registerServlet", postParams);
                            if (ss.startsWith("register_success")){
                                Looper.prepare();
                                ToastUtil.toast(register.this, "成功啦٩(๑^o^๑)۶");
                                Intent intent_register_home=new Intent(register.this,login.class);
                                Bundle bundle_auto_login = new Bundle();
                                bundle_auto_login.putString("just_id",just_id);
                                intent_register_home.putExtras(bundle_auto_login);
                                startActivity(intent_register_home);
                                register.this.finish();
                                //Log.i("111111111111",just_id);
                                Looper.loop();
                            }else if (ss.startsWith("register_fail_existed")){
                                Looper.prepare();
                                ToastUtil.toast(register.this, "已经注册过了哟（＾＿＾）");
                                Looper.loop();
                            }
                            Thread.sleep(2000);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            else{
                ToastUtil.toast(register.this, "好像不太对哦(〟-_・)ﾝ?");
            }
        }
        else {
            ToastUtil.toast(register.this, "好像少了点什么(〟-_・)ﾝ?");
        }
    }


}
