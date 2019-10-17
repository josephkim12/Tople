package com.example.project_4t_tople.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.project_4t_tople.R;
import com.example.project_4t_tople.model.Calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CalendarAdapter extends ArrayAdapter<Calendar> {

    Activity activity;
    int resource;
    String str_target, str_today;
    long diff, diffDays;

    public CalendarAdapter(Context context, int resource, List<Calendar> objects) {
        super(context, resource, objects);
        activity = (Activity) context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(resource, null);
        }

        Calendar item = getItem(position);
        if (item != null) {
            TextView calendar_Name = convertView.findViewById(R.id.calendar_Name);
            TextView calendar_Date = convertView.findViewById(R.id.calendar_Date);
            TextView calendar_Money = convertView.findViewById(R.id.calendar_Money);
            TextView calendar_Dday = convertView.findViewById(R.id.calendar_Dday);

            calendar_Name.setText(item.getSch_title());
            calendar_Date.setText(item.getSch_year() + "-" + item.getSch_month() + "-" + item.getSch_day());
            calendar_Money.setText(Integer.toString(item.getSch_amount()));

            str_target = item.getSch_year() + "-" + item.getSch_month() + "-" + item.getSch_day();
            str_today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date target = format.parse(str_target);
                Date today  = format.parse(str_today);

                if(target.getTime() > today.getTime()){
                    diff = target.getTime() - today.getTime();
                    diffDays = diff / (24 * 60 * 60 * 1000);
                    calendar_Dday.setText("D - " + Long.toString(diffDays));
                } else if (target.getTime() == today.getTime()){
                    calendar_Dday.setText("D-Day");
                } else if(target.getTime() < today.getTime()){
                    diff = today.getTime() - target.getTime();
                    diffDays = diff / (24 * 60 * 60 * 1000);
                    calendar_Dday.setText("D + " + Long.toString(diffDays));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return convertView;
    }
}