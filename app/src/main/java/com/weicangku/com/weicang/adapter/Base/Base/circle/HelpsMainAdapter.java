package com.weicangku.com.weicang.adapter.Base.Base.circle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.weicangku.com.weicang.Activity.circle.LookImageActivity;
import com.weicangku.com.weicang.Activity.circle.LookImageViewPagerActivity;
import com.weicangku.com.weicang.Bean.User;
import com.weicangku.com.weicang.Bean.circle.Comment;
import com.weicangku.com.weicang.Bean.circle.Expression;
import com.weicangku.com.weicang.Bean.circle.Helps;
import com.weicangku.com.weicang.Bean.circle.PhontoFiles;
import com.weicangku.com.weicang.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2017/1/12.
 */

public class HelpsMainAdapter extends BaseAdapter{

    private final static String TAG = "HelpsMainAdapter";

    private Context mContext;
    private List<Helps> mData;
    private LayoutInflater inflater;

    private List<Comment> mItemComment;
    private CommentAdapter adapter;


//jjh


//   private OnButtonOnclickListener mlistener;

    private int count = 0;
//    public HelpsMainAdapter(Context context, List<Helps> list,OnButtonOnclickListener listener) {
//        this.mContext = context;
//        this.mData = list;
//        inflater = LayoutInflater.from(context);
////        this.mlistener=listener;
//
//    }
public HelpsMainAdapter(Context context, List<Helps> list) {
    this.mContext = context;
    this.mData = list;
    inflater = LayoutInflater.from(context);
//        this.mlistener=listener;

}



    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }




    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        mItemComment = new ArrayList<Comment>();


        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.lxw_item_helps, null);
            holder = new ViewHolder();
            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.lxw_id_item_helps_include_avertor);
            holder.userimg = (ImageView) holder.linearLayout.findViewById(R.id.lxw_id_item_helps_userimg);
            holder.username = (TextView) holder.linearLayout.findViewById(R.id.lxw_id_item_helps_username);
//            holder.personality = (TextView) holder.linearLayout.findViewById(R.id.lxw_id_item_helps_user_personality);
            holder.creatTime = (TextView) holder.linearLayout.findViewById(R.id.lxw_id_item_helps_create_time);
            holder.content = (TextView) convertView.findViewById(R.id.lxw_id_item_helps_content);

            holder.frameLayout = (FrameLayout) convertView.findViewById(R.id.lxw_id_item_helps_include_img);
            holder.gridView = (GridView) holder.frameLayout.findViewById(R.id.lxw_id_item_helps_gridview);

            holder.gridView2=(GridView)convertView.findViewById(R.id.lxw_id_item_helps_gridview2);

            holder.contentImg = (ImageView) holder.frameLayout.findViewById(R.id.lxw_id_item_helps_content_img);
            //jjh
//            holder.comment= (ImageView) convertView.findViewById(R.id.comment);
//            holder.listView= (ListView) convertView.findViewById(R.id.lxw_id_helps_comment_listview);


//pengjing

//            holder.relativeLayout = (RelativeLayout) holder.frameLayout.findViewById(R.id.lxw_id_item_helps_content_layout_video);
//            holder.contentVid = (ImageView) holder.relativeLayout.findViewById(R.id.lxw_id_item_helps_content_video);
//            holder.videoplayimg = (ImageButton)holder.relativeLayout.findViewById(R.id.video_play_btn_content);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.userimg.setImageResource(R.drawable.github);
        holder.contentImg.setImageResource(R.drawable.pictures_no);

        holder.contentImg.setVisibility(View.GONE);
//        holder.relativeLayout.setVisibility(View.GONE);
        holder.gridView.setVisibility(View.GONE);
        holder.gridView2.setVisibility(View.GONE);
        holder.contentImg.setFocusable(false);
        holder.gridView.setFocusable(false);
        holder.gridView2.setFocusable(false);

//jjh
//
        final ViewHolder finalHolder = holder;
//        holder.comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (mlistener!=null){
//                    mlistener.onButtonClick(finalHolder.comment,position);
//                }
//            }
//        });

//
//        if (mItemComment.size() == 0) {
//            BmobQuery<Comment> query = new BmobQuery<Comment>();
//            query.addWhereEqualTo("helps", new BmobPointer(mData.get(position)));
//            query.setLimit(50);
//            query.include("user,helps.user");
//
//
//            query.findObjects(mContext, new FindListener<Comment>() {
//                @Override
//                public void onSuccess(List<Comment> list) {
//                    if (list.size() > 0) {
//                        mItemComment.clear();
//                        mItemComment.addAll(list);
//                        adapter.notifyDataSetChanged();
//
//                    }
//                }
//                @Override
//                public void onError(int i, String s) {
//                }
//            });
//        }

