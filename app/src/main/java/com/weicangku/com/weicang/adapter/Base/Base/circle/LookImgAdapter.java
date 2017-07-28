package com.weicangku.com.weicang.adapter.Base.Base.circle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.weicangku.com.weicang.R;
import com.weicangku.com.weicang.Util.circle.LxwImageView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DownloadFileListener;

/**
 * Created by luxin on 15-12-17.
 *  http://luxin.gitcafe.io
 */
public class LookImgAdapter extends PagerAdapter {
    private Context mContext;
    private List<BmobFile> mData;
    private ArrayList<View> views;
    private LxwImageView imageView;
    private ProgressBar progressBar;

    RelativeLayout layout_all;
    String path;



    public LookImgAdapter(Context context, List<BmobFile> list){
        this.mContext=context;
        this.mData=list;
        initViews();
    }

    private void initViews() {
        views=new ArrayList<View>();
        for (int i=0;i<mData.size();i++){

            View view= View.inflate(mContext, R.layout.lxw_item_viewpager_view,null);
            views.add(view);
        }
    }

    @Override
    public int getCount() {
        return mData.size();
    }



    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View view=views.get(position);


        layout_all= (RelativeLayout) view.findViewById(R.id.layout_all);

        imageView= (LxwImageView) view.findViewById(R.id.lxw_id_item_viewpager_img);

        progressBar= (ProgressBar) view.findViewById(R.id.lxw_id_item_viewpager_progressbar);
        progressBar.setVisibility(View.VISIBLE);
        //对应位置图片的url
         path=mData.get(position).getUrl();

        Glide.with(mContext).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
//        imageView.setDrawingCacheEnabled(true);
        progressBar.setVisibility(View.GONE);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext,"你点击了"+position,Toast.LENGTH_SHORT).show();
//                Log.e("jjh","你当前是"+position);
//                path=mData.get(position).getUrl();
//                showAvatarPop();
//                if (mContext instanceof Activity){
//                    Activity activity= (Activity) mContext;
//                    activity.finish();
//                }
            }
        });
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                path=mData.get(position).getUrl();
                showAvatarPop();
                return false;
            }
        });
//
        container.addView(view);


        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    RelativeLayout layout_cancle;
    RelativeLayout layout_photo;
    PopupWindow avatorPop;

    private void showAvatarPop() {
        View view= LayoutInflater.from(mContext).inflate(R.layout.pop_showavator, null);
        layout_photo = (RelativeLayout) view.findViewById(R.id.layout_savephoto);
        layout_cancle = (RelativeLayout) view.findViewById(R.id.layout_cancle);
        final String fileName = System.currentTimeMillis() + ".jpg";
        layout_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                imageView.buildDrawingCache(true);
//                imageView.buildDrawingCache();
//                imageView.setDrawingCacheEnabled(true);
//                  Bitmap bitmap = imageView.getDrawingCache(true);
//                  saveBitmapFile(bitmap,mContext);
//                imageView.setDrawingCacheEnabled(false);

                BmobFile bmobFile=new BmobFile(fileName,"",path);
                File temp = new File(Environment.getExternalStorageDirectory(),"WeiCang");
                if (!temp.exists()) {
                    temp.mkdir();
                }
                final File saveFile = new File(temp, bmobFile.getFilename());
                bmobFile.download(mContext, saveFile,new DownloadFileListener() {
                    @Override
                    public void onSuccess(String s) {
                        Toast.makeText(mContext,"保存成功",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        Uri uri = Uri.fromFile(saveFile);
                        intent.setData(uri);
                        mContext.sendBroadcast(intent);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(mContext,"保存失败",Toast.LENGTH_LONG).show();
                    }
                });

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
        avatorPop = new PopupWindow(view, 600, 600);
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
    public void saveBitmapFile(Bitmap bitmap,Context context){

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
            context.sendBroadcast(intent);
            Toast.makeText(context,"保存成功",Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

