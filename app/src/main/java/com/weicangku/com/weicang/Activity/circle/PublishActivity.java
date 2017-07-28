package com.weicangku.com.weicang.Activity.circle;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weicangku.com.weicang.Activity.Base.ParentWithNaviActivity;
import com.weicangku.com.weicang.Activity.Choose_goods_Activity;
import com.weicangku.com.weicang.Bean.User;
import com.weicangku.com.weicang.Bean.circle.Expression;
import com.weicangku.com.weicang.Bean.circle.Helps;
import com.weicangku.com.weicang.Bean.circle.PhontoFiles;
import com.weicangku.com.weicang.R;
import com.weicangku.com.weicang.Util.circle.ImagLoads;
import com.weicangku.com.weicang.adapter.Base.Base.circle.EmotionGridViewAdapter;
import com.weicangku.com.weicang.adapter.Base.Base.circle.EmotionPagerAdapter;
import com.weicangku.com.weicang.adapter.Base.Base.circle.ImageChoseAdapter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

public class PublishActivity extends ParentWithNaviActivity implements View.OnClickListener {

    private EditText ediContent;

    private String path;
    private HorizontalScrollView scrollPicContent;
    private HorizontalScrollView scrollVideoContent;
    private LinearLayout layPicContent;
    private LinearLayout layVideoContent;

    private LinearLayout btnCamera;
    private LinearLayout btnEmotion;

    private LinearLayout btnSend;
    private LinearLayout btnmore;
    private LinearLayout layout_add;
    private TextView Choose_good;

    private ViewPager emojPager;
    private boolean isOpen = false;

    private ArrayList<GridView> mGridViews;
    private static final int REQUEST_CODE = 1;

    private static final int CREQUEST_CODE = 2;



    private static final int TEXT_UPLOAD_TYPE = 1;

    private static final int PIC_UPLOAD_TYPE =2 ;





    private int uploadtype = TEXT_UPLOAD_TYPE;

