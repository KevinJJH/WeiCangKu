package com.weicangku.com.weicang.Activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.weicangku.com.weicang.Activity.Base.ParentWithNaviActivity;
import com.weicangku.com.weicang.Bean.AddFriendMessage;
import com.weicangku.com.weicang.Bean.User;
import com.weicangku.com.weicang.ImageLoads.ImageLoaderFactory;
import com.weicangku.com.weicang.R;

import java.util.HashMap;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by Administrator on 2017/1/3.
 */

public class UserInfoActivity extends ParentWithNaviActivity implements View.OnClickListener {
    private ImageView iv_avator;
    private TextView tv_name;
    private TextView tv_sex;
    private TextView tv_nick;
    private Button btn_add_friend;
    private Button btn_chat;

    private User user;
    private BmobIMUserInfo info;

    @Override
    protected String title() {
        return "个人资料";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initNaviView();
        iv_avator = (ImageView) findViewById(R.id.iv_avator);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_sex = (TextView) findViewById(R.id.sex);
        tv_nick= (TextView) findViewById(R.id.nick);
        btn_add_friend = (Button) findViewById(R.id.btn_add_friend);
        btn_chat = (Button) findViewById(R.id.btn_chat);



        user = (User) getBundle().getSerializable("u");
        if (user.getObjectId().equals(getCurrentUid())) {
            btn_add_friend.setVisibility(View.GONE);
            btn_chat.setVisibility(View.GONE);
        } else {
            btn_add_friend.setVisibility(View.VISIBLE);
            btn_chat.setVisibility(View.VISIBLE);
        }
        info = new BmobIMUserInfo(user.getObjectId(),user != null ? user.getNick() : null, user.getAvatar());
        ImageLoaderFactory.getLoader().loadAvator(iv_avator, user.getAvatar(), R.drawable.head);
        tv_name.setText(user.getUsername());
        if (user.getSex()!=null){
            tv_sex.setText(user.getSex()==true?"男":"女");
        }else{
            tv_sex.setText("");
        }
        tv_nick.setText(user.getNick());
        btn_chat.setOnClickListener(this);
        btn_add_friend.setOnClickListener(this);
    }


    /**
     * 发送添加好友的请求
     */
    private void sendAddFriendMessage(){
        //启动一个会话，如果isTransient设置为true,则不会创建在本地会话表中创建记录，
        //设置isTransient设置为false,则会在本地数据库的会话列表中先创建（如果没有）与该用户的会话信息，且将用户信息存储到本地的用户表中
        BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, true,null);
        //这个obtain方法才是真正创建一个管理消息发送的会话
        BmobIMConversation conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
        AddFriendMessage msg =new AddFriendMessage();
        User currentUser = BmobUser.getCurrentUser(this,User.class);
        msg.setContent("很高兴认识你，可以加个好友吗?");//给对方的一个留言信息
        Map<String,Object> map =new HashMap<>();
        map.put("name", currentUser.getNick());//发送者姓名，这里只是举个例子，其实可以不需要传发送者的信息过去
        map.put("avatar",currentUser.getAvatar());//发送者的头像
        map.put("uid",currentUser.getObjectId());//发送者的uid
        msg.setExtraMap(map);
        conversation.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage msg, BmobException e) {
                if (e == null) {//发送成功
                    toast("好友请求发送成功，等待验证");
                } else {//发送失败
                    toast("发送失败:" + e.getMessage());
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_friend:
                sendAddFriendMessage();
                break;
            case R.id.btn_chat:
                //启动一个会话，设置isTransient设置为false,则会在本地数据库的会话列表中先创建（如果没有）与该用户的会话信息，且将用户信息存储到本地的用户表中
                BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info,false,null);
                Bundle bundle = new Bundle();
                bundle.putSerializable("c", c);
                startActivity(ChatActivity.class, bundle, false);
//                BmobIM.getInstance().startPrivateConversation(info, false, new ConversationListener() {
//                    @Override
//                    public void done(BmobIMConversation bmobIMConversation, BmobException e) {
//                        Bundle bundle = new Bundle();
//                bundle.putSerializable("c", bmobIMConversation);
//                startActivity(ChatActivity.class, bundle, false);
//                    }
//                });
                break;
        }
    }
}
