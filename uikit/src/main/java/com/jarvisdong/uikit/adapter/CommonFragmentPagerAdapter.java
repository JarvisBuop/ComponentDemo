package com.jarvisdong.uikit.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class CommonFragmentPagerAdapter<K extends Fragment> extends FragmentStatePagerAdapter {
    private List<K> fragments;
    private List<String> titles;
    private K mCurrentFragment;

    public CommonFragmentPagerAdapter(FragmentManager fm, List<K> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public CommonFragmentPagerAdapter(FragmentManager fm, List<K> fragments, List<String> titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null && !titles.isEmpty())
            return titles.get(position);
        return super.getPageTitle(position);
    }

    public void setParams(List<String> titles, List<K> fragments) {
        if (titles != null)
            this.titles = titles;
        if (fragments != null)
            this.fragments = fragments;
        notifyDataSetChanged();
    }

    public void setTitles(Context mContext, List<Integer> titlesRes) {
        titles = new ArrayList<>();
        for (int i = 0; i < titlesRes.size(); i++) {
            titles.add(mContext.getResources().getString(titlesRes.get(i)));
        }
        notifyDataSetChanged();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentFragment = (K) object;
        super.setPrimaryItem(container, position, object);
    }

    public K getCurrentFragment() {
        return mCurrentFragment;
    }

}
