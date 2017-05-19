package com.udacity.stockhawk.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
;

/**
 * Created by waldek on 16.05.17.
 */

public class StockChartFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private class MyCustomXAxisValueFormatter implements IAxisValueFormatter {

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            SimpleDateFormat formatter = new SimpleDateFormat("MM/yy", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis((long) value);
            return formatter.format(calendar.getTime());
        }
    }

    public static final int STOCK_CHART_LOADER = 300 ;
    @BindView(R.id.detail_stock_chart)
    LineChart chart;

    //protected  LineDataSet lineDataSet;
    protected LineData data;

    protected LineDataSet dataSet;
    @Override
    public void onCreate(Bundle state){
        super.onCreate(state);
        Log.d("fragment_created", "true");

        //this.lineDataSet = new LineDataSet(new ArrayList<Entry>(), "CustomDataSet");
        this.dataSet = new LineDataSet(new ArrayList<Entry>(), "History");
        this.dataSet.setFillColor(android.R.color.holo_green_light);
        this.dataSet.setDrawFilled(true);
        this.dataSet.setCircleRadius(4);
        this.dataSet.setColor(this.getContext().getResources().getColor(android.R.color.holo_red_dark));
        this.dataSet.setLineWidth(2);
        this.dataSet.setDrawValues(false);
        this.data = new LineData(this.dataSet);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle state){

        View view = inflater.inflate(R.layout.stock_chart, group, false);
        ButterKnife.bind(this, view);
        //this.chart.setData(new LineData(this.lineDataSet));
        getLoaderManager().initLoader(STOCK_CHART_LOADER, null, this);
        this.chart.setBackgroundColor(getResources().getColor(android.R.color.white));
        this.chart.setData(this.data);
        XAxis xAxis = this.chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new MyCustomXAxisValueFormatter());
       // this.chart.invalidate();
        return view;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Bundle bundle = this.getArguments();
        Log.d("loader_created", String.valueOf(bundle.containsKey(Contract.PATH_QUOTE)));
        if(bundle == null || !bundle.containsKey(Contract.PATH_QUOTE))
            return  null;
        String symbol = bundle.getString(Contract.PATH_QUOTE);
        Log.d("bundle_stock", symbol);
        return new CursorLoader(this.getContext(),
                Contract.Quote.makeUriForStock(symbol),
                Contract.Quote.QUOTE_COLUMNS.toArray(new String[]{}),
                symbol, null, Contract.Quote.COLUMN_SYMBOL);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("load_finished", "true");
        if(data == null || data.getCount() == 0 || data.isClosed())
            return;
        //data.moveToFirst();
        Log.d("load_finished_data_size", data.getCount() +"");

        boolean invalidData = false;


        while(data.moveToNext()){
            if(!data.getColumnName(Contract.Quote.POSITION_HISTORY).equalsIgnoreCase(Contract.Quote.COLUMN_HISTORY)
                    || data.getString(Contract.Quote.POSITION_HISTORY) == null ){
                invalidData = true;
                continue;
            }

            String[] quotes = data.getString(Contract.Quote.POSITION_HISTORY).split("\n");

            for(String quote : quotes){
                String[] arr = quote.split(",");
                Entry entry = null;

                try {
                    entry = new Entry(Float.valueOf(arr[0].trim()), Float.valueOf(arr[1].trim()));
                } catch (NumberFormatException nfe){
                    nfe.printStackTrace();
                    continue;
                }
               // this.data.getDataSetByIndex(0).addEntry(entry);
                if(entry != null)
                    this.dataSet.addEntry(entry);
                Log.d("entry_val", "x: " + entry.getX() + " y: " +  String.valueOf(entry.getY()));
            }



            //Log.d("stock_history", data.getString(Contract.Quote.POSITION_HISTORY));
        }

        if(invalidData)
            return;

        this.data.notifyDataChanged();
        this.dataSet.notifyDataSetChanged();
        this.chart.notifyDataSetChanged();
        Log.d("chart_visible", String.valueOf(this.chart.isShown()));
        this.chart.invalidate();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //this.lineDataSet.clear();

        //this.data.clearValues();
        //this.lineDataSet.notifyDataSetChanged();
        //this.chart.notifyDataSetChanged();
        //this.chart.invalidate();
    }


    public void redrawChart(){
        Log.d("redraw_chart", "true");
       // this.chart.invalidate();
    }
}
