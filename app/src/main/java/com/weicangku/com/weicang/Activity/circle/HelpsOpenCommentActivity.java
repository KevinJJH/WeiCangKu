package com.weicangku.com.weicang.Activity.circle;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.weicangku.com.weicang.Activity.Base.ParentWithNaviActivity;
import com.weicangku.com.weicang.Bean.User;
import com.weicangku.com.weicang.Bean.circle.Comment;
import com.weicangku.com.weicang.Bean.circle.Helps;
import com.weicangku.com.weicang.Bean.circle.MyUserInstallation;
import com.weicangku.com.weicang.Bean.circle.NotifyMsg;
import com.weicangku.com.weicang.Bean.circle.PhontoFiles;
import com.weicangku.com.weicang.R;
import com.weicangku.com.weicang.Util.circle.ActionItem;
import com.weicangku.com.weicang.Util.circle.DateUtil;
import com.weicangku.com.weicang.Util.circle.ExpressionUtil;
import com.weicangku.com.weicang.Util.circle.ImagLoads;
import com.weicangku.com.weicang.Util.circle.TitlePopup;
import com.weicangku.com.weicang.Util.circle.Util;
import com.weicangku.com.weicang.adapter.Base.Base.circle.CommentAdapter;
import com.weicangku.com.weicang.adapter.Base.Base.circle.GridViewHelpsAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2017/3/2.
 */

public class HelpsOpenCommentActivity extends ParentWithNaviActivity implements View.OnClickListener {
    private ListView listView;
    private Helps helps;
    private List<Comment> mItemComment;
    private CommentAdapter adapter;
    private View view;
    private ImageView userImg;
    private TextView username;
    private TextView creatTime;
    private TextView content;
    private ImageView contentImg;
    private GridView gridView;
    private  ImageView comment;

    private TitlePopup titlePopup;
    private LinearLayout ll_huifu;
    private EditText et;
    private Button btn_comment;
    private Button btn_delete_help;
    String imgpath = null;
    List<BmobFile> phontos = null;
    private GridViewHelpsAdapter gridViewHelpsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helps_comment);
        initNaviView();
    }

    @Override
    protected String title() {
        return "正文";
    }

    @Override
    protected void initView() {
        super.initView();
        listView= (ListView) findViewById(R.id.lxw_id_helps_comment_listview);
        LayoutInflater inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.lxw_item_helps, null);

        userImg = (ImageView) view.findViewById(R.id.lxw_id_item_helps_userimg);
        username = (TextView) view.findViewById(R.id.lxw_id_item_helps_username);
        creatTime = (TextView) view.findViewById(R.id.lxw_id_item_helps_create_time);
        content = (TextView) view.findViewById(R.id.lxw_id_item_helps_content);
        contentImg = (ImageView) view.findViewById(R.id.lxw_id_item_helps_content_img);
        gridView = (GridView) view.findViewById(R.id.lxw_id_item_helps_gridview);
        comment= (ImageView) view.findViewById(R.id.comment);

        ll_huifu= (LinearLayout)findViewById(R.id.ll_huifu);
        et= (EditText)findViewById(R.id.et1);

        btn_comment= (Button) findViewById(R.id.btn_comment);
        btn_delete_help= (Button) view.findViewById(R.id.delete_comment);
