package com.weicangku.com.weicang.Fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.weicangku.com.weicang.Activity.Base.ParentWithNaviActivity;
import com.weicangku.com.weicang.Activity.circle.HelpsCommentActivity;
import com.weicangku.com.weicang.Activity.circle.PublishActivity;
import com.weicangku.com.weicang.Bean.User;
import com.weicangku.com.weicang.Bean.circle.Comment;
import com.weicangku.com.weicang.Bean.circle.Helps;
import com.weicangku.com.weicang.Fragment.Base.ParentWithNaviFragment;
import com.weicangku.com.weicang.ImageLoads.UniversalImageLoader;
import com.weicangku.com.weicang.R;
import com.weicangku.com.weicang.Util.circle.TitlePopup;
import com.weicangku.com.weicang.adapter.Base.Base.circle.HelpsMainAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class DongTaiFragment extends ParentWithNaviFragment implements View.OnClickListener {
    private ImageView set_avator;
    private TextView set_nick;
    private MaterialRefreshLayout materialRefreshLayout;
    private ListView listView;
    private boolean isRefresh = false;
    private int numPage;
    private User user = null;
    private ProgressDialog mProgressDialog;
    private List<Helps> mItemList;
    private HelpsMainAdapter adapter;

    private TitlePopup titlePopup;
    private LinearLayout ll_huifu;
    private EditText et;
    private Button btn_comment;
    private Helps helps;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_comment:
                User user= BmobUser.getCurrentUser(getActivity(),User.class);
                String comment=et.getText().toString();
                if (TextUtils.isEmpty(comment)) {
                  ShowToast("内容不能为空");
                    return;
                }
                push(comment,user);
                et.setText("");
                break;
        }
    }

    private enum RefleshType {
        REFRESH, LOAD_MORE
    }

    private RefleshType refleshType = RefleshType.LOAD_MORE;

    public DongTaiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_dong_tai, container, false);
        initNaviView();
        materialRefreshLayout = getView(R.id.main_refresh);
        listView = getView(R.id.main_listview);
        set_avator = getView(R.id.User_avatar);
        set_nick = getView(R.id.Tv_name);
        ll_huifu = getView(R.id.ll_huifu);
        et = getView(R.id.et1);
        btn_comment = getView(R.id.btn_comment);
        btn_comment.setOnClickListener(this);
//
        numPage = 0;
        initEvent();
        initData();
        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateUser(userManager.getCurrentUser());
    }

    private void updateUser(User user) {
        // 更改
        set_nick.setText(user.getNick());
        refreshAvatar(user.getAvatar());
    }

    private void refreshAvatar(String avatar) {
        User user = userManager.getCurrentUser();
        final String avatarUrl = user.getAvatar();

        if (avatarUrl != null && !avatarUrl.equals("")) {
            UniversalImageLoader i = new UniversalImageLoader();
            i.loadAvator(set_avator, avatarUrl, R.drawable.no_image);
        }
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

            }
        });
    }

    private void initData() {
        mItemList = new ArrayList<Helps>();
//        adapter = new HelpsMainAdapter(getActivity(), mItemList, new HelpsMainAdapter.OnButtonOnclickListener() {
//            @Override
//            public void onButtonClick(View view,int position) {
//                helps=mItemList.get(position);
//                titlePopup = new TitlePopup(getActivity(), Util.dip2px(getActivity(), 165), Util.dip2px(
//                        getActivity(), 40));
//                titlePopup
//                        .addAction(new ActionItem(getActivity(), "赞", R.drawable.circle_praise));
//                titlePopup.addAction(new ActionItem(getActivity(), "评论",
//                        R.drawable.circle_comment));
//                titlePopup.setAnimationStyle(R.style.cricleBottomAnimation);
//                titlePopup.show(view);
//                titlePopup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
//                    @Override
//                    public void onItemClick(ActionItem item, int position) {
//                        switch (position) {
//                            case 0:
//                                Toast.makeText(getActivity(), "你点击了赞", Toast.LENGTH_SHORT).show();
//                                break;
//                            case 1:
//
////
////
//                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                                imm.toggleSoftInput(0,
//                                        InputMethodManager.HIDE_NOT_ALWAYS);
//                                ll_huifu.setVisibility(View.VISIBLE);
//                                et.setFocusableInTouchMode(true);
//                                et.requestFocus();
//
//                                break;
//                        }
//                    }
//                });
//            }
//        });
        adapter=new HelpsMainAdapter(getActivity(),mItemList);
        listView.setAdapter(adapter);

        if (mItemList.size() == 0) {
            refleshType = RefleshType.REFRESH;
            query(0);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), HelpsCommentActivity.class);
                intent.putExtra("helps", mItemList.get(position));
                startActivity(intent);
            }
        });


//        listView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                hideSoftInput(et.getWindowToken());
//                ll_huifu.setVisibility(View.GONE);
//                return false;
//            }
//        });

    }

    private void push(final String comment, final User myUser) {

        Comment comment1=new Comment();
        comment1.setUser(myUser);
        comment1.setComment(comment);
        comment1.setHelps(helps);
        comment1.save(getActivity(), new SaveListener() {
            @Override
            public void onSuccess() {
            ShowToast("评论成功");

            }

            @Override
            public void onFailure(int i, String s) {
                ShowToast("评论失败");
            }
        });
    }

    private void query(int page) {
        BmobQuery<Helps> helpsBmobQuery = new BmobQuery<Helps>();
        helpsBmobQuery.setLimit(20);
        helpsBmobQuery.order("-createdAt");
//        BmobDate date=new BmobDate(new Date(System.currentTimeMillis()));
//        helpsBmobQuery.addWhereLessThan("createdAt",date);
//pengjing
        helpsBmobQuery.setSkip(20 * page);
        helpsBmobQuery.include("user,phontofile");//希望在查询的时候，把指针所指向的信息也查询出来
        boolean isCache = helpsBmobQuery.hasCachedResult(getActivity(),Helps.class);
        if(isCache){
            helpsBmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_THEN_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        }else{
            helpsBmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        }
//        helpsBmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_THEN_NETWORK);
//        helpsBmobQuery.include("phontofile");
        helpsBmobQuery.findObjects(getActivity(), new FindListener<Helps>() {
            @Override
            public void onSuccess(List<Helps> list) {
//                mProgressDialog.dismiss();
                if (list.size() > 0) {
                    if (refleshType == RefleshType.REFRESH) {
//                        Log.e(TAG, "====refresh list clear");
                        numPage = 0;
                        mItemList.clear();

                    }
                    if (list.size() < 20) {

                    }
                    mItemList.addAll(list);
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
//                mProgressDialog.dismiss();
                materialRefreshLayout.finishRefresh();
                materialRefreshLayout.finishRefreshLoadMore();

                shwoErrorDialog();
            }
        });
    }

    private void shwoErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
    protected String title() {
        return "动态";
    }

    @Override
    public Object right() {
        return "发表";
    }

    @Override
    public ParentWithNaviActivity.ToolBarListener setToolBarListener() {
        return new ParentWithNaviActivity.ToolBarListener() {
            @Override
            public void clickLeft() {

            }

            @Override
            public void clickRight() {
                startActivity(PublishActivity.class, null, false);
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isRefresh) {
            query(numPage - 1);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUser(userManager.getCurrentUser());
    }
}

