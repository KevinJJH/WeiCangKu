package com.weicangku.com.weicang.Util.view.dialog3;

import android.content.Context;

/**
 * 提示对话框，有一个确认、一个返回按钮
 */
public class DialogTips3 extends DialogBase3 {
	/**
	 * 构造函数
	 * @param context
	 */

	public DialogTips3(Context context, String buttonText, String negativeText, String AnotherText, boolean isCancel) {
		super(context);
		super.setNamePositiveButton(buttonText);
		super.setNameNegativeButton(negativeText);
		super.setNameAnotherButton(AnotherText);
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

	/**
	 * 第三选项按钮，触发onAnotherListener的onClick
	 */
	@Override
	protected boolean OnClickAnotherButton() {
		if(onAnotherListener != null){
			onAnotherListener.onClick(this, 1);
		}
		return true;
	}
}
