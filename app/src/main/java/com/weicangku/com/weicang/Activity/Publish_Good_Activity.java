package com.weicangku.com.weicang.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.weicangku.com.weicang.Activity.Base.ParentWithNaviActivity;
import com.weicangku.com.weicang.Activity.circle.ChoseImgActivity2;
import com.weicangku.com.weicang.Bean.User;
import com.weicangku.com.weicang.Bean.circle.Goods;
import com.weicangku.com.weicang.Bean.circle.PhontoFiles;
import com.weicangku.com.weicang.R;
import com.weicangku.com.weicang.Util.circle.ImagLoads;
import com.weicangku.com.weicang.adapter.Base.Base.circle.ImageChoseAdapter2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

public class Publish_Good_Activity extends ParentWithNaviActivity {
    private ImageView Choose_photo;
    private HorizontalScrollView scrollPicContent;
    private LinearLayout layPicContent;
    private String path;
    private ProgressDialog mProgressDialog;
    private List<String> filePhotos;

    private EditText good_title;
    private EditText good_content;
    private EditText good_price;
    private EditText good_stock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish__good_);
        initNaviView();
    }

    @Override
    protected String title() {
        return "发布商品";
    }

    @Override
    public Object right() {
        return "完成";
    }

    @Override
    public ToolBarListener setToolBarListener() {
        return new ToolBarListener() {
            @Override
            public void clickLeft() {
                finish();
            }

            @Override
            public void clickRight() {
//                if (filePhotos!=null){
                    pushHelp();
//                }else {
//                    ShowToast("请上传图片");
//                }

            }
        };
    }

    @Override
    protected void initView() {
        super.initView();
        Choose_photo = (ImageView) findViewById(R.id.pick_photo);
        scrollPicContent = (HorizontalScrollView) findViewById(R.id.id_lxw_push_scrollPicContent);
        layPicContent = (LinearLayout) findViewById(R.id.id_lxw_push_layPicContent);

        good_content= (EditText) findViewById(R.id.good_content);
        good_price= (EditText) findViewById(R.id.good_price);
        good_stock= (EditText) findViewById(R.id.good_stock);
        good_title= (EditText) findViewById(R.id.good_title);


        Choose_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Publish_Good_Activity.this, ChoseImgActivity2.class);
                startActivity(intent);
            }
        });
    }

    private void refresUI() {
        Set<String> Imgs = ImageChoseAdapter2.mSelectImg2;
        if (Imgs.size() == 0) {
            scrollPicContent.setVisibility(View.GONE);
            return;
        }
        if (Imgs.size() > 0) {
            if (layPicContent != null) {
                layPicContent.removeAllViews();
                scrollPicContent.setVisibility(View.VISIBLE);

//
            }
            for (String path : Imgs) {
                View itemView = LayoutInflater.from(Publish_Good_Activity.this).inflate(R.layout.lxw_item_publish_pic, null);
                ImageView img = (ImageView) itemView.findViewById(R.id.id_lxw_publish_pic_img);

                itemView.setTag(path);
                itemView.setOnClickListener(onPicTureClickListener);


                ImagLoads.getInstance(2, ImagLoads.Type.LIFO).loaderImage(path, img, false);


                if (layPicContent != null) {
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layPicContent.addView(itemView, lp);
                }
            }
        } else {
            if (scrollPicContent != null) {
                scrollPicContent.setVisibility(View.GONE);
            }
        }

    }

    private View.OnClickListener onPicTureClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {


            AlertDialog.Builder builder = new AlertDialog.Builder(Publish_Good_Activity.this);
            AlertDialog alert = builder.setMessage("确认删除图片？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            path = v.getTag().toString();
//                            ImageChoseAdapter.mSelectImg.remove(path);
                            for (int i = 0; i < layPicContent.getChildCount(); i++) {
                                View view = layPicContent.getChildAt(i);
                                if (view.getTag().toString().equals(path)) {
                                    layPicContent.removeView(view);
                                    ImageChoseAdapter2.mSelectImg2.remove(path);
                                    break;
                                }
                            }
                            if (ImageChoseAdapter2.mSelectImg2 == null || ImageChoseAdapter2.mSelectImg2.size() == 0) {
                                scrollPicContent.setVisibility(View.GONE);

                            }
                        }
                    }).create();
            alert.show();
        }
    };

    @Override
    protected void onRestart() {
        super.onRestart();
        refresUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresUI();
    }

    private void uploaderpic(List<String> list) {
        final PhontoFiles phontoFiles = new PhontoFiles();
        final PhontoFiles ps = new PhontoFiles();
        if (list.size() == 1) {
            File file = new File(list.get(0));
            final BmobFile bmobFile = new BmobFile(file);
            bmobFile.uploadblock(this, new UploadFileListener() {
                @Override
                public void onSuccess() {
                    phontoFiles.setGood_picture(bmobFile.getFileUrl(Publish_Good_Activity.this));
                    phontoFiles.save(Publish_Good_Activity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            //  phontoFiles.setObjectId(phontoFiles.getObjectId());
                            ps.setObjectId(phontoFiles.getObjectId());


                            savePulishPic(phontoFiles);
                            mProgressDialog.dismiss();
                            toast("上传成功");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            toast("上传失败");
                        }
                    });
                }

                @Override
                public void onFailure(int i, String s) {

                }
            });
        } else {
            final String[] filePaths = list.toArray(new String[list.size()]);
            final int fileLength = filePaths.length;
            BmobFile.uploadBatch(this, filePaths, new UploadBatchListener() {
                @Override
                public void onSuccess(List<BmobFile> list, List<String> list1) {
                    if (list1.size() == fileLength) {
                        phontoFiles.setGood_pictures(list);
                        phontoFiles.save(Publish_Good_Activity.this, new SaveListener() {
                            @Override
                            public void onSuccess() {

                                //  idAsClass(phontoFiles.getObjectId());
                                // phontoFiles.setObjectId(phontoFiles.getObjectId());
                                ps.setObjectId(phontoFiles.getObjectId());
                                savePulishPic(phontoFiles);
                                mProgressDialog.dismiss();
                                toast("上传成功");
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                toast("上传失败");
                            }
                        });
                    }
                }

                @Override
                public void onProgress(int i, int i1, int i2, int i3) {

                }

                @Override
                public void onError(int i, String s) {

                }
            });

        }
    }

    private void savePulishPic(PhontoFiles files) {
        String title=good_title.getText().toString().trim();
        String price=good_price.getText().toString().trim();
//        if ("".equals(price)){
//            price="0";
//        }
        double price2=Double.parseDouble(price);

        String stock=good_stock.getText().toString();
//        if ("".equals(stock)){
//            stock="0";
//        }
        int stock2=Integer.parseInt(stock);
        String content=good_content.getText().toString().trim();
//        if (TextUtils.isEmpty(good_title.getText())) {
//            ShowToast("标题不能为空");
//            return;
//        }
////        if (TextUtils.isEmpty(good_price.getText())) {
////            ShowToast("价格不能为空");
////            return;
////        }
////        if (TextUtils.isEmpty(good_stock.getText())) {
////            ShowToast("库存不能为空");
////            return;
////        }
//        if (TextUtils.isEmpty(good_content.getText())) {
//            ShowToast("内容不能为空");
//            return;
//        }
        User user = BmobUser.getCurrentUser(this, User.class);
        Goods helps = new Goods();
        helps.setUser(user);
        helps.setGoodsPrice(price2);
        helps.setGoodsName(title);
        helps.setGoodsStock(stock2);
        helps.setGoodsContent(content);
//        helps.setState(1);
//        helps.setContent("");
        helps.setGood_photo_file(files);
        helps.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                finish();
//                hideSoftInputView();
                mProgressDialog.dismiss();
                ImageChoseAdapter2.mSelectImg2.removeAll(ImageChoseAdapter2.mSelectImg2);
                toast("上传成功");

            }

            @Override
            public void onFailure(int i, String s) {
                mProgressDialog.dismiss();
                toast("上传失败");
            }
        });
    }

    private void pushHelp() {


//          final List<String> list = new ArrayList<String>(ImageChoseAdapter.mSelectImg);
//        filePhotos = new ArrayList<String>();
//        for (String path : list) {
//            filePhotos.add(compressBitmap(PublishActivity.this, path));
//        }
        List<String> list = new ArrayList<String>(ImageChoseAdapter2.mSelectImg2);
        if (list != null && list.size() > 0) {
            getCacheImgFiles(Publish_Good_Activity.this, list);
        }
        if (filePhotos == null) {
          ShowToast("请上传图片");
        }
       else if (TextUtils.isEmpty(good_title.getText())) {
            ShowToast("标题不能为空");
            return;
        }
//        if (TextUtils.isEmpty(good_price.getText())) {
//            ShowToast("价格不能为空");
//            return;
//        }
//        if (TextUtils.isEmpty(good_stock.getText())) {
//            ShowToast("库存不能为空");
//            return;
//        }
       else if (TextUtils.isEmpty(good_price.getText())) {
            ShowToast("价格不能为空");
            return;
        }
        else if (TextUtils.isEmpty(good_content.getText())) {
            ShowToast("内容不能为空");
            return;
        }
        else if (TextUtils.isEmpty(good_stock.getText())) {
            ShowToast("库存不能为空");
            return;
        }
        else{
        mProgressDialog = ProgressDialog.show(this, null, "正在上传");
        }
        new Thread() {
            @Override
            public void run() {
                super.run();
                //图片+文字这样可以上传
//                List<String> list = new ArrayList<String>(ImageChoseAdapter2.mSelectImg2);
//                if (list != null && list.size() > 0) {
//                    getCacheImgFiles(Publish_Good_Activity.this, list);
//                }

                if (filePhotos != null) {
                    uploaderpic(filePhotos);
                }
//
//                else{
//                    ShowToast("请上传图片");
//                }

            }
        }.start();
    }
    private void getCacheImgFiles(Context context, List<String> list) {
        filePhotos = new ArrayList<String>();
        for (String path : list) {
            filePhotos.add(compressBitmap(context, path));
        }
    }
    private String compressBitmap(Context context, String path) {
        Bitmap bitmap = compressBitmapFromFile(path);
        File srcFile = new File(path);
        String desPath = getImageCacheDir(context) + srcFile.getName();
        File file = new File(desPath);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return desPath;
    }
    private Bitmap compressBitmapFromFile(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        options.inJustDecodeBounds = false;
        int width = options.outWidth;
        int height = options.outHeight;

        float widthRadio = 480f;
        float heightRadio = 800f;
        int inSampleSize = 1;
        if (width > height && width > widthRadio) {
            inSampleSize = (int) (width * 1.0f / widthRadio);
        } else if (width < height && height > heightRadio) {
            inSampleSize = (int) (height * 1.0f / heightRadio);
        }
        if (inSampleSize <= 0) {
            inSampleSize = 1;
        }
        options.inSampleSize = inSampleSize;
        bitmap = BitmapFactory.decodeFile(path, options);
        return compressBitmap(bitmap);
    }
    private Bitmap compressBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        while (baos.toByteArray().length / 1024 > 200) {
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 10;
        }
        ByteArrayInputStream byins = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bm = BitmapFactory.decodeStream(byins, null, null);
        return bm;
    }
    private String getImageCacheDir(Context context) {
        String cachepath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            cachepath = context.getExternalCacheDir().getPath();
        } else {
            cachepath = context.getCacheDir().getPath();
        }
        return cachepath;
    }
}