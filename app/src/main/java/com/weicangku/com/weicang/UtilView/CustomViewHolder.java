package com.weicangku.com.weicang.UtilView;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.weicangku.com.weicang.Util.circle.ImagLoads;

/**
 * Created by Administrator on 2017/7/27.
 */

public class CustomViewHolder {
    private SparseArray<View> mViews;
    private int mPosition;
    private View mConvertView;

    public CustomViewHolder(Context context, ViewGroup parent,
                            int layoutId,int position){
        this.mPosition=position;
        this.mViews=new SparseArray<View>();
        mConvertView= LayoutInflater.from(context).inflate(layoutId,parent,false);
        mConvertView.setTag(this);
    }
    public static CustomViewHolder get(Context context,View convertView,
                                       ViewGroup parent,int layoutId,int position){
        if (convertView==null){
            return new CustomViewHolder(context,parent,layoutId,position);
        }else {
            CustomViewHolder holder = (CustomViewHolder) convertView.getTag();
            holder.mPosition=position;
            return holder;
        }

    }
    public View getConverView(){
        return mConvertView;
    }

    /**
     * 通过viewid获取控件
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View>T getView(int viewId){
        View view=mViews.get(viewId);
        if (view==null){
            view=mConvertView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return (T) view;
    }
    public CustomViewHolder setText(int viewId,String text){
        TextView tv= getView(viewId);
        tv.setText(text);
        return this;
    }
    public CustomViewHolder setImageResourse(int viewId,int resID){
        ImageView view=getView(viewId);
        view.setImageResource(resID);
        return this;
    }
    public CustomViewHolder setImageBitmap(int viewId,Bitmap bitmap){
        ImageView view=getView(viewId);
       view.setImageBitmap(bitmap);
        return this;
    }
    public CustomViewHolder setImageUrl(int viewId,String url){
        ImageView view=getView(viewId);
        ImagLoads.getmInstance().loaderImage(url,view,true);
        return this;
    }

}
