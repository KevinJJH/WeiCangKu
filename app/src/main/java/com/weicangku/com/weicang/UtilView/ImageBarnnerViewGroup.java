package com.weicangku.com.weicang.UtilView;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/7/28.
 * 自定义的轮播图效果
 */

public class ImageBarnnerViewGroup extends ViewGroup {
    private int children;
    private int childWidth;
    private int childHeight;

    private int x;//代表第一次按下的位置横坐标
    private int index=0;//代表我们每张图片的索引

    private Timer timer=new Timer();
    private TimerTask timerTask;
    private boolean isAuto=true;

    public ImageBarnnerListener getListener() {
        return listener;
    }

    public void setListener(ImageBarnnerListener listener) {
        this.listener = listener;
    }

    public void setImgaeBarnnerViewGroupListener(ImgaeBarnnerViewGroupListener imgaeBarnnerViewGroupListener) {
        this.imgaeBarnnerViewGroupListener = imgaeBarnnerViewGroupListener;
    }

    public interface ImageBarnnerListener{
        void clickImageIndex(int pos);
    }
    private ImgaeBarnnerViewGroupListener imgaeBarnnerViewGroupListener;
    private ImageBarnnerListener listener;

    private boolean isClicked;//true时候是点击事件，false的时候代表不是点击事件
    private Handler autoHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
           switch (msg.what){
               case 0:
                   if (++index>=children){//说明此时如果是最后一张图，将会从第一张图片开始重新滑动
                       index=0;
                   }
                   scrollTo(childWidth * index,0);
                   imgaeBarnnerViewGroupListener.selectImage(index);
                   break;
           }
        }
    };
    private void startAuto(){
        isAuto=true;
    }
    private void stopAuto(){
        isAuto=false;
    }

    private void initObj(){
        timerTask=new TimerTask() {
            @Override
            public void run() {
            if (isAuto){
                autoHandler.sendEmptyMessage(0);
            }
            }
        };
        timer.schedule(timerTask,100,3000);
    }

    public ImageBarnnerViewGroup(Context context) {
        super(context);
        initObj();
    }

    public ImageBarnnerViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initObj();
    }

    public ImageBarnnerViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initObj();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int lefMargin = 0;
            for (int i = 0; i < children; i++) {
                View view = getChildAt(i);
                view.layout(lefMargin, 0, lefMargin+childWidth, childHeight);
                lefMargin +=childWidth;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        children = getChildCount();        //求出子视图的个数
        if (children == 0) {
            setMeasuredDimension(0, 0);
        } else {
            measureChildren(widthMeasureSpec, heightMeasureSpec);//测量子视图的高度和宽度
            View view = getChildAt(0);
            childHeight = view.getMeasuredHeight();
            childWidth = view.getMeasuredWidth();
            int width = view.getMeasuredWidth() * children;
            setMeasuredDimension(width, childHeight);
        }
    }

    /**
     * 事件的传递
     * @param ev
     * @return true即处理，false则不处理，继续传递。
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN://用户按下
                stopAuto();
                isClicked=true;
                x= (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE://用户按下之后移动
                int moveX= (int) event.getX();
                int distance=moveX-x;
                scrollBy(-distance,0);
                x=moveX;
                isClicked=false;
                break;
            case MotionEvent.ACTION_UP://
                int scrollx=getScrollX();
                index=(scrollx + childWidth / 2)/childWidth;
                if (index<0){
                    index=0;
                }else if (index>children-1){
                    index=children-1;
                }
                if (isClicked==true){
                listener.clickImageIndex(index);
                }else {
                    scrollTo(index * childWidth,0);
                }
                startAuto();
                imgaeBarnnerViewGroupListener.selectImage(index);

                break;
        }
        return true;//告诉我们该ViewGroup容易的父View已经处理好该View的事件。
    }
    public interface ImgaeBarnnerViewGroupListener{
        void selectImage(int index);
    }
}
