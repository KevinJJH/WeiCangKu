package com.weicangku.com.weicang.Bean.circle;

import com.weicangku.com.weicang.Bean.User;

import cn.bmob.v3.BmobObject;

/**
 * Created by luxin on 15-12-23.
 * http://luxin.gitcafe.io
 */
public class NotifyMsg extends BmobObject {

    private Helps helps;
    private String message;
    private Comment comment;
    private User user;
    private boolean status;
    private User author;

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Helps getHelps() {
        return helps;
    }

    public void setHelps(Helps helps) {
        this.helps = helps;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
