package com.weicangku.com.weicang.Activity.circle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.weicangku.com.weicang.Activity.Base.ParentWithNaviActivity;
import com.weicangku.com.weicang.Bean.User;
import com.weicangku.com.weicang.Bean.circle.Helps;
import com.weicangku.com.weicang.Bean.circle.NotifyMsg;
import com.weicangku.com.weicang.R;
import com.weicangku.com.weicang.adapter.Base.Base.circle.MessageAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class MessageActivity extends ParentWithNaviActivity {

    private final static String TAG="MessageActivity";

    private ListView listView;

    private MessageAdapter adapter;

    private List<NotifyMsg> mItemData;

    private MaterialRefreshLayout materialRefreshLayout;

    private RefreshType mType=RefreshType.LOADMORE;

    private User myUser;

    private int numPage;

    private enum RefreshType{
        REFRESH,LOADMORE
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initNaviView();
        numPage=0;
        myUser= BmobUser.getCurrentUser(this,User.class);
        initEvent();
        initData();

    }


    @Override
    protected String title() {
        return "收到的评论";
    }

    @Override
    protected void initView() {
        super.initView();
        listView = (ListView) findViewById(R.id.lxw_id_message_listview);
        materialRefreshLayout= (MaterialRefreshLayout) findViewById(R.id.lxw_id_message_refresh);
    }
    private void initEvent() {
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                mType = RefreshType.REFRESH;
                query(0);
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                mType = RefreshType.LOADMORE;
                query(numPage);
            }
        });
    }
    private void initData() {
        mItemData=new ArrayList<NotifyMsg>();
        adapter=new MessageAdapter(this,mItemData);
        listView.setAdapter(adapter);
        if(mItemData.size()==0){
            mType=RefreshType.REFRESH;
            query(0);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Helps helps = mItemData.get(position).getHelps();
                Intent intent = new Intent(MessageActivity.this, HelpsOpenCommentActivity.class);
                intent.putExtra("helps", helps);
//                Bundle bundle=new Bundle();
//                bundle.putSerializable("helps",helps);
                if (helps.getContent()!=null){
                    startActivity(intent);
//                    startActivity(HelpsCommentActivity.class,bundle);
                }else{
                    ShowToast("原文已删除");
                }
//                startActivity(intent);
                if(!mItemData.get(position).isStatus()){
                    updateMessageStatus(position);
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                NotifyMsg msg=mItemData.get(position);
//                msg.setObjectId(myUser.getObjectId());
                msg.delete(MessageActivity.this, new DeleteListener() {
                    @Override
                    public void onSuccess() {
                        ShowToast("删除评论成功");
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        ShowToast("删除评论失败");
                    }
                });
                return false;
            }
        });
    }
    private void updateMessageStatus(int position) {
        NotifyMsg msg=mItemData.get(position);
        msg.setStatus(true);
        msg.update(this, new UpdateListener() {
            @Override
            public void onSuccess() {
                Log.e(TAG,"===update success ===");
            }

            @Override
            public void onFailure(int i, String s) {
                Log.e(TAG,"===update  error==="+s);
            }
        });
    }

    private void query(int page) {
        BmobQuery<NotifyMsg> query=new BmobQuery<NotifyMsg>();
        String objectId=myUser.getObjectId();
        query.setLimit(20);
        query.order("-createdAt");
        query.setSkip(20 * page);
        query.addWhereEqualTo("user", myUser);
        query.include("author,helps,user,comment,helps.phontofile");
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.findObjects(this, new FindListener<NotifyMsg>() {
            @Override
            public void onSuccess(List<NotifyMsg> list) {
                if(list.size()>0){
                    if(mType==RefreshType.REFRESH){
                        mItemData.clear();
                        numPage=0;
                    }
                    numPage++;
                    mItemData.addAll(list);
                    adapter.notifyDataSetChanged();
                    materialRefreshLayout.finishRefresh();
                    materialRefreshLayout.finishRefreshLoadMore();
                }else if(RefreshType.REFRESH==mType){
                    materialRefreshLayout.finishRefresh();
                }else if(RefreshType.LOADMORE==mType){
                    materialRefreshLayout.finishRefreshLoadMore();
                }else {
                    numPage--;
                }
            }

            @Override
            public void onError(int i, String s) {
                materialRefreshLayout.finishRefresh();
                materialRefreshLayout.finishRefreshLoadMore();
            }
        });    }



    @Override
    protected void onRestart() {
        super.onRestart();
        // query(numPage-1);
    }
}
