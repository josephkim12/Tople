package com.example.project_4t_tople.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.project_4t_tople.R;
import com.example.project_4t_tople.model.Board;

import java.util.List;

public class BoardAdapter extends ArrayAdapter<Board> {
    Activity activity;
    int resource;

    public BoardAdapter(Context context, int resource,List<Board> objects) {
        super(context, resource, objects);
        activity=(Activity)context;
        this.resource=resource;

    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(resource, null);
        }

        Board item = getItem(position);

        if (item != null) {
            TextView noticeSubject = convertView.findViewById(R.id.noticeSubject);
            TextView noticeContent = convertView.findViewById(R.id.noticeContent);

            noticeSubject.setText(item.getSubject());
            noticeContent.setText(item.getContent());
        }

        return convertView;
    }
}
