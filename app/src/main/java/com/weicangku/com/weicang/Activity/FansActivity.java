package com.weicangku.com.weicang.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.weicangku.com.weicang.Activity.Base.ParentWithNaviActivity;
import com.weicangku.com.weicang.Activity.circle.TagActivity;
import com.weicangku.com.weicang.Bean.BmobUserManager;
import com.weicangku.com.weicang.Bean.Friend;
import com.weicangku.com.weicang.Bean.User;
import com.weicangku.com.weicang.R;
import com.weicangku.com.weicang.Util.ClearEditText;
import com.weicangku.com.weicang.Util.circle.ModifyDialog;
import com.weicangku.com.weicang.Util.view.dialog2.DialogTips2;
import com.weicangku.com.weicang.adapter.Base.Base.Base.IMutlipleItem;
import com.weicangku.com.weicang.adapter.Base.Base.Base.OnRecyclerViewListener;
import com.weicangku.com.weicang.adapter.Base.Base.ContactAdapter;

import java.io.Serializable;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import static cn.bmob.newim.core.BmobIMClient.getContext;

public class FansActivity extends ParentWithNaviActivity implements View.OnClickListener {
    private ClearEditText mClearEditText;
    private LinearLayout layout_newfriend;
    private  LinearLayout Daili_group;

    private RecyclerView rc_view;
    private SwipeRefreshLayout sw_refresh;
    LinearLayoutManager layoutManager;
    ContactAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans);
        initNaviView();
        setListener();
    }

    @Override
    protected void initView() {
        super.initView();
        mClearEditText=getView(R.id.et_msg_search);
        layout_newfriend=getView(R.id.Layout_newfriend);
        Daili_group=getView(R.id.Daili_group);
        Daili_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryTags();
            }
        });
        rc_view=getView(R.id.rc_view);
        sw_refresh=getView(R.id.sw_refresh);
        layoutManager = new LinearLayoutManager(this);
        rc_view.setLayoutManager(layoutManager);


        IMutlipleItem<Friend> mutlipleItem = new IMutlipleItem<Friend>() {

            @Override
            public int getItemViewType(int postion, Friend friend) {
//                if(postion==0){
//                    return ContactAdapter.TYPE_NEW_FRIEND;
//                }
//                if (postion == 1) {
//                    return ContactAdapter.TYPE_TAG;
//                }
                return ContactAdapter.TYPE_ITEM;
            }

            @Override
            public int getItemLayoutId(int viewtype) {
//                if(viewtype== ContactAdapter.TYPE_NEW_FRIEND){
//                    return R.layout.header_new_friend;
//                }
//                if (viewtype == ContactAdapter.TYPE_TAG) {
//                    return R.layout.header_tag;
//                }
                return R.layout.item_contact;
            }

            @Override
            public int getItemCount(List<Friend> list) {
                return list.size() ;
            }
        };

        adapter = new ContactAdapter(this,mutlipleItem,null);

//        adapter = new ContactAdapter(this,mutlipleItem,null);
        rc_view.setAdapter(adapter);
        sw_refresh.setEnabled(true);
        layout_newfriend.setOnClickListener(this);

    }

    @Override
    protected String title() {
        return "粉丝与代理";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Layout_newfriend:
                startActivity(NewFriendActivity.class,null,false);
                break;
        }
    }

    private void setListener(){
        rc_view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rc_view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
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

                    Friend friend = adapter.getItem(position);
                    User user = friend.getFriendUser();
                    BmobIMUserInfo info = new BmobIMUserInfo(user.getObjectId(), user.getNick(), user.getAvatar());
                    //启动一个会话，实际上就是在本地数据库的会话列表中先创建（如果没有）与该用户的会话信息，且将用户信息存储到本地的用户表中
                    BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, null);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("c", c);
                    startActivity(ChatActivity.class, bundle);

            }

            @Override
            public boolean onItemLongClick(final int position) {
//                log("长按" + position);
//                if (position == 0) {
//                    return true;
//                }
                showChoiceDialog(position);
                return true;
            }
        });
    }
    public void query(){
        BmobUserManager.getInstance().queryFriends(new FindListener<Friend>() {
            @Override
            public void onSuccess(List<Friend> list) {
                adapter.bindDatas(list);
                //adapter.bindDatas(friends);
                adapter.notifyDataSetChanged();
                sw_refresh.setRefreshing(false);
            }

            @Override
            public void onError(int i, String s) {
                adapter.bindDatas(null);
                adapter.notifyDataSetChanged();
                sw_refresh.setRefreshing(false);
            }
        });
    }
    public void showChoiceDialog(final int position) {
        DialogTips2 dialog = new DialogTips2(this, "删除好友", "设置标签", true);
        // 设置添加标签事件
        dialog.SetOnCancelListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                TagDialog(FansActivity.this, "设置标签名称", position);

            }
        });
        //设置删除好友事件
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                //此处为服务器删除命令
                BmobUserManager.getInstance().deleteFriend(adapter.getItem(position), new DeleteListener() {
                    @Override
                    public void onSuccess() {
                        toast("好友删除成功");
                        adapter.remove(position);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        toast("好友删除失败：" + i + ",s =" + s);
                    }
                });
            }
        });

        // 显示确认对话框
        dialog.show();
        dialog = null;
    }
    String tag = null;
    public void TagDialog(Context context, String title, final int position) {
        final ModifyDialog dialog = new ModifyDialog(context, title, null);
        final EditText edit_modify = dialog.getEditText();
        dialog.setOnClickCommitListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tag = edit_modify.getText().toString();
                if(!tag.isEmpty()) {
                    adapter.getItem(position).setTag(tag);
                    adapter.getItem(position).update(getContext(), adapter.getItem(position).getObjectId(), new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(getContext(), "设置标签成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(getContext(), "设置标签失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(getContext(),"请输入标签名称",Toast.LENGTH_LONG).show();
                }
//                adapter.getItem(position).save(getContext(), new SaveListener() {
//                    @Override
//                    public void onSuccess() {
//                        Toast.makeText(getContext(), "设置标签成功", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onFailure(int i, String s) {
//                        Toast.makeText(getContext(),"设置标签失败",Toast.LENGTH_SHORT).show();
//                    }
//                });
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    List<Friend> tags;
    private void queryTags() {
        User user = BmobUser.getCurrentUser(getContext(), User.class);
        BmobQuery<Friend> query = new BmobQuery<>();
        query.addWhereEqualTo("user", user);
        query.include("friendUser");
        query.findObjects(this, new FindListener<Friend>() {
            @Override
            public void onSuccess(List<Friend> list) {
                if(list.size()>0)
                {   tags = list;
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("tags", (Serializable) tags);
                    Intent intent = new Intent(FansActivity.this, TagActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else {
                    Toast.makeText(FansActivity.this,"无好友，请添加好友",Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });

    }
}
