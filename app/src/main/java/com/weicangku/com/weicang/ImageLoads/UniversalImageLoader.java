package com.weicangku.com.weicang.ImageLoads;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.weicangku.com.weicang.R;

import java.io.File;

/**
 * Created by Administrator on 2016/12/16.
 */

public class UniversalImageLoader implements  ILoader {
    public UniversalImageLoader(){}
/**
 * 圆角头像
 * */
    @Override
    public void loadAvator(ImageView iv, String url, int defaultRes) {
        if(!TextUtils.isEmpty(url)){
            display(iv,url,true,defaultRes,null);
        } else {
            iv.setImageResource(defaultRes);
        }
    }
    /**
     * 正方形头像
     * */
    public void loadAvator2(ImageView iv, String url, int defaultRes) {
        if(!TextUtils.isEmpty(url)){
            display(iv,url,false,defaultRes,null);
        } else {
            iv.setImageResource(defaultRes);
        }
    }

    @Override
    public void load(ImageView iv, String url, int defaultRes,ImageLoadingListener listener) {
        if(!TextUtils.isEmpty(url)){
            display(iv,url.trim(),true,defaultRes,listener);
        } else {
            iv.setImageResource(defaultRes);
        }
    }


    /**
     * 展示图片
     * @param iv
     * @param url
     * @param defaultRes
     * @param listener
     */
    private void display(ImageView iv,String url,boolean isCircle,int defaultRes,ImageLoadingListener listener){
        if(!url.equals(iv.getTag())){//增加tag标记，减少UIL的display次数
            iv.setTag(url);
            //不直接display imageview改为ImageAware，解决ListView滚动时重复加载图片
            ImageAware imageAware = new ImageViewAware(iv, false);
            ImageLoader.getInstance().displayImage(url, imageAware, DisplayConfig.getDefaultOptions(isCircle,defaultRes),listener);
        }
    }

    /**
     * 初始化ImageLoader
     * @param context
     */
    public static void initImageLoader(Context context, String cacheDisc) {
        // 获取本地缓存的目录，该目录在SDCard的根目录下
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, cacheDisc);
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPoolSize(3);
        config.discCache(new UnlimitedDiskCache(cacheDir));
        config.memoryCacheExtraOptions(480, 800);
        config.memoryCache(new WeakMemoryCache());
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
//        config.writeDebugLogs(); // Remove for release app
        // Initialize ImageLoader with configuration.
        config.defaultDisplayImageOptions(DisplayConfig.getDefaultOptions(true, R.drawable.no_image));
        ImageLoader.getInstance().init(config.build());
    }

}
