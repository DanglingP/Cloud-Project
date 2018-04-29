package com.el.cloudproject;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Omar Sheikh on 4/29/2018.
 */
public class FragmentPagerAdapterClassRooms extends FragmentStatePagerAdapter {
    final int PAGE_COUNT = 2;
    Context context;
    Activity activity;
    private String tabTitles[] = new String[]{"Available Rooms","Booked Rooms"};
    public FragmentPagerAdapterClassRooms(FragmentManager fm, Context context, Activity activity) {
        super(fm);
        this.context = context;
        this.activity = activity;
    }
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
    @Override
    public Fragment getItem(int position) {
        if(position == 0)
            return new FragmentAvailableRooms();
        else if(position == 1)
            return new FragmentBookedRooms();
        else
            return null;

    }
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}