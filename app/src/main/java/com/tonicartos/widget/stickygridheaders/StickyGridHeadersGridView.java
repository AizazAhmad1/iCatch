package com.tonicartos.widget.stickygridheaders;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.ListAdapter;
import com.icatch.wificam.customer.type.ICatchLightFrequency;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class StickyGridHeadersGridView extends GridView implements OnScrollListener, OnItemClickListener, OnItemSelectedListener, OnItemLongClickListener {
    private static final String ERROR_PLATFORM = ("Error supporting platform " + VERSION.SDK_INT + ".");
    private static final int MATCHED_STICKIED_HEADER = -2;
    private static final int NO_MATCHED_HEADER = -1;
    static final String TAG = StickyGridHeadersGridView.class.getSimpleName();
    protected static final int TOUCH_MODE_DONE_WAITING = 2;
    protected static final int TOUCH_MODE_DOWN = 0;
    protected static final int TOUCH_MODE_FINISHED_LONG_PRESS = -2;
    protected static final int TOUCH_MODE_REST = -1;
    protected static final int TOUCH_MODE_TAP = 1;
    protected StickyGridHeadersBaseAdapterWrapper mAdapter;
    private boolean mAreHeadersSticky;
    private boolean mClipToPaddingHasBeenSet;
    private final Rect mClippingRect;
    private boolean mClippingToPadding;
    private int mColumnWidth;
    private long mCurrentHeaderId;
    protected boolean mDataChanged;
    private DataSetObserver mDataSetObserver;
    private int mHeaderBottomPosition;
    boolean mHeaderChildBeingPressed;
    private boolean mHeadersIgnorePadding;
    private int mHorizontalSpacing;
    private boolean mMaskStickyHeaderRegion;
    protected int mMotionHeaderPosition;
    private float mMotionY;
    private int mNumColumns;
    private boolean mNumColumnsSet;
    private int mNumMeasuredColumns;
    private OnHeaderClickListener mOnHeaderClickListener;
    private OnHeaderLongClickListener mOnHeaderLongClickListener;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private OnItemSelectedListener mOnItemSelectedListener;
    public CheckForHeaderLongPress mPendingCheckForLongPress;
    public CheckForHeaderTap mPendingCheckForTap;
    private PerformHeaderClick mPerformHeaderClick;
    private OnScrollListener mScrollListener;
    private int mScrollState;
    private View mStickiedHeader;
    protected int mTouchMode;
    private Runnable mTouchModeReset;
    private int mTouchSlop;
    private int mVerticalSpacing;

    private class WindowRunnable {
        private int mOriginalAttachCount;

        private WindowRunnable() {
        }

        public void rememberWindowAttachCount() {
            this.mOriginalAttachCount = StickyGridHeadersGridView.this.getWindowAttachCount();
        }

        public boolean sameWindow() {
            return StickyGridHeadersGridView.this.hasWindowFocus() && StickyGridHeadersGridView.this.getWindowAttachCount() == this.mOriginalAttachCount;
        }
    }

    private class CheckForHeaderLongPress extends WindowRunnable implements Runnable {
        private CheckForHeaderLongPress() {
            super();
        }

        public void run() {
            View child = StickyGridHeadersGridView.this.getHeaderAt(StickyGridHeadersGridView.this.mMotionHeaderPosition);
            if (child != null) {
                long longPressId = StickyGridHeadersGridView.this.headerViewPositionToId(StickyGridHeadersGridView.this.mMotionHeaderPosition);
                boolean handled = false;
                if (sameWindow() && !StickyGridHeadersGridView.this.mDataChanged) {
                    handled = StickyGridHeadersGridView.this.performHeaderLongPress(child, longPressId);
                }
                if (handled) {
                    StickyGridHeadersGridView.this.mTouchMode = StickyGridHeadersGridView.TOUCH_MODE_FINISHED_LONG_PRESS;
                    StickyGridHeadersGridView.this.setPressed(false);
                    child.setPressed(false);
                    return;
                }
                StickyGridHeadersGridView.this.mTouchMode = StickyGridHeadersGridView.TOUCH_MODE_DONE_WAITING;
            }
        }
    }

    final class CheckForHeaderTap implements Runnable {
        CheckForHeaderTap() {
        }

        public void run() {
            if (StickyGridHeadersGridView.this.mTouchMode == 0) {
                StickyGridHeadersGridView.this.mTouchMode = StickyGridHeadersGridView.TOUCH_MODE_TAP;
                View header = StickyGridHeadersGridView.this.getHeaderAt(StickyGridHeadersGridView.this.mMotionHeaderPosition);
                if (header != null && !StickyGridHeadersGridView.this.mHeaderChildBeingPressed) {
                    if (StickyGridHeadersGridView.this.mDataChanged) {
                        StickyGridHeadersGridView.this.mTouchMode = StickyGridHeadersGridView.TOUCH_MODE_DONE_WAITING;
                        return;
                    }
                    header.setPressed(true);
                    StickyGridHeadersGridView.this.setPressed(true);
                    StickyGridHeadersGridView.this.refreshDrawableState();
                    int longPressTimeout = ViewConfiguration.getLongPressTimeout();
                    if (StickyGridHeadersGridView.this.isLongClickable()) {
                        if (StickyGridHeadersGridView.this.mPendingCheckForLongPress == null) {
                            StickyGridHeadersGridView.this.mPendingCheckForLongPress = new CheckForHeaderLongPress();
                        }
                        StickyGridHeadersGridView.this.mPendingCheckForLongPress.rememberWindowAttachCount();
                        StickyGridHeadersGridView.this.postDelayed(StickyGridHeadersGridView.this.mPendingCheckForLongPress, (long) longPressTimeout);
                        return;
                    }
                    StickyGridHeadersGridView.this.mTouchMode = StickyGridHeadersGridView.TOUCH_MODE_DONE_WAITING;
                }
            }
        }
    }

    public interface OnHeaderClickListener {
        void onHeaderClick(AdapterView<?> adapterView, View view, long j);
    }

    public interface OnHeaderLongClickListener {
        boolean onHeaderLongClick(AdapterView<?> adapterView, View view, long j);
    }

    private class PerformHeaderClick extends WindowRunnable implements Runnable {
        int mClickMotionPosition;

        private PerformHeaderClick() {
            super();
        }

        public void run() {
            if (!StickyGridHeadersGridView.this.mDataChanged && StickyGridHeadersGridView.this.mAdapter != null && StickyGridHeadersGridView.this.mAdapter.getCount() > 0 && this.mClickMotionPosition != StickyGridHeadersGridView.TOUCH_MODE_REST && this.mClickMotionPosition < StickyGridHeadersGridView.this.mAdapter.getCount() && sameWindow()) {
                View view = StickyGridHeadersGridView.this.getHeaderAt(this.mClickMotionPosition);
                if (view != null) {
                    StickyGridHeadersGridView.this.performHeaderClick(view, StickyGridHeadersGridView.this.headerViewPositionToId(this.mClickMotionPosition));
                }
            }
        }
    }

    class RuntimePlatformSupportException extends RuntimeException {
        private static final long serialVersionUID = -6512098808936536538L;

        public RuntimePlatformSupportException(Exception e) {
            super(StickyGridHeadersGridView.ERROR_PLATFORM, e);
        }
    }

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        boolean areHeadersSticky;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.areHeadersSticky = in.readByte() != (byte) 0;
        }

        public String toString() {
            return "StickyGridHeadersGridView.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " areHeadersSticky=" + this.areHeadersSticky + "}";
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeByte((byte) (this.areHeadersSticky ? StickyGridHeadersGridView.TOUCH_MODE_TAP : StickyGridHeadersGridView.TOUCH_MODE_DOWN));
        }
    }

    private static PointerCoords[] getPointerCoords(MotionEvent e) {
        int n = e.getPointerCount();
        PointerCoords[] r = new PointerCoords[n];
        for (int i = TOUCH_MODE_DOWN; i < n; i += TOUCH_MODE_TAP) {
            r[i] = new PointerCoords();
            e.getPointerCoords(i, r[i]);
        }
        return r;
    }

    private static int[] getPointerIds(MotionEvent e) {
        int n = e.getPointerCount();
        int[] r = new int[n];
        for (int i = TOUCH_MODE_DOWN; i < n; i += TOUCH_MODE_TAP) {
            r[i] = e.getPointerId(i);
        }
        return r;
    }

    public StickyGridHeadersGridView(Context context) {
        this(context, null);
    }

    public StickyGridHeadersGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 16842865);
    }

    public StickyGridHeadersGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mAreHeadersSticky = true;
        this.mClippingRect = new Rect();
        this.mCurrentHeaderId = -1;
        this.mDataSetObserver = new DataSetObserver() {
            public void onChanged() {
                StickyGridHeadersGridView.this.reset();
            }

            public void onInvalidated() {
                StickyGridHeadersGridView.this.reset();
            }
        };
        this.mMaskStickyHeaderRegion = true;
        this.mNumMeasuredColumns = TOUCH_MODE_TAP;
        this.mScrollState = TOUCH_MODE_DOWN;
        this.mHeaderChildBeingPressed = false;
        super.setOnScrollListener(this);
        setVerticalFadingEdgeEnabled(false);
        if (!this.mNumColumnsSet) {
            this.mNumColumns = TOUCH_MODE_REST;
        }
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public boolean areHeadersSticky() {
        return this.mAreHeadersSticky;
    }

    public View getHeaderAt(int position) {
        if (position == TOUCH_MODE_FINISHED_LONG_PRESS) {
            return this.mStickiedHeader;
        }
        try {
            return (View) getChildAt(position).getTag();
        } catch (Exception e) {
            return null;
        }
    }

    public View getStickiedHeader() {
        return this.mStickiedHeader;
    }

    public boolean getStickyHeaderIsTranscluent() {
        return !this.mMaskStickyHeaderRegion;
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.mOnItemClickListener.onItemClick(parent, view, this.mAdapter.translatePosition(position).mPosition, id);
    }

    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return this.mOnItemLongClickListener.onItemLongClick(parent, view, this.mAdapter.translatePosition(position).mPosition, id);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.mOnItemSelectedListener.onItemSelected(parent, view, this.mAdapter.translatePosition(position).mPosition, id);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        this.mOnItemSelectedListener.onNothingSelected(parent);
    }

    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        this.mAreHeadersSticky = ss.areHeadersSticky;
        requestLayout();
    }

    public Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.areHeadersSticky = this.mAreHeadersSticky;
        return ss;
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (this.mScrollListener != null) {
            this.mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
        if (VERSION.SDK_INT >= 8) {
            scrollChanged(firstVisibleItem);
        }
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (this.mScrollListener != null) {
            this.mScrollListener.onScrollStateChanged(view, scrollState);
        }
        this.mScrollState = scrollState;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        View tempHeader;
        int action = ev.getAction();
        boolean wasHeaderChildBeingPressed = this.mHeaderChildBeingPressed;
        if (this.mHeaderChildBeingPressed) {
            View headerHolder;
            tempHeader = getHeaderAt(this.mMotionHeaderPosition);
            if (this.mMotionHeaderPosition == TOUCH_MODE_FINISHED_LONG_PRESS) {
                headerHolder = tempHeader;
            } else {
                headerHolder = getChildAt(this.mMotionHeaderPosition);
            }
            if (action == TOUCH_MODE_TAP || action == 3) {
                this.mHeaderChildBeingPressed = false;
            }
            if (tempHeader != null) {
                tempHeader.dispatchTouchEvent(transformEvent(ev, this.mMotionHeaderPosition));
                tempHeader.invalidate();
                tempHeader.postDelayed(new Runnable() {
                    public void run() {
                        StickyGridHeadersGridView.this.invalidate(StickyGridHeadersGridView.TOUCH_MODE_DOWN, headerHolder.getTop(), StickyGridHeadersGridView.this.getWidth(), headerHolder.getTop() + headerHolder.getHeight());
                    }
                }, (long) ViewConfiguration.getPressedStateDuration());
                invalidate(TOUCH_MODE_DOWN, headerHolder.getTop(), getWidth(), headerHolder.getTop() + headerHolder.getHeight());
            }
        }
        final View header;
        Handler handler;
        switch (action & ICatchLightFrequency.ICH_LIGHT_FREQUENCY_UNDEFINED) {
            case TOUCH_MODE_DOWN /*0*/:
                if (this.mPendingCheckForTap == null) {
                    this.mPendingCheckForTap = new CheckForHeaderTap();
                }
                postDelayed(this.mPendingCheckForTap, (long) ViewConfiguration.getTapTimeout());
                int y = (int) ev.getY();
                this.mMotionY = (float) y;
                this.mMotionHeaderPosition = findMotionHeader((float) y);
                if (!(this.mMotionHeaderPosition == TOUCH_MODE_REST || this.mScrollState == TOUCH_MODE_DONE_WAITING)) {
                    tempHeader = getHeaderAt(this.mMotionHeaderPosition);
                    if (tempHeader != null) {
                        if (tempHeader.dispatchTouchEvent(transformEvent(ev, this.mMotionHeaderPosition))) {
                            this.mHeaderChildBeingPressed = true;
                            tempHeader.setPressed(true);
                        }
                        tempHeader.invalidate();
                        if (this.mMotionHeaderPosition != TOUCH_MODE_FINISHED_LONG_PRESS) {
                            tempHeader = getChildAt(this.mMotionHeaderPosition);
                        }
                        invalidate(TOUCH_MODE_DOWN, tempHeader.getTop(), getWidth(), tempHeader.getTop() + tempHeader.getHeight());
                    }
                    this.mTouchMode = TOUCH_MODE_DOWN;
                    return true;
                }
            case TOUCH_MODE_TAP /*1*/:
                if (this.mTouchMode == TOUCH_MODE_FINISHED_LONG_PRESS) {
                    this.mTouchMode = TOUCH_MODE_REST;
                    return true;
                } else if (!(this.mTouchMode == TOUCH_MODE_REST || this.mMotionHeaderPosition == TOUCH_MODE_REST)) {
                    header = getHeaderAt(this.mMotionHeaderPosition);
                    if (!(wasHeaderChildBeingPressed || header == null)) {
                        if (this.mTouchMode != 0) {
                            header.setPressed(false);
                        }
                        if (this.mPerformHeaderClick == null) {
                            this.mPerformHeaderClick = new PerformHeaderClick();
                        }
                        final PerformHeaderClick performHeaderClick = this.mPerformHeaderClick;
                        performHeaderClick.mClickMotionPosition = this.mMotionHeaderPosition;
                        performHeaderClick.rememberWindowAttachCount();
                        if (this.mTouchMode == 0 || this.mTouchMode == TOUCH_MODE_TAP) {
                            handler = getHandler();
                            if (handler != null) {
                                Runnable runnable;
                                if (this.mTouchMode == 0) {
                                    runnable = this.mPendingCheckForTap;
                                } else {
                                    runnable = this.mPendingCheckForLongPress;
                                }
                                handler.removeCallbacks(runnable);
                            }
                            if (this.mDataChanged) {
                                this.mTouchMode = TOUCH_MODE_REST;
                            } else {
                                this.mTouchMode = TOUCH_MODE_TAP;
                                header.setPressed(true);
                                setPressed(true);
                                if (this.mTouchModeReset != null) {
                                    removeCallbacks(this.mTouchModeReset);
                                }
                                this.mTouchModeReset = new Runnable() {
                                    public void run() {
                                        StickyGridHeadersGridView.this.mMotionHeaderPosition = StickyGridHeadersGridView.TOUCH_MODE_REST;
                                        StickyGridHeadersGridView.this.mTouchModeReset = null;
                                        StickyGridHeadersGridView.this.mTouchMode = StickyGridHeadersGridView.TOUCH_MODE_REST;
                                        header.setPressed(false);
                                        StickyGridHeadersGridView.this.setPressed(false);
                                        header.invalidate();
                                        StickyGridHeadersGridView.this.invalidate(StickyGridHeadersGridView.TOUCH_MODE_DOWN, header.getTop(), StickyGridHeadersGridView.this.getWidth(), header.getHeight());
                                        if (!StickyGridHeadersGridView.this.mDataChanged) {
                                            performHeaderClick.run();
                                        }
                                    }
                                };
                                postDelayed(this.mTouchModeReset, (long) ViewConfiguration.getPressedStateDuration());
                            }
                        } else if (!this.mDataChanged) {
                            performHeaderClick.run();
                        }
                    }
                    this.mTouchMode = TOUCH_MODE_REST;
                    return true;
                }
                break;
            case TOUCH_MODE_DONE_WAITING /*2*/:
                if (this.mMotionHeaderPosition != TOUCH_MODE_REST && Math.abs(ev.getY() - this.mMotionY) > ((float) this.mTouchSlop)) {
                    this.mTouchMode = TOUCH_MODE_REST;
                    header = getHeaderAt(this.mMotionHeaderPosition);
                    if (header != null) {
                        header.setPressed(false);
                        header.invalidate();
                    }
                    handler = getHandler();
                    if (handler != null) {
                        handler.removeCallbacks(this.mPendingCheckForLongPress);
                    }
                    this.mMotionHeaderPosition = TOUCH_MODE_REST;
                    break;
                }
        }
        return super.onTouchEvent(ev);
    }

    public boolean performHeaderClick(View view, long id) {
        if (this.mOnHeaderClickListener == null) {
            return false;
        }
        playSoundEffect(TOUCH_MODE_DOWN);
        if (view != null) {
            view.sendAccessibilityEvent(TOUCH_MODE_TAP);
        }
        this.mOnHeaderClickListener.onHeaderClick(this, view, id);
        return true;
    }

    public boolean performHeaderLongPress(View view, long id) {
        boolean handled = false;
        if (this.mOnHeaderLongClickListener != null) {
            handled = this.mOnHeaderLongClickListener.onHeaderLongClick(this, view, id);
        }
        if (handled) {
            if (view != null) {
                view.sendAccessibilityEvent(TOUCH_MODE_DONE_WAITING);
            }
            performHapticFeedback(TOUCH_MODE_DOWN);
        }
        return handled;
    }

    public void setAdapter(ListAdapter adapter) {
        StickyGridHeadersBaseAdapter baseAdapter;
        if (!(this.mAdapter == null || this.mDataSetObserver == null)) {
            this.mAdapter.unregisterDataSetObserver(this.mDataSetObserver);
        }
        if (!this.mClipToPaddingHasBeenSet) {
            this.mClippingToPadding = true;
        }
        if (adapter instanceof StickyGridHeadersBaseAdapter) {
            baseAdapter = (StickyGridHeadersBaseAdapter) adapter;
        } else if (adapter instanceof StickyGridHeadersSimpleAdapter) {
            baseAdapter = new StickyGridHeadersSimpleAdapterWrapper((StickyGridHeadersSimpleAdapter) adapter);
        } else {
            baseAdapter = new StickyGridHeadersListAdapterWrapper(adapter);
        }
        this.mAdapter = new StickyGridHeadersBaseAdapterWrapper(getContext(), this, baseAdapter);
        this.mAdapter.registerDataSetObserver(this.mDataSetObserver);
        reset();
        super.setAdapter(this.mAdapter);
    }

    public void setAreHeadersSticky(boolean useStickyHeaders) {
        if (useStickyHeaders != this.mAreHeadersSticky) {
            this.mAreHeadersSticky = useStickyHeaders;
            requestLayout();
        }
    }

    public void setClipToPadding(boolean clipToPadding) {
        super.setClipToPadding(clipToPadding);
        this.mClippingToPadding = clipToPadding;
        this.mClipToPaddingHasBeenSet = true;
    }

    public void setColumnWidth(int columnWidth) {
        super.setColumnWidth(columnWidth);
        this.mColumnWidth = columnWidth;
    }

    public void setHeadersIgnorePadding(boolean b) {
        this.mHeadersIgnorePadding = b;
    }

    public void setHorizontalSpacing(int horizontalSpacing) {
        super.setHorizontalSpacing(horizontalSpacing);
        this.mHorizontalSpacing = horizontalSpacing;
    }

    public void setNumColumns(int numColumns) {
        super.setNumColumns(numColumns);
        this.mNumColumnsSet = true;
        this.mNumColumns = numColumns;
        if (numColumns != TOUCH_MODE_REST && this.mAdapter != null) {
            this.mAdapter.setNumColumns(numColumns);
        }
    }

    public void setOnHeaderClickListener(OnHeaderClickListener listener) {
        this.mOnHeaderClickListener = listener;
    }

    public void setOnHeaderLongClickListener(OnHeaderLongClickListener listener) {
        if (!isLongClickable()) {
            setLongClickable(true);
        }
        this.mOnHeaderLongClickListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
        super.setOnItemClickListener(this);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
        super.setOnItemLongClickListener(this);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.mOnItemSelectedListener = listener;
        super.setOnItemSelectedListener(this);
    }

    public void setOnScrollListener(OnScrollListener listener) {
        this.mScrollListener = listener;
    }

    public void setStickyHeaderIsTranscluent(boolean isTranscluent) {
        this.mMaskStickyHeaderRegion = !isTranscluent;
    }

    public void setVerticalSpacing(int verticalSpacing) {
        super.setVerticalSpacing(verticalSpacing);
        this.mVerticalSpacing = verticalSpacing;
    }

    private int findMotionHeader(float y) {
        if (this.mStickiedHeader != null && y <= ((float) this.mHeaderBottomPosition)) {
            return TOUCH_MODE_FINISHED_LONG_PRESS;
        }
        int vi = TOUCH_MODE_DOWN;
        int i = getFirstVisiblePosition();
        while (i <= getLastVisiblePosition()) {
            if (getItemIdAtPosition(i) == -1) {
                View headerWrapper = getChildAt(vi);
                int bottom = headerWrapper.getBottom();
                int top = headerWrapper.getTop();
                if (y <= ((float) bottom) && y >= ((float) top)) {
                    return vi;
                }
            }
            i += this.mNumMeasuredColumns;
            vi += this.mNumMeasuredColumns;
        }
        return TOUCH_MODE_REST;
    }

    private int getHeaderHeight() {
        if (this.mStickiedHeader != null) {
            return this.mStickiedHeader.getMeasuredHeight();
        }
        return TOUCH_MODE_DOWN;
    }

    private long headerViewPositionToId(int pos) {
        if (pos == TOUCH_MODE_FINISHED_LONG_PRESS) {
            return this.mCurrentHeaderId;
        }
        return this.mAdapter.getHeaderId(getFirstVisiblePosition() + pos);
    }

    private void measureHeader() {
        if (this.mStickiedHeader != null) {
            int widthMeasureSpec;
            int heightMeasureSpec;
            if (this.mHeadersIgnorePadding) {
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(getWidth(), 1073741824);
            } else {
                widthMeasureSpec = MeasureSpec.makeMeasureSpec((getWidth() - getPaddingLeft()) - getPaddingRight(), 1073741824);
            }
            LayoutParams params = this.mStickiedHeader.getLayoutParams();
            if (params == null || params.height <= 0) {
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(TOUCH_MODE_DOWN, TOUCH_MODE_DOWN);
            } else {
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(params.height, 1073741824);
            }
            this.mStickiedHeader.measure(MeasureSpec.makeMeasureSpec(TOUCH_MODE_DOWN, TOUCH_MODE_DOWN), MeasureSpec.makeMeasureSpec(TOUCH_MODE_DOWN, TOUCH_MODE_DOWN));
            this.mStickiedHeader.measure(widthMeasureSpec, heightMeasureSpec);
            if (this.mHeadersIgnorePadding) {
                this.mStickiedHeader.layout(getLeft(), TOUCH_MODE_DOWN, getRight(), this.mStickiedHeader.getMeasuredHeight());
            } else {
                this.mStickiedHeader.layout(getLeft() + getPaddingLeft(), TOUCH_MODE_DOWN, getRight() - getPaddingRight(), this.mStickiedHeader.getMeasuredHeight());
            }
        }
    }

    private void reset() {
        this.mHeaderBottomPosition = TOUCH_MODE_DOWN;
        swapStickiedHeader(null);
        this.mCurrentHeaderId = Long.MIN_VALUE;
    }

    private void scrollChanged(int firstVisibleItem) {
        if (this.mAdapter != null && this.mAdapter.getCount() != 0 && this.mAreHeadersSticky && getChildAt(TOUCH_MODE_DOWN) != null) {
            long newHeaderId;
            int selectedHeaderPosition = firstVisibleItem;
            int beforeRowPosition = firstVisibleItem - this.mNumMeasuredColumns;
            if (beforeRowPosition < 0) {
                beforeRowPosition = firstVisibleItem;
            }
            int secondRowPosition = firstVisibleItem + this.mNumMeasuredColumns;
            if (secondRowPosition >= this.mAdapter.getCount()) {
                secondRowPosition = firstVisibleItem;
            }
            if (this.mVerticalSpacing == 0) {
                newHeaderId = this.mAdapter.getHeaderId(firstVisibleItem);
            } else if (this.mVerticalSpacing < 0) {
                newHeaderId = this.mAdapter.getHeaderId(firstVisibleItem);
                if (getChildAt(this.mNumMeasuredColumns).getTop() <= 0) {
                    newHeaderId = this.mAdapter.getHeaderId(secondRowPosition);
                    selectedHeaderPosition = secondRowPosition;
                } else {
                    newHeaderId = this.mAdapter.getHeaderId(firstVisibleItem);
                }
            } else {
                int margin = getChildAt(TOUCH_MODE_DOWN).getTop();
                if (margin <= 0 || margin >= this.mVerticalSpacing) {
                    newHeaderId = this.mAdapter.getHeaderId(firstVisibleItem);
                } else {
                    newHeaderId = this.mAdapter.getHeaderId(beforeRowPosition);
                    selectedHeaderPosition = beforeRowPosition;
                }
            }
            if (this.mCurrentHeaderId != newHeaderId) {
                swapStickiedHeader(this.mAdapter.getHeaderView(selectedHeaderPosition, this.mStickiedHeader, this));
                measureHeader();
                this.mCurrentHeaderId = newHeaderId;
            }
            int childCount = getChildCount();
            if (childCount != 0) {
                View viewToWatch = null;
                int watchingChildDistance = 99999;
                int i = TOUCH_MODE_DOWN;
                while (i < childCount) {
                    int childDistance;
                    View child = super.getChildAt(i);
                    if (this.mClippingToPadding) {
                        childDistance = child.getTop() - getPaddingTop();
                    } else {
                        childDistance = child.getTop();
                    }
                    if (childDistance >= 0 && this.mAdapter.getItemId(getPositionForView(child)) == -1 && childDistance < watchingChildDistance) {
                        viewToWatch = child;
                        watchingChildDistance = childDistance;
                    }
                    i += this.mNumMeasuredColumns;
                }
                int headerHeight = getHeaderHeight();
                if (viewToWatch == null) {
                    this.mHeaderBottomPosition = headerHeight;
                    if (this.mClippingToPadding) {
                        this.mHeaderBottomPosition += getPaddingTop();
                    }
                } else if (firstVisibleItem == 0 && super.getChildAt(TOUCH_MODE_DOWN).getTop() > 0 && !this.mClippingToPadding) {
                    this.mHeaderBottomPosition = TOUCH_MODE_DOWN;
                } else if (this.mClippingToPadding) {
                    int paddingTop;
                    this.mHeaderBottomPosition = Math.min(viewToWatch.getTop(), getPaddingTop() + headerHeight);
                    if (this.mHeaderBottomPosition < getPaddingTop()) {
                        paddingTop = getPaddingTop() + headerHeight;
                    } else {
                        paddingTop = this.mHeaderBottomPosition;
                    }
                    this.mHeaderBottomPosition = paddingTop;
                } else {
                    this.mHeaderBottomPosition = Math.min(viewToWatch.getTop(), headerHeight);
                    if (this.mHeaderBottomPosition >= 0) {
                        headerHeight = this.mHeaderBottomPosition;
                    }
                    this.mHeaderBottomPosition = headerHeight;
                }
            }
        }
    }

    private void swapStickiedHeader(View newStickiedHeader) {
        detachHeader(this.mStickiedHeader);
        attachHeader(newStickiedHeader);
        this.mStickiedHeader = newStickiedHeader;
    }

    private MotionEvent transformEvent(MotionEvent e, int headerPosition) {
        if (headerPosition == TOUCH_MODE_FINISHED_LONG_PRESS) {
            return e;
        }
        long downTime = e.getDownTime();
        long eventTime = e.getEventTime();
        int action = e.getAction();
        int pointerCount = e.getPointerCount();
        int[] pointerIds = getPointerIds(e);
        PointerCoords[] pointerCoords = getPointerCoords(e);
        int metaState = e.getMetaState();
        float xPrecision = e.getXPrecision();
        float yPrecision = e.getYPrecision();
        int deviceId = e.getDeviceId();
        int edgeFlags = e.getEdgeFlags();
        int source = e.getSource();
        int flags = e.getFlags();
        View headerHolder = getChildAt(headerPosition);
        for (int i = TOUCH_MODE_DOWN; i < pointerCount; i += TOUCH_MODE_TAP) {
            PointerCoords pointerCoords2 = pointerCoords[i];
            pointerCoords2.y -= (float) headerHolder.getTop();
        }
        return MotionEvent.obtain(downTime, eventTime, action, pointerCount, pointerIds, pointerCoords, metaState, xPrecision, yPrecision, deviceId, edgeFlags, source, flags);
    }

    protected void dispatchDraw(Canvas canvas) {
        int widthMeasureSpec;
        int wantedWidth;
        if (VERSION.SDK_INT < 8) {
            scrollChanged(getFirstVisiblePosition());
        }
        boolean drawStickiedHeader = this.mStickiedHeader != null && this.mAreHeadersSticky && this.mStickiedHeader.getVisibility() == 0;
        int headerHeight = getHeaderHeight();
        int top = this.mHeaderBottomPosition - headerHeight;
        if (drawStickiedHeader && this.mMaskStickyHeaderRegion) {
            if (this.mHeadersIgnorePadding) {
                this.mClippingRect.left = TOUCH_MODE_DOWN;
                this.mClippingRect.right = getWidth();
            } else {
                this.mClippingRect.left = getPaddingLeft();
                this.mClippingRect.right = getWidth() - getPaddingRight();
            }
            this.mClippingRect.top = this.mHeaderBottomPosition;
            this.mClippingRect.bottom = getHeight();
            canvas.save();
            canvas.clipRect(this.mClippingRect);
        }
        super.dispatchDraw(canvas);
        List<Integer> headerPositions = new ArrayList();
        int vi = TOUCH_MODE_DOWN;
        int i = getFirstVisiblePosition();
        while (i <= getLastVisiblePosition()) {
            if (getItemIdAtPosition(i) == -1) {
                headerPositions.add(Integer.valueOf(vi));
            }
            i += this.mNumMeasuredColumns;
            vi += this.mNumMeasuredColumns;
        }
        i = TOUCH_MODE_DOWN;
        while (i < headerPositions.size()) {
            View frame = getChildAt(((Integer) headerPositions.get(i)).intValue());
            try {
                View header = (View) frame.getTag();
                boolean headerIsStickied = ((long) ((HeaderFillerView) frame).getHeaderId()) == this.mCurrentHeaderId && frame.getTop() < 0 && this.mAreHeadersSticky;
                if (header.getVisibility() == 0 && !headerIsStickied) {
                    if (this.mHeadersIgnorePadding) {
                        widthMeasureSpec = MeasureSpec.makeMeasureSpec(getWidth(), 1073741824);
                    } else {
                        widthMeasureSpec = MeasureSpec.makeMeasureSpec((getWidth() - getPaddingLeft()) - getPaddingRight(), 1073741824);
                    }
                    int heightMeasureSpec = MeasureSpec.makeMeasureSpec(TOUCH_MODE_DOWN, TOUCH_MODE_DOWN);
                    header.measure(MeasureSpec.makeMeasureSpec(TOUCH_MODE_DOWN, TOUCH_MODE_DOWN), MeasureSpec.makeMeasureSpec(TOUCH_MODE_DOWN, TOUCH_MODE_DOWN));
                    header.measure(widthMeasureSpec, heightMeasureSpec);
                    if (this.mHeadersIgnorePadding) {
                        header.layout(getLeft(), TOUCH_MODE_DOWN, getRight(), frame.getHeight());
                    } else {
                        header.layout(getLeft() + getPaddingLeft(), TOUCH_MODE_DOWN, getRight() - getPaddingRight(), frame.getHeight());
                    }
                    if (this.mHeadersIgnorePadding) {
                        this.mClippingRect.left = TOUCH_MODE_DOWN;
                        this.mClippingRect.right = getWidth();
                    } else {
                        this.mClippingRect.left = getPaddingLeft();
                        this.mClippingRect.right = getWidth() - getPaddingRight();
                    }
                    this.mClippingRect.bottom = frame.getBottom();
                    this.mClippingRect.top = frame.getTop();
                    canvas.save();
                    canvas.clipRect(this.mClippingRect);
                    if (this.mHeadersIgnorePadding) {
                        canvas.translate(0.0f, (float) frame.getTop());
                    } else {
                        canvas.translate((float) getPaddingLeft(), (float) frame.getTop());
                    }
                    header.draw(canvas);
                    canvas.restore();
                }
                i += TOUCH_MODE_TAP;
            } catch (Exception e) {
                return;
            }
        }
        if (drawStickiedHeader && this.mMaskStickyHeaderRegion) {
            canvas.restore();
        } else if (!drawStickiedHeader) {
            return;
        }
        if (this.mHeadersIgnorePadding) {
            wantedWidth = getWidth();
        } else {
            wantedWidth = (getWidth() - getPaddingLeft()) - getPaddingRight();
        }
        if (this.mStickiedHeader.getWidth() != wantedWidth) {
            if (this.mHeadersIgnorePadding) {
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(getWidth(), 1073741824);
            } else {
                widthMeasureSpec = MeasureSpec.makeMeasureSpec((getWidth() - getPaddingLeft()) - getPaddingRight(), 1073741824);
            }
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(TOUCH_MODE_DOWN, TOUCH_MODE_DOWN);
            this.mStickiedHeader.measure(MeasureSpec.makeMeasureSpec(TOUCH_MODE_DOWN, TOUCH_MODE_DOWN), MeasureSpec.makeMeasureSpec(TOUCH_MODE_DOWN, TOUCH_MODE_DOWN));
            this.mStickiedHeader.measure(widthMeasureSpec, heightMeasureSpec);
            if (this.mHeadersIgnorePadding) {
                this.mStickiedHeader.layout(getLeft(), TOUCH_MODE_DOWN, getRight(), this.mStickiedHeader.getHeight());
            } else {
                this.mStickiedHeader.layout(getLeft() + getPaddingLeft(), TOUCH_MODE_DOWN, getRight() - getPaddingRight(), this.mStickiedHeader.getHeight());
            }
        }
        if (this.mHeadersIgnorePadding) {
            this.mClippingRect.left = TOUCH_MODE_DOWN;
            this.mClippingRect.right = getWidth();
        } else {
            this.mClippingRect.left = getPaddingLeft();
            this.mClippingRect.right = getWidth() - getPaddingRight();
        }
        this.mClippingRect.bottom = top + headerHeight;
        if (this.mClippingToPadding) {
            this.mClippingRect.top = getPaddingTop();
        } else {
            this.mClippingRect.top = TOUCH_MODE_DOWN;
        }
        canvas.save();
        canvas.clipRect(this.mClippingRect);
        if (this.mHeadersIgnorePadding) {
            canvas.translate(0.0f, (float) top);
        } else {
            canvas.translate((float) getPaddingLeft(), (float) top);
        }
        if (this.mHeaderBottomPosition != headerHeight) {
            canvas.saveLayerAlpha(0.0f, 0.0f, (float) canvas.getWidth(), (float) canvas.getHeight(), (this.mHeaderBottomPosition * ICatchLightFrequency.ICH_LIGHT_FREQUENCY_UNDEFINED) / headerHeight, 31);
        }
        this.mStickiedHeader.draw(canvas);
        if (this.mHeaderBottomPosition != headerHeight) {
            canvas.restore();
        }
        canvas.restore();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mNumColumns == TOUCH_MODE_REST) {
            int numFittedColumns;
            if (this.mColumnWidth > 0) {
                int gridWidth = Math.max((MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()) - getPaddingRight(), TOUCH_MODE_DOWN);
                numFittedColumns = gridWidth / this.mColumnWidth;
                if (numFittedColumns > 0) {
                    while (numFittedColumns != TOUCH_MODE_TAP && (this.mColumnWidth * numFittedColumns) + ((numFittedColumns + TOUCH_MODE_REST) * this.mHorizontalSpacing) > gridWidth) {
                        numFittedColumns += TOUCH_MODE_REST;
                    }
                } else {
                    numFittedColumns = TOUCH_MODE_TAP;
                }
            } else {
                numFittedColumns = TOUCH_MODE_DONE_WAITING;
            }
            this.mNumMeasuredColumns = numFittedColumns;
        } else {
            this.mNumMeasuredColumns = this.mNumColumns;
        }
        if (this.mAdapter != null) {
            this.mAdapter.setNumColumns(this.mNumMeasuredColumns);
        }
        measureHeader();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    void attachHeader(View header) {
        if (header != null) {
            try {
                Field attachInfoField = View.class.getDeclaredField("mAttachInfo");
                attachInfoField.setAccessible(true);
                Class[] clsArr = new Class[TOUCH_MODE_DONE_WAITING];
                clsArr[TOUCH_MODE_DOWN] = Class.forName("android.view.View$AttachInfo");
                clsArr[TOUCH_MODE_TAP] = Integer.TYPE;
                Method method = View.class.getDeclaredMethod("dispatchAttachedToWindow", clsArr);
                method.setAccessible(true);
                Object[] objArr = new Object[TOUCH_MODE_DONE_WAITING];
                objArr[TOUCH_MODE_DOWN] = attachInfoField.get(this);
                objArr[TOUCH_MODE_TAP] = Integer.valueOf(8);
                method.invoke(header, objArr);
            } catch (NoSuchMethodException e) {
                throw new RuntimePlatformSupportException(e);
            } catch (ClassNotFoundException e2) {
                throw new RuntimePlatformSupportException(e2);
            } catch (IllegalArgumentException e3) {
                throw new RuntimePlatformSupportException(e3);
            } catch (IllegalAccessException e4) {
                throw new RuntimePlatformSupportException(e4);
            } catch (InvocationTargetException e5) {
                throw new RuntimePlatformSupportException(e5);
            } catch (NoSuchFieldException e6) {
                throw new RuntimePlatformSupportException(e6);
            }
        }
    }

    void detachHeader(View header) {
        if (header != null) {
            try {
                Method method = View.class.getDeclaredMethod("dispatchDetachedFromWindow", new Class[TOUCH_MODE_DOWN]);
                method.setAccessible(true);
                method.invoke(header, new Object[TOUCH_MODE_DOWN]);
            } catch (NoSuchMethodException e) {
                throw new RuntimePlatformSupportException(e);
            } catch (IllegalArgumentException e2) {
                throw new RuntimePlatformSupportException(e2);
            } catch (IllegalAccessException e3) {
                throw new RuntimePlatformSupportException(e3);
            } catch (InvocationTargetException e4) {
                throw new RuntimePlatformSupportException(e4);
            }
        }
    }
}
