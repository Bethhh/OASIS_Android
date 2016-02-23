package com.bethyueshi.oasis2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.IconPagerAdapter;

/**
 * Created by bethyueshi on 2/23/16.
 */
public class MapPagerAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
    public static final int NUM_OF_MAPS = 4;

    protected static final int[] ICONS = new int[] {
            R.drawable.taste_tile,
            R.drawable.mercury_tile,
            R.drawable.smell_tile,
            R.drawable.ph_tile
    };

    public MapPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int pos) {
        Fragment fragment;
        Bundle args = new Bundle();
        switch(pos) {
            case 1:
                fragment = new MapFragment();
                args.putString("url", "http://leafletjs.com/examples/mobile-example.html");
                break;
            case 2:
                fragment = new MapFragment();
                args.putString("url", "http://leafletjs.com/examples/mobile-example.html");
                break;
            case 3:
                fragment = new MapFragment();
                args.putString("url", "http://leafletjs.com/examples/mobile-example.html");
                break;
            default:
                fragment = new MapFragment();
                args.putString("url", "http://leafletjs.com/examples/mobile-example.html");
                break;
        }
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getCount() {
        return NUM_OF_MAPS;
    }

    @Override
    public int getIconResId(int index) {
        return ICONS[index % ICONS.length];
    }
}