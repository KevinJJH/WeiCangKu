package com.weicangku.com.weicang.UtilView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.weicangku.com.weicang.R;

import java.util.List;

/**
 * Created by Administrator on 2017/7/28.
 */

public class ImageBarnnerFramLayout extends FrameLayout implements ImageBarnnerViewGroup.ImgaeBarnnerViewGroupListener{
    private  ImageBarnnerViewGroup mgroup;
    private LinearLayout ll;
    private FramLaoutListener framlayoutlisten;
    public ImageBarnnerFramLayout(@NonNull Context context) {
        super(context);
        initImageBarnnerViewGroup();
        initDotLinearLayout();
    }

    private void initDotLinearLayout() {
        ll=new LinearLayout(getContext());
        FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
               40);
        ll.setLayoutParams(lp);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setGravity(Gravity.CENTER);
        ll.setBackgroundColor(Color.TRANSPARENT);
        addView(ll);
        FrameLayout.LayoutParams layoutparms= (LayoutParams) ll.getLayoutParams();
        layoutparms.gravity=Gravity.BOTTOM;
        ll.setLayoutParams(layoutparms);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
            ll.setAlpha(0.5f);
        }else {
            ll.getBackground().setAlpha(100);
        }
    }

    /**
     * 初始化图片轮播功能
     */
    private void initImageBarnnerViewGroup() {
        mgroup=new ImageBarnnerViewGroup(getContext());
        FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        mgroup.setLayoutParams(lp);
        mgroup.setImgaeBarnnerViewGroupListener(this);
        addView(mgroup);
    }

    public ImageBarnnerFramLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initImageBarnnerViewGroup();
        initDotLinearLayout();
    }

    public ImageBarnnerFramLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initImageBarnnerViewGroup();
        initDotLinearLayout();
    }
    public void addBitmap(List<Bitmap> list){
    for (int i=0;i<list.size();i++){
        Bitmap bitmap=list.get(i);
        addBitmapToImageBarnnerViewGroup(bitmap);
        addDotToLinearLayout();
    }
    }

    private void addDotToLinearLayout() {
        ImageView view=new ImageView(getContext());
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5,5,5,5);
        view.setLayoutParams(lp);
        view.setImageResource(R.drawable.guide_shape_retangle_unselect);
        ll.addView(view);
    }

    private void addBitmapToImageBarnnerViewGroup(Bitmap bitmap) {
        ImageView view=new ImageView(getContext());
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            view.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            view.setImageBitmap(bitmap);
            mgroup.addView(view);
            mgroup.setListener(new ImageBarnnerViewGroup.ImageBarnnerListener() {
                @Override
                public void clickImageIndex(int pos) {
                    framlayoutlisten.clickImageIndex(pos);
                }
            });
    }


    @Override
    public void selectImage(int index) {
        int count=ll.getChildCount();
        for (int i=0;i<count;i++){
            ImageView iv= (ImageView) ll.getChildAt(i);
            if (i==index){
                iv.setImageResource(R.drawable.guide_shape_retangle_select);
            }else {
                iv.setImageResource(R.drawable.guide_shape_retangle_unselect);
            }
        }
    }

    public FramLaoutListener getFramlayoutlisten() {
        return framlayoutlisten;
    }

    public void setFramlayoutlisten(FramLaoutListener framlayoutlisten) {
        this.framlayoutlisten = framlayoutlisten;
    }

    public interface FramLaoutListener{
        void clickImageIndex(int index);
    }
}
