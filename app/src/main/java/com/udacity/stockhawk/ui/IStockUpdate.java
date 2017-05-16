package com.udacity.stockhawk.ui;

import android.app.Activity;
import android.content.Context;

import yahoofinance.Stock;

/**
 * Created by waldek on 16.05.17.
 */

public interface IStockUpdate {
    
    public void updateStock(Activity activity, Stock stock);
}
