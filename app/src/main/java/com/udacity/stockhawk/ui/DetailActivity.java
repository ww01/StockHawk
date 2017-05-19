package com.udacity.stockhawk.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.ui.fragment.StockChartFragment;
import com.udacity.stockhawk.ui.fragment.StockNewsFragment;
import com.udacity.stockhawk.ui.fragment.StockSummaryFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by waldek on 16.05.17.
 */

public class DetailActivity extends AppCompatActivity {

    private class DetailsPagerAdapter extends FragmentPagerAdapter {

        private String stockId;

        protected ArrayList<Fragment> fragments = new ArrayList<Fragment>();

        public DetailsPagerAdapter(FragmentManager fm, String stockId) {
            super(fm);
            this.stockId = stockId;
            this.fragments.add(new StockChartFragment());
            this.fragments.add(new StockSummaryFragment());
            this.fragments.add(new StockNewsFragment());

        }

        @Override
        public Fragment getItem(int position) {
            //Log.d("position_asked", position+"");
            if (position >= fragments.size())
                return null;
            Fragment f = this.fragments.get(position);
            /*switch (position){
                case 0:
                    f = new StockSummaryFragment();
                    break;
                case 1:
                    f = new StockChartFragment();
                    break;
                case 2:
                    f = new StockNewsFragment();
                    break;
            }*/

            Bundle fragmentArgs = new Bundle();
            fragmentArgs.putString(Contract.PATH_QUOTE, stockId);
            f.setArguments(fragmentArgs);
            return f;
        }

        @Override
        public int getCount() {
            return 3;
        }

        public Fragment getFragmentAt(int position){
            if(position >= this.fragments.size())
                return null;
            return fragments.get(position);
        }
    }

    @BindView(R.id.details_view_pager)
    protected ViewPager viewPager;

    protected DetailsPagerAdapter pagerAdapter;

    @Override
    public void onCreate(Bundle state){
        super.onCreate(state);
        setContentView(R.layout.details_activity);
        ButterKnife.bind(this);

        this.pagerAdapter = new DetailsPagerAdapter(getSupportFragmentManager(), this.getIntent().getStringExtra(Contract.PATH_QUOTE));
        viewPager.setAdapter(pagerAdapter);

        this.viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position){
                super.onPageSelected(position);
                if(pagerAdapter.getFragmentAt(position) instanceof StockChartFragment){
                    ((StockChartFragment)pagerAdapter.getFragmentAt(position)).redrawChart();
                }
            }
        });
    }
}
