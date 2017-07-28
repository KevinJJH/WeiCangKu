package com.weicangku.com.weicang.Config;

import android.app.Application;

import com.weicangku.com.weicang.Bean.BmobUserManager;
import com.weicangku.com.weicang.ImageLoads.UniversalImageLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import cn.bmob.newim.BmobIM;

/**
 * Created by Administrator on 2016/12/17.
 */

public class CustomApplication extends Application {
    public static CustomApplication mInstance;

    private static void setCustomApplication(CustomApplication a) {
        CustomApplication.mInstance = a;
    }
    private void setInstance(CustomApplication app) {
        setCustomApplication(app);
    }
    public static CustomApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
        //只有主进程运行的时候才需要初始化
        if (getApplicationInfo().packageName.equals(getMyProcessName())){
            //im初始化
            BmobIM.init(this);
            //注册消息接收器
            BmobIM.registerDefaultMessageHandler(new DemoMessageHandler(this));
        }
        UniversalImageLoader.initImageLoader(this, Config.BASE_IMAGE_CACHE);
    }
    public void logout() {
        BmobUserManager.getInstance().logout();
    }
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
  
}
