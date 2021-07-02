package com.example.tmdb_hw9.ui.home.tabs;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.tmdb_hw9.ui.home.tabs.TVFragment;
import com.example.tmdb_hw9.ui.home.tabs.MovieFragment;


public class TabAdapter extends FragmentPagerAdapter {
    int numTabs;
    Context context;
    //FragmentPagerAdapter fm;

    private final String[] tabTitles = new String[]{"Movies", "TV Shows"};


    public TabAdapter(Context context, FragmentManager fm, int numTabs) {
        super(fm);
        this.numTabs = numTabs;
        this.context = context;
    }

    // overriding getPageTitle()
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }


    @Override
    public int getCount() {
        return numTabs;
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new MovieFragment();
            case 1:
                return new TVFragment();
            default:
                return null;
        }
    }
}
