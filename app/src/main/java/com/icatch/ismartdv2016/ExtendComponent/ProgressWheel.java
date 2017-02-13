package com.icatch.ismartdv2016.ExtendComponent;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import com.icatch.ismartdv2016.R;
import uk.co.senab.photoview.BuildConfig;

public class ProgressWheel extends View {
    private int barColor = -1442840576;
    private int barLength = 60;
    private Paint barPaint = new Paint();
    private int barWidth = 20;
    private RectF circleBounds = new RectF();
    private int circleColor = 0;
    private RectF circleInnerContour = new RectF();
    private RectF circleOuterContour = new RectF();
    private Paint circlePaint = new Paint();
    private int circleRadius = 80;
    private int contourColor = -1442840576;
    private Paint contourPaint = new Paint();
    private float contourSize = 0.0f;
    private int delayMillis = 10;
    private int fullRadius = 100;
    private RectF innerCircleBounds = new RectF();
    boolean isSpinning = false;
    private int layoutHeight = 0;
    private int layoutWidth = 0;
    private int paddingBottom = 5;
    private int paddingLeft = 5;
    private int paddingRight = 5;
    private int paddingTop = 5;
    private float progress = 0.0f;
    private int rimColor = -1428300323;
    private Paint rimPaint = new Paint();
    private int rimWidth = 20;
    private float spinSpeed = 2.0f;
    private String[] splitText = new String[0];
    private String text = BuildConfig.FLAVOR;
    private int textColor = -16777216;
    private Paint textPaint = new Paint();
    private int textSize = 20;

    public ProgressWheel(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(context.obtainStyledAttributes(attrs, R.styleable.ProgressWheel));
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int widthWithoutPadding = (width - getPaddingLeft()) - getPaddingRight();
        int heightWithoutPadding = (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom();
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (heightMode == 0 || widthMode == 0) {
            size = Math.max(heightWithoutPadding, widthWithoutPadding);
        } else if (widthWithoutPadding > heightWithoutPadding) {
            size = heightWithoutPadding;
        } else {
            size = widthWithoutPadding;
        }
        setMeasuredDimension((getPaddingLeft() + size) + getPaddingRight(), (getPaddingTop() + size) + getPaddingBottom());
    }

    protected void onSizeChanged(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight);
        this.layoutWidth = newWidth;
        this.layoutHeight = newHeight;
        setupBounds();
        setupPaints();
        invalidate();
    }

    private void setupPaints() {
        this.barPaint.setColor(this.barColor);
        this.barPaint.setAntiAlias(true);
        this.barPaint.setStyle(Style.STROKE);
        this.barPaint.setStrokeWidth((float) this.barWidth);
        this.rimPaint.setColor(this.rimColor);
        this.rimPaint.setAntiAlias(true);
        this.rimPaint.setStyle(Style.STROKE);
        this.rimPaint.setStrokeWidth((float) this.rimWidth);
        this.circlePaint.setColor(this.circleColor);
        this.circlePaint.setAntiAlias(true);
        this.circlePaint.setStyle(Style.FILL);
        this.textPaint.setColor(this.textColor);
        this.textPaint.setStyle(Style.FILL);
        this.textPaint.setAntiAlias(true);
        this.textPaint.setTextSize((float) this.textSize);
        this.contourPaint.setColor(this.contourColor);
        this.contourPaint.setAntiAlias(true);
        this.contourPaint.setStyle(Style.STROKE);
        this.contourPaint.setStrokeWidth(this.contourSize);
    }

