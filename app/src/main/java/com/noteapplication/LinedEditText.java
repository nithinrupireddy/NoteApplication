package com.noteapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;

public class LinedEditText extends AppCompatEditText {

    private Rect mRect;
    private Paint mPaint;

    public LinedEditText(@NonNull Context context, AttributeSet attrs) {
        super(context,attrs);
        mRect= new Rect();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        mPaint.setColor(0xFFFFD966);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int height = ((View)this.getParent()).getHeight();
        int lineHeight = getLineHeight();
        int noOfLines = height/lineHeight;

        Rect r = mRect;
        Paint paint = mPaint;

        int baseLineBounds = getLineBounds(0,r);
        for(int i=0;i<noOfLines;i++){
            canvas.drawLine(r.left,baseLineBounds+1,r.right,baseLineBounds+1,paint);
            baseLineBounds += lineHeight;
        }

        super.onDraw(canvas);
    }
}
