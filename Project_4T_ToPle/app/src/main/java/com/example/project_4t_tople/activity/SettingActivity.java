package com.example.project_4t_tople.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.project_4t_tople.R;
import com.example.project_4t_tople.model.Mypage;
import com.example.project_4t_tople.model.TopleModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    Button buttonChangeMoimName, buttonChangeProdS, buttonChangeBannerS, buttonChangeColorS, buttonLoca, backToWhere;
    // 색상 버튼
    Button buttonWine, buttonDarkBrown, buttonGold, buttonSadGreen, buttonStrongGreen, buttonNavy, buttonLightViolet, buttonViolet;
    ImageView imageViewDefaultColor, imageViewSelectedColor;
    EditText editTextMoimNameS, editTextProdS;

    AsyncHttpClient client;
    HttpResponse response;
    // 넘겨받은 데이터
    Mypage moimUSetting;
    // 업데이트된 데이터
    TopleModel updateSetting;

    String filePath;

    int info = 0;
    // 색상
    int wineInt;
    int darkBrownInt;
    int darkGoldInt;
    int sadGreenInt;
    int strongGreenInt;
    int navyInt;
    int lightVioletInt;
    int violetInt;
    int grayInt;

    String wine = "wine";
    String darkBrown = "darkBrown";
    String darkGold = "darkGold";
    String sadGreen = "sadGreen";
    String strongGreen = "strongGreen";
    String navy = "navy";
    String lightViolet = "lightViolet";
    String violet = "violet";


    // 모임정보 수정
    String URLMoim = "http://175.198.87.149:8080/moim.4t.spring/testUpdateMoim.tople";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        client = new AsyncHttpClient();
        response = new HttpResponse();

        moimUSetting = (Mypage) getIntent().getSerializableExtra("moimUSetting");

        updateSetting = new TopleModel();

        buttonChangeMoimName = findViewById(R.id.buttonChangeMoimName);
        buttonChangeProdS = findViewById(R.id.buttonChangeProdS);
        buttonChangeBannerS = findViewById(R.id.buttonChangeBannerS);
        buttonChangeColorS = findViewById(R.id.buttonChangeColorS);
        buttonLoca = findViewById(R.id.buttonLoca);
        backToWhere = findViewById(R.id.backToWhere);

        buttonWine = findViewById(R.id.buttonWine);
        buttonDarkBrown = findViewById(R.id.buttonDarkBrown);
        buttonGold = findViewById(R.id.buttonGold);
        buttonSadGreen = findViewById(R.id.buttonSadGreen);
        buttonStrongGreen = findViewById(R.id.buttonStrongGreen);
        buttonNavy = findViewById(R.id.buttonNavy);
        buttonLightViolet = findViewById(R.id.buttonLightViolet);
        buttonViolet = findViewById(R.id.buttonViolet);

        editTextMoimNameS = findViewById(R.id.editTextMoimNameS);
        editTextProdS = findViewById(R.id.editTextProdS);

        imageViewDefaultColor = findViewById(R.id.imageViewDefaultColor);
        imageViewSelectedColor = findViewById(R.id.imageViewSelectedColor);

        buttonChangeMoimName.setOnClickListener(this);
        buttonChangeProdS.setOnClickListener(this);
        buttonChangeBannerS.setOnClickListener(this);
        buttonChangeColorS.setOnClickListener(this);
        buttonLoca.setOnClickListener(this);
        backToWhere.setOnClickListener(this);

        buttonWine.setOnClickListener(this);
        buttonDarkBrown.setOnClickListener(this);
        buttonGold.setOnClickListener(this);
        buttonSadGreen.setOnClickListener(this);
        buttonStrongGreen.setOnClickListener(this);
        buttonNavy.setOnClickListener(this);
        buttonLightViolet.setOnClickListener(this);
        buttonViolet.setOnClickListener(this);

        colorInit();
        DefaultColor();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 모임 정보 불러오기
        editTextMoimNameS.setText(moimUSetting.getMoimname());
        editTextProdS.setText(moimUSetting.getProd());
    }

    private void colorInit() {
        wineInt = getResources().getColor(R.color.wine);
        darkBrownInt = getResources().getColor(R.color.darkBrown);
        darkGoldInt = getResources().getColor(R.color.darkGold);
        sadGreenInt = getResources().getColor(R.color.sadGreen);
        strongGreenInt = getResources().getColor(R.color.strongGreen);
        navyInt = getResources().getColor(R.color.navy);
        lightVioletInt = getResources().getColor(R.color.lightViolet);
        violetInt = getResources().getColor(R.color.violet);
        grayInt = getResources().getColor(R.color.gray);
    }

    private void DefaultColor() {

        if (moimUSetting.getColor().equals("wine")) {imageViewDefaultColor.setBackgroundColor(wineInt);}
        if (moimUSetting.getColor().equals("darkBrown")) {imageViewDefaultColor.setBackgroundColor(darkBrownInt);}
        if (moimUSetting.getColor().equals("darkGold")) {imageViewDefaultColor.setBackgroundColor(darkGoldInt);}
        if (moimUSetting.getColor().equals("sadGreen")) {imageViewDefaultColor.setBackgroundColor(sadGreenInt);}
        if (moimUSetting.getColor().equals("strongGreen")) {imageViewDefaultColor.setBackgroundColor(strongGreenInt);}
        if (moimUSetting.getColor().equals("navy")) {imageViewDefaultColor.setBackgroundColor(navyInt);}
        if (moimUSetting.getColor().equals("lightViolet")) {imageViewDefaultColor.setBackgroundColor(lightVioletInt);}
        if (moimUSetting.getColor().equals("violet")) {imageViewDefaultColor.setBackgroundColor(violetInt);}
        if (moimUSetting.getColor().equals("gray")) {imageViewDefaultColor.setBackgroundColor(grayInt);}
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if (v.getId() == R.id.buttonChangeMoimName) {     // 모임이름 변경
            updateSetting.setMoimname(editTextMoimNameS.getText().toString().trim());
            info = 1;
            if(updateSetting.getMoimname() == moimUSetting.getMoimname()) {
                Toast.makeText(this, "모임이름을 수정한 후 눌러주세요.", Toast.LENGTH_SHORT).show();
                return;
            } else {
                moimUSetting.setMoimname(updateSetting.getMoimname());
                MoimUpdate();
            }
        } if (v.getId() == R.id.buttonChangeProdS) {      // 모임소개 변경
            updateSetting.setProd(editTextProdS.getText().toString().trim());
            info = 2;
            if(updateSetting.getProd() == moimUSetting.getProd()) {
                Toast.makeText(this, "모임소개를 수정한 후 눌러주세요.", Toast.LENGTH_SHORT).show();
                return;
            } else {
                moimUSetting.setProd(updateSetting.getProd());
                MoimUpdate();
            }
        } if (v.getId() == R.id.buttonChangeBannerS) {      // 모임배너 변경 (임시)
            intent = new Intent(this, BannerActivity.class);
            intent.putExtra("moimUSetting", moimUSetting);
            startActivityForResult(intent, 111);

        } if (v.getId() == R.id.buttonChangeColorS) {      // 대표색상 변경 (임시)
            info = 4;
            if(updateSetting.getColor().equals(moimUSetting.getColor())) {
                Toast.makeText(this, "색상을 수정한 후 눌러주세요.", Toast.LENGTH_SHORT).show();
                return;
            } else {
                moimUSetting.setColor(updateSetting.getColor());
                MoimUpdate();
            }
        } if (v.getId() == R.id.buttonLoca) {      // 지역
            region_set();
        } if (v.getId() == R.id.backToWhere) {      // 돌아가기
            intent = new Intent(this, MypageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        } if(v.getId() == R.id.buttonWine) {
            imageViewSelectedColor.setBackgroundColor(wineInt);
            updateSetting.setColor(wine);
        } if(v.getId() == R.id.buttonDarkBrown) {
            imageViewSelectedColor.setBackgroundColor(darkBrownInt);
            updateSetting.setColor(darkBrown);
        } if(v.getId() == R.id.buttonGold) {
            imageViewSelectedColor.setBackgroundColor(darkGoldInt);
            updateSetting.setColor(darkGold);
        } if(v.getId() == R.id.buttonSadGreen) {
            imageViewSelectedColor.setBackgroundColor(sadGreenInt);
            updateSetting.setColor(sadGreen);
        } if(v.getId() == R.id.buttonStrongGreen) {
            imageViewSelectedColor.setBackgroundColor(strongGreenInt);
            updateSetting.setColor(strongGreen);
        } if(v.getId() == R.id.buttonNavy) {
            imageViewSelectedColor.setBackgroundColor(navyInt);
            updateSetting.setColor(navy);
        } if(v.getId() == R.id.buttonLightViolet) {
            imageViewSelectedColor.setBackgroundColor(lightVioletInt);
            updateSetting.setColor(lightViolet);
        } if(v.getId() == R.id.buttonViolet) {
            imageViewSelectedColor.setBackgroundColor(violetInt);
            updateSetting.setColor(violet);
        }
    }

    private void MoimUpdate() {
        RequestParams params = new RequestParams();
        params.setForceMultipartEntityContentType(true);
        try {
            if (updateSetting.getPic() == null) {
                params.put("pic", new File(moimUSetting.getPic()));
            } else {
                params.put("pic", new File(updateSetting.getPic()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        params.put("moimname", moimUSetting.getMoimname());
        params.put("loca", moimUSetting.getLoca());
        params.put("prod", moimUSetting.getProd());
        params.put("color", moimUSetting.getColor());
        params.put("moimcode", moimUSetting.getMoimcode());
        client.post(URLMoim, params, response);

        if(info == 1) {          // id, loca, moimname, prod, color, pic
            Toast.makeText(this, "모임이름이 변경되었습니다.", Toast.LENGTH_SHORT).show();
        } else if(info ==2) {
            Toast.makeText(this, "모임소개가 변경되었습니다.", Toast.LENGTH_SHORT).show();
        } else if(info ==4) {
            Toast.makeText(this, "대표색상이 변경되었습니다.", Toast.LENGTH_SHORT).show();
        } else if(info ==5) {
            Toast.makeText(this, "지역이 변경되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void region_set() {
        final String[] items = new String[]{"서울", "부산", "대구", "인천", "광주", "대전", "울산", "세종", "경기", "강원", "충북", "충남", "전북", "전남", "경북", "경남", "제주"};
        final int[] selectedIndex = {0};

        AlertDialog.Builder dialog = new AlertDialog.Builder(SettingActivity.this);
        dialog.setTitle("지역 선택").setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedIndex[0] = which;
            }
        }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateSetting.setLoca(items[selectedIndex[0]]);
                info = 5;
                if(updateSetting.getLoca() == moimUSetting.getLoca()) {
                    Toast.makeText(SettingActivity.this, "지역을 수정한 후 눌러주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    moimUSetting.setLoca(updateSetting.getLoca());
                    MoimUpdate();
                }
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 111:
                filePath = data.getStringExtra("filePath");
                updateSetting.setPic(filePath);
                MoimUpdate();
                if(resultCode == RESULT_OK) {

                }
                break;
        }
    }

    class HttpResponse extends AsyncHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String strJson = new String(responseBody);
            try {
                JSONObject json = new JSONObject(strJson);
                String result = json.getString("result");
                if (result.equals("OK")) {
                    Toast.makeText(SettingActivity.this, "변경 성공", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SettingActivity.this, "변경 실패", Toast.LENGTH_SHORT).show();
                }
                JSONArray items = json.getJSONArray("items");
                for(int i=0; i<items.length(); i++) {
                    JSONObject temp = items.getJSONObject(i);
                    moimUSetting.setProd(temp.getString("prod"));
                    moimUSetting.setLoca(temp.getString("loca"));
                    moimUSetting.setColor(temp.getString("color"));
                    moimUSetting.setPic(temp.getString("pic"));
                    moimUSetting.setMoimcode(temp.getInt("moimcode"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(getApplicationContext(), "통신 실패", Toast.LENGTH_SHORT).show();
        }
    }
}