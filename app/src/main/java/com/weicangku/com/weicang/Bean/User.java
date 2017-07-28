package com.weicangku.com.weicang.Bean;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2016/12/17.
 */

public class User extends BmobChatUser {
    private static final long serialVersionUID = 1L;
    private String info;
    private Boolean sex;
    private String sign;
    private BmobFile image;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }


    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public BmobFile getImage() {
        return image;
    }

    public void setImage(BmobFile image) {
        this.image = image;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public User(){}
}
