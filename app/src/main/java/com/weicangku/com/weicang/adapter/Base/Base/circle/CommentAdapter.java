package com.weicangku.com.weicang.adapter.Base.Base.circle;

import android.content.Context;
import android.widget.TextView;

import com.weicangku.com.weicang.Bean.User;
import com.weicangku.com.weicang.Bean.circle.Comment;
import com.weicangku.com.weicang.R;
import com.weicangku.com.weicang.UtilView.CommonAdapter;
import com.weicangku.com.weicang.UtilView.CustomViewHolder;

import java.util.List;


/**
 * Created by luxin on 15-12-18.
 * http://luxin.gitcafe.io
 */
public class CommentAdapter extends CommonAdapter<Comment> {


    public CommentAdapter(Context context, List<Comment> list) {
        super(context, list);
    }



    @Override
    public void convert(CustomViewHolder holder, Comment comment) {
        TextView username = holder.getView(R.id.lxw_id_helps_comment_username);
        User myUser = comment.getUser();
        username.setText(myUser.getNick() + ":");
        TextView Comment = holder.getView(R.id.lxw_id_helps_comment_content);
        Comment.setText(comment.getComment());
    }


}