    private void setupBounds() {
        int minValue = Math.min(this.layoutWidth, this.layoutHeight);
        int xOffset = this.layoutWidth - minValue;
        int yOffset = this.layoutHeight - minValue;
        this.paddingTop = getPaddingTop() + (yOffset / 2);
        this.paddingBottom = getPaddingBottom() + (yOffset / 2);
        this.paddingLeft = getPaddingLeft() + (xOffset / 2);
        this.paddingRight = getPaddingRight() + (xOffset / 2);
        int width = getWidth();
        int height = getHeight();
        this.innerCircleBounds = new RectF(((float) this.paddingLeft) + (((float) this.barWidth) * 1.5f), ((float) this.paddingTop) + (((float) this.barWidth) * 1.5f), ((float) (width - this.paddingRight)) - (((float) this.barWidth) * 1.5f), ((float) (height - this.paddingBottom)) - (((float) this.barWidth) * 1.5f));
        this.circleBounds = new RectF((float) (this.paddingLeft + this.barWidth), (float) (this.paddingTop + this.barWidth), (float) ((width - this.paddingRight) - this.barWidth), (float) ((height - this.paddingBottom) - this.barWidth));
        this.circleInnerContour = new RectF((this.circleBounds.left + (((float) this.rimWidth) / 2.0f)) + (this.contourSize / 2.0f), (this.circleBounds.top + (((float) this.rimWidth) / 2.0f)) + (this.contourSize / 2.0f), (this.circleBounds.right - (((float) this.rimWidth) / 2.0f)) - (this.contourSize / 2.0f), (this.circleBounds.bottom - (((float) this.rimWidth) / 2.0f)) - (this.contourSize / 2.0f));
        this.circleOuterContour = new RectF((this.circleBounds.left - (((float) this.rimWidth) / 2.0f)) - (this.contourSize / 2.0f), (this.circleBounds.top - (((float) this.rimWidth) / 2.0f)) - (this.contourSize / 2.0f), (this.circleBounds.right + (((float) this.rimWidth) / 2.0f)) + (this.contourSize / 2.0f), (this.circleBounds.bottom + (((float) this.rimWidth) / 2.0f)) + (this.contourSize / 2.0f));
        this.fullRadius = ((width - this.paddingRight) - this.barWidth) / 2;
        this.circleRadius = (this.fullRadius - this.barWidth) + 1;
    }

    private void parseAttributes(TypedArray a) {
        this.barWidth = (int) a.getDimension(10, (float) this.barWidth);
        this.rimWidth = (int) a.getDimension(5, (float) this.rimWidth);
        this.spinSpeed = (float) ((int) a.getDimension(6, this.spinSpeed));
        this.barLength = (int) a.getDimension(11, (float) this.barLength);
        this.delayMillis = a.getInteger(7, this.delayMillis);
        if (this.delayMillis < 0) {
            this.delayMillis = 10;
        }
        if (a.hasValue(0)) {
            setText(a.getString(0));
        }
        this.barColor = a.getColor(3, this.barColor);
        this.textColor = a.getColor(1, this.textColor);
        this.rimColor = a.getColor(4, this.rimColor);
        this.circleColor = a.getColor(8, this.circleColor);
        this.contourColor = a.getColor(12, this.contourColor);
        this.textSize = (int) a.getDimension(2, (float) this.textSize);
        this.contourSize = a.getDimension(13, this.contourSize);
        a.recycle();
    }

    protected void onDraw(Canvas canvas) {
        int i = 0;
        super.onDraw(canvas);
        canvas.drawArc(this.innerCircleBounds, 360.0f, 360.0f, false, this.circlePaint);
        canvas.drawArc(this.circleBounds, 360.0f, 360.0f, false, this.rimPaint);
        canvas.drawArc(this.circleOuterContour, 360.0f, 360.0f, false, this.contourPaint);
        if (this.isSpinning) {
            canvas.drawArc(this.circleBounds, this.progress - 90.0f, (float) this.barLength, false, this.barPaint);
        } else {
            canvas.drawArc(this.circleBounds, -90.0f, this.progress, false, this.barPaint);
        }
        float verticalTextOffset = ((this.textPaint.descent() - this.textPaint.ascent()) / 2.0f) - this.textPaint.descent();
        String[] strArr = this.splitText;
        int length = strArr.length;
        while (i < length) {
            String line = strArr[i];
            canvas.drawText(line, ((float) (getWidth() / 2)) - (this.textPaint.measureText(line) / 2.0f), ((float) (getHeight() / 2)) + verticalTextOffset, this.textPaint);
            i++;
        }
        if (this.isSpinning) {
            scheduleRedraw();
        }
    }

