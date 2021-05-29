package com.greyka.imgr.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.greyka.imgr.R;
import com.greyka.imgr.adapters.myRecyclerViewAdapter;
import com.greyka.imgr.data.Data;
import com.greyka.imgr.utilities.myUtils;

import java.util.List;

public class CalendarDialog extends Dialog {
    private Context context;

    private dateSetter ds;
    public interface dateSetter{
        void setDate(int year, int month, int day);
    }
    public void setDateSetter(dateSetter ds){
        this.ds = ds;
    }
    public CalendarDialog(Context context) {
        super(context);
        this.context = context;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_calendar);
        this.setCanceledOnTouchOutside(true); // 点击外部会消失
        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                ds.setDate(year, month+1, dayOfMonth);
            }
        });
    }

}
