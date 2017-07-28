package com.weicangku.com.weicang.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.weicangku.com.weicang.Activity.Base.ParentWithNaviActivity;
import com.weicangku.com.weicang.Bean.User;
import com.weicangku.com.weicang.R;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.listener.SaveListener;

import static com.weicangku.com.weicang.R.id.et_password_again;

public class RegisterActivity extends ParentWithNaviActivity implements View.OnClickListener {
    private EditText et_name,et_password,et_password2;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initNaviView();
    }

    @Override
    protected String title() {
        return "用户注册";
    }

    @Override
    protected void initView() {
        super.initView();
        et_name= (EditText) findViewById(R.id.et_username);
        et_password= (EditText) findViewById(R.id.et_password);
        et_password2= (EditText) findViewById(et_password_again);
        btn_register= (Button) findViewById(R.id.btn_register);

        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
    switch (v.getId()){
        case  R.id.btn_register:
            String username=et_name.getText().toString().trim();
            String pwd=et_password.getText().toString().trim();
            String pwd2=et_password2.getText().toString().trim();
            if (TextUtils.isEmpty(et_name.getText())) {
                ShowToast("账号不能为空");
                return;
            }
            if (TextUtils.isEmpty(et_password.getText())) {
                ShowToast("密码不能为空");
                return;
            }
            if (!pwd.equals(pwd2)){
                ShowToast("两次输入的密码不一致，请重新输入");
                return;
            }
            final User u=new User();
            u.setUsername(username);
            u.setPassword(pwd);
            u.setInfo(pwd);
            u.setDeviceType("android");
            u.setInstallId(BmobInstallation.getInstallationId(getApplicationContext()));
            u.signUp(this, new SaveListener() {
                @Override
                public void onSuccess() {
                    startActivity(LoginActivity.class,null,true);
                    ShowToast("注册成功");
                }

                @Override
                public void onFailure(int i, String s) {
                    ShowToast(s);
                }
            });
            break;
    }
    }
}
