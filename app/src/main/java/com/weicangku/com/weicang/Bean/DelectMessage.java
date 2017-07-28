package com.weicangku.com.weicang.Bean;

import cn.bmob.newim.bean.BmobIMExtraMessage;

/**请求消息撤回
 * Created by pengjing on 2016/7/6.
 *我利用自定义消息自定义消息撤回机制
 */
public class DelectMessage extends BmobIMExtraMessage {
    public DelectMessage(){}

    @Override
    public String getMsgType() {
        return "deback";
    }


    @Override
    public boolean isTransient() {
        //设置为true,表明为暂态消息，那么这条消息并不会保存到本地db中，SDK只负责发送出去
        //设置为false,则会保存到指定会话的数据库中
        return false;
    }

}