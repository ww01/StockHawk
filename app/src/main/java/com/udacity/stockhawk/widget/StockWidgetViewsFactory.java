package com.udacity.stockhawk.widget;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

/**
 * Created by waldek on 19.05.17.
 */

public class StockWidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    protected Cursor data;
    protected Context context;

    public StockWidgetViewsFactory(Context context){
        this.context = context;
    }

    @Override
    public void onCreate() {
        // Intentionally left empty
    }

    @Override
    public void onDataSetChanged() {
        this.data = this.context.getContentResolver().query(Contract.Quote.URI, null, null, null, null);
    }

    @Override
    public void onDestroy() {
        if(this.data != null && !this.data.isClosed()){
            this.data.close();
            this.data = null;
        }
    }

    @Override
    public int getCount() {
        return this.data != null? this.data.getCount() : 0;
    }

    @Override
    public RemoteViews getViewAt(int i) {
        if(this.data == null || !this.data.moveToPosition(i)){
            Log.d("invalid_cursor_position", i+"");
            return null;
        }

        RemoteViews remoteViews = new RemoteViews(this.context.getPackageName(), R.layout.widget_stock_item);
        remoteViews.setTextViewText(R.id.widget_item_symbol, data.getString(Contract.Quote.POSITION_SYMBOL));
        remoteViews.setTextViewText(R.id.widget_item_price,
                String.format(
                        this.context.getResources().getConfiguration().locale,
                        "%.2f",
                        data.getDouble(Contract.Quote.POSITION_PRICE)
                )
        );

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(this.context.getPackageName(), R.layout.widget_stock_item);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
