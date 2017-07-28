package com.weicangku.com.weicang.Util.view.dialog2;

import android.content.Context;

/**
 * 提示对话框，有一个确认、一个返回按钮
 */
public class DialogTips2 extends DialogBase2 {

	/**
	 * 构造函数
	 * @param context
	 */
	public DialogTips2(Context context, String buttonText, String negetiveText, boolean isCancel) {
		super(context);
		super.setNamePositiveButton(buttonText);
		super.setNameNegativeButton(negetiveText);
		super.setCancel(isCancel);
	}


	public int dip2px(Context context, float dipValue){
		float scale=context.getResources().getDisplayMetrics().density;
		return (int) (scale*dipValue+0.5f);
	}

	@Override
	protected void onDismiss() { }

	@Override
	protected boolean OnClickNegativeButton() {
		if(onCancelListener != null){
			onCancelListener.onClick(this, 1);
		}
		return true;
	}

	/**
	 * 确认按钮，触发onSuccessListener的onClick
	 */
	@Override
	protected boolean OnClickPositiveButton() {
		if(onSuccessListener != null){
			onSuccessListener.onClick(this, 1);
		}
		return true;
	}
}
