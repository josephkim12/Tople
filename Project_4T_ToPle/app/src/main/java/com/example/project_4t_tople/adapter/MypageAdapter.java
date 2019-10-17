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
import com.example.project_4t_tople.model.Mypage;

import java.util.List;

public class MypageAdapter extends ArrayAdapter<Mypage> {
    Activity activity;
    int resource;

    //  즐겨찾기 true ★ false ☆
    private String favTrue = "★  ";
    private String favFalse = "☆  ";

    public MypageAdapter(Context context, int resource, List<Mypage> objects) {
        super(context, resource, objects);
        activity = (Activity) context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if(itemView == null) {
            itemView = activity.getLayoutInflater().inflate(resource, null);
        }
        Mypage item = getItem(position); // position : List클래스의 위치값 getList -> getItem

        if(item != null) {
            // 백그라운드 컬러 셋팅
            int wineInt = itemView.getResources().getColor(R.color.wine);
            int darkBrownInt = itemView.getResources().getColor(R.color.darkBrown);
            int darkGoldInt = itemView.getResources().getColor(R.color.darkGold);
            int sadGreenInt = itemView.getResources().getColor(R.color.sadGreen);
            int strongGreenInt = itemView.getResources().getColor(R.color.strongGreen);
            int navyInt = itemView.getResources().getColor(R.color.navy);
            int lightVioletInt = itemView.getResources().getColor(R.color.lightViolet);
            int violetInt = itemView.getResources().getColor(R.color.violet);
            int grayInt = itemView.getResources().getColor(R.color.gray);

            String wine = "wine";  String darkBrown ="darkBrown"; String darkGold ="darkGold";
            String sadGreen ="sadGreen"; String strongGreen ="strongGreen"; String navy ="navy";
            String lightViolet ="lightViolet"; String violet ="violet";
            String gray ="gray";

            if(!item.getColor().equals(wine) && !item.getColor().equals(darkBrown)
                    && !item.getColor().equals(darkGold) && !item.getColor().equals(sadGreen)
                    && !item.getColor().equals(strongGreen) && !item.getColor().equals(navy)
                    && !item.getColor().equals(lightViolet) && !item.getColor().equals(violet)) {
                itemView.setBackgroundColor(grayInt);
                item.setColor(gray);
            }

            if (item.getColor().equals(String.valueOf(wine))) { itemView.setBackgroundColor(wineInt); }
            if (item.getColor().equals(String.valueOf(darkBrown))) { itemView.setBackgroundColor(darkBrownInt); }
            if (item.getColor().equals(String.valueOf(darkGold))) { itemView.setBackgroundColor(darkGoldInt); }
            if (item.getColor().equals(String.valueOf(sadGreen))) { itemView.setBackgroundColor(sadGreenInt); }
            if (item.getColor().equals(String.valueOf(strongGreen))) { itemView.setBackgroundColor(strongGreenInt); }
            if (item.getColor().equals(String.valueOf(navy))) { itemView.setBackgroundColor(navyInt);}
            if (item.getColor().equals(String.valueOf(lightViolet))) { itemView.setBackgroundColor(lightVioletInt); }
            if (item.getColor().equals(String.valueOf(violet))) { itemView.setBackgroundColor(violetInt); }

            TextView textViewMoimname = itemView.findViewById(R.id.textMoimname); // 모임이름
            TextView textViewProd = itemView.findViewById(R.id.textProd);      // 모임설명
            TextView textViewCount = itemView.findViewById(R.id.textCount);     // 모임인원
            ImageView imageViewforTest = itemView.findViewById(R.id.imageViewforTest);

            TextView textViewFav = itemView.findViewById(R.id.textFav);       // 즐겨찾기여부

            textViewMoimname.setText(item.getMoimname());
            textViewProd.setText(item.getProd());
            textViewCount.setText(String.valueOf(item.getCount() + "명 참여중"));

            if (item.getFav().equals("true")) {
                textViewFav.setText(favTrue);
            } else {
                textViewFav.setText(favFalse);
            }

            if (item.getPic() != null) {
                String exe = item.getPic().substring(item.getPic().lastIndexOf('.') +1);

                if (exe.equals("gif")) {
                    // Glide 사용
                    Glide
                        .with(imageViewforTest)
                        .asGif()
                        .load(item.getPic())
                        .apply(RequestOptions.circleCropTransform())
                        .error(R.drawable.ic_error_w)
                        .placeholder(R.drawable.ic_empty_b)
                        .into(imageViewforTest);
                } else {
                    // Glide 사용
                    Glide
                        .with(imageViewforTest)
                        .load(item.getPic())
                        .apply(RequestOptions.circleCropTransform())
                        .error(R.drawable.ic_error_w)
                        .placeholder(R.drawable.ic_empty_b)
                        .into(imageViewforTest);
                }
            } else {
                itemView.setVisibility(View.GONE);
            }
        }
        // 1줄화면을 되돌려줌
        return itemView;
    }
}









