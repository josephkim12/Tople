package com.example.project_4t_tople.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.project_4t_tople.R;
import com.example.project_4t_tople.model.Board;

import java.util.List;

public class ShowAdapter extends ArrayAdapter<Board> {
    Activity activity;
    int resource;

    public ShowAdapter(Context context, int resource,List<Board> objects) {
        super(context, resource, objects);
        activity=(Activity)context;
        this.resource=resource;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(resource, null);
        }

        Board item = getItem(position);
        if (item != null) {
            TextView textUsername = convertView.findViewById(R.id.textUsername);
            TextView textEditdate = convertView.findViewById(R.id.textEditdate);
            TextView textSubject = convertView.findViewById(R.id.textSubject);
            TextView textContent = convertView.findViewById(R.id.textContent);

            ImageView boardImage = convertView.findViewById(R.id.boardImage);

            textUsername.setText(item.getName());
            textEditdate.setText(item.getEditdate());
            textSubject.setText(item.getSubject());
            textContent.setText(item.getContent());

            // Glide 사용
            Glide
                .with(boardImage)
                .load(item.getFilename())
                .apply(RequestOptions.circleCropTransform())
                .error(R.drawable.icon_empty)
                .placeholder(R.drawable.ic_empty_b)
                .into(boardImage);
        }

        return convertView;
    }
}