package com.icatch.ismartdv2016.ExtendComponent;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.icatch.ismartdv2016.R;

public class BorderImageView extends ImageView {
    private Context context;
    private boolean isShow = false;
    Paint paint;

    public BorderImageView(Context context) {
        super(context);
        this.context = context;
    }

    public BorderImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public BorderImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.isShow) {
            Rect rec = canvas.getClipBounds();
            rec.bottom--;
            rec.right--;
            Paint paint = new Paint();
            paint.setColor(this.context.getResources().getColor(R.color.gray));
            paint.setStyle(Style.STROKE);
            paint.setStrokeWidth(8.0f);
            canvas.drawRect(rec, paint);
        }
    }

    public void showBorder(boolean isShow) {
        this.isShow = isShow;
    }
}
