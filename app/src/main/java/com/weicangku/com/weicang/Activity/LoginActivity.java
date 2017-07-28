package com.weicangku.com.weicang.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.weicangku.com.weicang.Activity.Base.BaseActivity;
import com.weicangku.com.weicang.Bean.User;
import com.weicangku.com.weicang.Bean.circle.MyUserInstallation;
import com.weicangku.com.weicang.R;

import java.util.List;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_name, et_password;
    private Button btn_login;
    private TextView tv_register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//
    }


    @Override
    protected void initView() {
        super.initView();
        et_name = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_register = (TextView) findViewById(R.id.tv_register);

        btn_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
//                BmobUserManager.getInstance().login(et_name.getText().toString(), et_password.getText().toString(), new LogInListener() {
//                    @Override
//                    public void done(Object o, BmobException e) {
//                        if (e == null) {
//                            User user = (User) o;
//                             bindUserIdAndDriverice(user);
////                            BmobIM.getInstance().updateUserInfo(new BmobIMUserInfo(user.getObjectId(), user.getNick(), user.getAvatar()));
//                            startActivity(HomeActivity.class, null, true);
//                        } else {
//                            toast(e.getMessage() + "(" + e.getErrorCode() + ")");
//                        }
//                    }
//                });
                final User user=new User();
                user.setUsername(et_name.getText().toString().trim());
                user.setPassword(et_password.getText().toString().trim());
                if (TextUtils.isEmpty(et_name.getText())) {
                    ShowToast("账号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(et_password.getText())) {
                    ShowToast("密码不能为空");
                    return;
                }
                user.login(this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        ShowToast("登陆成功");
                        bindUserIdAndDriverice(user);
                        startActivity(HomeActivity.class,null,true);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        ShowToast("登陆失败");
                    }
                });
//
                break;
            case R.id.tv_register:
                startActivity(RegisterActivity.class,null,true);
                break;
        }
    }

    /**
    * 将用户与设备绑定起来
    *
            * @param user
    */
    private void bindUserIdAndDriverice(final User user) {


        BmobQuery<MyUserInstallation> query = new BmobQuery<MyUserInstallation>();
        query.addWhereEqualTo("installationId", BmobInstallation.getInstallationId(this));
        query.findObjects(this, new FindListener<MyUserInstallation>() {
            @Override
            public void onSuccess(List<MyUserInstallation> list) {


                if (list.size() > 0) {

                    MyUserInstallation myUserInstallation = list.get(0);
                    myUserInstallation.setUid(user.getObjectId());
                    myUserInstallation.update(LoginActivity.this, new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            ShowToast("设备更新成功");
                            finish();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            ShowToast("设备更新失败");
                        }
                    });
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
    }

