package com.weicangku.com.weicang.UtilView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.weicangku.com.weicang.R;

import java.util.List;

/**
 * Created by JJH on 2017/7/27.
 * 万能的listView适配器
 */

public abstract class CommonAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mDatas;
    protected LayoutInflater mInflate;
    public CommonAdapter(Context context, List<T> datas) {
        this.mContext = context;
        this.mDatas = datas;
        mInflate=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        CustomViewHolder holder=CustomViewHolder.get(mContext,convertView,parent, R.layout.lxw_item_helps_comment,position);
        convert(holder,getItem(position));
        return holder.getConverView();
    }
    public abstract void convert(CustomViewHolder holder,T t);
}
