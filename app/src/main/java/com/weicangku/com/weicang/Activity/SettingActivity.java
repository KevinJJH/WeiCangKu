package com.weicangku.com.weicang.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weicangku.com.weicang.Activity.Base.ParentWithNaviActivity;
import com.weicangku.com.weicang.Config.CustomApplication;
import com.weicangku.com.weicang.Fragment.MineFragment;
import com.weicangku.com.weicang.R;
import com.weicangku.com.weicang.Util.DataCleanManger;
import com.weicangku.com.weicang.UtilView.ImageBarnnerFramLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;

public class SettingActivity extends ParentWithNaviActivity implements View.OnClickListener,ImageBarnnerFramLayout.FramLaoutListener{
    private TextView exit_app;
    private RelativeLayout cleancache;
    private TextView calcuatecache;
    private ImageBarnnerFramLayout mgroup;

    private int[] lunbo=new int[]{
            R.drawable.lunbo2,
            R.drawable.lunbo1,
            R.drawable.lunbo3
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initNaviView();
    }

    @Override
    protected void initView() {
        super.initView();
        exit_app=(TextView)findViewById(R.id.exit_app);
        calcuatecache=(TextView)findViewById(R.id.tv_calcuatecache_);
        cleancache=(RelativeLayout) findViewById(R.id.clean_cache);
        cleancache.setOnClickListener(this);
        exit_app.setOnClickListener(this);
        mgroup= (ImageBarnnerFramLayout) findViewById(R.id.mGroup);
        mgroup.setFramlayoutlisten(this);
        List<Bitmap> list=new ArrayList<>();
        for (int i=0;i<lunbo.length;i++){
            Bitmap bitmap= BitmapFactory.decodeResource(getResources(),lunbo[i]);
            list.add(bitmap);
        }
        mgroup.addBitmap(list);
//        for (int i=0;i<lunbo.length;i++){
//            ImageView view=new ImageView(this);
//            view.setScaleType(ImageView.ScaleType.FIT_XY);
//            view.setLayoutParams(new RelativeLayout.LayoutParams(mScreenWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
//            view.setImageResource(lunbo[i]);
//            mgroup.addView(view);
//        }
//        mgroup.setListener(this);
        try {
            String cache=DataCleanManger.getTotalCacheSize(this);
            calcuatecache.setText(cache);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected String title() {
        return "设置";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exit_app:
                AlertDialog.Builder dialog1=new AlertDialog.Builder(this)
                        .setTitle("退出登陆")
                        .setMessage("确定退出登陆？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                CustomApplication.getInstance().logout();
                                finish();
                                if (CustomApplication.mInstance!=null) {
                                    MineFragment.instance.getActivity().finish();
                                    BmobIM.getInstance().disConnect();
                                  startActivity(LoginActivity.class,null,true);
                                }
                            }
                        }).setNegativeButton("取消", null);
                dialog1.show();
                break;
            case R.id.clean_cache:
                AlertDialog.Builder dialog=new AlertDialog.Builder(this)
                        .setTitle("清除缓存")
                        .setMessage("确定清除缓存?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                DataCleanManger.clearAllCache(getBaseContext());
                                ShowToast("清除缓存成功");
                                calcuatecache.setText("0K");
                            }
                        }).setNegativeButton("取消", null);
                dialog.show();
                break;
            default:
                break;
        }

    }


    @Override
    public void clickImageIndex(int index) {
        ShowToast("你点击了"+index);
    }
}
