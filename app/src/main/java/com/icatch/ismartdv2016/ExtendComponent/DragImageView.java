package com.icatch.ismartdv2016.ExtendComponent;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;
import com.icatch.ismartdv2016.Tools.ScaleTool;
import com.icatch.wificam.customer.type.ICatchLightFrequency;
import com.slidingmenu.lib.R;
import com.slidingmenu.lib.SlidingMenu;
import uk.co.senab.photoview.IPhotoView;

public class DragImageView extends ImageView {
    private int MAX_H;
    private int MAX_W;
    private int MIN_H;
    private int MIN_W;
    private float afterLenght;
    private float beforeLenght;
    private int bitmap_H;
    private int bitmap_W;
    private Bitmap bm;
    private int current_Bottom;
    private int current_Left;
    private int current_Right;
    private int current_Top;
    private int current_x;
    private int current_y;
    private boolean firstTouch = true;
    private boolean isControl_H = false;
    private boolean isControl_V = false;
    private Activity mActivity;
    private int maxZoom = 3;
    private int minZoom = 1;
    private MODE mode = MODE.NONE;
    private int originalBottom = 0;
    private int originalLeft = 0;
    private int originalRight = 0;
    private int originalTop = 0;
    private float scale_temp;
    private int screen_H;
    private int screen_W;
    private int start_x;
    private int start_y;

    private enum MODE {
        NONE,
        DRAG,
        ZOOM
    }

    public DragImageView(Context context) {
        super(context);
    }

    public void setmActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void setScreen_W(int screen_W) {
        this.screen_W = screen_W;
    }

    public void setScreen_H(int screen_H) {
        this.screen_H = screen_H;
    }

    public DragImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setImageBitmap(Bitmap bm, int width, int heigth) {
        setImageBitmap(bm);
        this.bm = bm;
        if (bm != null) {
            Rect drawFrameRect = ScaleTool.getScaledPosition(bm.getWidth(), bm.getHeight(), width, heigth);
            LayoutParams dragImageViewLayoutParams = (LayoutParams) getLayoutParams();
            dragImageViewLayoutParams.leftMargin = drawFrameRect.left;
            dragImageViewLayoutParams.rightMargin = drawFrameRect.left;
            dragImageViewLayoutParams.topMargin = drawFrameRect.top;
            dragImageViewLayoutParams.bottomMargin = drawFrameRect.top;
            setLayoutParams(dragImageViewLayoutParams);
            setScaleType(ScaleType.FIT_XY);
            this.screen_W = width;
            this.screen_H = heigth;
        }
    }

    public void setMaxAndMin() {
        this.bitmap_W = getWidth();
        this.bitmap_H = getHeight();
        this.MAX_W = this.bitmap_W * this.maxZoom;
        this.MAX_H = this.bitmap_H * this.maxZoom;
        this.MIN_W = this.bitmap_W * this.minZoom;
        this.MIN_H = this.bitmap_H * this.minZoom;
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        if (this.firstTouch) {
            this.firstTouch = false;
            setMaxAndMin();
            this.originalLeft = getLeft();
            this.originalRight = getRight();
            this.originalTop = getTop();
            this.originalBottom = getBottom();
        }
        switch (event.getAction() & ICatchLightFrequency.ICH_LIGHT_FREQUENCY_UNDEFINED) {
            case SlidingMenu.TOUCHMODE_MARGIN /*0*/:
                onTouchDown(event);
                break;
            case SlidingMenu.TOUCHMODE_FULLSCREEN /*1*/:
                this.mode = MODE.NONE;
                break;
            case SlidingMenu.TOUCHMODE_NONE /*2*/:
                onTouchMove(event);
                break;
            case R.styleable.SlidingMenu_behindScrollScale /*5*/:
                onPointerDown(event);
                break;
            case R.styleable.SlidingMenu_touchModeAbove /*6*/:
                this.mode = MODE.NONE;
                break;
        }
        return true;
    }

    void onTouchDown(MotionEvent event) {
        this.mode = MODE.DRAG;
        this.current_x = (int) event.getRawX();
        this.current_y = (int) event.getRawY();
        this.start_x = (int) event.getX();
        this.start_y = this.current_y - getTop();
    }

