package com.example.fqzhang.myapplication.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.fqzhang.myapplication.R;

/**
 * TODO: document your custom view class.
 */
public class TextImage extends View {
    public String mText; // TODO: use a default from R.string...
    private int mTextColor = Color.RED; // TODO: use a default from R.color...
    private float mTextSize = 0; // TODO: use a default from R.dimen...
    private Bitmap mTextDrawable;
    private int mImageScale;
    private static final int FITXY = 0;
    private static final int CENTER = 1;
    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;
    private Rect rect ,mTextBound;
    private int mWidth,mHeight;
    public TextImage(Context context) {
        super(context);
        init(null, 0);
    }
    public TextImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public TextImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TextImage, defStyle, 0);
        int n = a.getIndexCount();
        for (int i=0 ;i<n ;i++) {
            int index = a.getIndex(i);
            switch (index) {
                case R.styleable.TextImage_titleText:
                    mText = a.getString(index);
                    break;
                case R.styleable.TextImage_titleTextSize:
                    mTextSize = a.getDimension(index,15);
                    break;
                case R.styleable.TextImage_titleTextColor:
                    mTextColor = a.getColor(index,Color.BLUE);
                    break;
                case R.styleable.TextImage_titleDrawable:
                    mTextDrawable = BitmapFactory.decodeResource(getResources(),a.getResourceId(index,0));
                    break;
                case R.styleable.TextImage_imageScaleType:
                    mImageScale = a.getInt(index,0);
                    break;
            }
        }

        a.recycle();

        rect = new Rect();
        mTextBound = new Rect();
        mTextPaint = new TextPaint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.getTextBounds(mText,0,mText.length(),mTextBound);
       // invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
        mTextPaint.getTextBounds(mText,0,mText.length(),mTextBound);
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = width;
        } else {
            int imageWidth = mTextDrawable.getWidth();
            int textWidth = mTextBound.width();
            int actualWidth = Math.max(imageWidth,textWidth)+getPaddingRight()+getPaddingLeft();
            mWidth = Math.min(actualWidth,width);
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = height;
        } else {
            int imageHeight = mTextDrawable.getHeight();
            int textHeight = mTextBound.height();
            int actualHeight = imageHeight + textHeight +getPaddingTop() +getPaddingBottom();
            mHeight = Math.min(actualHeight,height);
        }
        setMeasuredDimension(mWidth,mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       /* mTextPaint.set*/


        rect.left = getPaddingLeft();
        rect.right = mWidth - getPaddingRight();
        rect.top = getPaddingTop();
        rect.bottom = mHeight-getPaddingBottom();
/*        mTextPaint.setColor(Color.GRAY);
        mTextPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mTextPaint);*/
        mTextPaint.setColor(mTextColor);
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        if (mTextBound.width() > mWidth) {
            mText = TextUtils.ellipsize(mText,mTextPaint,mWidth-getPaddingLeft()-getPaddingRight(), TextUtils.TruncateAt.END).toString();
            canvas.drawText(mText,getPaddingLeft(),mHeight-getPaddingBottom(),mTextPaint);
        } else {
            canvas.drawText(mText, (float) ((mWidth-mTextBound.width())/2.0),mHeight-getPaddingBottom(),mTextPaint);
        }
        rect.bottom -= mTextBound.height();
        if (mImageScale == FITXY) {

            canvas.drawBitmap(mTextDrawable,null,rect,mTextPaint);
        }else if (mImageScale == CENTER) {
            rect.left = mWidth/2-mTextDrawable.getWidth()/2;
            rect.right = mWidth/2+mTextDrawable.getWidth()/2;
            rect.bottom = (mHeight-mTextBound.height()-getPaddingBottom())/2 + mTextDrawable.getHeight()/2;
            rect.top = (mHeight-mTextBound.height()-getPaddingBottom())/2 - mTextDrawable.getHeight()/2;
            canvas.drawBitmap(mTextDrawable,null,rect,mTextPaint);
        }
/*        // TODO: consider storing these as member variables to reduce
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
        }*/
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
     * @param text The example string attribute value to use.
     */
    public void setText(String text) {
        mText = text;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param textColor The example color attribute value to use.
     */
    public void setTextColor(int textColor) {
        mTextColor = textColor;
        invalidateTextPaintAndMeasurements();
    }


    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param textSize The example dimension attribute value to use.
     */
    public void setTextSize(float textSize) {
        mTextSize = textSize;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param TextDrawable The example drawable attribute value to use.
     */
    public void setTextDrawable(Bitmap TextDrawable) {
        mTextDrawable = TextDrawable;invalidateTextPaintAndMeasurements();
    }

    public int getTextColor() {
        return mTextColor;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public Bitmap getTextDrawable() {
        return mTextDrawable;
    }

    public int getImageScale() {
        return mImageScale;
    }
}
