package com.weicangku.com.weicang.Util.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Administrator on 2017/3/12.
 */

public class CustomEditText extends EditText {

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onDraw(Canvas canvas){
        Paint paint = new Paint();
        paint.setTextSize(50);
        paint.setColor(Color.BLACK);
        canvas.drawText("库存：", 25, 100, paint);
        super.onDraw(canvas);
    }
}