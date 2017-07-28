package com.weicangku.com.weicang.Bean;

import android.content.Context;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.weicangku.com.weicang.Config.CustomApplication;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2016/12/17.
 */

public class BmobUserManager {
    Context context;

    private static BmobUserManager instance=new BmobUserManager();


    public int CODE_NULL=1000;
    public static int CODE_NOT_EQUAL=1001;

    public static final int DEFAULT_LIMIT=20;

    public Context getContext(){
        return CustomApplication.getInstance();
    }

    private BmobUserManager() {

    }
    public void login(String username, String password, final LogInListener listener) {
        if(TextUtils.isEmpty(username)){
            listener.internalDone(new BmobException(CODE_NULL, "请填写用户名"));
            return;
        }
        if(TextUtils.isEmpty(password)){
            listener.internalDone(new BmobException(CODE_NULL, "请填写密码"));
            return;
        }
        final User user =new User();
        user.setUsername(username);
        user.setPassword(password);
        user.login(getContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                listener.done(getCurrentUser(), null);
            }

            @Override
            public void onFailure(int i, String s) {
                listener.done(user, new BmobException(i, s));
            }
        });
    }


    public static BmobUserManager getInstance() {
//        if (instance == null) {
//            synchronized (BmobUserManager.class) {
//                if (instance == null) {
//                    instance = new BmobUserManager();
//                }
//                instance.init(context);
//            }
//        }
        return instance;
    }

//    private void init(Context c) {
//        this.context = c;
//    }

    /**
     * 若重载BombChatUser，请使用此获取当前用户
     */
    public <T> T getCurrentUser(Class<T> arg0){
        return BmobUser.getCurrentUser(getContext(), arg0);
    }

    /**
     * 获取当前登陆用户对象 getCurrentUser
     */
    public User getCurrentUser(){
        return BmobUser.getCurrentUser(getContext(), User.class);
    }

    /**
     * 获取当前登录用户的用户名
     */
    public String getCurrentUserName(){
        return getCurrentUser()!=null ? getCurrentUser().getUsername():"";
    }

    /**
     * 获取当前登录用户的ObjectId
     */
    public String getCurrenUserObjectId(){
        return getCurrentUser()!=null ? getCurrentUser().getObjectId():"";
    }
    /**
     * 退出登陆
     */
    public void logout() {
        BmobUser.logOut(getContext());
    }

    /**查询用户
     * @param username
     * @param limit
     * @param listener
     */
    public void queryUsers(String username,int limit,final FindListener<User> listener){
        BmobQuery<User> query = new BmobQuery<>();
        //去掉当前用户
        try {
            BmobUser user = BmobUser.getCurrentUser(getContext());
            query.addWhereNotEqualTo("username",user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
        query.addWhereEqualTo("username", username);
        query.setLimit(limit);
        query.order("-createdAt");
        query.findObjects(getContext(), new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                if (list != null && list.size() > 0) {
                    listener.onSuccess(list);
                } else {
                    listener.onError(CODE_NULL, "查无此人");
                }
            }

            @Override
            public void onError(int i, String s) {
                listener.onError(i, s);
            }
        });
    }

    /**查询用户信息
     * @param objectId
     * @param listener
     */
    public void queryUserInfo(String objectId, final QueryUserListener listener){
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId", objectId);
        query.findObjects(getContext(), new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                if(list!=null && list.size()>0){
                    listener.internalDone(list.get(0), null);
                }else{
                    listener.internalDone(new BmobException(000, "查无此人"));
                }
            }

            @Override
            public void onError(int i, String s) {
                listener.internalDone(new BmobException(i, s));
            }
        });
    }


    /**更新用户资料和会话资料
     * @param event
     * @param listener
     */
    public void updateUserInfo(MessageEvent event,final UpdateCacheListener listener){
        final BmobIMConversation conversation=event.getConversation();
        final BmobIMUserInfo info =event.getFromUserInfo();
        final BmobIMMessage msg =event.getMessage();
        String username =info.getName();
        String title =conversation.getConversationTitle();
        Logger.i("" + username + "," + title);
        //sdk内部，将新会话的会话标题用objectId表示，因此需要比对用户名和会话标题--单聊，后续会根据会话类型进行判断
        if(!username.equals(title)) {
            BmobUserManager.getInstance().queryUserInfo(info.getUserId(), new QueryUserListener() {
                @Override
                public void done(User s, BmobException e) {
                    if(e==null){
                        String name =s.getNick();
                        String avatar = s.getAvatar();
                        Logger.i("query success："+name+","+avatar);
                        conversation.setConversationIcon(avatar);
                        conversation.setConversationTitle(name);
                        info.setName(name);
                        info.setAvatar(avatar);
                        //更新用户资料
                        BmobIM.getInstance().updateUserInfo(info);
                        //更新会话资料-如果消息是暂态消息，则不更新会话资料
                        if(!msg.isTransient()){
                            BmobIM.getInstance().updateConversation(conversation);
                        }
                    }else{
                        Logger.e(e);
                    }
                    listener.done(null);
                }
            });
        }else{
            listener.internalDone(null);
        }
    }

    /**
     * 同意添加好友：1、发送同意添加的请求，2、添加对方到自己的好友列表中
     */
    public void agreeAddFriend(User friend,SaveListener listener){
        Friend f = new Friend();
        User user =BmobUser.getCurrentUser(getContext(), User.class);
        f.setUser(user);
        f.setFriendUser(friend);
        f.save(getContext(),listener);
    }

    /**
     * 查询好友
     * @param listener
     */
    public void queryFriends(final FindListener<Friend> listener){
        BmobQuery<Friend> query = new BmobQuery<>();
        User user =BmobUser.getCurrentUser(getContext(), User.class);
        query.addWhereEqualTo("user", user);
        query.include("friendUser");
//        query.order("-updatedAt");
        query.order("-user");
        query.findObjects(getContext(), new FindListener<Friend>() {
            @Override
            public void onSuccess(List<Friend> list) {
                if (list != null && list.size() > 0) {
                    listener.onSuccess(list);
                } else {
                    listener.onError(0, "暂无联系人");
                }
            }

            @Override
            public void onError(int i, String s) {
                listener.onError(i, s);
            }
        });
    }
    /**zsp
     * 查询是否有某个好友
     * @param
     */
    public boolean isfriendexist;
    public boolean querySingleFriend(String uid){
        BmobQuery<Friend> query = new BmobQuery<>();

        User user =BmobUser.getCurrentUser(getContext(), User.class);
        User frienduser =new User();
        frienduser.setObjectId(uid);

        query.addWhereEqualTo("user", user);
        query.addWhereEqualTo("friendUser", frienduser);
        query.include("friendUser");
        query.order("-updatedAt");

        query.findObjects(getContext(), new FindListener<Friend>() {
            @Override
            public void onSuccess(List<Friend> list) {
                if (list != null && list.size() > 0) {
                    //listener.onSuccess("true");
                    isfriendexist = true;

                } else {
                    //listener.onError(0, "暂无联系人");
                    isfriendexist = false;
                }
            }

            @Override
            public void onError(int i, String s) {
                isfriendexist = true;
                //listener.onError(i, s);
            }
        });
        return isfriendexist;
    }


    /**
     * 删除好友
     * @param f
     * @param listener
     */
    public void deleteFriend(Friend f,DeleteListener listener){
        Friend friend =new Friend();
        friend.delete(getContext(),f.getObjectId(),listener);
    }
}
