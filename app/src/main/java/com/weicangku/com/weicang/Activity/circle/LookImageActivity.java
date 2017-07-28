package com.weicangku.com.weicang.Activity.circle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.weicangku.com.weicang.Bean.circle.Helps;
import com.weicangku.com.weicang.R;
import com.weicangku.com.weicang.Util.circle.LxwImageView;
import com.weicangku.com.weicang.event.ZoomBus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class LookImageActivity extends AppCompatActivity implements View.OnLongClickListener {


    private ProgressBar mProgressBar;
    private LxwImageView imageView;
    private String imgpath;

    private RelativeLayout layout_all;
    protected int mScreenWidth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_image);

        DisplayMetrics metrics=new DisplayMetrics();
        // 将当前窗口的一些信息放在DisplayMetrics类中
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;
//        EventBus.getDefault().register(this);

        initView();
        initData();
        setData();
    }
    private void setData() {
        Glide.with(this).load(imgpath).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }



    private void initView() {
        layout_all=(RelativeLayout) findViewById(R.id.layout_all);
        imageView= (LxwImageView) findViewById(R.id.lxw_id_item_viewpager_img);
        mProgressBar= (ProgressBar) findViewById(R.id.lxw_id_item_viewpager_progressbar);
        imageView.setOnLongClickListener(this);



    }
    private void initData() {
        Helps helps= (Helps) this.getIntent().getSerializableExtra("helps");
        imgpath=helps.getPhontofile().getPhoto();
//            imgpath=getIntent().getStringExtra("helps");
    }


    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.lxw_id_item_viewpager_img:
                showAvatarPop();
//                Toast.makeText(this,"你点击了",Toast.LENGTH_SHORT).show();
//                imageView.buildDrawingCache(true);
//                imageView.buildDrawingCache();
//                Bitmap bitmap = imageView.getDrawingCache();
//                saveBitmapFile(bitmap);
//                imageView.setDrawingCacheEnabled(false);
                break;
        }
        return false;
    }

    public void saveBitmapFile(Bitmap bitmap){

        File temp = new File(Environment.getExternalStorageDirectory(),"WeiCang");//要保存文件先创建文件夹
        if (!temp.exists()) {
            temp.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        ////重复保存时，覆盖原同名图片
        File file=new File(temp, fileName);//将要保存图片的路径和图片名称
        //    File file =  new File("/sdcard/1delete/1.png");/////延时较长
        try {
            BufferedOutputStream bos= new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            sendBroadcast(intent);
            Toast.makeText(this,"保存成功",Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    public void deleteFile(View v){////点击按钮删除这个文件
//        File file = new File("/sdcard/1spray/1.png");
//        if(file.exists()){
//            file.delete();
//        }
//
//    }
    RelativeLayout layout_cancle;
    RelativeLayout layout_photo;
    PopupWindow avatorPop;
    private void showAvatarPop() {
        View view= LayoutInflater.from(this).inflate(R.layout.pop_showavator, null);
        layout_photo = (RelativeLayout) view.findViewById(R.id.layout_savephoto);
        layout_cancle = (RelativeLayout) view.findViewById(R.id.layout_cancle);
        layout_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.buildDrawingCache(true);
                imageView.buildDrawingCache();
                Bitmap bitmap = imageView.getDrawingCache();
                saveBitmapFile(bitmap);
                imageView.setDrawingCacheEnabled(false);
                avatorPop.dismiss();
            }
        });
        layout_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(LookImageActivity.this,"123456",Toast.LENGTH_LONG).show();
                avatorPop.dismiss();
            }
        });
        avatorPop = new PopupWindow(view, mScreenWidth, 600);
        avatorPop.setTouchInterceptor(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    avatorPop.dismiss();
                    return true;
                }
                return false;
            }
        });

        avatorPop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        avatorPop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        avatorPop.setTouchable(true);
        avatorPop.setFocusable(true);
        avatorPop.setOutsideTouchable(true);
        avatorPop.setBackgroundDrawable(new BitmapDrawable());
        avatorPop.setAnimationStyle(R.style.Animations_GrowFromBottom);
        avatorPop.showAtLocation(layout_all, Gravity.BOTTOM, 0, 0);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserEvent(ZoomBus zoomBus) {  //自己定义个ZoomBuS实体，可以是空实体

        finish();
    }

    @Override
    protected void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
