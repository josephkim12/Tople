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
import com.example.project_4t_tople.model.TopleModel;

import java.util.List;

public class MemberAdapter extends ArrayAdapter<TopleModel> {
    Activity activity;
    int resource;

    public MemberAdapter(Context context, int resource, List<TopleModel> objects) {
        super(context, resource, objects);
        this.activity = (Activity) context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = activity.getLayoutInflater().inflate(resource, null);
        }

        TopleModel item = getItem(position);
        if(item != null){
            TextView memitem_name = convertView.findViewById(R.id.memitem_name);
            TextView memitem_permit = convertView.findViewById(R.id.memitem_permit);
            ImageView memitem_img = convertView.findViewById(R.id.memitem_img);

            Glide
                .with(memitem_img)
                .load(item.getThumb())
                .apply(RequestOptions.circleCropTransform())
                .error(R.drawable.anom)
                .placeholder(R.drawable.ic_empty_b)
                .into(memitem_img);

            memitem_name.setText(item.getName());

            String grade = "";
            if(item.getPermit()==1){
                grade = "모임장";
            }else if (item.getPermit()==2){
                grade = "관리자";
            }else if (item.getPermit()==3){
                grade = "일반회원";
            }
            memitem_permit.setText(grade);
        }
        return convertView;
    }
}
