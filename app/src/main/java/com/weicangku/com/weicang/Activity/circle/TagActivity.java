package com.weicangku.com.weicang.Activity.circle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.weicangku.com.weicang.Activity.Base.ParentWithNaviActivity;
import com.weicangku.com.weicang.Activity.ChatActivity;
import com.weicangku.com.weicang.Bean.Friend;
import com.weicangku.com.weicang.Bean.User;
import com.weicangku.com.weicang.R;
import com.weicangku.com.weicang.Util.view.PinnedHeaderExpandableListView;
import com.weicangku.com.weicang.adapter.Base.Base.circle.TagAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;

public class TagActivity extends ParentWithNaviActivity {

    private PinnedHeaderExpandableListView explistview;
    private List<String> childrenphoto=new ArrayList<>();
    private List<String> childrenName = new ArrayList<>();
    private List<Integer> positions = new ArrayList<>();
    private List<Integer> firstChildPosition = new ArrayList<>();
    private List<String> groupData = new ArrayList<>();
    private int expandFlag = -1;//控制列表的展开
    private TagAdapter adapter;
    public static List<Friend>tags;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        initNaviView();
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        tags=(List<Friend>)bundle.getSerializable("tags");
        queryTags(tags);
        initData();
    }

    @Override
    protected String title() {
        return "我的代理";
    }

    @Override
    protected void initView() {
        super.initView();
        explistview = (PinnedHeaderExpandableListView)findViewById(R.id.explistview);
    }
    /**
     * 初始化数据
     */
    private void queryTags(List<Friend> tags) {
        groupData.clear();
        childrenName.clear();
        firstChildPosition.clear();
        positions.clear();
        int len = tags.size();
        for (int i = 0; i < len; i++) {
            String tagname = tags.get(i).getTag();//获取标签名
            if(tagname==null){
                continue;
            }
            if(groupData.contains(tagname))
                continue;
            groupData.add(tagname);

            int tmp = childrenName.size();
            firstChildPosition.add(tmp);

            for (int j=i; j<len; j++) {
                String tt = tags.get(j).getTag();
                if (tt==null)
                    continue;
                if (tt.equals(tagname)) {
                    String childname = tags.get(j).getFriendUser().getNick();
                    String child=tags.get(j).getFriendUser().getAvatar();
                    childrenName.add(childname);
                    childrenphoto.add(child);
                    positions.add(j);
                }
            }
        }
        // initData1();

        if(groupData.size()==0){
            TagActivity.this.finish();
            Toast.makeText(getApplicationContext(),"无标签分组，请长按用户进行设置",Toast.LENGTH_LONG).show();
        }
    }
    private void initData() {

//        for(int i=0;i<10;i++){
//            groupData.add("分组"+i) ;
//        }
//
//        for(int i=0;i<10;i++){
//            for(int j=0;j<10;j++){
//                childrenName.add("好友"+i+"-"+j);
//            }
//            firstChildPosition.add(1);
//            firstChildPosition.add(2);
//        }

//        queryTags();
        //设置悬浮头部VIEW
        explistview.setHeaderView(getLayoutInflater().inflate(R.layout.list_tag_parent,
                explistview, false));
        adapter = new TagAdapter(childrenphoto,childrenName, firstChildPosition,positions, groupData, getApplicationContext(),explistview);
        explistview.setAdapter(adapter);

        //设置单个分组展开
        //explistview.setOnGroupClickListener(new GroupClickListener());
        explistview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                childPosition = childPosition + firstChildPosition.get(groupPosition);
                int positon= positions.get(childPosition);
                Friend friend=TagActivity.tags.get(positon);
                User user =friend.getFriendUser();
                BmobIMUserInfo info = new BmobIMUserInfo(user.getObjectId(), user.getNick(), user.getAvatar());
                //启动一个会话，实际上就是在本地数据库的会话列表中先创建（如果没有）与该用户的会话信息，且将用户信息存储到本地的用户表中
                BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, null);
                Bundle bundle = new Bundle();
                bundle.putSerializable("c", c);
                startActivity(ChatActivity.class, bundle, false);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("c", c);
//                Intent intent = new Intent(TagActivity.this,ChatActivity.class);
//                intent.putExtras(bundle);
//                startActivity(intent);
                //context.startActivity(ge,CircleMainActivity.class);
                return false;
            }
        });
    }
}
