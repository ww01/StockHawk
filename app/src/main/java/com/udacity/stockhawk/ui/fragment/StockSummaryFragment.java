package com.udacity.stockhawk.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.stockhawk.R;

import butterknife.ButterKnife;

/**
 * Created by waldek on 16.05.17.
 */

public class StockSummaryFragment extends Fragment {

    @Override
    public void onCreate(Bundle state){
        super.onCreate(state);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group,  Bundle bundle){
        View view = inflater.inflate(R.layout.stock_summary, group, false);
        ButterKnife.bind(this, view);
        return view;
    }

}
