package com.weicangku.com.weicang.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weicangku.com.weicang.Activity.Base.ParentWithNaviActivity;
import com.weicangku.com.weicang.Activity.PersonalActivity;
import com.weicangku.com.weicang.Activity.SettingActivity;
import com.weicangku.com.weicang.Bean.User;
import com.weicangku.com.weicang.Fragment.Base.ParentWithNaviFragment;
import com.weicangku.com.weicang.ImageLoads.UniversalImageLoader;
import com.weicangku.com.weicang.R;
import com.weicangku.com.weicang.Util.CustomScrollView1;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends ParentWithNaviFragment implements View.OnClickListener {
    private ImageView set_avator;
    private TextView set_nick;
    private TextView weicang_number;
    private RelativeLayout layout_head;

    private CustomScrollView1 mCustomScrollView1=null;
    private ImageView mBackgroundImageView = null;
    public static MineFragment instance;

        public MineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_mine,container, false);
        initNaviView();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        instance=this;
        updateUser(userManager.getCurrentUser());
    }

    private void initViews() {
        mCustomScrollView1 = getView(R.id.personal_scrollView);
        mBackgroundImageView =getView(R.id.personal_background_image);
        mCustomScrollView1.setImageView(mBackgroundImageView);

        set_avator=getView(R.id.set_avator);
        set_nick=getView(R.id.set_nick);
        weicang_number=getView(R.id.set_weicanghao);
        layout_head=getView(R.id.layout_head);
        layout_head.setOnClickListener(this);
    }
    private void updateUser(User user) {
        // 更改
        set_nick.setText(user.getNick());
        weicang_number.setText(user.getUsername());
        refreshAvatar(user.getAvatar());
    }
    private void refreshAvatar(String avatar) {
        User user=userManager.getCurrentUser();
//        BmobFiles avatarFile = user.getImage();
        final String avatarUrl = user.getAvatar();

        if (avatarUrl != null && !avatarUrl.equals("")) {
//            ImageLoader.getInstance().displayImage(avatarUrl, set_avator, DisplayConfig.getDefaultOptions(false,R.drawable.no_image));
//					ImageLoadOptions.getOptions());
            UniversalImageLoader i=new UniversalImageLoader();
            i.loadAvator(set_avator,avatarUrl,R.drawable.no_image);
        }
    }

    @Override
    protected String title() {
        return "我的";
    }

    @Override
    public Object right() {
        return "设置";
    }

    @Override
    public ParentWithNaviActivity.ToolBarListener setToolBarListener() {
        return new ParentWithNaviActivity.ToolBarListener() {
            @Override
            public void clickLeft() {

            }

            @Override
            public void clickRight() {
            startActivity(SettingActivity.class,null,false);
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_head:
                startActivity(PersonalActivity.class,null,false);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUser(userManager.getCurrentUser());
    }

}
