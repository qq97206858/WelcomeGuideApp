package com.example.fqzhang.myapplication.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fqzhang.myapplication.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * TODO: document your custom view class.
 */
public class ChangeNum extends LinearLayout {
    View view = null;
    ViewGroup vg = (ViewGroup) view;
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;
    private Drawable leftIcon;
    private Drawable rightIcon;
    private float numTextSize;
    private int currentNum;
    private TextView currentNumTextView;
    public ChangeNum(Context context) {
        super(context);
        init(null, 0);
    }

    public ChangeNum(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ChangeNum(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ChangeNum, defStyle, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.ChangeNum_currentNum:
                    try {
                        currentNum = Integer.parseInt(a.getString(attr));
                    }catch (Exception e) {
                        e.printStackTrace();
                        currentNum = 0;
                    }
                    break;
                case R.styleable.ChangeNum_NumDimension:
                    numTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.ChangeNum_leftIcon:
                    leftIcon = a.getDrawable(attr);
                    break;
                case R.styleable.ChangeNum_rightIcon:
                    rightIcon =a.getDrawable(attr);
                    break;

            }
        }
        a.recycle();

        View.inflate(getContext(),R.layout.changenum_layout,this);

        createView();
    }

    private void createView() {
        View view = View.inflate(getContext(), R.layout.changenum_layout, this);
        currentNumTextView = (TextView) view.findViewById(R.id.current_num);
        final ImageView leftbtn = (ImageView) view.findViewById(R.id.left_btn);
        final ImageView rightbtn = (ImageView) view.findViewById(R.id.right_btn);
        leftbtn.setImageDrawable(leftIcon);
        rightbtn.setImageDrawable(rightIcon);
        currentNumTextView.setText(currentNum+"");
        leftbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickCallBack != null) {
                    int num = onClickCallBack.leftBtnClick(leftbtn);
                    currentNum += num;
                    currentNumTextView.setText(currentNum + "");
                }
            }
        });
        rightbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickCallBack != null) {
                    int num = onClickCallBack.rightBtnClick(rightbtn);
                    currentNum += num;
                    currentNumTextView.setText(currentNum+"");
                }
            }
        });
    }
    public OnClickCallBack onClickCallBack;

    public void setOnClickCallBack(OnClickCallBack onClickCallBack) {
        this.onClickCallBack = onClickCallBack;
    }

    public void setCurrentNum(int currentNum) {
        this.currentNum = currentNum;
        currentNumTextView.setText(currentNum+"");
    }

    public int getCurrentNum() {
        return currentNum;
    }

    public interface OnClickCallBack {
        int leftBtnClick (ImageView leftBtn);
        int rightBtnClick (ImageView leftBtn);
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);
        mTextWidth = mTextPaint.measureText(mExampleString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
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
        canvas.drawText(mExampleString,
                paddingLeft + (contentWidth - mTextWidth) / 2,
                paddingTop + (contentHeight + mTextHeight) / 2,
                mTextPaint);

        // Draw the example drawable on top of the text.
        if (mExampleDrawable != null) {
            mExampleDrawable.setBounds(paddingLeft, paddingTop,
                    paddingLeft + contentWidth, paddingTop + contentHeight);
            mExampleDrawable.draw(canvas);
        }
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
}