//          adapter=new CommentAdapter(mContext,mItemComment);
//          holder.listView.setAdapter(adapter);


       Helps  helps = mData.get(position);
        Log.e(TAG, "===helps=====createAt=" + helps.getCreatedAt());

        User myUser = helps.getUser();

        if (myUser == null) {
            Log.e(TAG, "====myUser is null===");
        }

        if (myUser.getAvatar() != null) {
            String AvatarPath = myUser.getAvatar();
            //  Log.e(TAG,"===Avatar url===="+myUser.getAvatar().getUrl());

            // ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loaderImage(AvatarPath, holder.userimg, true);
            Glide.with(mContext).load(AvatarPath).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.userimg);
        }

        holder.username.setText(myUser.getNick());
//        if(myUser.getPersonality()!=null){
//            holder.personality.setText(myUser.getPersonality().toString().length()>16?myUser.getPersonality().substring(0, 22)+"...":myUser.getPersonality().toString());
//        }
        holder.creatTime.setText(getCreateTimes(helps.getCreatedAt()));

        SpannableString spannableString=getSpannableString(helps.getContent(),mContext);

        holder.content.setText(spannableString);


        PhontoFiles phontoFiles = helps.getPhontofile();
        if (phontoFiles != null) {
            setImages(helps, phontoFiles, holder.frameLayout, holder.gridView, holder.contentImg);
            setImages2(helps, phontoFiles,holder.gridView2, holder.contentImg);
        } else {
            Log.e(TAG, "===phontofiles is null");
        }
        //pengjing
//        VideoFiles videoFile = helps.getVideofile();
//        if(videoFile != null){
//            setVideo(videoFile,holder.frameLayout,holder.gridView,holder.videoplayimg,holder.contentVid,holder.relativeLayout);
//        }

        return convertView;
    }
//        public  interface OnButtonOnclickListener{
//            void onButtonClick(View view,int position);
//        }

    /**
     * 设置图片
     *
     * @param phontoFiles
     * @param frameLayout
     * @param gridView
     * @param contentImg
     */
    private void setImages(final Helps helps, PhontoFiles phontoFiles, FrameLayout frameLayout, GridView gridView, ImageView contentImg) {
        List<BmobFile> list = phontoFiles.getPhotos();
        String path = phontoFiles.getPhoto();
        if (list != null && list.size() > 1) {

            for (BmobFile file : list) {
                Log.e(TAG, "====list----bmobfile===" + file.getUrl());
            }

            frameLayout.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.VISIBLE);
            contentImg.setVisibility(View.GONE);


            gridView.setAdapter(new GridViewHelpsAdapter(mContext, list));

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mContext, LookImageViewPagerActivity.class);
                    intent.putExtra("phontoFiles", helps.getPhontofile());
                    intent.putExtra("position", position);
                    mContext.startActivity(intent);
                }
            });

        } else if (path != null) {
            Log.e(TAG, "===path url====" + path);
            frameLayout.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
            contentImg.setVisibility(View.VISIBLE);
            // ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loaderImage(path, contentImg, true);
            Glide.with(mContext).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).into(contentImg);
            contentImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, LookImageActivity.class);
                    intent.putExtra("helps", helps);
                    mContext.startActivity(intent);
                }
            });
        } else {
            frameLayout.setVisibility(View.GONE);
            gridView.setVisibility(View.GONE);
            contentImg.setVisibility(View.GONE);
        }
    }
    private void setImages2(final Helps helps, PhontoFiles phontoFiles,GridView gridView, ImageView contentImg) {
        List<BmobFile> list = phontoFiles.getPhotos();
        String path = phontoFiles.getPhoto();
        if (list != null && list.size() > 1) {

            for (BmobFile file : list) {
                Log.e(TAG, "====list----bmobfile===" + file.getUrl());
            }

//            frameLayout.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.VISIBLE);
//            contentImg.setVisibility(View.GONE);
            int size = list.size();
            int length = 100;
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
            float density = dm.density;
            int gridviewWidth = (int) (size * (length + 4) * density);
            int itemWidth = (int) (length * density);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
            gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
            gridView.setColumnWidth(itemWidth); // 设置列表项宽
//            gridView.setHorizontalSpacing(5); // 设置列表项水平间距
            gridView.setStretchMode(GridView.NO_STRETCH);
            gridView.setNumColumns(size); // 设置列数量=列表集合数
//
            gridView.setAdapter(new GridViewHelpsAdapter(mContext, list));


            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mContext, LookImageViewPagerActivity.class);
                    intent.putExtra("phontoFiles", helps.getPhontofile());
                    intent.putExtra("position", position);
                    mContext.startActivity(intent);
                }
            });

        } else if (path != null) {
            Log.e(TAG, "===path url====" + path);
//            frameLayout.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
            contentImg.setVisibility(View.VISIBLE);
            // ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loaderImage(path, contentImg, true);
            Glide.with(mContext).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).into(contentImg);
            contentImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, LookImageActivity.class);
                    intent.putExtra("helps", helps);
                    mContext.startActivity(intent);
                }
            });
        } else {
//            frameLayout.setVisibility(View.GONE);
            gridView.setVisibility(View.GONE);
            contentImg.setVisibility(View.GONE);
        }
    }

//

