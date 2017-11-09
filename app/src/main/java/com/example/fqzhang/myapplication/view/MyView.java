package com.example.fqzhang.myapplication.view;

import android.app.Application;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.fqzhang.myapplication.R;

/**
 * TODO: document your custom view class.
 */
public class MyView extends View {
    private String mText ;
    private float textSize;
    private final int[] mAttrs= {R.attr.mTextSize,R.attr.mText};
    private  int ATTR_TEXT = 1;
    private  int ATTR_TEXTSIZE = 0;
    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;
    private Rect rect;
    public MyView(Context context) {
        super(context);
        init(null, 0);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.MyView);

        textSize = a.getDimension(R.styleable.MyView_mTextSize,0);

        textSize = a.getDimensionPixelSize(R.styleable.MyView_mTextSize, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
        mText = a.getString(R.styleable.MyView_mText);


        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(textSize);
        float width = mTextPaint.measureText(mText);
        rect = new Rect();
        mTextPaint.getTextBounds(mText,0,mText.length(),rect);
        mTextWidth = width;
        mTextHeight = rect.height();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int lastWidth = 0,lastHeight = 0;
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY){
            lastWidth = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            lastWidth = (int) (mTextWidth + getPaddingLeft() +getPaddingRight());
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            lastHeight = heightSize;
        } else {
            lastHeight = (int) (mTextHeight +getPaddingBottom() +getPaddingTop());
        }
        setMeasuredDimension(lastWidth,lastHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        // Draw the text.
        canvas.drawText(mText,
                paddingLeft + (contentWidth - mTextWidth) / 2,
                paddingTop + (contentHeight + mTextHeight) / 2,
                mTextPaint);

    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getText() {
        return mText;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setText(String exampleString) {
        mText = exampleString;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getTextDimension() {
        return textSize;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setTextDimension(float exampleDimension) {
        textSize = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }
}
