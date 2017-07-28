package com.weicangku.com.weicang.adapter.Base.Base;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.weicangku.com.weicang.Bean.circle.Goods;
import com.weicangku.com.weicang.Bean.circle.PhontoFiles;
import com.weicangku.com.weicang.R;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2017/3/16.
 */

public class ActGoodsResultAdapter extends BaseAdapter {
    private  String path2;
    private Context mcontext;
    private List<Goods> goodsList;
    private LayoutInflater inflater;
    public static Set<String> mSelectImg=new HashSet<String>();

    public ActGoodsResultAdapter(List<Goods> goodsList, Context context) {
        this.mcontext=context;
        this.goodsList = goodsList;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return goodsList.size();
    }

    @Override
    public Object getItem(int position) {
        return goodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      final  ViewHolder  vh ;
        if (convertView==null){
            convertView = inflater.inflate(R.layout.act_goods_result_gv_item,null);
            vh = new ViewHolder();
            vh.imageView = (ImageView) convertView.findViewById(R.id.act_goods_result_gv_item_iv);
            vh.textView = (TextView) convertView.findViewById(R.id.act_goods_result_gv_item_tv_name);
            vh.mTvMoney = (TextView) convertView.findViewById(R.id.act_goods_result_gv_item_tv_money);

            vh.mSelect= (ImageButton) convertView.findViewById(R.id.id_lxw_item_chose_img_select);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder) convertView.getTag();
        }

        Goods goods = goodsList.get(position);
        vh.textView.setText(goods.getGoodsName());
        vh.mTvMoney.setText("¥ "+goods.getGoodsPrice());
        PhontoFiles phontoFiles = goodsList.get(position).getGood_photo_file();
//        String path=phontoFiles.getGood_pictures().get(0).getUrl();
        List<BmobFile> list = phontoFiles.getGood_pictures();
        final String path1=goods.getGood_photo_file().getGood_picture();
//       final String path2=list.get(0).getUrl();
        if (list != null && list.size() > 1) {
             path2=list.get(0).getUrl();
            Glide.with(mcontext).load(path2).diskCacheStrategy(DiskCacheStrategy.ALL).into(vh.imageView);
        }else if (path1!=null){
            Glide.with(mcontext).load(path1).diskCacheStrategy(DiskCacheStrategy.ALL).into(vh.imageView);
        }

        vh.imageView.setColorFilter(null);
        vh.mSelect.setImageResource(R.drawable.picture_unselected);
        vh.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectImg.contains(path1)){
                    vh.imageView.setColorFilter(null);
                    vh.mSelect.setImageResource(R.drawable.picture_unselected);
                    mSelectImg.remove(path1);
                }
//                else if (mSelectImg.contains(path2)){
//                    vh.imageView.setColorFilter(null);
//                    vh.mSelect.setImageResource(R.drawable.picture_unselected);
//                    mSelectImg.remove(path2);
//                }
                else{
                    if(mSelectImg.size()>8){
                        Toast.makeText(mcontext,"最多选择9张图片喔~", Toast.LENGTH_SHORT).show();
                    }else {
                        vh.imageView.setColorFilter(Color.parseColor("#77000000"));
                        vh.mSelect.setImageResource(R.drawable.pictures_selected);
                        mSelectImg.add(path1);
//                        mSelectImg.add(path2);
                    }
                }
            }
        });
        if(mSelectImg.contains(path1)){
            vh.imageView.setColorFilter(Color.parseColor("#77000000"));
            vh.mSelect.setImageResource(R.drawable.pictures_selected);
        }
// else if (mSelectImg.contains(path2)){
//            vh.imageView.setColorFilter(Color.parseColor("#77000000"));
//            vh.mSelect.setImageResource(R.drawable.pictures_selected);
//        }
        return convertView;
    }

    class ViewHolder{
        TextView textView,mTvMoney;
        ImageView imageView;
        ImageButton mSelect;
    }
}
