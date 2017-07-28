package com.weicangku.com.weicang.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.weicangku.com.weicang.Activity.Base.BaseActivity;
import com.weicangku.com.weicang.Bean.User;
import com.weicangku.com.weicang.DB.Dao.NewFriendManager;
import com.weicangku.com.weicang.Fragment.DongTaiFragment;
import com.weicangku.com.weicang.Fragment.HomeFragment;
import com.weicangku.com.weicang.Fragment.HouseFragment;
import com.weicangku.com.weicang.Fragment.MessageFragment;
import com.weicangku.com.weicang.Fragment.MineFragment;
import com.weicangku.com.weicang.R;
import com.weicangku.com.weicang.Util.ChangeColorIconWithText;
import com.weicangku.com.weicang.Util.IMMLeaks;
import com.weicangku.com.weicang.event.RefreshEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.listener.ObseverListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

public class HomeActivity extends BaseActivity implements ObseverListener,View.OnClickListener,ViewPager.OnPageChangeListener{
    private HomeFragment homeFragment;
    private MessageFragment messageFragment;
    private DongTaiFragment dongtaiFragment;
    private HouseFragment houseFragment;
    private MineFragment mineFragment;
    private ViewPager mViewPager;

    private ImageView iv_contact_tips;
    private ImageView iv_contact_tips2;

    private List<Fragment> mTabs=new ArrayList<Fragment>();

    private FragmentPagerAdapter mAdapter;
    private List<ChangeColorIconWithText> mTabIndicators=new ArrayList<ChangeColorIconWithText>();


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final User user= BmobUser.getCurrentUser(this,User.class);
        BmobIM.getInstance().getUserInfo(user.getObjectId());
        BmobIM.connect(user.getObjectId(), new ConnectListener() {
            @Override
            public void done(String s, BmobException e) {
             if (e==null){
//                 //服务器连接成功就发送一个更新事件，同步更新会话及主页的小红点
                 EventBus.getDefault().post(new RefreshEvent());
                 Log.d("bitch","success connect");



             }else {
                 ShowToast("连接失败啊啊啊啊啊啊");
             }
            }
        });
        //监听连接状态，也可通过BmobIM.getInstance().getCurrentStatus()来获取当前的长连接状态
        BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
            @Override
            public void onChange(ConnectionStatus status) {
                toast("" + status.getMsg());
            }
        });
        //解决leancanary提示InputMethodManager内存泄露的问题
        IMMLeaks.fixFocusedViewLeak(getApplication());


    }

    @Override
    protected void initView() {
        super.initView();
        mViewPager=(ViewPager) findViewById(R.id.viewpager);
        ChangeColorIconWithText one=(ChangeColorIconWithText) findViewById(R.id.indicator_one);
        mTabIndicators.add(one);
        ChangeColorIconWithText two=(ChangeColorIconWithText) findViewById(R.id.indicator_two);
        mTabIndicators.add(two);
        ChangeColorIconWithText three=(ChangeColorIconWithText) findViewById(R.id.indicator_three);
        mTabIndicators.add(three);
        ChangeColorIconWithText four=(ChangeColorIconWithText) findViewById(R.id.indicator_four);
        mTabIndicators.add(four);
        ChangeColorIconWithText five=(ChangeColorIconWithText) findViewById(R.id.indicator_five);
        mTabIndicators.add(five);

        iv_contact_tips= (ImageView) findViewById(R.id.iv_contact_tips);
        iv_contact_tips2= (ImageView) findViewById(R.id.iv_contact_tips2);

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        one.setIconAlpah(1.0f);
        initdatas();
        initevent();
    }
    private void initevent() {
        mViewPager.setOnPageChangeListener(this);
    }
    private void initdatas() {

        homeFragment=new HomeFragment();
        messageFragment=new MessageFragment();
        dongtaiFragment=new DongTaiFragment();
        houseFragment=new HouseFragment();
        mineFragment=new MineFragment();

        mTabs.add(homeFragment);
        mTabs.add(messageFragment);
        mTabs.add(dongtaiFragment);
        mTabs.add(houseFragment);
        mTabs.add(mineFragment);

        mAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return mTabs.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mTabs.get(position);
            }
        };
        mViewPager.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        clickTab(v);

    }
    private void clickTab(View v) {
        resetOtherTabs();
        switch (v.getId()) {
            case R.id.indicator_one:
                mTabIndicators.get(0).setIconAlpah(1.0f);
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.indicator_two:
                mTabIndicators.get(1).setIconAlpah(1.0f);
                mViewPager.setCurrentItem(1, false);
                break;
            case R.id.indicator_three:
                mTabIndicators.get(2).setIconAlpah(1.0f);
                mViewPager.setCurrentItem(2, false);
                break;
            case R.id.indicator_four:
                mTabIndicators.get(3).setIconAlpah(1.0f);
                mViewPager.setCurrentItem(3, false);
                break;
            case R.id.indicator_five:
                mTabIndicators.get(4).setIconAlpah(1.0f);
                mViewPager.setCurrentItem(4, false);
                break;


        }
    }
    private void resetOtherTabs() {

        for (int i = 0; i < mTabIndicators.size(); i++) {
            mTabIndicators.get(i).setIconAlpah(0);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        if (positionOffset>0) {
            ChangeColorIconWithText left=mTabIndicators.get(position);
            ChangeColorIconWithText right=mTabIndicators.get(position+1);

            left.setIconAlpah(1-positionOffset);
            right.setIconAlpah(positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //从这里开始是不确定的
    private void checkRedPoint(){
        int count = (int)BmobIM.getInstance().getAllUnReadCount();
        if(count>0){
            iv_contact_tips.setVisibility(View.VISIBLE);
        }else {
            iv_contact_tips.setVisibility(View.GONE);
        }
        //是否有好友添加的请求
        if(NewFriendManager.getInstance(this).hasNewFriendInvitation()){
            iv_contact_tips2.setVisibility(View.VISIBLE);
        }else{
            iv_contact_tips2.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //显示小红点
        checkRedPoint();
        //进入应用后，通知栏应取消
        BmobNotificationManager.getInstance(this).cancelNotification();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清理导致内存泄露的资源
        BmobIM.getInstance().clear();
        BmobNotificationManager.getInstance(this).clearObserver();
    }
    /**注册消息接收事件
     * @param event
     */
    @Subscribe
    public void onEventMainThread(MessageEvent event){
        checkRedPoint();
    }

    /**注册离线消息接收事件
     * @param event
     */
    @Subscribe
    public void onEventMainThread(OfflineMessageEvent event){
        checkRedPoint();
    }

    /**注册自定义消息接收事件
     * @param event
     */
    @Subscribe
    public void onEventMainThread(RefreshEvent event){
        log("---主页接收到自定义消息---");
        checkRedPoint();
    }
}