//    /**
//     * 设置视频
//     *
//     * @param videoFile
//     * @param frameLayout
//     * @param gridView
//     * @param videoplayimg
//     * @param contentVid
//     * @param relativeLayout
//     */
//    private void setVideo(VideoFiles videoFile, FrameLayout frameLayout, GridView gridView, ImageButton videoplayimg, ImageView contentVid,RelativeLayout relativeLayout) {
//        BmobFiles vfile = videoFile.getVideo();
//        if (vfile.getUrl() != null) {
//            Log.e(TAG, "===path url====" + vfile.getUrl());
//            frameLayout.setVisibility(View.VISIBLE);
//            gridView.setVisibility(View.GONE);
//            relativeLayout.setVisibility(View.VISIBLE);
//            //BmobProFile.getInstance(mContext).download(vfile.getFileUrl(),)
//            FileUtil linshide = new FileUtil();//默认在teachat
//            String savePath = linshide.getMyPath();
//            File hahaha = new File(savePath);//下载路径
//            Log.e("lujing",savePath);
//            if(!linshide.isFileExist(vfile.getFilename())){
//                vfile.download(mContext, hahaha, new DownloadFileListener() {
//
//                    @Override
//                    public void onStart() {
//                        Log.e("1", "开始下载...");
//                    }
//
//                    @Override
//                    public void onSuccess(String savePath) {
//                        Log.e("2", "下载成功,保存路径:" + savePath);
//                    }
//
//                    @Override
//                    public void onProgress(Integer value, long newworkSpeed) {
//                        //Log.i("bmob", "下载进度：" + value + "," + newworkSpeed);
//                    }
//
//                    @Override
//                    public void onFailure(int code, String msg) {
//                        Log.i("bmob", "下载失败：" + code + "," + msg);
//                    }
//                });
//            }
//            else{
//                Log.e("pengjing", "video has already exits!");
//            }
//            final String filecompletename = savePath  +  vfile.getFilename();
//            // ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loaderImage(path, contentImg, true);
//            MediaMetadataRetriever media = new MediaMetadataRetriever();
//            media.setDataSource(filecompletename);
//
//            Bitmap bitmap = media.getFrameAtTime();
//            contentVid.setImageBitmap(bitmap);
//            //Glide.with(mContext).load(filecompletename).diskCacheStrategy(DiskCacheStrategy.ALL).into(contentVid);
//            videoplayimg.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(mContext, PlayMP4.class);
//                    intent.putExtra("video_url", filecompletename);
//                    mContext.startActivity(intent);
//                }
//            });
//        } else {
//            frameLayout.setVisibility(View.GONE);
//            gridView.setVisibility(View.GONE);
//            contentVid.setVisibility(View.GONE);
//        }
//    }

    private class ViewHolder {
        LinearLayout linearLayout;
        ImageView userimg;
        TextView username;
        TextView personality;
        TextView creatTime;
        TextView content;
        FrameLayout frameLayout;
        GridView gridView;
        GridView gridView2;
        ImageView contentImg;
        //jjh
        ImageView comment;
        ListView listView;
        CommentAdapter adapter;
        //pengjing
        RelativeLayout  relativeLayout;
        ImageView contentVid;
        ImageButton videoplayimg;
    }

    private String getCreateTimes(String dates) {
        Date old = toDate(dates);
        Date nowtime = new Date(System.currentTimeMillis());
        long values = nowtime.getTime() - old.getTime();
        values=values/1000;
        Log.e(TAG, "====values  time===" + values);
        if (values < 60 && values > 0) {
            return  values + "秒前";
        }
        if (values > 60 && values < 60 * 60) {
            return values/60 + "分钟前";
        }
        if (values < 60 * 60 * 24 && values > 60 * 60) {
            return values/3600+"小时前";
        }
        if(values<60*60*24*2&&values>60*60*24){
            return "昨天";
        }
        if (values < 60 * 60 * 3 * 24 && values > 60 * 60 * 24*2) {
            return  "前天";
        }
        if (values < 60 * 60 * 24 * 30 && values > 60 * 60 * 24 * 3) {
            return  values / (60 * 60 * 24 ) + "天前";
        }
        if (values < 60 * 60 * 24 * 365 && values > 60 * 60 * 24 * 30) {
            return nowtime.getMonth()-old.getMonth() + "个月前";
        }
        return  values / (60 * 60 * 24 * 30 * 365) + "年前";
    }

    private Date toDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
        Date date1 = null;
        try {
            date1 = format.parse(date);
            //  Log.e(TAG,"===date==="+date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }

    /**
     * 替换表情
     * @param str
     * @param context
     * @return
     */
    private SpannableString getSpannableString(String str,Context context){
        SpannableString spannableString=new SpannableString(str);
        String s="\\[(.+?)\\]";
        Pattern pattern=Pattern.compile(s,Pattern.CASE_INSENSITIVE);
        Matcher matcher=pattern.matcher(spannableString);
        while(matcher.find()){
            String key=matcher.group();
            int id= Expression.getIdAsName(key);
            if(id!=0){
                Drawable drawable=context.getResources().getDrawable(id);
                drawable.setBounds(0,0,60,60);
                ImageSpan imageSpan=new ImageSpan(drawable);
                spannableString.setSpan(imageSpan,matcher.start(),matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableString;
    }
}