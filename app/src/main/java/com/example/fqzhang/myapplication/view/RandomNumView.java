package com.example.fqzhang.myapplication.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.fqzhang.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * TODO: document your custom view class.
 */
public class RandomNumView extends View {
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private Paint mTextPaint = new Paint();
    private float mTextWidth;
    private float mTextHeight;
    /***************************************/
    private int randomNumColor;
    private int backgroundColor;
    private float numTextSize;
    private String numText = "";
    private Rect textRect = new Rect();
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Message obtain = Message.obtain();
                obtain.what = 0;
                if (isStop) {
                    numText = randomNum();
                    postInvalidate();
                    this.sendMessageDelayed(obtain,200);
                }
            }
        }
    };
    public RandomNumView(Context context) {
        super(context);
        init(null, 0);
    }

    public RandomNumView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);

    }

    public RandomNumView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }
    private int flag = 0;
    private boolean isStop = false;
    private void init(AttributeSet attrs, int defStyle) {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                generateRandom();
            }
        });
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.RandomNumView, defStyle, 0);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.RandomNumView_RandomNumColor:
                    randomNumColor = a.getColor(attr, Color.RED);
                    break;
                case R.styleable.RandomNumView_BackgroundColor:
                    // 默认颜色设置为黑色
                    backgroundColor = a.getColor(attr, Color.YELLOW);
                    break;
                case R.styleable.RandomNumView_RandomNumSize:
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    numTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.RandomNumView_RandomNumText:
                    numText = a.getString(attr);
                    break;

            }
        }
        a.recycle();

        // Set up a default TextPaint object
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
//        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setTextSize(numTextSize);
        mTextPaint.setColor(randomNumColor);
        mTextPaint.getTextBounds(numText,0,numText.length(),textRect);
        // Update TextPaint and text measurements from attributes
        //invalidateTextPaintAndMeasurements();
    }

    private void generateRandom() {
        if (flag%2==0) {
            flag += 1;
            isStop = true;
            Message obtain = Message.obtain();
            obtain.what = 0;
            mHandler.sendMessage(obtain);
        } else {
            flag += 1;
            isStop =false;
        }

    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);
        mTextWidth = mTextPaint.measureText(mExampleString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int lWidth=0,lHeight = 0 ;
        switch (widthMode){
            case MeasureSpec.EXACTLY:
                lWidth = width;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                lWidth = textRect.width()+getPaddingLeft()+getPaddingRight();
                break;
        }
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                lHeight = height;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                lHeight = textRect.height() + getPaddingBottom() + getPaddingTop();
                break;
        }
        setMeasuredDimension(lWidth,lHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint rectPaint = new Paint();
        rectPaint.setColor(backgroundColor);
        rectPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),rectPaint);
        canvas.drawText(numText,(getMeasuredWidth()-textRect.width())/2,(getMeasuredHeight()+textRect.height())/2,mTextPaint);
        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
      /*  int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        // Draw the text.
        canvas.drawText(mExampleString,
                paddingLeft + (contentWidth - mTextWidth) / 2,
                paddingTop + (contentHeight + mTextHeight) / 2,
                mTextPaint);

        // Draw the example drawable on top of the text.
        if (mExampleDrawable != null) {
            mExampleDrawable.setBounds(paddingLeft, paddingTop,
                    paddingLeft + contentWidth, paddingTop + contentHeight);
            mExampleDrawable.draw(canvas);
        }*/
    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getExampleString() {
        return mExampleString;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setExampleString(String exampleString) {
        mExampleString = exampleString;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getExampleColor() {
        return mExampleColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getExampleDimension() {
        return mExampleDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }

    public String randomNum () {
        List<Integer> nums = new ArrayList<>();
        Random r = new Random();
        for (int i=0;i<4;i++) {
            nums.add(r.nextInt(10));
        }
        StringBuilder sb = new StringBuilder();
        for (Integer num:nums
             ) {
            sb.append(num);
        }
        return sb.toString();
    }
}
