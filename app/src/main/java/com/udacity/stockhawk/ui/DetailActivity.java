package com.udacity.stockhawk.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.ui.fragment.StockChartFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by waldek on 16.05.17.
 */

public class DetailActivity extends AppCompatActivity {

    //@BindView(R.id.details_view_pager)
   // protected ViewPager viewPager;


    @Override
    public void onCreate(Bundle state){
        super.onCreate(state);
        setContentView(R.layout.details_activity);
        ButterKnife.bind(this);

        StockChartFragment stockChartFragment = new StockChartFragment();

        Bundle fragmentArgs = new Bundle();
        fragmentArgs.putString(Contract.PATH_QUOTE, this.getIntent().getStringExtra(Contract.PATH_QUOTE));
        stockChartFragment.setArguments(fragmentArgs);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(android.R.id.content, stockChartFragment).commit();
       // ft.commit();

    }
}
