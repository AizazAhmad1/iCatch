package com.tonicartos.widget.stickygridheaders;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

public class StickyGridHeadersBaseAdapterWrapper extends BaseAdapter {
    protected static final int ID_FILLER = -2;
    protected static final int ID_HEADER = -1;
    protected static final int ID_HEADER_FILLER = -3;
    protected static final int POSITION_FILLER = -1;
    protected static final int POSITION_HEADER = -2;
    protected static final int POSITION_HEADER_FILLER = -3;
    protected static final int VIEW_TYPE_FILLER = 0;
    protected static final int VIEW_TYPE_HEADER = 1;
    protected static final int VIEW_TYPE_HEADER_FILLER = 2;
    private static final int sNumViewTypes = 3;
    private final Context mContext;
    private int mCount;
    private boolean mCounted = false;
    private DataSetObserver mDataSetObserver = new DataSetObserver() {
        public void onChanged() {
            StickyGridHeadersBaseAdapterWrapper.this.updateCount();
        }

        public void onInvalidated() {
            StickyGridHeadersBaseAdapterWrapper.this.mCounted = false;
        }
    };
    private final StickyGridHeadersBaseAdapter mDelegate;
    private StickyGridHeadersGridView mGridView;
    private View mLastHeaderViewSeen;
    private View mLastViewSeen;
    private int mNumColumns = VIEW_TYPE_HEADER;

    protected class FillerView extends View {
        private View mMeasureTarget;

        public FillerView(Context context) {
            super(context);
        }