    void onPointerDown(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            this.mode = MODE.ZOOM;
            this.beforeLenght = getDistance(event);
        }
    }

    void onTouchMove(MotionEvent event) {
        if (this.mode == MODE.DRAG) {
            int left = this.current_x - this.start_x;
            int right = (this.current_x + getWidth()) - this.start_x;
            int top = this.current_y - this.start_y;
            int bottom = (this.current_y - this.start_y) + getHeight();
            if (this.isControl_H) {
                if (left >= 0) {
                    left = 0;
                    right = getWidth();
                }
                if (right <= this.screen_W) {
                    left = this.screen_W - getWidth();
                    right = this.screen_W;
                }
            } else {
                left = getLeft();
                right = getRight();
            }
            if (this.isControl_V) {
                if (top >= 0) {
                    top = 0;
                    bottom = getHeight();
                }
                if (bottom <= this.screen_H) {
                    top = this.screen_H - getHeight();
                    bottom = this.screen_H;
                }
            } else {
                top = getTop();
                bottom = getBottom();
            }
            if (this.isControl_H) {
                if ((getLeft() == 0 && event.getX() - ((float) this.start_x) > 0.0f) || (getRight() == this.screen_W && event.getX() - ((float) this.start_x) < 0.0f)) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
            } else if (event.getX() - ((float) this.start_x) > 0.0f || event.getX() - ((float) this.start_x) < 0.0f) {
                getParent().requestDisallowInterceptTouchEvent(false);
            }
            if (this.isControl_H || this.isControl_V) {
                setPosition(left, top, right, bottom);
            }
            this.current_x = (int) event.getRawX();
            this.current_y = (int) event.getRawY();
        } else if (this.mode == MODE.ZOOM) {
            this.afterLenght = getDistance(event);
            if (Math.abs(this.afterLenght - this.beforeLenght) > 5.0f) {
                this.scale_temp = this.afterLenght / this.beforeLenght;
                setScale(this.scale_temp);
                this.beforeLenght = this.afterLenght;
            }
        }
    }

    float getDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt((double) ((x * x) + (y * y)));
    }

    private void setPosition(int left, int top, int right, int bottom) {
        layout(left, top, right, bottom);
    }

    void setScale(float scale) {
        int disX = ((int) (((float) getWidth()) * Math.abs(IPhotoView.DEFAULT_MIN_SCALE - scale))) / 4;
        int disY = ((int) (((float) getHeight()) * Math.abs(IPhotoView.DEFAULT_MIN_SCALE - scale))) / 4;
        if (scale > IPhotoView.DEFAULT_MIN_SCALE && getWidth() <= this.MAX_W) {
            this.current_Left = getLeft() - disX;
            this.current_Top = getTop() - disY;
            this.current_Right = getRight() + disX;
            this.current_Bottom = getBottom() + disY;
            setFrame(this.current_Left, this.current_Top, this.current_Right, this.current_Bottom);
            if (this.current_Top > 0 || this.current_Bottom < this.screen_H) {
                this.isControl_V = false;
            } else {
                this.isControl_V = true;
            }
            if (this.current_Left > 0 || this.current_Right < this.screen_W) {
                this.isControl_H = false;
            } else {
                this.isControl_H = true;
            }
        } else if (scale < IPhotoView.DEFAULT_MIN_SCALE && getWidth() >= this.MIN_W) {
            this.current_Left = getLeft() + disX;
            this.current_Top = getTop() + disY;
            this.current_Right = getRight() - disX;
            this.current_Bottom = getBottom() - disY;
            if (this.current_Right - this.current_Left < this.MIN_W) {
                this.current_Left = this.originalLeft;
                this.current_Right = this.originalRight;
            }
            if (this.current_Bottom - this.current_Top < this.MIN_H) {
                this.current_Top = this.originalTop;
                this.current_Bottom = this.originalBottom;
            }
            if (this.isControl_V && this.current_Top > 0) {
                this.current_Top = 0;
                this.current_Bottom = getBottom() - (disY * 2);
                if (this.current_Bottom < this.screen_H) {
                    this.current_Bottom = this.screen_H;
                    this.isControl_V = false;
                }
            }
            if (this.isControl_V && this.current_Bottom < this.screen_H) {
                this.current_Bottom = this.screen_H;
                this.current_Top = getTop() + (disY * 2);
                if (this.current_Top > 0) {
                    this.current_Top = 0;
                    this.isControl_V = false;
                }
            }
            if (this.isControl_H && this.current_Left >= 0) {
                this.current_Left = 0;
                this.current_Right = getRight() - (disX * 2);
                if (this.current_Right <= this.screen_W) {
                    this.current_Right = this.screen_W;
                    this.isControl_H = false;
                }
            }
            if (this.isControl_H && this.current_Right <= this.screen_W) {
                this.current_Right = this.screen_W;
                this.current_Left = getLeft() + (disX * 2);
                if (this.current_Left >= 0) {
                    this.current_Left = 0;
                    this.isControl_H = false;
                }
            }
            if (this.isControl_H || this.isControl_V) {
                setFrame(this.current_Left, this.current_Top, this.current_Right, this.current_Bottom);
            } else {
                setFrame(this.current_Left, this.current_Top, this.current_Right, this.current_Bottom);
            }
        }
    }

    public void recyleBitmap() {
        if (this.bm != null) {
            this.bm.recycle();
            this.bm = null;
        }
    }
}
