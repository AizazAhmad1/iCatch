package uk.co.senab.photoview.gestures;

import android.annotation.TargetApi;
import android.content.Context;
import android.view.MotionEvent;
import com.icatch.wificam.customer.type.ICatchLightFrequency;
import com.slidingmenu.lib.R;
import com.slidingmenu.lib.SlidingMenu;
import uk.co.senab.photoview.Compat;

@TargetApi(5)
public class EclairGestureDetector extends CupcakeGestureDetector {
    private static final int INVALID_POINTER_ID = -1;
    private int mActivePointerId = INVALID_POINTER_ID;
    private int mActivePointerIndex = 0;

    public EclairGestureDetector(Context context) {
        super(context);
    }

    float getActiveX(MotionEvent ev) {
        try {
            return ev.getX(this.mActivePointerIndex);
        } catch (Exception e) {
            return ev.getX();
        }
    }

    float getActiveY(MotionEvent ev) {
        try {
            return ev.getY(this.mActivePointerIndex);
        } catch (Exception e) {
            return ev.getY();
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        boolean z = true;
        int i = 0;
        switch (ev.getAction() & ICatchLightFrequency.ICH_LIGHT_FREQUENCY_UNDEFINED) {
            case SlidingMenu.TOUCHMODE_MARGIN /*0*/:
                this.mActivePointerId = ev.getPointerId(0);
                break;
            case SlidingMenu.TOUCHMODE_FULLSCREEN /*1*/:
            case R.styleable.SlidingMenu_behindOffset /*3*/:
                this.mActivePointerId = INVALID_POINTER_ID;
                break;
            case R.styleable.SlidingMenu_touchModeAbove /*6*/:
                int pointerIndex = Compat.getPointerIndex(ev.getAction());
                if (ev.getPointerId(pointerIndex) == this.mActivePointerId) {
                    int newPointerIndex;
                    if (pointerIndex == 0) {
                        newPointerIndex = z;
                    } else {
                        newPointerIndex = 0;
                    }
                    this.mActivePointerId = ev.getPointerId(newPointerIndex);
                    this.mLastTouchX = ev.getX(newPointerIndex);
                    this.mLastTouchY = ev.getY(newPointerIndex);
                    break;
                }
                break;
        }
        if (this.mActivePointerId != INVALID_POINTER_ID) {
            i = this.mActivePointerId;
        }
        this.mActivePointerIndex = ev.findPointerIndex(i);
        try {
            z = super.onTouchEvent(ev);
        } catch (IllegalArgumentException e) {
        }
        return z;
    }
}
