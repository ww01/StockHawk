package com.udacity.stockhawk.task;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.PrefUtils;
import com.udacity.stockhawk.sync.QuoteSyncJob;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.regex.Pattern;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

/**
 * Created by waldek on 16.05.17.
 */

public class StockFindTask extends AsyncTask<String, Void, Stock> {


    protected WeakReference<Context> weakContext;

    public StockFindTask(Context context){
        this.weakContext = new WeakReference<Context>(context);
    }

    @Override
    protected Stock doInBackground(String... strings) {

        if(strings.length == 0)
            return null;

        String s = strings[0];

        Pattern p = Pattern.compile("[^a-zA-Z0-9]");
        boolean hasSpecialChar = p.matcher(s).find();

        if(hasSpecialChar)
            return null;
        try {
            return YahooFinance.get(s);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onPostExecute(Stock stock){
        if(this.weakContext.get() == null)
            return;

        int resId = 0;
        if(stock == null || stock.getName() == null ){
            resId = R.string.error_no_such_stock;
        } else {
            PrefUtils.addStock(this.weakContext.get(), stock.getSymbol());
            QuoteSyncJob.syncImmediately(this.weakContext.get());
            resId = R.string.toast_stock_added;
        }

        Toast.makeText(this.weakContext.get(), resId, Toast.LENGTH_LONG).show();
    }

}
