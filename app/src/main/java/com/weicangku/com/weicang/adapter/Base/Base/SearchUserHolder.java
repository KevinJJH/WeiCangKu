package com.weicangku.com.weicang.adapter.Base.Base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.weicangku.com.weicang.Activity.UserInfoActivity;
import com.weicangku.com.weicang.Bean.User;
import com.weicangku.com.weicang.ImageLoads.ImageLoaderFactory;
import com.weicangku.com.weicang.R;
import com.weicangku.com.weicang.adapter.Base.Base.Base.BaseViewHolder;
import com.weicangku.com.weicang.adapter.Base.Base.Base.OnRecyclerViewListener;

import butterknife.Bind;


public class SearchUserHolder extends BaseViewHolder {

  @Bind(R.id.avatar)
  public ImageView avatar;
  @Bind(R.id.name)
  public TextView name;
  @Bind(R.id.btn_add)
  public Button btn_add;

  public SearchUserHolder(Context context, ViewGroup root,OnRecyclerViewListener onRecyclerViewListener) {
    super(context, root, R.layout.item_search_user,onRecyclerViewListener);
  }

  @Override
  public void bindData(Object o) {
    final User user =(User)o;
    ImageLoaderFactory.getLoader().loadAvator(avatar,user.getAvatar(), R.drawable.head);
    name.setText(user.getUsername());
    btn_add.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {//查看个人详情
          Bundle bundle = new Bundle();
          bundle.putSerializable("u", user);
          startActivity(UserInfoActivity.class,bundle);
        }
    });
  }
}