package com.weicangku.com.weicang.Activity.circle;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.weicangku.com.weicang.Bean.circle.PhontoFiles;
import com.weicangku.com.weicang.R;
import com.weicangku.com.weicang.adapter.Base.Base.circle.LookImgAdapter;
import com.weicangku.com.weicang.event.ZoomBus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

public class LookImageViewPagerActivity extends AppCompatActivity {
    private ViewPager viewPager;

    private List<BmobFile> mData;
    private int position;
    private LookImgAdapter adapter;

    private TextView pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_image_view_pager);
//        EventBus.getDefault().register(this);
        initView();
        initData();
        setData();
    }
    private void initData() {
        //  helps= (Helps) getIntent().getSerializableExtra("helps");
        PhontoFiles phontoFiles= (PhontoFiles) getIntent().getSerializableExtra("phontoFiles");
        mData=phontoFiles.getPhotos();
        position=getIntent().getIntExtra("position", 0);

        pos.setText(position+1+"/"+mData.size());
    }

    private void initView() {
        viewPager= (ViewPager) findViewById(R.id.lxw_id_look_image_viewpager);
        pos= (TextView) findViewById(R.id.lxw_id_look_img_position);
    }

    private void setData() {
        adapter=new LookImgAdapter(this,mData);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);

        final int size=mData.size();
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                pos.setText(position+1+"/"+size);
                Log.e("jjh", "onPageScrolled: "+position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserEvent(ZoomBus zoomBus) {  //自己定义个ZoomBuS实体，可以是空实体
        Log.d("zoomBus",zoomBus+"");
        finish();
    }

    @Override
    protected void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
