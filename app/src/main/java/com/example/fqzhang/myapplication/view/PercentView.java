package com.example.fqzhang.myapplication.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.fqzhang.myapplication.R;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * TODO: document your custom view class.
 */
public class PercentView extends View {
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;
    /*************************/
    private OnChangeProgressListener mChangeProgresslistener;
    private final static String TAG = PercentView.class.getSimpleName();
    private int percentColor;
    private int backgroundColor;
    private float radius;
    private int progress;
    private int gravity = CENTER;
    public static final int LEFT = 0;
    public static final int TOP = 1;
    public static final int CENTER = 2;

    public void setChangeProgresslistener(OnChangeProgressListener mChangeProgresslistener) {
        this.mChangeProgresslistener = mChangeProgresslistener;
    }

    public static final int RIGHT = 3;
    public static final int BOTTOM = 4;
    private Paint mPaint;
    private RectF rectF;
    float centerX;
    float centerY;
    public PercentView(Context context) {
        super(context);
        init(null, 0);
    }

    public PercentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PercentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);

    }

    private void randomProgress() {TextPaint textPaint;
        Random r = new Random();
        progress = r.nextInt(100);
    }

    private void init(AttributeSet attrs, int defStyle) {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
/*                if (mChangeProgresslistener != null ){
                    mChangeProgresslistener.onChangeProgress(PercentView.this);
                }*/
            }
        });
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        rectF = new RectF();
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.PercentView, defStyle, 0);
        if (a != null) {
            percentColor = a.getColor(R.styleable.PercentView_precent_progress_color,Color.BLUE);
            backgroundColor = a.getColor(R.styleable.PercentView_precent_background_color,Color.GRAY);
            radius = a.getDimension(R.styleable.PercentView_percent_circel_radius,0);
            progress = a.getInt(R.styleable.PercentView_precent_circel_progress,0);
            gravity = a.getInt(R.styleable.PercentView_precent_circel_gravity,CENTER);
            a.recycle();
        }
/*        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();*/
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
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int hightMode = MeasureSpec.getMode(heightMeasureSpec);
        int hightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthF=0,heightF=0;
        switch (widthMode){
            case MeasureSpec.EXACTLY:
            widthF = widthSize;
            break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                widthF = (int) (radius*2+getPaddingRight()+getPaddingLeft());
                break;
        }
        switch (hightMode){
            case MeasureSpec.EXACTLY:
                heightF = hightSize;
                break;
            case MeasureSpec.AT_MOST:
                case MeasureSpec.UNSPECIFIED:
                    heightF = (int) (radius*2+getPaddingBottom()+getPaddingTop());
                    break;
        }
        setMeasuredDimension(widthF,heightF);
        int height = getHeight();
        int width = getWidth();
        centerX = width/2;
        centerY = width/2;
        switch (gravity){
            case LEFT:
                centerX = radius + getPaddingLeft();
                break;
            case TOP:
                centerY = radius +getPaddingTop();
                break;
            case RIGHT:
                centerX = width -radius - getPaddingRight();
                break;
            case BOTTOM:
                centerY = height - radius - getPaddingBottom();
                break;
            case CENTER:
                break;
        }
        rectF.set(centerX-radius,centerY-radius,centerX+radius,centerY+radius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(backgroundColor);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle(centerX,centerY,radius,mPaint);
        mPaint.setColor(percentColor);
        double percent = progress*1.0/100;
        int angle = (int) (percent*360);
        canvas.drawArc(rectF,180,angle,true,mPaint);

      /*  // TODO: consider storing these as member variables to reduce
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
    public interface OnChangeProgressListener{
        void onChangeProgress(PercentView view);
    }
    public void start(){
/*        if (mChangeProgresslistener != null ){
            mChangeProgresslistener.onChangeProgress();
        }*/
        progress = 0;
        final Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (progress == 100) {
                    t.cancel();
                }
                progress+=10;
                postInvalidate();
            }
        },500,800);

    }
}
