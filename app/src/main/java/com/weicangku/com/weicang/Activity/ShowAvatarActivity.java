package com.weicangku.com.weicang.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.weicangku.com.weicang.Activity.Base.ParentWithNaviActivity;
import com.weicangku.com.weicang.Bean.User;
import com.weicangku.com.weicang.ImageLoads.DisplayConfig;
import com.weicangku.com.weicang.R;
import com.weicangku.com.weicang.Util.Photo_Utils;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class ShowAvatarActivity extends ParentWithNaviActivity{
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESIZE_REQUEST_CODE = 2;

    private static Uri tempUri;
    private Bitmap photo;
    private String imagePath;
    private LinearLayout layout_all;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_avatar);
        initNaviView();
    }

    @Override
    protected void initView() {
        super.initView();
        imageView=(ImageView) findViewById(R.id.show_avator);
        layout_all=(LinearLayout) findViewById(R.id.layout_all);
        updateUser(userManager.getCurrentUser());
    }
    private void updateUser(User user) {
        // 更改

        refreshAvatar(user.getAvatar());

    }
    private void refreshAvatar(String avatar) {
        User user = userManager.getCurrentUser();
//        BmobFiles avatarFile = user.getImage();
        final String avatarUrl = user.getAvatar();
        if (avatarUrl != null && !avatarUrl.equals("")) {
            ImageLoader.getInstance().displayImage(avatarUrl, imageView, DisplayConfig.getDefaultOptions(false, R.drawable.no_image));
        } else {
            imageView.setImageResource(R.drawable.no_image);
        }
    }
    RelativeLayout layout_choose;
    RelativeLayout layout_photo;
    PopupWindow avatorPop;
    private void showAvatarPop() {
        View view= LayoutInflater.from(this).inflate(R.layout.pop_choosephoto, null);
        layout_photo = (RelativeLayout) view.findViewById(R.id.layout_photo);
        layout_choose = (RelativeLayout) view.findViewById(R.id.layout_choose);
        layout_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                tempUri = Uri.fromFile(
                        new File(Environment
                        .getExternalStorageDirectory(), "image.jpg"));
                // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                startActivityForResult(openCameraIntent, CAMERA_REQUEST_CODE);
            }
        });
        layout_choose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent openAlbm=new Intent(Intent.ACTION_GET_CONTENT);
                openAlbm.addCategory(Intent.CATEGORY_OPENABLE);
                openAlbm.setType("image/*");
                startActivityForResult(openAlbm, IMAGE_REQUEST_CODE);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case CAMERA_REQUEST_CODE:
                    resizeImage(tempUri);
                    //	            	startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case IMAGE_REQUEST_CODE:
                    resizeImage(data.getData())	;
                    //	            	startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case RESIZE_REQUEST_CODE:
                    if (avatorPop != null) {
                        avatorPop.dismiss();
                    }
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    uploadAvatar();
                    break;
            }
        }
    }
    public void resizeImage(Uri uri) {//重塑图片大小
        tempUri=uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");//可以裁剪
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESIZE_REQUEST_CODE);
    }
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            photo = extras.getParcelable("data");
//            imageView.setImageBitmap(photo);
            imageView.post(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageBitmap(photo);
                }
            });
            imagePath = Photo_Utils.savePhoto(photo, Environment
                    .getExternalStorageDirectory().getAbsolutePath(), String
                    .valueOf(System.currentTimeMillis()));
            if (photo != null && photo.isRecycled()) {
                photo.recycle();
            }
        }
    }
    private void uploadAvatar() {
        final BmobFile bmobFile = new BmobFile(new File(imagePath));
        bmobFile.uploadblock(this, new UploadFileListener() {
            @Override
            public void onSuccess() {
            String avatr=bmobFile.getFileUrl(ShowAvatarActivity.this);
                User current=userManager.getCurrentUser();
                final User u =new User();
                u.setAvatar(avatr);
                u.update(ShowAvatarActivity.this, current.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        ShowToast("上传头像成功");
//                        refreshAvatar(u.getAvatar());
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });

            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    @Override
    protected String title() {
        return "个人头像";
    }

    @Override
    public Object right() {
        return "选择照片";
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
            showAvatarPop();
            }
        };
    }

}
