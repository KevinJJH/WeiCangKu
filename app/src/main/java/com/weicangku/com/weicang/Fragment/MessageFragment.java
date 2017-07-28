package com.weicangku.com.weicang.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;

import com.weicangku.com.weicang.Activity.Base.ParentWithNaviActivity;
import com.weicangku.com.weicang.Activity.SearchUserActivity;
import com.weicangku.com.weicang.Activity.circle.MessageActivity;
import com.weicangku.com.weicang.Bean.Conversation;
import com.weicangku.com.weicang.Bean.NewFriendConversation;
import com.weicangku.com.weicang.Bean.PrivateConversation;
import com.weicangku.com.weicang.DB.Dao.NewFriend;
import com.weicangku.com.weicang.DB.Dao.NewFriendManager;
import com.weicangku.com.weicang.Fragment.Base.ParentWithNaviFragment;
import com.weicangku.com.weicang.R;
import com.weicangku.com.weicang.Util.ClearEditText;
import com.weicangku.com.weicang.adapter.Base.Base.Base.IMutlipleItem;
import com.weicangku.com.weicang.adapter.Base.Base.Base.OnRecyclerViewListener;
import com.weicangku.com.weicang.adapter.Base.Base.ConversationAdapter;
import com.weicangku.com.weicang.event.RefreshEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.ObseverListener;
import cn.bmob.newim.notification.BmobNotificationManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends ParentWithNaviFragment implements ObseverListener, View.OnClickListener {
    private RecyclerView rc_view;
    private SwipeRefreshLayout sw_refresh;
    LinearLayoutManager layoutManager;
    ConversationAdapter adapter;

    private Button Comment;

    private ClearEditText mClearEditText;

    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_message,container, false);

        rc_view=getView(R.id.rc_view);
        sw_refresh=getView(R.id.sw_refresh);

        mClearEditText=getView(R.id.et_msg_search);
        Comment=getView(R.id.PingLun);
        Comment.setOnClickListener(this);
        initNaviView();
        IMutlipleItem<Conversation> mutlipleItem=new IMutlipleItem<Conversation>() {
            @Override
            public int getItemLayoutId(int viewtype) {
                return R.layout.item_conversation;
            }

            @Override
            public int getItemViewType(int postion, Conversation conversation) {
                return 0;
            }

            @Override
            public int getItemCount(List<Conversation> list) {
                return list.size();
            }
        };
        adapter = new ConversationAdapter(getActivity(),mutlipleItem,null);
        rc_view.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity());
        rc_view.setLayoutManager(layoutManager);
        sw_refresh.setEnabled(true);
        setListener();
        return rootView;
    }

    private void setListener(){
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                sw_refresh.setRefreshing(true);
                query();
            }
        });
        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                adapter.getItem(position).onClick(getActivity());

            }

            @Override
            public boolean onItemLongClick(int position) {
                adapter.getItem(position).onLongClick(getActivity());
                adapter.remove(position);
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        sw_refresh.setRefreshing(true);
        BmobNotificationManager.getInstance(getActivity()).addObserver(this);
        query();
    }

    @Override
    public void onPause() {
        super.onPause();
        BmobNotificationManager.getInstance(getActivity()).removeObserver(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     查询本地会话
     */
    public void query(){
//        adapter.bindDatas(BmobIM.getInstance().loadAllConversation());
        adapter.bindDatas(getConversations());
        adapter.notifyDataSetChanged();
    sw_refresh.setRefreshing(false);
}

    /**
     * 获取会话列表的数据：增加新朋友会话
     * @return
     */
    private List<Conversation> getConversations(){
        //添加会话
        List<Conversation> conversationList = new ArrayList<>();
        conversationList.clear();
        List<BmobIMConversation> list = BmobIM.getInstance().loadAllConversation();
        if(list!=null && list.size()>0){
            for (BmobIMConversation item:list){
                switch (item.getConversationType()){
                    case 1://私聊
                        conversationList.add(new PrivateConversation(item));
                        break;
                    default:
                        break;
                }
            }
        }
        //添加新朋友会话-获取好友请求表中最新一条记录
        List<NewFriend> friends = NewFriendManager.getInstance(getActivity()).getAllNewFriend();
        if(friends!=null && friends.size()>0){
            conversationList.add(new NewFriendConversation(friends.get(0)));
        }
        //重新排序
        Collections.sort(conversationList);
        return conversationList;
    }

    /**注册自定义消息接收事件
     * @param event
     */
    @Subscribe
    public void onEventMainThread(RefreshEvent event){
        log("---会话页接收到自定义消息---");
        //因为新增`新朋友`这种会话类型
        adapter.bindDatas(getConversations());
        adapter.notifyDataSetChanged();
    }

    /**注册离线消息接收事件
     * @param event
     */
    @Subscribe
    public void onEventMainThread(OfflineMessageEvent event){
        //重新刷新列表
        adapter.bindDatas(getConversations());
        adapter.notifyDataSetChanged();
    }

    /**注册消息接收事件
     * @param event
     * 1、与用户相关的由开发者自己维护，SDK内部只存储用户信息
     * 2、开发者获取到信息后，可调用SDK内部提供的方法更新会话
     */
    @Subscribe
    public void onEventMainThread(MessageEvent event){
        //重新获取本地消息并刷新列表
        adapter.bindDatas(getConversations());
        adapter.notifyDataSetChanged();

    }


    @Override
    protected String title() {
        return "消息";
    }

    @Override
    public Object right() {
        return  R.drawable.base_action_bar_add_bg_selector;
    }

    @Override
    public ParentWithNaviActivity.ToolBarListener setToolBarListener() {
        return new ParentWithNaviActivity.ToolBarListener() {
            @Override
            public void clickLeft() {

            }

            @Override
            public void clickRight() {
                startActivity(SearchUserActivity.class,null,false);
            }
        };
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.PingLun:
                startActivity(MessageActivity.class,null,false);
                break;
        }
    }
}


