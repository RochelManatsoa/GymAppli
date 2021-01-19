package com.example.gymnastiqueapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.example.gymnastiqueapp.View.CustomCalendarView;

public class CalendarActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private CustomCalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        InitializeFields();
        calendarView = (CustomCalendarView) findViewById(R.id.custom_calendar_view);

    }

    private void InitializeFields() {
        mToolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Calendrier");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}