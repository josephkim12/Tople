package com.example.project_4t_tople.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.project_4t_tople.R;
import com.example.project_4t_tople.model.TopleModel;

import java.util.List;

public class CallAdapter extends ArrayAdapter<TopleModel> {
    Activity activity;
    int resource;

    public CallAdapter(Context context, int resource, List<TopleModel> objects) {
        super(context, resource, objects);
        activity = (Activity) context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(resource, null);
        }

        TopleModel item = getItem(position);
        if (item != null) {
            ImageView imageView3 = convertView.findViewById(R.id.imageView3);
            TextView textView1 = convertView.findViewById(R.id.textView1);
            TextView textView2 = convertView.findViewById(R.id.textView2);

            if(item.getTel() == null) {
                item.setTel("번호 없음");
            }

            Glide
                .with(activity)
                .load(item.getThumb())
                .into(imageView3);

            textView1.setText("닉네임 : " +item.getName());
            textView2.setText("전화번호 : " +item.getTel());
        }
        return convertView;
    }
}
