package com.weicangku.com.weicang.adapter.Base.Base;

import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.weicangku.com.weicang.Activity.UserInfoActivity;
import com.weicangku.com.weicang.Bean.BmobUserManager;
import com.weicangku.com.weicang.Bean.QueryUserListener;
import com.weicangku.com.weicang.Bean.User;
import com.weicangku.com.weicang.ImageLoads.ImageLoaderFactory;
import com.weicangku.com.weicang.R;
import com.weicangku.com.weicang.Util.FaceTextUtils;
import com.weicangku.com.weicang.adapter.Base.Base.Base.BaseViewHolder;
import com.weicangku.com.weicang.adapter.Base.Base.Base.OnRecyclerViewListener;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.exception.BmobException;

/**
 * 接收到的文本类型
 */
public class ReceiveTextHolder extends BaseViewHolder {

  @Bind(R.id.iv_avatar)
  protected ImageView iv_avatar;

  @Bind(R.id.tv_time)
  protected TextView tv_time;

  @Bind(R.id.tv_message)
  protected TextView tv_message;

  public ReceiveTextHolder(Context context, ViewGroup root,OnRecyclerViewListener onRecyclerViewListener) {
    super(context, root, R.layout.item_chat_received_message,onRecyclerViewListener);
  }

  @OnClick({R.id.iv_avatar})
  public void onAvatarClick(View view) {

  }

  @Override
  public void bindData(Object o) {
    final BmobIMMessage message = (BmobIMMessage)o;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    String time = dateFormat.format(message.getCreateTime());
    tv_time.setText(time);
    final BmobIMUserInfo info = message.getBmobIMUserInfo();
    ImageLoaderFactory.getLoader().loadAvator(iv_avatar,info != null ? info.getAvatar() : null, R.drawable.head);
    String content =  message.getContent();
//    tv_message.setText(content);
    try {
      SpannableString spannableString = FaceTextUtils
              .toSpannableString(getContext(), content);
      tv_message.setText(spannableString);
    } catch (Exception e) {
    }
    iv_avatar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
//        toast("点击" + info.getName() + "的头像");
        BmobUserManager.getInstance().queryUserInfo(info.getUserId(), new QueryUserListener() {
          @Override
          public void done(User s, BmobException e) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("u", s);
            startActivity(UserInfoActivity.class, bundle);
          }
        });
      }
    });
    tv_message.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          toast("点击"+message.getContent());
          if(onRecyclerViewListener!=null){
            onRecyclerViewListener.onItemClick(getAdapterPosition());
          }
        }
    });

    tv_message.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
          if (onRecyclerViewListener != null) {
            onRecyclerViewListener.onItemLongClick(getAdapterPosition());
          }
          return true;
        }
    });
  }

  public void showTime(boolean isShow) {
    tv_time.setVisibility(isShow ? View.VISIBLE : View.GONE);
  }
}