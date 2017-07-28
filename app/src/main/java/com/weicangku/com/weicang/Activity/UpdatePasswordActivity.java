package com.weicangku.com.weicang.Activity;

import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;

import com.weicangku.com.weicang.Activity.Base.ParentWithNaviActivity;
import com.weicangku.com.weicang.Bean.User;
import com.weicangku.com.weicang.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

public class UpdatePasswordActivity extends ParentWithNaviActivity {
    private EditText origialpassword;
    private EditText newpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        initNaviView();
    }

    @Override
    protected void initView() {
        super.initView();
        origialpassword=(EditText) findViewById(R.id.et_originalpassword);
        newpassword=(EditText) findViewById(R.id.et_newpassword);
    }

    @Override
    protected String title() {
        return "修改密码";
    }

    @Override
    public Object right() {
        return "保存";
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
                String orignal_password=origialpassword.getText().toString();
                if (orignal_password.equals("")) {
                    ShowToast("请填写原密码!");
                    return;
                }
                String new_password=newpassword.getText().toString();
                if (new_password.equals("")) {
                    ShowToast("请填写新密码!");
                    return;
                }
                updatePassword(orignal_password, new_password);
            }
        };
    }
    private void updatePassword(final String oldPassword, final String newPassword) {
        BmobUser.updateCurrentUserPassword(this,oldPassword,newPassword, new UpdateListener() {
            @Override
            public void onSuccess() {
                if (userManager.getCurrentUser().getInfo().equals(oldPassword)) {
                    ShowToast("修改密码成功");
                    User u=userManager.getCurrentUser();
                    u.setInfo(newPassword);
                    u.update(UpdatePasswordActivity.this, userManager.getCurrenUserObjectId(), new UpdateListener() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFailure(int i, String s) {

                        }
                    });
                    finish();
                }

            }

            @Override
            public void onFailure(int i, String s) {
                ShowToast("原密码不正确，请重新输入");
            }
        });
    }
}
