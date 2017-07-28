package com.weicangku.com.weicang.Activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.LinearLayout;

import com.weicangku.com.weicang.Activity.Base.ParentWithNaviActivity;
import com.weicangku.com.weicang.DB.Dao.NewFriend;
import com.weicangku.com.weicang.DB.Dao.NewFriendManager;
import com.weicangku.com.weicang.R;
import com.weicangku.com.weicang.adapter.Base.Base.Base.IMutlipleItem;
import com.weicangku.com.weicang.adapter.Base.Base.NewFriendAdapter;
import com.weicangku.com.weicang.adapter.Base.Base.Base.OnRecyclerViewListener;

import java.util.List;

public class NewFriendActivity extends ParentWithNaviActivity {
    private LinearLayout ll_root;
    private RecyclerView rc_view;
    private SwipeRefreshLayout sw_refresh;
    NewFriendAdapter adapter;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);
        initNaviView();
    }

    @Override
    protected void initView() {
        super.initView();
        ll_root= (LinearLayout) findViewById(R.id.ll_root);
        rc_view= (RecyclerView) findViewById(R.id.rc_view);
        sw_refresh= (SwipeRefreshLayout) findViewById(R.id.sw_refresh);

        IMutlipleItem<NewFriend> mutlipleItem=new IMutlipleItem<NewFriend>() {
            @Override
            public int getItemLayoutId(int viewtype) {
                return R.layout.item_new_friend;
            }

            @Override
            public int getItemViewType(int postion, NewFriend newFriend) {
                return 0;
            }

            @Override
            public int getItemCount(List<NewFriend> list) {
                return list.size();
            }
        };
        adapter = new NewFriendAdapter(this,mutlipleItem,null);
        rc_view.setAdapter(adapter);
        layoutManager=new LinearLayoutManager(this);
        rc_view.setLayoutManager(layoutManager);
        sw_refresh.setEnabled(true);
        //批量更新未读未认证的消息为已读状态
        NewFriendManager.getInstance(this).updateBatchStatus();
        setListener();
    }
    private void setListener(){
        ll_root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ll_root.getViewTreeObserver().removeGlobalOnLayoutListener(this);
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
                ShowToast("点击+position");
            }

            @Override
            public boolean onItemLongClick(int position) {
                NewFriendManager.getInstance(NewFriendActivity.this).deleteNewFriend(adapter.getItem(position));
                adapter.remove(position);
                return true;
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        sw_refresh.setRefreshing(true);
        query();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     查询本地会话
     */
    public void query(){
        adapter.bindDatas(NewFriendManager.getInstance(this).getAllNewFriend());
        adapter.notifyDataSetChanged();
        sw_refresh.setRefreshing(false);
    }

    @Override
    protected String title() {
        return "新朋友";
    }
}
