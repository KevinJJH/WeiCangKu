package com.weicangku.com.weicang.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weicangku.com.weicang.Activity.Base.ParentWithNaviActivity;
import com.weicangku.com.weicang.Bean.User;
import com.weicangku.com.weicang.ImageLoads.UniversalImageLoader;
import com.weicangku.com.weicang.R;
import com.weicangku.com.weicang.Util.CustomScrollView1;

import cn.bmob.v3.listener.UpdateListener;

public class PersonalActivity extends ParentWithNaviActivity implements View.OnClickListener {
    private ImageView setavater;
    private TextView set_name;
    private TextView set_gender;
    private TextView set_weicanghao;
    private TextView set_personalsign;
    private ImageButton imageButton;

    private CustomScrollView1 mCustomScrollView1;
    private ImageView mBackgroundImageView = null;

    private RelativeLayout layout_gender;
    private RelativeLayout update_name;
    private RelativeLayout update_photo;
    private RelativeLayout layout_account;
    private RelativeLayout layout_myaddress;
    private RelativeLayout layout_personalsign;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        initNaviView();
    }

    @Override
    protected void initView() {
        super.initView();
        mCustomScrollView1 = (CustomScrollView1) findViewById(R.id.personal_scrollView);
        mBackgroundImageView = (ImageView) findViewById(R.id.personal_background_image);
        mCustomScrollView1.setImageView(mBackgroundImageView);

        setavater=(ImageView) findViewById(R.id.set_touxiang);
        set_name=(TextView) findViewById(R.id.set_name);
        set_gender=(TextView) findViewById(R.id.set_gender);
        set_weicanghao=(TextView) findViewById(R.id.tv_2);
        set_personalsign=(TextView) findViewById(R.id.set_sign);

        update_name=(RelativeLayout) findViewById(R.id.update_name);
        layout_gender=(RelativeLayout) findViewById(R.id.Rl_gender);
        update_photo=(RelativeLayout) findViewById(R.id.layout_head);
        layout_account=(RelativeLayout) findViewById(R.id.Rl_account);
        layout_myaddress=(RelativeLayout) findViewById(R.id.Rl_myaddress);
        layout_personalsign=(RelativeLayout) findViewById(R.id.update_personal_sign);


        update_name.setOnClickListener(this);
        update_photo.setOnClickListener(this);
        layout_gender.setOnClickListener(this);
        layout_account.setOnClickListener(this);
        layout_myaddress.setOnClickListener(this);
        layout_personalsign.setOnClickListener(this);
        updateUser(userManager.getCurrentUser());
    }
    private void updateUser(User user) {
        // 更改
        if (user.getNick()!=null){
        set_name.setText(user.getNick());
        }else{
            set_name.setText("");
        }
        if (user.getSign()!=null){
        set_personalsign.setText(user.getSign().trim());
        }else {
            set_personalsign.setText("");
        }
        set_weicanghao.setText(user.getUsername());
        refreshAvatar(user.getAvatar());
        if (user.getSex()!=null){
        set_gender.setText(user.getSex() == true ? "男" : "女");
        }else{
            set_gender.setText("");
        }

    }
    private void refreshAvatar(String avatar) {
        User user=userManager.getCurrentUser();
//        BmobFiles avatarFile = user.getImage();
         final String avatarUrl = user.getAvatar();
//         new Thread(new Runnable() {
//            @Override
//            public void run() {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (avatarUrl != null && !avatarUrl.equals("")) {
//                    ImageLoader.getInstance().displayImage(avatarUrl, setavater, DisplayConfig.getDefaultOptions(false,R.drawable.no_image));
////					ImageLoadOptions.getOptions(true);
//                } else {
//                    setavater.setImageResource(R.drawable.no_image);
//                }
//                }
//            });
//            }
//        }).start();
//        setavater.post(new Runnable() {
//            @Override
//            public void run() {
//                if (avatarUrl != null && !avatarUrl.equals("")) {
//                    ImageLoader.getInstance().displayImage(avatarUrl, setavater, DisplayConfig.getDefaultOptions(false,R.drawable.no_image));
////					ImageLoadOptions.getOptions(true);
//                } else {
//                    setavater.setImageResource(R.drawable.no_image);
//                }
//            }
//        });
        if (avatarUrl != null && !avatarUrl.equals("")) {
            UniversalImageLoader i=new UniversalImageLoader();
            i.loadAvator(setavater,avatarUrl,R.drawable.no_image);
        }

    }

    @Override
    protected String title() {
        return "个人信息";
    }


    @Override
    public void onClick(View v) {
    switch (v.getId()){
        case  R.id.layout_head:
            startActivity(ShowAvatarActivity.class,null,false);
            break;
        case R.id.Rl_gender:
            showSexChooseDialog();
            break;
        case R.id.update_name:
            startActivity(UpdateNameActivity.class,null,false);
            break;
        case R.id.Rl_account:
            startActivity(UpdatePasswordActivity.class,null,false);
            break;
        case R.id.update_personal_sign:
            startActivity(UpDateSignActivity.class,null,false);
            break;
        case R.id.Rl_myaddress:
            startActivity(MyDeliveryActivity.class,null,false);
            break;

    }
    }
    String[] sexs = new String[] { "男", "女" };
    private void showSexChooseDialog() {
        new AlertDialog.Builder(this)
                .setTitle("单选框")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(sexs, 0, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        updateInfo(which);
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", null).show();

    }
    private void updateInfo(int which) {
        final User user = new User();
        if (which == 0) {
            user.setSex(true);
        } else {
            user.setSex(false);
        }
        updateUserData(user, new UpdateListener() {
            @Override
            public void onSuccess() {
                ShowToast("修改成功");
                set_gender.setText(user.getSex() == true ? "男" : "女");
            }

            @Override
            public void onFailure(int i, String s) {
                ShowToast("修改失败");
            }
        });
    }

    private void updateUserData(User user, UpdateListener listen) {
        User current = userManager.getCurrentUser();
        user.setObjectId(current.getObjectId());
        user.update(this,listen);
    }
    protected void onResume() {
        super.onResume();
        updateUser(userManager.getCurrentUser());
    }

}
