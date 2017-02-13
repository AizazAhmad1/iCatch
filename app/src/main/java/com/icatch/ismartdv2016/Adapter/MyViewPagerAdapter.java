package com.icatch.ismartdv2016.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.icatch.ismartdv2016.R;
import java.util.ArrayList;

public class MyViewPagerAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<View> viewList;

    public MyViewPagerAdapter(Context context, ArrayList<View> viewList) {
        this.viewList = viewList;
        this.context = context;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        if (position < this.viewList.size()) {
            container.removeView((View) this.viewList.get(position));
            this.viewList.set(position, null);
        }
    }

    public Object instantiateItem(ViewGroup container, int position) {
        View v = View.inflate(this.context, R.layout.pb_photo_item, null);
        this.viewList.set(position, v);
        container.addView(v, 0);
        return v;
    }

    public int getCount() {
        return this.viewList.size();
    }

    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
}
