package com.weicangku.com.weicang.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

import com.weicangku.com.weicang.Activity.Base.BaseActivity;
import com.weicangku.com.weicang.Service.landDivideServeice;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.BmobInstallation;

public class SplashActivity extends BaseActivity {

    private static final int GO_HOME = 100;
    private static final int GO_LOGIN = 200;
    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_HOME:
                   startActivity(HomeActivity.class,null,true);
                    break;
                case GO_LOGIN:
                    startActivity(LoginActivity.class,null,true);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
//        Bmob.initialize(this, "1637caa3eafcdf03400690c9c9217d28");
        Intent i=new Intent(getBaseContext(), landDivideServeice.class);
        startService(i);
        BmobInstallation.getCurrentInstallation(this).save();
//                // 启动推送服务
        BmobPush.startWork(this,"1637caa3eafcdf03400690c9c9217d28");

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(userManager.getCurrentUser()!=null){
            mHandler.sendEmptyMessageDelayed(GO_HOME, 1000);
        } else {
            mHandler.sendEmptyMessageDelayed(GO_LOGIN, 1000);
        }
    }
}
