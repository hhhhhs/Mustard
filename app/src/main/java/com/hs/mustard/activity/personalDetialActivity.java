package com.hs.mustard.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.hs.mustard.R;

public class personalDetialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_detial);
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
        Intent ccc = new Intent(personalDetialActivity.this,MainActivity.class);
        startActivity(ccc);
        personalDetialActivity.this.finish();
    }

}
