package com.icatch.ismartdv2016.ExtendComponent;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import com.icatch.ismartdv2016.Listener.OnProgressBarListener;
import com.icatch.ismartdv2016.R;
import uk.co.senab.photoview.BuildConfig;
import uk.co.senab.photoview.IPhotoView;

public class NumberProgressBar extends View {
    private static final String INSTANCE_MAX = "max";
    private static final String INSTANCE_PREFIX = "prefix";
    private static final String INSTANCE_PROGRESS = "progress";
    private static final String INSTANCE_REACHED_BAR_COLOR = "reached_bar_color";
    private static final String INSTANCE_REACHED_BAR_HEIGHT = "reached_bar_height";
    private static final String INSTANCE_STATE = "saved_instance";
    private static final String INSTANCE_SUFFIX = "suffix";
    private static final String INSTANCE_TEXT_COLOR = "text_color";
    private static final String INSTANCE_TEXT_SIZE = "text_size";
    private static final String INSTANCE_TEXT_VISIBILITY = "text_visibility";
    private static final String INSTANCE_UNREACHED_BAR_COLOR = "unreached_bar_color";
    private static final String INSTANCE_UNREACHED_BAR_HEIGHT = "unreached_bar_height";
    private static final int PROGRESS_TEXT_VISIBLE = 0;
    private final float default_progress_text_offset;
    private final float default_reached_bar_height;
    private final int default_reached_color;
    private final int default_text_color;
    private final float default_text_size;
    private final float default_unreached_bar_height;
    private final int default_unreached_color;
    private String mCurrentDrawText;
    private int mCurrentProgress;
    private boolean mDrawReachedBar;
    private float mDrawTextEnd;
    private float mDrawTextStart;
    private float mDrawTextWidth;
    private boolean mDrawUnreachedBar;
    private boolean mIfDrawText;
    private OnProgressBarListener mListener;
    private int mMaxProgress;
    private float mOffset;
    private String mPrefix;
    private int mReachedBarColor;
    private float mReachedBarHeight;
    private Paint mReachedBarPaint;
    private RectF mReachedRectF;
    private String mSuffix;
    private int mTextColor;
    private Paint mTextPaint;
    private float mTextSize;
    private int mUnreachedBarColor;
    private float mUnreachedBarHeight;
    private Paint mUnreachedBarPaint;
    private RectF mUnreachedRectF;

    public enum ProgressTextVisibility {
        Visible,
        Invisible
    }

    public NumberProgressBar(Context context) {
        this(context, null);
    }

