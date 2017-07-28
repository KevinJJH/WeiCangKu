package com.weicangku.com.weicang.Activity;

import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;

import com.weicangku.com.weicang.Activity.Base.ParentWithNaviActivity;
import com.weicangku.com.weicang.Bean.User;
import com.weicangku.com.weicang.R;

import cn.bmob.v3.listener.UpdateListener;

public class UpDateSignActivity extends ParentWithNaviActivity{
    private EditText et_sign;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_date_sign);
        initNaviView();
    }

    @Override
    protected void initView() {
        super.initView();
        et_sign=(EditText) findViewById(R.id.Et_sign);
    }

    @Override
    protected String title() {
        return "个性签名";
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
                String sign = et_sign.getText().toString().trim();
                updateSign(sign);
            }
        };
    }
    private void updateSign(String sign){
        final User user=userManager.getCurrentUser();
        User u=new User();
        u.setSign(sign);
        u.update(this, user.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                ShowToast("修改成功");
                finish();
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });

    }
}