    private ProgressDialog mProgressDialog;
    private List<String> filePhotos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        initNaviView();

    }

    @Override
    protected String title() {
        return "发表";
    }



    @Override
    public ToolBarListener setToolBarListener() {
        return new ToolBarListener() {
            @Override
            public void clickLeft() {
//                ImageChoseAdapter.mSelectImg.remove(path);
                finish();
                hideSoftInputView();
            }

            @Override
            public void clickRight() {

            }
        };
    }

        @Override
    protected void initView() {
        super.initView();
        ediContent = (EditText) findViewById(R.id.id_lxw_push_content);

        scrollPicContent = (HorizontalScrollView) findViewById(R.id.id_lxw_push_scrollPicContent);
//        scrollVideoContent = (HorizontalScrollView) findViewById(R.id.id_lxw_push_scrollVideoContent);
        layPicContent = (LinearLayout) findViewById(R.id.id_lxw_push_layPicContent);
//        layVideoContent = (LinearLayout) findViewById(R.id.id_lxw_push_layVideoContent);

        btnCamera = (LinearLayout) findViewById(R.id.id_lxw_push_btn_btnCamera);
        btnEmotion = (LinearLayout) findViewById(R.id.id_lxw_push_btn_btnEmotion);

        btnSend = (LinearLayout) findViewById(R.id.btnSend);
            btnmore= (LinearLayout) findViewById(R.id.id_lxw_push_btn_good);
            layout_add= (LinearLayout) findViewById(R.id.layout_add);
            Choose_good= (TextView) findViewById(R.id.tv_picture);

            emojPager = (ViewPager) findViewById(R.id.id_lxw_push_emoj_viewpager);
        btnCamera.setOnClickListener(this);
        btnEmotion.setOnClickListener(this);
        //pengjing
        emojPager.setOnClickListener(this);
        btnSend.setOnClickListener(this);
            btnmore.setOnClickListener(this);
            Choose_good.setOnClickListener(this);
        ediContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isOpen) {
                    openKeyBoard();
                    isOpen = false;
                    showEmotion(isOpen);
                }
                return false;
            }
        });

        initEmojGridview();
    }
    public void openKeyBoard() {

        InputMethodManager imm = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // 得到InputMethodManager的实例
        if (imm.isActive()) {
            // 如果开启
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);
            // 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
        }
    }
    private void showEmotion(boolean isOpen) {
        if (isOpen) {

//            openKeyBoard();
            emojPager.setVisibility(View.VISIBLE);

            initEmotionUp();
        } else {
            emojPager.setVisibility(View.GONE);
        }
    }
    private void initEmotionUp() {
        emojPager.setAdapter(new EmotionPagerAdapter(this, mGridViews));
        emojPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void initEmojGridview() {

        mGridViews = new ArrayList<GridView>();
        LayoutInflater inflater = LayoutInflater.from(this);
        mGridViews.clear();
        for (int i = 0; i < 6; i++) {
            final int j = i;
            GridView gridView = (GridView) inflater.inflate(R.layout.lxw_emoj_gridview, null, false);
            gridView.setAdapter(new EmotionGridViewAdapter(this, i));
            mGridViews.add(gridView);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0 && (position % 20 == 0) || (j == 5 && position == 5)) {
                        int selectionStart = ediContent.getSelectionStart();
                        String str = ediContent.getText().toString();
                        String strTemp = str.substring(0, selectionStart);
                        if (!TextUtils.isEmpty(str)) {
                            int i = strTemp.lastIndexOf("]");
                            if (i == strTemp.length() - 1) {
                                int j = strTemp.lastIndexOf("[");
                                ediContent.getEditableText().delete(j, selectionStart);
                            } else {
                                ediContent.getEditableText().delete(strTemp.length() - 1, selectionStart);
                            }
                        }
                    } else {
                        String str = Expression.emojName[position + j * 20];
                        SpannableString spannableString = new SpannableString(str);
                        Drawable drawable = PublishActivity.this.getResources().getDrawable(Expression.getIdAsName(str));
                        drawable.setBounds(0, 0,60,60);
                        ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
                        spannableString.setSpan(imageSpan, 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        int cuors = ediContent.getSelectionStart();
                        ediContent.getText().insert(cuors, spannableString);
                    }
                }
            });
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_lxw_push_btn_btnEmotion:
                if (isOpen) {
                    isOpen = false;
                } else {
                    isOpen = true;
                }
                showEmotion(isOpen);
                layout_add.setVisibility(View.GONE);
                hideSoftInputView();
                break;
            case R.id.id_lxw_push_btn_btnCamera:
                Intent intent = new Intent(this, ChoseImgActivity.class);
                startActivity(intent);
                break;
            case R.id.btnSend:
                openKeyBoard();
                pushHelp();

                break;
            case R.id.id_lxw_push_btn_good:
                if (layout_add.getVisibility()==View.GONE){
                layout_add.setVisibility(View.VISIBLE);
                    emojPager.setVisibility(View.GONE);
                hideSoftInputView();
                }else {
                        layout_add.setVisibility(View.GONE);
                }
                break;
            case R.id.tv_picture:
                startActivity(Choose_goods_Activity.class,null,false);
                layout_add.setVisibility(View.GONE);
                break;

        }
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_CODE && resultCode == CREQUEST_CODE) {
//            uploadtype = PIC_UPLOAD_TYPE;
//            Bundle datas = data.getExtras();
//
//            refresUI();
//
//        }
//
//    }
    private void refresUI() {
        Set<String> Imgs = ImageChoseAdapter.mSelectImg;
        if (Imgs.size() == 0) {
            scrollPicContent.setVisibility(View.GONE);
            return;
        }
        if (Imgs.size() > 0) {
            if (layPicContent != null) {
                layPicContent.removeAllViews();
                scrollPicContent.setVisibility(View.VISIBLE);

//
            }
            for (String path : Imgs) {
                View itemView = LayoutInflater.from(PublishActivity.this).inflate(R.layout.lxw_item_publish_pic, null);
                ImageView img = (ImageView) itemView.findViewById(R.id.id_lxw_publish_pic_img);

                itemView.setTag(path);
                itemView.setOnClickListener(onPicTureClickListener);


                ImagLoads.getInstance(2, ImagLoads.Type.LIFO).loaderImage(path,img,false);


                if (layPicContent != null) {
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layPicContent.addView(itemView, lp);
                }
            }
        } else {
            if (scrollPicContent != null) {
                scrollPicContent.setVisibility(View.GONE);
            }
        }

    }
    private View.OnClickListener onPicTureClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {


            AlertDialog.Builder builder = new AlertDialog.Builder(PublishActivity.this);
            AlertDialog alert = builder.setMessage("确认删除图片？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            path = v.getTag().toString();
//                            ImageChoseAdapter.mSelectImg.remove(path);
                            for (int i = 0; i < layPicContent.getChildCount(); i++) {
                                View view = layPicContent.getChildAt(i);
                                if (view.getTag().toString().equals(path)) {
                                    layPicContent.removeView(view);
                                    ImageChoseAdapter.mSelectImg.remove(path);
                                    break;
                                }
                            }
                            if (ImageChoseAdapter.mSelectImg == null || ImageChoseAdapter.mSelectImg.size() == 0) {
                                scrollPicContent.setVisibility(View.GONE);

                            }
                        }
                    }).create();
            alert.show();
        }
    };

    @Override
    protected void onRestart() {
        super.onRestart();
        refresUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresUI();
    }
    private void pushHelp() {

        final String content = ediContent.getText().toString().trim();
//          final List<String> list = new ArrayList<String>(ImageChoseAdapter.mSelectImg);
//        filePhotos = new ArrayList<String>();
//        for (String path : list) {
//            filePhotos.add(compressBitmap(PublishActivity.this, path));
//        }


        mProgressDialog = ProgressDialog.show(this, null, "正在上传");
        new Thread(){
            @Override
            public void run() {
                super.run();
                //图片+文字这样可以上传
                List<String> list = new ArrayList<String>(ImageChoseAdapter.mSelectImg);
                if (list != null && list.size() > 0) {
                    getCacheImgFiles(PublishActivity.this, list);
                }

                if (filePhotos!=null&&content!=null) {
                    uploaderpic(filePhotos, content);
                }else if (filePhotos!=null&&content.equals("")){
                    uploaderpic(filePhotos,content);
                }else if (filePhotos==null&&content.equals("")){
                    toast("您还没输入任何内容呢");
                    mProgressDialog.dismiss();
                }else {
                    saveText(content);
                }



            }
        }.start();

//




    }
    private void getCacheImgFiles(Context context, List<String> list) {
        filePhotos = new ArrayList<String>();
        for (String path : list) {
            filePhotos.add(compressBitmap(context, path));
        }
    }
    private String compressBitmap(Context context, String path) {
        Bitmap bitmap = compressBitmapFromFile(path);
        File srcFile = new File(path);
        String desPath = getImageCacheDir(context) + srcFile.getName();
        File file = new File(desPath);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return desPath;
    }
    private String getImageCacheDir(Context context) {
        String cachepath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            cachepath = context.getExternalCacheDir().getPath();
        } else {
            cachepath = context.getCacheDir().getPath();
        }
        return cachepath;
    }
    private Bitmap compressBitmapFromFile(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        options.inJustDecodeBounds = false;
        int width = options.outWidth;
        int height = options.outHeight;

        float widthRadio = 480f;
        float heightRadio = 800f;
        int inSampleSize = 1;
        if (width > height && width > widthRadio) {
            inSampleSize = (int) (width * 1.0f / widthRadio);
        } else if (width < height && height > heightRadio) {
            inSampleSize = (int) (height * 1.0f / heightRadio);
        }
        if (inSampleSize <= 0) {
            inSampleSize = 1;
        }
        options.inSampleSize = inSampleSize;
        bitmap = BitmapFactory.decodeFile(path, options);
        return compressBitmap(bitmap);
    }
    private Bitmap compressBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        while (baos.toByteArray().length / 1024 > 200) {
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 10;
        }
        ByteArrayInputStream byins = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bm = BitmapFactory.decodeStream(byins, null, null);
        return bm;
    }
    /**
     * 上传图片
     *
     * @param list
     * @return
     */

    private void uploaderpic(List<String> list, final String content) {
        final PhontoFiles phontoFiles = new PhontoFiles();
        final PhontoFiles ps = new PhontoFiles();
        if (list.size() == 1) {
            File file = new File(list.get(0));
            final BmobFile bmobFile = new BmobFile(file);
            bmobFile.uploadblock(this, new UploadFileListener() {
                @Override
                public void onSuccess() {
                    phontoFiles.setPhoto(bmobFile.getFileUrl(PublishActivity.this));
                    phontoFiles.save(PublishActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            //  phontoFiles.setObjectId(phontoFiles.getObjectId());
                            ps.setObjectId(phontoFiles.getObjectId());


                            savePulishPic(content, phontoFiles);
                            toast("上传成功");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            toast("上传失败");
                        }
                    });
                }

                @Override
                public void onFailure(int i, String s) {

                }
            });
        } else {
            final String[] filePaths = list.toArray(new String[list.size()]);
            final int fileLength = filePaths.length;
            BmobFile.uploadBatch(this, filePaths, new UploadBatchListener() {
                @Override
                public void onSuccess(List<BmobFile> list, List<String> list1) {
                    if(list1.size()==fileLength){
                        phontoFiles.setPhotos(list);
                        phontoFiles.save(PublishActivity.this, new SaveListener() {
                            @Override
                            public void onSuccess() {

                                //  idAsClass(phontoFiles.getObjectId());
                                // phontoFiles.setObjectId(phontoFiles.getObjectId());
                                ps.setObjectId(phontoFiles.getObjectId());
                                savePulishPic(content,phontoFiles);
                                toast("上传成功");
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                toast("上传失败");
                            }
                        });
                    }
                }

                @Override
                public void onProgress(int i, int i1, int i2, int i3) {

                }

                @Override
                public void onError(int i, String s) {

                }
            });

        }



    }
    private void savePulishPic(String content, PhontoFiles files) {
        User user = BmobUser.getCurrentUser(this, User.class);
        Helps helps = new Helps();
        helps.setUser(user);
        helps.setContent(content);
        helps.setState(0);
        helps.setPhontofile(files);
        helps.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                finish();
                hideSoftInputView();
                mProgressDialog.dismiss();
                ImageChoseAdapter.mSelectImg.removeAll(ImageChoseAdapter.mSelectImg);
                toast("上传成功");

            }

            @Override
            public void onFailure(int i, String s) {
                mProgressDialog.dismiss();
                toast("上传失败");
            }
        });
    }
    private void saveText(String content) {
        User user = BmobUser.getCurrentUser(this, User.class);
        Helps helps = new Helps();
        helps.setUser(user);
        helps.setContent(content);
        helps.setState(0);
        helps.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                finish();
                mProgressDialog.dismiss();
                toast("上传成功");
            }

            @Override
            public void onFailure(int i, String s) {
                mProgressDialog.dismiss();
                toast("上传失败");
            }
        });
    }
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x110) {
//                  mProgressDialog.dismiss();
            }
        }
    };
}
