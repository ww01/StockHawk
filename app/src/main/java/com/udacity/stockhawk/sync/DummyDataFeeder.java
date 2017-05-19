package com.udacity.stockhawk.sync;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import yahoofinance.Stock;

/**
 * Created by waldek on 19.05.17.
 */

public class DummyDataFeeder {

    protected final Context context;

    public DummyDataFeeder(Context context){
        this.context = context;
    }


    public Map<String, Stock> getDummyStockList( int csvId) throws IOException {
        HashMap<String, Stock> stockMap = new HashMap<String, Stock>();
        InputStream inputStream = context.getResources().openRawResource(csvId);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String dataLine = null;

        try {

            while((dataLine = reader.readLine()) != null){
                String[] data = dataLine.split(",");
                if(data.length < 2)
                    continue;

                Stock stock = new Stock(data[1]);
                stock.setName(data[0]);
                stock.setCurrency(data[2]);

                stockMap.put(data[1], stock);
            }
        } finally {
            inputStream.close();
            reader.close();
        }



        return stockMap;
    }

}