    private void scheduleRedraw() {
        this.progress += this.spinSpeed;
        if (this.progress > 360.0f) {
            this.progress = 0.0f;
        }
        postInvalidateDelayed((long) this.delayMillis);
    }

    public boolean isSpinning() {
        return this.isSpinning;
    }

    public void resetCount() {
        this.progress = 0.0f;
        setText("0%");
        invalidate();
    }

    public void stopSpinning() {
        this.isSpinning = false;
        this.progress = 0.0f;
        postInvalidate();
    }

    public void startSpinning() {
        this.isSpinning = true;
        postInvalidate();
    }

    public void incrementProgress() {
        incrementProgress(1);
    }

    public void incrementProgress(int amount) {
        this.isSpinning = false;
        this.progress += (float) amount;
        if (this.progress > 360.0f) {
            this.progress %= 360.0f;
        }
        postInvalidate();
    }

    public void setProgress(int i) {
        this.isSpinning = false;
        this.progress = (float) i;
        postInvalidate();
    }

    public void setText(String text) {
        this.text = text;
        this.splitText = this.text.split("\n");
    }

    public int getCircleRadius() {
        return this.circleRadius;
    }

    public void setCircleRadius(int circleRadius) {
        this.circleRadius = circleRadius;
    }

    public int getBarLength() {
        return this.barLength;
    }

    public void setBarLength(int barLength) {
        this.barLength = barLength;
    }

    public int getBarWidth() {
        return this.barWidth;
    }

    public void setBarWidth(int barWidth) {
        this.barWidth = barWidth;
        if (this.barPaint != null) {
            this.barPaint.setStrokeWidth((float) this.barWidth);
        }
    }

    public int getTextSize() {
        return this.textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        if (this.textPaint != null) {
            this.textPaint.setTextSize((float) this.textSize);
        }
    }

    public int getPaddingTop() {
        return this.paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public int getPaddingBottom() {
        return this.paddingBottom;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public int getPaddingLeft() {
        return this.paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public int getPaddingRight() {
        return this.paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public int getBarColor() {
        return this.barColor;
    }

    public void setBarColor(int barColor) {
        this.barColor = barColor;
        if (this.barPaint != null) {
            this.barPaint.setColor(this.barColor);
        }
    }

    public int getCircleColor() {
        return this.circleColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
        if (this.circlePaint != null) {
            this.circlePaint.setColor(this.circleColor);
        }
    }

    public int getRimColor() {
        return this.rimColor;
    }

    public void setRimColor(int rimColor) {
        this.rimColor = rimColor;
        if (this.rimPaint != null) {
            this.rimPaint.setColor(this.rimColor);
        }
    }

    public Shader getRimShader() {
        return this.rimPaint.getShader();
    }

    public void setRimShader(Shader shader) {
        this.rimPaint.setShader(shader);
    }

    public int getTextColor() {
        return this.textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        if (this.textPaint != null) {
            this.textPaint.setColor(this.textColor);
        }
    }

    public float getSpinSpeed() {
        return this.spinSpeed;
    }

    public void setSpinSpeed(float spinSpeed) {
        this.spinSpeed = spinSpeed;
    }

    public int getRimWidth() {
        return this.rimWidth;
    }

    public void setRimWidth(int rimWidth) {
        this.rimWidth = rimWidth;
        if (this.rimPaint != null) {
            this.rimPaint.setStrokeWidth((float) this.rimWidth);
        }
    }

    public int getDelayMillis() {
        return this.delayMillis;
    }

    public void setDelayMillis(int delayMillis) {
        this.delayMillis = delayMillis;
    }

    public int getContourColor() {
        return this.contourColor;
    }

    public void setContourColor(int contourColor) {
        this.contourColor = contourColor;
        if (this.contourPaint != null) {
            this.contourPaint.setColor(this.contourColor);
        }
    }

    public float getContourSize() {
        return this.contourSize;
    }

    public void setContourSize(float contourSize) {
        this.contourSize = contourSize;
        if (this.contourPaint != null) {
            this.contourPaint.setStrokeWidth(this.contourSize);
        }
    }

    public int getProgress() {
        return (int) this.progress;
    }
}
