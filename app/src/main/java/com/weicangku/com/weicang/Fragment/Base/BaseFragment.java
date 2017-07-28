package com.weicangku.com.weicang.Fragment.Base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Config;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.orhanobut.logger.Logger;


public class BaseFragment extends Fragment {
    protected void runOnMain(Runnable runnable) {
        getActivity().runOnUiThread(runnable);
    }
    protected final static String NULL = "";
    private Toast toast;
    public void toast(final Object obj) {
        try {
            runOnMain(new Runnable() {

                @Override
                public void run() {
                    if (toast == null)
                        toast = Toast.makeText(getActivity(), NULL,Toast.LENGTH_SHORT);
                    toast.setText(obj.toString());
                    toast.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    Toast mToast;

    public void ShowToast(final String text) {
        if (mToast == null) {
            mToast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    public void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**启动指定Activity
     * @param target
     * @param bundle
     * @param b
     */
    public void startActivity(Class<? extends Activity> target, Bundle bundle, boolean b) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), target);
        if (bundle != null)
            intent.putExtra(getActivity().getPackageName(), bundle);
        getActivity().startActivity(intent);
    }

    /**Log日志
     * @param msg
     */
    public void log(String msg){
        if(Config.DEBUG){
            Logger.i(msg);
        }
    }

//    public static final String TAG = "tag";
////    BmobUserManager userManager=BmobUserManager.getInstance(getActivity());
//    // 公用的Header布局
//    private HeaderLayout mHeaderLayout;
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//    public BaseFragment() {
//
//    }
//    Toast mToast;
//
//    public void ShowToast(final String text) {
//        if (mToast == null) {
//            mToast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
//        } else {
//            mToast.setText(text);
//        }
//        mToast.show();
//    }
//    public View findViewById(int ParamInt) {
//        return getView().findViewById(ParamInt);
//    }
//
////    public CustomApplication mApplication;
//    /**
//     * 只有标题
//     */
//    public void initTopBarForOnlyTitle(String titleName) {
//        mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
//        mHeaderLayout.setStyle(HeaderLayout.HeaderStyle.DEFAULT_TITLE.DEFAULT_TITLE);
//        mHeaderLayout.setDefaultTitle(titleName);
//    }
//
//    /**
//     * 初始化标题栏-带左右按钮
//     */
//    public void initTopBarForBoth(String titleName, int rightDrawableId,
//                                  String text, HeaderLayout.onRightImageButtonClickListener listener) {
//        mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
//        mHeaderLayout.setStyle(HeaderLayout.HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
//        mHeaderLayout.setTitleAndLeftImageButton(titleName,
//                R.drawable.base_action_bar_back_bg_selector,
//                new OnLeftButtonClickListener());
//        mHeaderLayout.setTitleAndRightImageButton(titleName, rightDrawableId,
//                text, listener);
//    }
//
//    /**
//     * 只有左边按钮和Title
//     */
//    public void initTopBarForLeft(String titleName) {
//        mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
//        mHeaderLayout.setStyle(HeaderLayout.HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
//        mHeaderLayout.setTitleAndLeftImageButton(titleName,
//                R.drawable.base_action_bar_back_bg_selector,
//                new OnLeftButtonClickListener());
//    }
//
//    /**
//     * 右边+title
//     */
//    public void initTopBarForRight(String titleName, int rightDrawableId,
//                                   HeaderLayout.onRightImageButtonClickListener listener) {
//        mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
//        mHeaderLayout.setStyle(HeaderLayout.HeaderStyle.TITLE_RIGHT_IMAGEBUTTON);
//        mHeaderLayout.setTitleAndRightImageButton(titleName, rightDrawableId,
//                listener);
//    }
//    public void initTopBarForRight(String titleName, int rightDrawableId,String text,
//                                   HeaderLayout.onRightImageButtonClickListener listener) {
//        mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
//        mHeaderLayout.setStyle(HeaderLayout.HeaderStyle.TITLE_RIGHT_IMAGEBUTTON);
//        mHeaderLayout.setTitleAndRightImageButton(titleName, rightDrawableId,text,
//                listener);
//    }
//
//    /**
//     * 左边按钮的点击事件
//     */
//    public class OnLeftButtonClickListener implements
//            HeaderLayout.onLeftImageButtonClickListener {
//        public void onClick() {
//            getActivity().finish();
//        }
//    }
//
//    /**
//     * 动画启动页面 startAnimActivity
//     */
//    public void startAnimActivity(Intent intent) {
//        this.startActivity(intent);
//    }
//
//    public void startAnimActivity(Class<?> cla) {
//        getActivity().startActivity(new Intent(getActivity(), cla));
//    }

}