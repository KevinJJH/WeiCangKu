package com.weicangku.com.weicang.Util.view.dialog1;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;

import com.weicangku.com.weicang.R;


/**
 *自定义对话框基类
 *支持：对话框全屏显示控制、title显示控制，一个button或两个
 */
public abstract class DialogBase1 extends Dialog {
	protected OnClickListener onSuccessListener;
	protected Context mainContext;
	protected OnDismissListener onDismissListener;

	protected View view;
	protected Button Button1;
	private boolean isFullScreen = false;

	private boolean hasTitle = true;//是否有title

	private int width = 0, height = 0, x = 0, y = 0;
	private int iconTitle = 0;
	private String message, title;
	private String nameButton1;
	private final int MATCH_PARENT = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

	private boolean isCancel = true;//默认是否可点击back按键/点击外部区域取消对话框


	public boolean isCancel() {
		return isCancel;
	}

	public void setCancel(boolean isCancel) {
		this.isCancel = isCancel;
	}

	/**
	 * 构造函数
	 * @param context 对象应该是Activity
	 */
	public DialogBase1(Context context) {
		super(context, R.style.alert);
		this.mainContext = context;
	}

	/**
	 * 创建事件
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog1);

		// 设置按钮事件监听
		Button1 = (Button)findViewById(R.id.dialog_button1);
		if(nameButton1.length()>0){
			Button1.setText(nameButton1);
			Button1.setOnClickListener(GetPositiveButtonOnClickListener());
		}

		// 设置对话框的位置和大小
		LayoutParams params = this.getWindow().getAttributes();
		if(this.getWidth()>0)
			params.width = this.getWidth();
		if(this.getHeight()>0)
			params.height = this.getHeight();
		if(this.getX()>0)
			params.width = this.getX();
		if(this.getY()>0)
			params.height = this.getY();

		// 如果设置为全屏
		if(isFullScreen) {
			params.width = WindowManager.LayoutParams.MATCH_PARENT;
			params.height = WindowManager.LayoutParams.MATCH_PARENT;
		}

		//设置点击dialog外部区域可取消
		if(isCancel){
			setCanceledOnTouchOutside(true);
			setCancelable(true);
		}else{
			setCanceledOnTouchOutside(false);
			setCancelable(false);
		}
		getWindow().setAttributes(params);
		this.setOnDismissListener(GetOnDismissListener());
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	}

	/**
	 * 获取OnDismiss事件监听，释放资源
	 * @return OnDismiss事件监听
	 */
	protected OnDismissListener GetOnDismissListener() {
		return new OnDismissListener(){
			public void onDismiss(DialogInterface arg0) {
				DialogBase1.this.onDismiss();
				DialogBase1.this.setOnDismissListener(null);
				view = null;
				mainContext = null;
				Button1 = null;
				if(onDismissListener != null){
					onDismissListener.onDismiss(null);
				}
			}
		};
	}

	/**
	 * 获取确认按钮单击事件监听
	 * @return 确认按钮单击事件监听
	 */
	protected View.OnClickListener GetPositiveButtonOnClickListener() {
		return new View.OnClickListener() {
			public void onClick(View v) {
				if(OnClickPositiveButton())
					DialogBase1.this.dismiss();
			}
		};
	}


	/**
	 * 获取焦点改变事件监听，设置EditText文本默认全选
	 * @return 焦点改变事件监听
	 */
	protected OnFocusChangeListener GetOnFocusChangeListener() {
		return new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus && v instanceof EditText) {
					((EditText) v).setSelection(0, ((EditText) v).getText().length());
				}
			}
		};
	}

	/**
	 * 设置成功事件监听，用于提供给调用者的回调函数
	 * @param listener 成功事件监听
	 */
	public void SetOnSuccessListener(OnClickListener listener){
		onSuccessListener = listener;
	}

	/**
	 * 设置关闭事件监听，用于提供给调用者的回调函数
	 * @param listener 关闭事件监听
	 */
	public void SetOnDismissListener(OnDismissListener listener){
		onDismissListener = listener;
	}


	/**
	 * 确认按钮单击方法，用于子类定制
	 */
	protected abstract boolean OnClickPositiveButton();

	/**
	 * 关闭方法，用于子类定制
	 */
	protected abstract void onDismiss();

	/**
	 * @return 对话框标题
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title 对话框标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param iconTitle 标题图标的资源Id
	 */
	public void setIconTitle(int iconTitle) {
		this.iconTitle = iconTitle;
	}

	/**
	 * @return 标题图标的资源Id
	 */
	public int getIconTitle() {
		return iconTitle;
	}

	/**
	 * @return 对话框提示信息
	 */
	protected String getMessage() {
		return message;
	}

	/**
	 * @param message 对话框提示信息
	 */
	protected void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return 对话框View
	 */
	protected View getView() {
		return view;
	}

	/**
	 * @param view 对话框View
	 */
	protected void setView(View view) {
		this.view = view;
	}

	/**
	 * @return 是否全屏
	 */
	public boolean getIsFullScreen() {
		return isFullScreen;
	}

	/**
	 * @param isFullScreen 是否全屏
	 */
	public void setIsFullScreen(boolean isFullScreen) {
		this.isFullScreen = isFullScreen;
	}

	public boolean isHasTitle() {
		return hasTitle;
	}


	public void setHasTitle(boolean hasTitle) {
		this.hasTitle = hasTitle;
	}


	/**
	 * @return 对话框宽度
	 */
	protected int getWidth() {
		return width;
	}

	/**
	 * @param width 对话框宽度
	 */
	protected void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return 对话框高度
	 */
	protected int getHeight() {
		return height;
	}

	/**
	 * @param height 对话框高度
	 */
	protected void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return 对话框X坐标
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x 对话框X坐标
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return 对话框Y坐标
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y 对话框Y坐标
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return 确认按钮名称
	 */
	protected String getNamePositiveButton() {
		return nameButton1;
	}

	/**
	 * @param namePositiveButton 确认按钮名称
	 */
	protected void setNamePositiveButton(String namePositiveButton) {
		this.nameButton1 = namePositiveButton;
	}

}