package com.example.project_4t_tople.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.project_4t_tople.R;
import com.example.project_4t_tople.model.Board;

import java.util.List;


public class PhotoAdapter extends ArrayAdapter<Board> {
    Activity activity;
    int resource;

    public PhotoAdapter(Context context, int resource, List<Board> objects) {
        super(context, resource, objects);
        activity=(Activity)context;
        this.resource=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if(itemView == null) {
            itemView = activity.getLayoutInflater().inflate(resource, null);
        }

        Board item = getItem(position);

        if(item != null) {
            ImageView imageViewThumb= itemView.findViewById(R.id.imageViewThumb);
            Glide.with(imageViewThumb).load(item.getThumb()).error(R.drawable.ic_error_w).placeholder(R.drawable.ic_empty_b).into(imageViewThumb);
        }
        return itemView;
    }
}
