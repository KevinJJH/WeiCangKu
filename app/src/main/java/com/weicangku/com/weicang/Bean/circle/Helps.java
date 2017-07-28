package com.weicangku.com.weicang.Bean.circle;

import com.weicangku.com.weicang.Bean.User;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by luxin on 15-12-8.
 *  http://luxin.gitcafe.io
 */
public class Helps extends BmobObject implements Serializable {
    private static final long serialVersionUID=1L;

    private String content;
    private String pushdate;
    private int state;
    private User user;
    private User helpuser;
    private HelpInfo helpInfo;
    private PhontoFiles phontofile;
//    private PhontoFiles good_photo_file;
//    private VideoFiles videofile;



    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPushdate() {
        return pushdate;
    }

    public void setPushdate(String pushdate) {
        this.pushdate = pushdate;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getHelpuser() {
        return helpuser;
    }

    public void setHelpuser(User hepluser) {
        this.helpuser = hepluser;
    }

    public HelpInfo getHelpInfo() {
        return helpInfo;
    }

    public void setHelpInfo(HelpInfo helpInfo) {
        this.helpInfo = helpInfo;
    }

    public PhontoFiles getPhontofile() {
        return phontofile;
    }

    public void setPhontofile(PhontoFiles phontofile) {
        this.phontofile = phontofile;
    }

//    public PhontoFiles getGood_photo_file() {
//        return good_photo_file;
//    }
//
//    public void setGood_photo_file(PhontoFiles good_photo_file) {
//        this.good_photo_file = good_photo_file;
//    }
//    public VideoFiles getVideofile() {
//        return videofile;
//    }
//
//    public void setVideofile(VideoFiles videofile) {
//        this.videofile = videofile;
//    }



}
