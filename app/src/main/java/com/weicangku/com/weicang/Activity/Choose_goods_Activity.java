package com.weicangku.com.weicang.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.weicangku.com.weicang.Activity.Base.ParentWithNaviActivity;
import com.weicangku.com.weicang.Bean.User;
import com.weicangku.com.weicang.Bean.circle.Goods;
import com.weicangku.com.weicang.R;
import com.weicangku.com.weicang.adapter.Base.Base.ActGoodsResultAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

public class Choose_goods_Activity extends ParentWithNaviActivity {
    private MaterialRefreshLayout materialRefreshLayout;
    private Button push_goods;
    private GridView gridView;

    private List<Goods> goodslists;
    private ActGoodsResultAdapter adapter;
    private boolean isRefresh = false;

    private int numPage;
    private enum RefleshType {
        REFRESH, LOAD_MORE
    }

    private RefleshType refleshType = RefleshType.LOAD_MORE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_goods_);
        initNaviView();
        numPage = 0;
        initEvent();
        initdata();
    }

    @Override
    protected String title() {
        return "选择商品";
    }

    @Override
    protected void initView() {
        super.initView();
        materialRefreshLayout = getView(R.id.main_refresh);
        push_goods=getView(R.id.push_goods);
        gridView=getView(R.id.act_goods_result_gv);
        push_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(Publish_Good_Activity.class,null,false);
            }
        });
    }
    private void initEvent() {
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
             refleshType = RefleshType.REFRESH;
                query(0);
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                refleshType = RefleshType.LOAD_MORE;
                query(numPage);
//                materialRefreshLayout.finishRefreshLoadMore();
            }
        });
    }
    private void initdata(){
        goodslists=new ArrayList<Goods>();
        adapter=new ActGoodsResultAdapter(goodslists,this);
        gridView.setAdapter(adapter);
        if (goodslists.size()==0){
            refleshType=RefleshType.REFRESH;
            query(0);
        }
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               Goods goods=goodslists.get(position);
                goods.delete(Choose_goods_Activity.this, new DeleteListener() {
                    @Override
                    public void onSuccess() {
                        ShowToast("删除成功");
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });

                return false;
            }
        });
       gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               ShowToast("你点击了"+position);
           }
       });
    }
    private void query(int page) {
        BmobQuery<Goods> query =  new BmobQuery<>();
        query.setLimit(20);
        query.order("-createdAt");
        query.setSkip(20*page);
        query.include("user,good_photo_file");
        query.addWhereEqualTo("user", BmobUser.getCurrentUser(this,User.class));
//        boolean isCache = query.hasCachedResult(this,Goods.class);
//        if(isCache){
//            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_THEN_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
//        }else{
//            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
//        }
        query.findObjects(this, new FindListener<Goods>() {
            @Override
            public void onSuccess(List<Goods> list) {
                if (list.size() > 0) {
                    if (refleshType == RefleshType.REFRESH) {
//                        Log.e(TAG, "====refresh list clear");
                        numPage = 0;
                        goodslists.clear();

                    }
                    goodslists.addAll(list);
                    numPage++;
                    adapter.notifyDataSetChanged();
                    materialRefreshLayout.finishRefresh();
                    materialRefreshLayout.finishRefreshLoadMore();
                }
                else if (refleshType == RefleshType.REFRESH) {

                    materialRefreshLayout.finishRefresh();
                }
                else if (refleshType == RefleshType.LOAD_MORE) {
                    materialRefreshLayout.finishRefreshLoadMore();
                }
                else {
                    numPage--;
                }

            }
            @Override
            public void onError(int i, String s) {
                materialRefreshLayout.finishRefresh();
                materialRefreshLayout.finishRefreshLoadMore();
                shwoErrorDialog();
            }
        });
    }
    private void shwoErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alert = builder.setMessage("未获取到数据，请检查网络数据后重试。")
                .setNegativeButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        alert.show();
    }
    @Override
    public void onStart() {
        super.onStart();
        if (isRefresh) {
            query(numPage - 1);
        }
    }
}
