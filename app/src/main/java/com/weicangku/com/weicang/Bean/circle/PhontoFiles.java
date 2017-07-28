package com.weicangku.com.weicang.Bean.circle;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by luxin on 15-12-8.
 *  http://luxin.gitcafe.io
 */
public class PhontoFiles extends BmobObject implements Serializable {
    private static final long serialVersionUID=1L;

    private List<BmobFile> photos;
    private List<BmobFile> good_pictures;
    private String photo;
    private String good_picture;

    public List<BmobFile> getGood_pictures() {
        return good_pictures;
    }

    public void setGood_pictures(List<BmobFile> good_pictures) {
        this.good_pictures = good_pictures;
    }

    public String getGood_picture() {
        return good_picture;
    }

    public void setGood_picture(String good_picture) {
        this.good_picture = good_picture;
    }

    public List<BmobFile> getPhotos() {
        return photos;
    }

    public void setPhotos(List<BmobFile> photos) {
        this.photos = photos;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
