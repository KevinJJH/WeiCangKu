package com.weicangku.com.weicang.Activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.weicangku.com.weicang.Activity.Base.ParentWithNaviActivity;
import com.weicangku.com.weicang.Bean.BmobUserManager;
import com.weicangku.com.weicang.Bean.User;
import com.weicangku.com.weicang.R;
import com.weicangku.com.weicang.adapter.Base.Base.Base.OnRecyclerViewListener;
import com.weicangku.com.weicang.adapter.Base.Base.SearchUserAdapter;

import java.util.List;

import cn.bmob.v3.listener.FindListener;

public class SearchUserActivity extends ParentWithNaviActivity implements View.OnClickListener {
    private EditText et_find_name;
    private SwipeRefreshLayout sw_refresh;
    private Button btn_search;
    private RecyclerView rc_view;
    private LinearLayoutManager layoutManager;
    SearchUserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        initNaviView();
    }

    @Override
    protected String title() {
        return "搜索好友";
    }

    @Override
    protected void initView() {
        super.initView();
        et_find_name= (EditText) findViewById(R.id.et_find_name);
        btn_search= (Button) findViewById(R.id.btn_search);
        sw_refresh= (SwipeRefreshLayout) findViewById(R.id.sw_refresh);
        rc_view= (RecyclerView) findViewById(R.id.rc_view);
        layoutManager = new LinearLayoutManager(this);
        rc_view.setLayoutManager(layoutManager);
        adapter =new SearchUserAdapter();
        rc_view.setAdapter(adapter);
        sw_refresh.setEnabled(true);
        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                User user = adapter.getItem(position);
                bundle.putSerializable("u", user);
                startActivity(UserInfoActivity.class, bundle, false);
            }

            @Override
            public boolean onItemLongClick(int position) {
                return true;
            }
        });
        btn_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                sw_refresh.setRefreshing(true);
                query();
                break;
        }
    }
    public void query(){
        String name =et_find_name.getText().toString().trim();
        if(TextUtils.isEmpty(name)){
            toast("请填写用户名");
            sw_refresh.setRefreshing(false);
            return;
        }
        BmobUserManager.getInstance().queryUsers(name, BmobUserManager.DEFAULT_LIMIT, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                sw_refresh.setRefreshing(false);
                adapter.setDatas(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {
                sw_refresh.setRefreshing(false);
                adapter.setDatas(null);
                adapter.notifyDataSetChanged();
                toast(s + "(" + i + ")");
            }
        });
    }


}
