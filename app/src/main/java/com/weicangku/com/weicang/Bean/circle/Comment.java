package com.weicangku.com.weicang.Bean.circle;

import com.weicangku.com.weicang.Bean.User;

import cn.bmob.v3.BmobObject;

/**
 * Created by luxin on 15-12-17.
 *  http://luxin.gitcafe.io
 */
public class Comment extends BmobObject {
    private User user;
    private String comment;
    private Helps helps;


    public Comment(){}

    public Helps getHelps() {
        return helps;
    }

    public void setHelps(Helps helps) {
        this.helps = helps;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