//        btn_delete_help.setVisibility(View.VISIBLE);
//        if(helps.getUser().getObjectId().equals(userManager.getCurrentUser().getObjectId()) ){
//            btn_delete_help.setVisibility(View.VISIBLE);
//        }
//        else{
//            btn_delete_help.setVisibility(View.GONE);
//        }

        comment.setVisibility(View.VISIBLE);
        comment.setOnClickListener(this);
        btn_comment.setOnClickListener(this);
        btn_delete_help.setOnClickListener(this);
        initData();
    }

    private void initData() {
        helps = (Helps) getIntent().getSerializableExtra("helps");
//        helps= (Helps) getBundle().getSerializable("helps");
        User myUser = BmobUser.getCurrentUser(this,User.class);

        if (myUser.getAvatar() != null) {
            ImagLoads.getInstance(1, ImagLoads.Type.LIFO).loaderImage(myUser.getAvatar(), userImg, true);
//            ImageLoaderFactory.getLoader().loadAvator(userImg,myUser.getAvatar(),R.drawable.no_image);
        }
        username.setText(myUser.getNick());
        creatTime.setText(DateUtil.getFriendlyDate(helps.getCreatedAt()));
        SpannableString spannableString = ExpressionUtil.getSpannableString(this, helps.getContent());
        content.setText(spannableString);


        final PhontoFiles phontoFiles = helps.getPhontofile();
        if (phontoFiles != null) {

            imgpath = helps.getPhontofile().getPhoto();
            phontos = helps.getPhontofile().getPhotos();
//            relativeLayout.setVisibility(View.GONE);
        }
        if (imgpath != null) {

            contentImg.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
            ImagLoads.getmInstance().loaderImage(imgpath, contentImg, true);
            contentImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HelpsOpenCommentActivity.this, LookImageActivity.class);
                    intent.putExtra("helps", helps);
                    startActivity(intent);
                }
            });
        } else if (phontos != null) {
            contentImg.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
            gridViewHelpsAdapter = new GridViewHelpsAdapter(this, phontos);
            gridView.setAdapter(gridViewHelpsAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(HelpsOpenCommentActivity.this, LookImageViewPagerActivity.class);
                    intent.putExtra("phontoFiles", phontoFiles);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            });
        }
        else{
            contentImg.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
        }
        mItemComment = new ArrayList<Comment>();
        if (mItemComment.size() == 0) {
            query();
        }
        listView.addHeaderView(view);
        adapter = new CommentAdapter(this, mItemComment);
        listView.setAdapter(adapter);

        if(helps.getUser().getObjectId().equals( BmobUser.getCurrentUser(this, User.class).getObjectId()) ){
            btn_delete_help.setVisibility(View.VISIBLE);
        }
        else{
            btn_delete_help.setVisibility(View.GONE);
        }

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftInput(et.getWindowToken());
                ll_huifu.setVisibility(View.GONE);
                return false;
            }
        });
    }
    private void query() {
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.addWhereEqualTo("helps", new BmobPointer(helps));
        query.setLimit(50);
        query.include("user,helps.user");
//        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.findObjects(this, new FindListener<Comment>() {
            @Override
            public void onSuccess(List<Comment> list) {
                if (list.size() > 0) {
                    mItemComment.clear();
                    mItemComment.addAll(list);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onError(int i, String s) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.comment:
                titlePopup = new TitlePopup(this, Util.dip2px(this, 165), Util.dip2px(
                        this, 40));
                titlePopup
                        .addAction(new ActionItem(this, "赞", R.drawable.circle_praise));
                titlePopup.addAction(new ActionItem(this, "评论",
                        R.drawable.circle_comment));
                titlePopup.setAnimationStyle(R.style.cricleBottomAnimation);
                titlePopup.show(v);
                titlePopup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
                    @Override
                    public void onItemClick(ActionItem item, int position) {
                        switch (position){
                            case 0:
                                ShowToast("你点击了赞");
                                break;
                            case 1:
                                InputMethodManager imm = (InputMethodManager)HelpsOpenCommentActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.toggleSoftInput(0,
                                        InputMethodManager.SHOW_FORCED);
                                ll_huifu.setVisibility(View.VISIBLE);
                                et.setFocusableInTouchMode(true);
                                et.requestFocus();
                                break;
                        }
                    }
                });
                break;
            case R.id.btn_comment:
                User user= BmobUser.getCurrentUser(this,User.class);
                String comment=et.getText().toString();
                if (TextUtils.isEmpty(comment)) {
                    ShowToast("内容不能为空");
                    return;
                }
                push(comment,user);
                et.setText("");
                break;
            case R.id.delete_comment:
                AlertDialog.Builder dialog1=new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT)
                        .setTitle("删除动态")
                        .setMessage("确定删除？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                deleteEvent();
                            }
                        }).setNegativeButton("取消", null);
                dialog1.show();
                break;
        }

    }
    private boolean isFirst = true;
    private void push(final String comment, final User myUser) {

        Comment comment1=new Comment();
        comment1.setUser(myUser);
        comment1.setComment(comment);
        comment1.setHelps(helps);
        comment1.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                ShowToast("评论成功");
                if(!myUser.getObjectId().equals(helps.getUser().getObjectId())){
                    bmobpush(myUser,comment);
                }
                isFirst = false;
                query();
            }

            @Override
            public void onFailure(int i, String s) {
                ShowToast("评论失败");
            }
        });
    }

    /**
     * 使用bmob进行消息推送
     * @param myUser
     */
    private void bmobpush(User myUser,String comment) {
        String installationId=helps.getUser().getObjectId();
        BmobPushManager bmobPushManager=new BmobPushManager(this);
        BmobQuery<MyUserInstallation> query=new BmobQuery<MyUserInstallation>();
        query.addWhereEqualTo("uid", installationId);
        bmobPushManager.setQuery(query);
        bmobPushManager.pushMessage(myUser.getNick() + "评论了你");

        NotifyMsg notifyMsg=new NotifyMsg();
        notifyMsg.setHelps(helps);
        notifyMsg.setUser(helps.getUser());
        notifyMsg.setAuthor(myUser);
        notifyMsg.setStatus(false);
        notifyMsg.setMessage(comment);
        notifyMsg.save(this, new SaveListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    private void deleteEvent() {


        Helps helps1 = new Helps();
        helps1.setObjectId(helps.getObjectId());
        helps1.delete(this, new DeleteListener() {
            @Override
            public void onSuccess() {
                ShowToast("删除动态成功");
                finish();
            }
            @Override
            public void onFailure(int i, String s) {
                ShowToast("删除动态失败");
            }
        });
    }
}
