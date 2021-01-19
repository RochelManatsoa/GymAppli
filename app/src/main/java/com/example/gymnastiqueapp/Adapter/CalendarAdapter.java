package com.example.gymnastiqueapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.gymnastiqueapp.Models.Events;
import com.example.gymnastiqueapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CalendarAdapter extends ArrayAdapter {

    List<Date> dates;
    List<Events> events;
    Calendar currentDate;
    LayoutInflater inflater;


    public CalendarAdapter(@NonNull Context context, List<Date> dates,  Calendar currentDate, List<Events> events) {
        super(context, R.layout.calendar_single_cell);

        this.dates = dates;
        this.currentDate = currentDate;
        this.events = events;
        inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Date monthDate = dates.get(position);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(monthDate);
        int DayNo = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayDay = dateCalendar.get(Calendar.DATE);
        int displayMonth = dateCalendar.get(Calendar.MONTH) +1;
        int displayYear = dateCalendar.get(Calendar.YEAR);
        int currentDay = currentDate.get(Calendar.DATE);
        int currentMonth = currentDate.get(Calendar.MONTH) +1;
        int currentYear = currentDate.get(Calendar.YEAR);

        View view = convertView;

        if (view == null){
            view = inflater.inflate(R.layout.calendar_single_cell, parent, false);
        }

        if (displayMonth == currentMonth && displayYear == currentYear){
            if(displayDay == currentDay){
                view = inflater.inflate(R.layout.calendar_single_cell_date, parent, false);
            }else{
                view = inflater.inflate(R.layout.calendar_single_cell, parent, false);
            }
        }else{
            //view.setEnabled(true);
            view = inflater.inflate(R.layout.calendar_single_cell_off, parent, false);
        }



        TextView Date_Number = view.findViewById(R.id.calendar_day);
        TextView EventNumber = view.findViewById(R.id.events_id);
        Date_Number.setText(String.valueOf(DayNo));
        Calendar eventCalendar = Calendar.getInstance();
        ArrayList<String> arrayList = new ArrayList<>();

        for (int i=0; i < events.size(); i++){
            eventCalendar.setTime(ConvertStringToDate(events.get(i).getDATE()));
            if(DayNo == eventCalendar.get(Calendar.DAY_OF_MONTH) && displayMonth == eventCalendar.get(Calendar.MONTH)+1 && displayYear == eventCalendar.get(Calendar.YEAR)){
                arrayList.add(events.get(i).getEVENTS());
                EventNumber.setText(arrayList.size()+" Events");
            }
        }

        return view;
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }

    @Override
    public int getPosition(@Nullable Object item) {
        return dates.indexOf(item);
    }

    private Date ConvertStringToDate(String eventDate) {

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.FRANCE);
        Date date = null;

        try {
            date = format.parse(eventDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;

    }
}
