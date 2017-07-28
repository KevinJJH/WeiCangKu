package com.weicangku.com.weicang.Bean;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.weicangku.com.weicang.Activity.NewFriendActivity;
import com.weicangku.com.weicang.Config.Config;
import com.weicangku.com.weicang.Config.CustomApplication;
import com.weicangku.com.weicang.DB.Dao.NewFriend;
import com.weicangku.com.weicang.DB.Dao.NewFriendManager;
import com.weicangku.com.weicang.R;


/**
 * 新朋友会话
 * Created by Administrator on 2016/5/25.
 */
public class NewFriendConversation extends Conversation{

    NewFriend lastFriend;

    public NewFriendConversation(NewFriend friend){
        this.lastFriend=friend;
        this.cName="新朋友";
    }

    @Override
    public String getLastMessageContent() {
        if(lastFriend!=null){
            Integer status =lastFriend.getStatus();
            String name = lastFriend.getName();
            if(TextUtils.isEmpty(name)){
                name = lastFriend.getUid();
            }
            //目前的好友请求都是别人发给我的
            if(status==null || status== Config.STATUS_VERIFY_NONE||status ==Config.STATUS_VERIFY_READED){
                return name+"请求添加好友";
            }else{
                return "我已添加"+name;
            }
        }else{
            return "";
        }
    }

    @Override
    public long getLastMessageTime() {
        if(lastFriend!=null){
            return lastFriend.getTime();
        }else{
            return 0;
        }
    }

    @Override
    public Object getAvatar() {
        return R.drawable.new_friends_icon;
    }

    @Override
    public int getUnReadCount() {
        return NewFriendManager.getInstance(CustomApplication.getInstance()).getNewInvitationCount();
    }

    @Override
    public void readAllMessages() {
        //批量更新未读未认证的消息为已读状态
        NewFriendManager.getInstance(CustomApplication.getInstance()).updateBatchStatus();
    }

    @Override
    public void onClick(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, NewFriendActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onLongClick(Context context) {
        NewFriendManager.getInstance(context).deleteNewFriend(lastFriend);
    }
}
