package com.example.project_4t_tople.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.project_4t_tople.R;
import com.example.project_4t_tople.model.Detail_Todo;

import java.util.List;

public class Detail_MemberAdapter extends ArrayAdapter<Detail_Todo> {
    Activity activity;
    int resource;

    public Detail_MemberAdapter(Context context, int resource, List<Detail_Todo> objects) {
        super(context, resource, objects);
        activity = (Activity) context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = activity.getLayoutInflater().inflate(resource, null);
        }

        Detail_Todo item = getItem(position);
        if(item != null){
            ImageView detail_item_img = convertView.findViewById(R.id.memitem_img);
            TextView detail_item_name = convertView.findViewById(R.id.memitem_name);
            TextView detail_item_todo = convertView.findViewById(R.id.memitem_permit);
            TextView detail_item_ex = convertView.findViewById(R.id.Detail_item_ex);
            CheckBox detail_item_amount = convertView.findViewById(R.id.Detail_item_amount);

            if(!item.getDetail_item_img().equals("")){
                Glide.with(detail_item_img)
                     .load(item.getDetail_item_img())
                     .error(R.drawable.ic_error_w)
                     .placeholder(R.drawable.ic_empty_b)
                     .into(detail_item_img);
            } else detail_item_img.setVisibility(View.INVISIBLE);

            detail_item_name.setText(item.getId());
            detail_item_todo.setText(item.getTodo());
            detail_item_ex.setText(item.getEx());
            if(item.getAmount() == 0 ){
                detail_item_amount.setChecked(false);
            } else {
                detail_item_amount.setChecked(true);
            }
        }

        return convertView;
    }
}
