package vickypatel.ca.shiftmanager.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import vickypatel.ca.shiftmanager.R;
import vickypatel.ca.shiftmanager.extras.MyApplication;
import vickypatel.ca.shiftmanager.fragments.SummaryFragment;

/**
 * Created by VickyPatel on 2015-10-18.
 */
public class SummaryPagerAdapter extends FragmentStatePagerAdapter {

    Context context;
    public SummaryPagerAdapter(FragmentManager fm,Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        SummaryFragment myFragment = SummaryFragment.newInstance(String.valueOf(position), "");
        return myFragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getResources().getStringArray(R.array.summaryTabs)[position];
    }
}