        public FillerView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public FillerView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        public void setMeasureTarget(View lastViewSeen) {
            this.mMeasureTarget = lastViewSeen;
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(this.mMeasureTarget.getMeasuredHeight(), 1073741824));
        }
    }

    protected class HeaderFillerView extends FrameLayout {
        private int mHeaderId;

        public HeaderFillerView(Context context) {
            super(context);
        }

        public HeaderFillerView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public HeaderFillerView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        public int getHeaderId() {
            return this.mHeaderId;
        }

        public void setHeaderId(int headerId) {
            this.mHeaderId = headerId;
        }

        protected LayoutParams generateDefaultLayoutParams() {
            return new LayoutParams(StickyGridHeadersBaseAdapterWrapper.POSITION_FILLER, StickyGridHeadersBaseAdapterWrapper.POSITION_FILLER);
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            View v = (View) getTag();
            ViewGroup.LayoutParams params = v.getLayoutParams();
            if (params == null) {
                params = generateDefaultLayoutParams();
                v.setLayoutParams(params);
            }
            if (v.getVisibility() != 8) {
                v.measure(getChildMeasureSpec(MeasureSpec.makeMeasureSpec(StickyGridHeadersBaseAdapterWrapper.this.mGridView.getWidth(), 1073741824), StickyGridHeadersBaseAdapterWrapper.VIEW_TYPE_FILLER, params.width), getChildMeasureSpec(MeasureSpec.makeMeasureSpec(StickyGridHeadersBaseAdapterWrapper.VIEW_TYPE_FILLER, StickyGridHeadersBaseAdapterWrapper.VIEW_TYPE_FILLER), StickyGridHeadersBaseAdapterWrapper.VIEW_TYPE_FILLER, params.height));
            }
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), v.getMeasuredHeight());
        }
    }

    protected class HeaderHolder {
        protected View mHeaderView;

        protected HeaderHolder() {
        }
    }

    protected class Position {
        protected int mHeader;
        protected int mPosition;

        protected Position(int position, int header) {
            this.mPosition = position;
            this.mHeader = header;
        }
    }

    public StickyGridHeadersBaseAdapterWrapper(Context context, StickyGridHeadersGridView gridView, StickyGridHeadersBaseAdapter delegate) {
        this.mContext = context;
        this.mDelegate = delegate;
        this.mGridView = gridView;
        delegate.registerDataSetObserver(this.mDataSetObserver);
    }

    public boolean areAllItemsEnabled() {
        return false;
    }

    public int getCount() {
        if (this.mCounted) {
            return this.mCount;
        }
        this.mCount = VIEW_TYPE_FILLER;
        int numHeaders = this.mDelegate.getNumHeaders();
        if (numHeaders == 0) {
            this.mCount = this.mDelegate.getCount();
            this.mCounted = true;
            return this.mCount;
        }
        for (int i = VIEW_TYPE_FILLER; i < numHeaders; i += VIEW_TYPE_HEADER) {
            this.mCount += (this.mDelegate.getCountForHeader(i) + unFilledSpacesInHeaderGroup(i)) + this.mNumColumns;
        }
        this.mCounted = true;
        return this.mCount;
    }

    public Object getItem(int position) throws ArrayIndexOutOfBoundsException {
        Position adapterPosition = translatePosition(position);
        if (adapterPosition.mPosition == POSITION_FILLER || adapterPosition.mPosition == POSITION_HEADER) {
            return null;
        }
        return this.mDelegate.getItem(adapterPosition.mPosition);
    }

    public long getItemId(int position) {
        Position adapterPosition = translatePosition(position);
        if (adapterPosition.mPosition == POSITION_HEADER) {
            return -1;
        }
        if (adapterPosition.mPosition == POSITION_FILLER) {
            return -2;
        }
        if (adapterPosition.mPosition == POSITION_HEADER_FILLER) {
            return -3;
        }
        return this.mDelegate.getItemId(adapterPosition.mPosition);
    }

    public int getItemViewType(int position) {
        Position adapterPosition = translatePosition(position);
        if (adapterPosition.mPosition == POSITION_HEADER) {
            return VIEW_TYPE_HEADER;
        }
        if (adapterPosition.mPosition == POSITION_FILLER) {
            return VIEW_TYPE_FILLER;
        }
        if (adapterPosition.mPosition == POSITION_HEADER_FILLER) {
            return VIEW_TYPE_HEADER_FILLER;
        }
        int itemViewType = this.mDelegate.getItemViewType(adapterPosition.mPosition);
        return itemViewType != POSITION_FILLER ? itemViewType + sNumViewTypes : itemViewType;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Position adapterPosition = translatePosition(position);
        if (adapterPosition.mPosition == POSITION_HEADER) {
            View v = getHeaderFillerView(adapterPosition.mHeader, convertView, parent);
            View view = this.mDelegate.getHeaderView(adapterPosition.mHeader, (View) v.getTag(), parent);
            this.mGridView.detachHeader((View) v.getTag());
            v.setTag(view);
            this.mGridView.attachHeader(view);
            convertView = v;
            this.mLastHeaderViewSeen = v;
            v.forceLayout();
            return convertView;
        } else if (adapterPosition.mPosition == POSITION_HEADER_FILLER) {
            convertView = getFillerView(convertView, parent, this.mLastHeaderViewSeen);
            convertView.forceLayout();
            return convertView;
        } else if (adapterPosition.mPosition == POSITION_FILLER) {
            return getFillerView(convertView, parent, this.mLastViewSeen);
        } else {
            convertView = this.mDelegate.getView(adapterPosition.mPosition, convertView, parent);
            this.mLastViewSeen = convertView;
            return convertView;
        }
    }

    public int getViewTypeCount() {
        return this.mDelegate.getViewTypeCount() + sNumViewTypes;
    }

    public StickyGridHeadersBaseAdapter getWrappedAdapter() {
        return this.mDelegate;
    }

    public boolean hasStableIds() {
        return this.mDelegate.hasStableIds();
    }

    public boolean isEmpty() {
        return this.mDelegate.isEmpty();
    }

    public boolean isEnabled(int position) {
        Position adapterPosition = translatePosition(position);
        if (adapterPosition.mPosition == POSITION_FILLER || adapterPosition.mPosition == POSITION_HEADER) {
            return false;
        }
        return this.mDelegate.isEnabled(adapterPosition.mPosition);
    }

    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
        this.mDelegate.registerDataSetObserver(observer);
    }

    public void setNumColumns(int numColumns) {
        this.mNumColumns = numColumns;
        this.mCounted = false;
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        super.unregisterDataSetObserver(observer);
        this.mDelegate.unregisterDataSetObserver(observer);
    }

    private FillerView getFillerView(View convertView, ViewGroup parent, View lastViewSeen) {
        FillerView fillerView = (FillerView) convertView;
        if (fillerView == null) {
            fillerView = new FillerView(this.mContext);
        }
        fillerView.setMeasureTarget(lastViewSeen);
        return fillerView;
    }

    private HeaderFillerView getHeaderFillerView(int headerPosition, View convertView, ViewGroup parent) {
        HeaderFillerView headerFillerView = (HeaderFillerView) convertView;
        if (headerFillerView == null) {
            return new HeaderFillerView(this.mContext);
        }
        return headerFillerView;
    }

    private int unFilledSpacesInHeaderGroup(int header) {
        if (this.mNumColumns == 0) {
            return VIEW_TYPE_FILLER;
        }
        int remainder = this.mDelegate.getCountForHeader(header) % this.mNumColumns;
        if (remainder != 0) {
            return this.mNumColumns - remainder;
        }
        return VIEW_TYPE_FILLER;
    }

    protected long getHeaderId(int position) {
        return (long) translatePosition(position).mHeader;
    }

    protected View getHeaderView(int position, View convertView, ViewGroup parent) {
        if (this.mDelegate.getNumHeaders() == 0) {
            return null;
        }
        return this.mDelegate.getHeaderView(translatePosition(position).mHeader, convertView, parent);
    }

    protected Position translatePosition(int position) {
        int numHeaders = this.mDelegate.getNumHeaders();
        if (numHeaders != 0) {
            int adapterPosition = position;
            int place = position;
            int i = VIEW_TYPE_FILLER;
            while (i < numHeaders) {
                int sectionCount = this.mDelegate.getCountForHeader(i);
                if (place == 0) {
                    return new Position(POSITION_HEADER, i);
                }
                place -= this.mNumColumns;
                if (place < 0) {
                    return new Position(POSITION_HEADER_FILLER, i);
                }
                adapterPosition -= this.mNumColumns;
                if (place < sectionCount) {
                    return new Position(adapterPosition, i);
                }
                int filler = unFilledSpacesInHeaderGroup(i);
                adapterPosition -= filler;
                place -= sectionCount + filler;
                if (place < 0) {
                    return new Position(POSITION_FILLER, i);
                }
                i += VIEW_TYPE_HEADER;
            }
            return new Position(POSITION_FILLER, i);
        } else if (position >= this.mDelegate.getCount()) {
            return new Position(POSITION_FILLER, VIEW_TYPE_FILLER);
        } else {
            return new Position(position, VIEW_TYPE_FILLER);
        }
    }

    protected void updateCount() {
        this.mCount = VIEW_TYPE_FILLER;
        int numHeaders = this.mDelegate.getNumHeaders();
        if (numHeaders == 0) {
            this.mCount = this.mDelegate.getCount();
            this.mCounted = true;
            return;
        }
        for (int i = VIEW_TYPE_FILLER; i < numHeaders; i += VIEW_TYPE_HEADER) {
            this.mCount += this.mDelegate.getCountForHeader(i) + this.mNumColumns;
        }
        this.mCounted = true;
    }
}
