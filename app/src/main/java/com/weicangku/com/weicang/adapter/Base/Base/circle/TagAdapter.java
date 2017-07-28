package com.weicangku.com.weicang.adapter.Base.Base.circle;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.weicangku.com.weicang.ImageLoads.ImageLoaderFactory;
import com.weicangku.com.weicang.R;
import com.weicangku.com.weicang.Util.view.PinnedHeaderExpandableListView;

import java.util.List;


public class TagAdapter extends BaseExpandableListAdapter implements PinnedHeaderExpandableListView.HeaderAdapter{

    private List<String> childrenphoto;

    private List<String> childrenName;
    private List<String> groupData;
    private List<Integer> firstChildPosition;
    private List<Integer> positions;
    private Context context;
    private PinnedHeaderExpandableListView listView;
    private LayoutInflater inflater;


    public TagAdapter( List<String> childrenphoto,List<String> childrenName, List<Integer> firstChildPosition, List<Integer> positions, List<String> groupData
            , Context context, PinnedHeaderExpandableListView listView){

        this.childrenphoto=childrenphoto;

        this.groupData = groupData;
        this.childrenName = childrenName;
        this.firstChildPosition = firstChildPosition;
        this.positions = positions;
        this.context = context;
        this.listView = listView;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childrenName.get(childPosition + firstChildPosition.get(groupPosition));
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        childPosition = childPosition + firstChildPosition.get(groupPosition);
        final int position = positions.get(childPosition);
        View view = null;
        if (convertView != null) {
            view = convertView;
        } else {
            view = createChildrenView();
        }
        TextView text = (TextView) view.findViewById(R.id.tv_recent_name);
        text.setText(childrenName.get(childPosition));
        ImageView view1= (ImageView) view.findViewById(R.id.iv_recent_avatar);
        ImageLoaderFactory.getLoader().loadAvator(view1,childrenphoto.get(childPosition), R.drawable.head);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Friend friend = TagActivity.tags.get(position);
//                User user = friend.getFriendUser();
//                BmobIMUserInfo info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar());
//                //启动一个会话，实际上就是在本地数据库的会话列表中先创建（如果没有）与该用户的会话信息，且将用户信息存储到本地的用户表中
//                BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, null);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("c", c);
//                Intent intent = new Intent(context, LoginActivity.class);
////                intent.putExtras(bundle);
//                context.startActivity(intent);
//                //context.startActivity(ge,CircleMainActivity.class);
//            }
//        });
        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupPosition == groupData.size()-1){
            return childrenName.size() - firstChildPosition.get(groupPosition);}
        else {
          return   firstChildPosition.get(groupPosition+1)-firstChildPosition.get(groupPosition);
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupData.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groupData.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        View view = null;
        if (convertView != null) {
            view = convertView;
        } else {
            view = createGroupView();
        }

        ImageView iv = (ImageView)view.findViewById(R.id.image_parent);

        if (isExpanded) {
            iv.setImageResource(R.drawable.image_parent1);
        }
        else{
            iv.setImageResource(R.drawable.image_parent2);
        }

        TextView text = (TextView)view.findViewById(R.id.text_parent);
        text.setText(groupData.get(groupPosition));
        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private View createChildrenView() {
        return inflater.inflate(R.layout.list_item_child, null);
    }

    private View createGroupView() {
        return inflater.inflate(R.layout.list_tag_parent, null);
    }

    @Override
    public int getHeaderState(int groupPosition, int childPosition) {
        childPosition = childPosition + firstChildPosition.get(groupPosition);
        final int childCount = getChildrenCount(groupPosition);
        // 最後一個
        if (childPosition == childCount - 1) {
            return PINNED_HEADER_PUSHED_UP;
        }
        //
        else if (childPosition == -1
                && !listView.isGroupExpanded(groupPosition)) {
            return PINNED_HEADER_GONE;
        } else {
            return PINNED_HEADER_VISIBLE;
        }
    }

    @Override
    public void configureHeader(View header, int groupPosition,
                                int childPosition, int alpha) {
        String groupData =  this.groupData.get(groupPosition);
        ((TextView) header.findViewById(R.id.text_parent)).setText(groupData);

    }

    private SparseIntArray groupStatusMap = new SparseIntArray();

    @Override
    public void setGroupClickStatus(int groupPosition, int status) {
        groupStatusMap.put(groupPosition, status);
    }

    @Override
    public int getGroupClickStatus(int groupPosition) {
        if (groupStatusMap.keyAt(groupPosition)>=0) {
            return groupStatusMap.get(groupPosition);
        } else {
            return 0;
        }
    }




}