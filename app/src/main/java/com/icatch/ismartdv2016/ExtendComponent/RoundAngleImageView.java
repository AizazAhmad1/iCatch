package com.icatch.ismartdv2016.ExtendComponent;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.icatch.ismartdv2016.R;

public class RoundAngleImageView extends ImageView {
    private Paint paint;
    private Paint paint2;
    private int roundHeight = 5;
    private int roundWidth = 5;

    public RoundAngleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public RoundAngleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundAngleImageView(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundAngleImageView);
            this.roundWidth = a.getDimensionPixelSize(0, this.roundWidth);
            this.roundHeight = a.getDimensionPixelSize(1, this.roundHeight);
            a.recycle();
        } else {
            float density = context.getResources().getDisplayMetrics().density;
            this.roundWidth = (int) (((float) this.roundWidth) * density);
            this.roundHeight = (int) (((float) this.roundHeight) * density);
        }
        this.paint = new Paint();
        this.paint.setColor(-1);
        this.paint.setAntiAlias(true);
        this.paint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
        this.paint2 = new Paint();
        this.paint2.setXfermode(null);
    }

    public void draw(Canvas canvas) {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
        Canvas canvas2 = new Canvas(bitmap);
        super.draw(canvas2);
        drawLiftUp(canvas2);
        drawRightUp(canvas2);
        drawLiftDown(canvas2);
        drawRightDown(canvas2);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, this.paint2);
        bitmap.recycle();
    }

    private void drawLiftUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0.0f, (float) this.roundHeight);
        path.lineTo(0.0f, 0.0f);
        path.lineTo((float) this.roundWidth, 0.0f);
        path.arcTo(new RectF(0.0f, 0.0f, (float) (this.roundWidth * 2), (float) (this.roundHeight * 2)), -90.0f, -90.0f);
        path.close();
        canvas.drawPath(path, this.paint);
    }

    private void drawLiftDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0.0f, (float) (getHeight() - this.roundHeight));
        path.lineTo(0.0f, (float) getHeight());
        path.lineTo((float) this.roundWidth, (float) getHeight());
        path.arcTo(new RectF(0.0f, (float) (getHeight() - (this.roundHeight * 2)), (float) ((this.roundWidth * 2) + 0), (float) getHeight()), 90.0f, 90.0f);
        path.close();
        canvas.drawPath(path, this.paint);
    }

    private void drawRightDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo((float) (getWidth() - this.roundWidth), (float) getHeight());
        path.lineTo((float) getWidth(), (float) getHeight());
        path.lineTo((float) getWidth(), (float) (getHeight() - this.roundHeight));
        path.arcTo(new RectF((float) (getWidth() - (this.roundWidth * 2)), (float) (getHeight() - (this.roundHeight * 2)), (float) getWidth(), (float) getHeight()), 0.0f, 90.0f);
        path.close();
        canvas.drawPath(path, this.paint);
    }

    private void drawRightUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo((float) getWidth(), (float) this.roundHeight);
        path.lineTo((float) getWidth(), 0.0f);
        path.lineTo((float) (getWidth() - this.roundWidth), 0.0f);
        path.arcTo(new RectF((float) (getWidth() - (this.roundWidth * 2)), 0.0f, (float) getWidth(), (float) ((this.roundHeight * 2) + 0)), -90.0f, 90.0f);
        path.close();
        canvas.drawPath(path, this.paint);
    }
}
