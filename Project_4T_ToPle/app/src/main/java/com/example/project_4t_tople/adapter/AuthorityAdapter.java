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

public class AuthorityAdapter extends ArrayAdapter<TopleModel> {
    Activity activity;
    int resource;

    public AuthorityAdapter(Context context, int resource, List<TopleModel> objects) {
        super(context, resource, objects);
        activity = (Activity) context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = activity.getLayoutInflater().inflate(resource, null);
        }

        TopleModel item = getItem(position);
        if(item != null) {
            ImageView imageView = convertView.findViewById(R.id.imageView);
            TextView textView1 = convertView.findViewById(R.id.textView1);
            TextView textView2 = convertView.findViewById(R.id.textView2);

            Glide
                .with(activity)
                .load(item.getThumb())
                .into(imageView);

            String grade = "";
            if(item.getPermit()==1){
                grade = "모임장";
            }else if (item.getPermit()==2){
                grade = "관리자";
            }else if (item.getPermit()==3){
                grade = "일반회원";
            }

            textView1.setText("닉네임 = " + item.getName() + "/ 성별 = " + item.getGender());
            textView2.setText("생년월일 = " + item.getBirth() + "/ 권한 = " + grade);
        }
        return convertView;
    }
}
