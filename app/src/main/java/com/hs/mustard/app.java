package com.hs.mustard;

import android.app.Application;

import com.mob.MobSDK;

/**
 * Created by hs on 2017/11/24.
 */

public class app extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 通过代码注册你的AppKey和AppSecret
        MobSDK.init(this, "229a180375aed", "a321c82e2b9a35ba4ebac14d983a8af8");
    }
}
