package com.weicangku.com.weicang.Activity;

import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;

import com.weicangku.com.weicang.Activity.Base.ParentWithNaviActivity;
import com.weicangku.com.weicang.Bean.User;
import com.weicangku.com.weicang.R;

import cn.bmob.v3.listener.UpdateListener;

public class UpdateNameActivity extends ParentWithNaviActivity{
    private EditText edit_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_name);
        initNaviView();
    }

    @Override
    protected void initView() {
        super.initView();
        edit_name=(EditText) findViewById(R.id.Et_name);
    }

    @Override
    protected String title() {
        return "修改呢称";
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
                String name1 = edit_name.getText().toString();
                if (name1.equals("")) {
                    ShowToast("请填写名字!");
                    return;
                }
                updateInfo(name1);
            }
        };
    }
    private void updateInfo(final String name){
        final User user=userManager.getCurrentUser();
        User u=new User();
        u.setNick(name);
        u.setObjectId(user.getObjectId());
        u.update(this, new UpdateListener() {
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
