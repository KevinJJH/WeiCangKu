package com.weicangku.com.weicang.Bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Administrator on 2016/12/17.
 */

public class BmobChatUser extends BmobUser{

    private static final long serialVersionUID = 1L;
    private String nick;// 昵称
    private String avatar;// 头像信息
    private BmobRelation contacts;// 好友联系人
    private String installId;// 设备Id
    private String deviceType;// 设备类型
    private BmobRelation blacklist;// 黑名单


    public  BmobChatUser(){

    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setContacts(BmobRelation contacts) {
        this.contacts = contacts;
    }

    public void setInstallId(String installId) {
        this.installId = installId;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public void setBlacklist(BmobRelation blacklist) {
        this.blacklist = blacklist;
    }

    public String getNick() {

        return nick;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public  String  getAvatar() {
        return avatar;
    }

    public BmobRelation getContacts() {
        return contacts;
    }

    public String getInstallId() {
        return installId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public BmobRelation getBlacklist() {
        return blacklist;
    }


}
