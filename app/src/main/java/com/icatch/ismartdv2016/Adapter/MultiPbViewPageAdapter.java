package com.icatch.ismartdv2016.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;

public class MultiPbViewPageAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> list;

    public MultiPbViewPageAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        this.list = list;
    }

    public Fragment getItem(int position) {
        return (Fragment) this.list.get(position);
    }

    public int getCount() {
        return this.list.size();
    }
}
