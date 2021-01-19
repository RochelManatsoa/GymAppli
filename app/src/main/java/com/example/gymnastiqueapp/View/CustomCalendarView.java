package com.example.gymnastiqueapp.View;

import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gymnastiqueapp.Adapter.CalendarAdapter;
import com.example.gymnastiqueapp.CalendarActivity;
import com.example.gymnastiqueapp.Models.Events;
import com.example.gymnastiqueapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class CustomCalendarView extends LinearLayout {

    private ImageButton previousBtn, nextBtn;
    private TextView currentDate;
    GridView gridView;
    private  static final int MAX_CURRENT_DAYS = 42;
    Calendar calendar = Calendar.getInstance(Locale.FRANCE);
    Context context;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.FRANCE);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.FRANCE);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.FRANCE);
    SimpleDateFormat eventDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.FRANCE);
    AlertDialog alertDialog;
    List<Date> dates = new ArrayList<>();
    List<Events> eventsList = new ArrayList<>();
    int alarmYear, alarmMonth, alarmDay, alarmHour, alarmMinute;

    CalendarAdapter calendarAdapter;


    public CustomCalendarView(Context context) {
        super(context);
    }

    public CustomCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        InitializeLayout();
        SetupCalendar();
    }

    public CustomCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void SetupCalendar() {
        String CurrentDate = dateFormat.format(calendar.getTime());
        currentDate.setText(CurrentDate);
        dates.clear();
        Calendar monthCalendar = (Calendar)calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK) -1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth);
        //CollectEventsPerMonth(monthFormat.format(calendar.getTime()), yearFormat.format(calendar.getTime()));

        while (dates.size() < MAX_CURRENT_DAYS){
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        calendarAdapter = new CalendarAdapter(context, dates, calendar, eventsList);
        gridView.setAdapter(calendarAdapter);

    }

    private void CollectEventsPerMonth(String month, String year) {

    }

    private void InitializeLayout() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.calendar_layout, this);
        nextBtn = view.findViewById(R.id.nextBtn);
        previousBtn = view.findViewById(R.id.previousBtn);
        currentDate = view.findViewById(R.id.currentDate);
        gridView = view.findViewById(R.id.gridView);
    }
}