    public NumberProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.numberProgressBarStyle);
    }

    public NumberProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mMaxProgress = 100;
        this.mCurrentProgress = 0;
        this.mSuffix = "%";
        this.mPrefix = BuildConfig.FLAVOR;
        this.default_text_color = Color.rgb(66, 145, 241);
        this.default_reached_color = Color.rgb(66, 145, 241);
        this.default_unreached_color = Color.rgb(204, 204, 204);
        this.mUnreachedRectF = new RectF(0.0f, 0.0f, 0.0f, 0.0f);
        this.mReachedRectF = new RectF(0.0f, 0.0f, 0.0f, 0.0f);
        this.mDrawUnreachedBar = true;
        this.mDrawReachedBar = true;
        this.mIfDrawText = true;
        this.default_reached_bar_height = dp2px(1.5f);
        this.default_unreached_bar_height = dp2px(IPhotoView.DEFAULT_MIN_SCALE);
        this.default_text_size = sp2px(10.0f);
        this.default_progress_text_offset = dp2px(IPhotoView.DEFAULT_MAX_SCALE);
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.NumberProgressBar, defStyleAttr, 0);
        this.mReachedBarColor = attributes.getColor(3, this.default_reached_color);
        this.mUnreachedBarColor = attributes.getColor(2, this.default_unreached_color);
        this.mTextColor = attributes.getColor(7, this.default_text_color);
        this.mTextSize = attributes.getDimension(6, this.default_text_size);
        this.mReachedBarHeight = attributes.getDimension(4, this.default_reached_bar_height);
        this.mUnreachedBarHeight = attributes.getDimension(5, this.default_unreached_bar_height);
        this.mOffset = attributes.getDimension(8, this.default_progress_text_offset);
        if (attributes.getInt(9, 0) != 0) {
            this.mIfDrawText = false;
        }
        setProgress(attributes.getInt(0, 0));
        setMax(attributes.getInt(1, 100));
        attributes.recycle();
        initializePainters();
    }

    protected int getSuggestedMinimumWidth() {
        return (int) this.mTextSize;
    }

    protected int getSuggestedMinimumHeight() {
        return Math.max((int) this.mTextSize, Math.max((int) this.mReachedBarHeight, (int) this.mUnreachedBarHeight));
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec, true), measure(heightMeasureSpec, false));
    }

    private int measure(int measureSpec, boolean isWidth) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int padding = isWidth ? getPaddingLeft() + getPaddingRight() : getPaddingTop() + getPaddingBottom();
        if (mode == 1073741824) {
            return size;
        }
        int result = (isWidth ? getSuggestedMinimumWidth() : getSuggestedMinimumHeight()) + padding;
        if (mode != Integer.MIN_VALUE) {
            return result;
        }
        if (isWidth) {
            return Math.max(result, size);
        }
        return Math.min(result, size);
    }

    protected void onDraw(Canvas canvas) {
        if (this.mIfDrawText) {
            calculateDrawRectF();
        } else {
            calculateDrawRectFWithoutProgressText();
        }
        if (this.mDrawReachedBar) {
            canvas.drawRect(this.mReachedRectF, this.mReachedBarPaint);
        }
        if (this.mDrawUnreachedBar) {
            canvas.drawRect(this.mUnreachedRectF, this.mUnreachedBarPaint);
        }
        if (this.mIfDrawText) {
            canvas.drawText(this.mCurrentDrawText, this.mDrawTextStart, this.mDrawTextEnd, this.mTextPaint);
        }
    }

    private void initializePainters() {
        this.mReachedBarPaint = new Paint(1);
        this.mReachedBarPaint.setColor(this.mReachedBarColor);
        this.mUnreachedBarPaint = new Paint(1);
        this.mUnreachedBarPaint.setColor(this.mUnreachedBarColor);
        this.mTextPaint = new Paint(1);
        this.mTextPaint.setColor(this.mTextColor);
        this.mTextPaint.setTextSize(this.mTextSize);
    }

    private void calculateDrawRectFWithoutProgressText() {
        this.mReachedRectF.left = (float) getPaddingLeft();
        this.mReachedRectF.top = (((float) getHeight()) / 2.0f) - (this.mReachedBarHeight / 2.0f);
        this.mReachedRectF.right = ((((float) ((getWidth() - getPaddingLeft()) - getPaddingRight())) / (((float) getMax()) * IPhotoView.DEFAULT_MIN_SCALE)) * ((float) getProgress())) + ((float) getPaddingLeft());
        this.mReachedRectF.bottom = (((float) getHeight()) / 2.0f) + (this.mReachedBarHeight / 2.0f);
        this.mUnreachedRectF.left = this.mReachedRectF.right;
        this.mUnreachedRectF.right = (float) (getWidth() - getPaddingRight());
        this.mUnreachedRectF.top = (((float) getHeight()) / 2.0f) + ((-this.mUnreachedBarHeight) / 2.0f);
        this.mUnreachedRectF.bottom = (((float) getHeight()) / 2.0f) + (this.mUnreachedBarHeight / 2.0f);
    }

    private void calculateDrawRectF() {
        this.mCurrentDrawText = String.format("%d", new Object[]{Integer.valueOf((getProgress() * 100) / getMax())});
        this.mCurrentDrawText = this.mPrefix + this.mCurrentDrawText + this.mSuffix;
        this.mDrawTextWidth = this.mTextPaint.measureText(this.mCurrentDrawText);
        if (getProgress() == 0) {
            this.mDrawReachedBar = false;
            this.mDrawTextStart = (float) getPaddingLeft();
        } else {
            this.mDrawReachedBar = true;
            this.mReachedRectF.left = (float) getPaddingLeft();
            this.mReachedRectF.top = (((float) getHeight()) / 2.0f) - (this.mReachedBarHeight / 2.0f);
            this.mReachedRectF.right = (((((float) ((getWidth() - getPaddingLeft()) - getPaddingRight())) / (((float) getMax()) * IPhotoView.DEFAULT_MIN_SCALE)) * ((float) getProgress())) - this.mOffset) + ((float) getPaddingLeft());
            this.mReachedRectF.bottom = (((float) getHeight()) / 2.0f) + (this.mReachedBarHeight / 2.0f);
            this.mDrawTextStart = this.mReachedRectF.right + this.mOffset;
        }
        this.mDrawTextEnd = (float) ((int) ((((float) getHeight()) / 2.0f) - ((this.mTextPaint.descent() + this.mTextPaint.ascent()) / 2.0f)));
        if (this.mDrawTextStart + this.mDrawTextWidth >= ((float) (getWidth() - getPaddingRight()))) {
            this.mDrawTextStart = ((float) (getWidth() - getPaddingRight())) - this.mDrawTextWidth;
            this.mReachedRectF.right = this.mDrawTextStart - this.mOffset;
        }
        float unreachedBarStart = (this.mDrawTextStart + this.mDrawTextWidth) + this.mOffset;
        if (unreachedBarStart >= ((float) (getWidth() - getPaddingRight()))) {
            this.mDrawUnreachedBar = false;
            return;
        }
        this.mDrawUnreachedBar = true;
        this.mUnreachedRectF.left = unreachedBarStart;
        this.mUnreachedRectF.right = (float) (getWidth() - getPaddingRight());
        this.mUnreachedRectF.top = (((float) getHeight()) / 2.0f) + ((-this.mUnreachedBarHeight) / 2.0f);
        this.mUnreachedRectF.bottom = (((float) getHeight()) / 2.0f) + (this.mUnreachedBarHeight / 2.0f);
    }

    public int getTextColor() {
        return this.mTextColor;
    }

    public float getProgressTextSize() {
        return this.mTextSize;
    }

    public int getUnreachedBarColor() {
        return this.mUnreachedBarColor;
    }

    public int getReachedBarColor() {
        return this.mReachedBarColor;
    }

    public int getProgress() {
        return this.mCurrentProgress;
    }

    public int getMax() {
        return this.mMaxProgress;
    }

    public float getReachedBarHeight() {
        return this.mReachedBarHeight;
    }

    public float getUnreachedBarHeight() {
        return this.mUnreachedBarHeight;
    }

    public void setProgressTextSize(float textSize) {
        this.mTextSize = textSize;
        this.mTextPaint.setTextSize(this.mTextSize);
        invalidate();
    }

    public void setProgressTextColor(int textColor) {
        this.mTextColor = textColor;
        this.mTextPaint.setColor(this.mTextColor);
        invalidate();
    }

    public void setUnreachedBarColor(int barColor) {
        this.mUnreachedBarColor = barColor;
        this.mUnreachedBarPaint.setColor(this.mReachedBarColor);
        invalidate();
    }

    public void setReachedBarColor(int progressColor) {
        this.mReachedBarColor = progressColor;
        this.mReachedBarPaint.setColor(this.mReachedBarColor);
        invalidate();
    }

    public void setReachedBarHeight(float height) {
        this.mReachedBarHeight = height;
    }

    public void setUnreachedBarHeight(float height) {
        this.mUnreachedBarHeight = height;
    }

    public void setMax(int maxProgress) {
        if (maxProgress > 0) {
            this.mMaxProgress = maxProgress;
            invalidate();
        }
    }

    public void setSuffix(String suffix) {
        if (suffix == null) {
            this.mSuffix = BuildConfig.FLAVOR;
        } else {
            this.mSuffix = suffix;
        }
    }

    public String getSuffix() {
        return this.mSuffix;
    }

    public void setPrefix(String prefix) {
        if (prefix == null) {
            this.mPrefix = BuildConfig.FLAVOR;
        } else {
            this.mPrefix = prefix;
        }
    }

    public String getPrefix() {
        return this.mPrefix;
    }

    public void incrementProgressBy(int by) {
        if (by > 0) {
            setProgress(getProgress() + by);
        }
        if (this.mListener != null) {
            this.mListener.onProgressChange(getProgress(), getMax());
        }
    }

    public void setProgress(int progress) {
        if (progress <= getMax() && progress >= 0) {
            this.mCurrentProgress = progress;
            invalidate();
        }
    }

    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putInt(INSTANCE_TEXT_COLOR, getTextColor());
        bundle.putFloat(INSTANCE_TEXT_SIZE, getProgressTextSize());
        bundle.putFloat(INSTANCE_REACHED_BAR_HEIGHT, getReachedBarHeight());
        bundle.putFloat(INSTANCE_UNREACHED_BAR_HEIGHT, getUnreachedBarHeight());
        bundle.putInt(INSTANCE_REACHED_BAR_COLOR, getReachedBarColor());
        bundle.putInt(INSTANCE_UNREACHED_BAR_COLOR, getUnreachedBarColor());
        bundle.putInt(INSTANCE_MAX, getMax());
        bundle.putInt(INSTANCE_PROGRESS, getProgress());
        bundle.putString(INSTANCE_SUFFIX, getSuffix());
        bundle.putString(INSTANCE_PREFIX, getPrefix());
        bundle.putBoolean(INSTANCE_TEXT_VISIBILITY, getProgressTextVisibility());
        return bundle;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            this.mTextColor = bundle.getInt(INSTANCE_TEXT_COLOR);
            this.mTextSize = bundle.getFloat(INSTANCE_TEXT_SIZE);
            this.mReachedBarHeight = bundle.getFloat(INSTANCE_REACHED_BAR_HEIGHT);
            this.mUnreachedBarHeight = bundle.getFloat(INSTANCE_UNREACHED_BAR_HEIGHT);
            this.mReachedBarColor = bundle.getInt(INSTANCE_REACHED_BAR_COLOR);
            this.mUnreachedBarColor = bundle.getInt(INSTANCE_UNREACHED_BAR_COLOR);
            initializePainters();
            setMax(bundle.getInt(INSTANCE_MAX));
            setProgress(bundle.getInt(INSTANCE_PROGRESS));
            setPrefix(bundle.getString(INSTANCE_PREFIX));
            setSuffix(bundle.getString(INSTANCE_SUFFIX));
            setProgressTextVisibility(bundle.getBoolean(INSTANCE_TEXT_VISIBILITY) ? ProgressTextVisibility.Visible : ProgressTextVisibility.Invisible);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public float dp2px(float dp) {
        return (dp * getResources().getDisplayMetrics().density) + 0.5f;
    }

    public float sp2px(float sp) {
        return sp * getResources().getDisplayMetrics().scaledDensity;
    }

    public void setProgressTextVisibility(ProgressTextVisibility visibility) {
        this.mIfDrawText = visibility == ProgressTextVisibility.Visible;
        invalidate();
    }

    public boolean getProgressTextVisibility() {
        return this.mIfDrawText;
    }

    public void setOnProgressBarListener(OnProgressBarListener listener) {
        this.mListener = listener;
    }
}
