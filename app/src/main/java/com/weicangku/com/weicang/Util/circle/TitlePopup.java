package com.weicangku.com.weicang.Util.circle;

/**
 * Created by Administrator on 2017/1/13.
 */

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.weicangku.com.weicang.R;

import java.util.ArrayList;

/**
 * @author yangyu 功能描述：标题按钮上的弹窗（继承自PopupWindow）
 */
public class TitlePopup extends PopupWindow {

    private TextView priase;
    private TextView comment;

    private Context mContext;

    // 列表弹窗的间隔
    protected final int LIST_PADDING = 10;

    // 实例化一个矩形
    private Rect mRect = new Rect();

    // 坐标的位置（x、y）
    private final int[] mLocation = new int[2];

    // 屏幕的宽度和高度
    private int mScreenWidth, mScreenHeight;

    // 判断是否需要添加或更新列表子类项
    private boolean mIsDirty;

    // 位置不在中心
    private int popupGravity = Gravity.NO_GRAVITY;

    // 弹窗子类项选中时的监听
    private OnItemOnClickListener mItemOnClickListener;

    // 定义弹窗子类项列表
    private ArrayList<ActionItem> mActionItems = new ArrayList<ActionItem>();

    public TitlePopup(Context context) {
        // 设置布局的参数
        this(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public TitlePopup(Context context, int width, int height) {
        this.mContext = context;

        // 设置可以获得焦点
        setFocusable(true);
        // 设置弹窗内可点击
        setTouchable(true);
        // 设置弹窗外可点击
        setOutsideTouchable(true);

        // 获得屏幕的宽度和高度
//		mScreenWidth = Util.getScreenWidth(mContext);
//		mScreenHeight = Util.getScreenHeight(mContext);

        // 设置弹窗的宽度和高度
        setWidth(width);
        setHeight(height);

        setBackgroundDrawable(new BitmapDrawable());

        // 设置弹窗的布局界面
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.comment_popu, null);
        setContentView(view);
        Log.e("",
                "3333==========" + view.getHeight() + "    " + view.getWidth());
        priase = (TextView) view.findViewById(R.id.popu_praise);
        comment = (TextView) view.findViewById(R.id.popu_comment);
        priase.setOnClickListener(onclick);
        comment.setOnClickListener(onclick);
    }

    /**
     * 显示弹窗列表界面
     */
    public void show(final View c) {
        // 获得点击屏幕的位置坐标
        c.getLocationOnScreen(mLocation);
        // 设置矩形的大小
        mRect.set(mLocation[0], mLocation[1], mLocation[0] + c.getWidth(),
                mLocation[1] + c.getHeight());
        priase.setText(mActionItems.get(0).mTitle);
        // 判断是否需要添加或更新列表子类项
        if (mIsDirty) {
            // populateActions();
        }
        Log.e("", "333  " + this.getHeight());// 50
        Log.e("", "333  " + c.getHeight());// 96
        Log.e("", "333  " + this.getWidth());

        Log.e("", "333  " + (mLocation[1]));

        // 显示弹窗的位置
        // showAtLocation(view, popupGravity, mScreenWidth - LIST_PADDING
        // - (getWidth() / 2), mRect.bottom);
        showAtLocation(c, Gravity.NO_GRAVITY, mLocation[0] - this.getWidth()
                - 10, mLocation[1] - ((this.getHeight() - c.getHeight()) / 2));
    }

    View.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            switch (v.getId()) {
                case R.id.popu_comment:
                    mItemOnClickListener.onItemClick(mActionItems.get(1), 1);
                    break;
                case R.id.popu_praise:
                    mItemOnClickListener.onItemClick(mActionItems.get(0), 0);
                    break;
            }
        }

    };

    /**
     * 添加子类项
     */
    public void addAction(ActionItem action) {
        if (action != null) {
            mActionItems.add(action);
            mIsDirty = true;
        }
    }

    /**
     * 清除子类项
     */
    public void cleanAction() {
        if (mActionItems.isEmpty()) {
            mActionItems.clear();
            mIsDirty = true;
        }
    }

    /**
     * 根据位置得到子类项
     */
    public ActionItem getAction(int position) {
        if (position < 0 || position > mActionItems.size())
            return null;
        return mActionItems.get(position);
    }

    /**
     * 设置监听事件
     */
    public void setItemOnClickListener(
            OnItemOnClickListener onItemOnClickListener) {
        this.mItemOnClickListener = onItemOnClickListener;
    }

    /**
     * @author yangyu 功能描述：弹窗子类项按钮监听事件
     */
    public static interface OnItemOnClickListener {
        public void onItemClick(ActionItem item, int position);
    }
}
