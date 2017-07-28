package com.weicangku.com.weicang.Bean.circle;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/2/15.
 */

public class BmobFiles extends BmobObject {
    private  String filename;
    private String group;
    private String url;
    public BmobFiles(String fileName, String group, String url){
        this.filename = fileName;
        this.group=group;
        this.url = url;
    }
}
