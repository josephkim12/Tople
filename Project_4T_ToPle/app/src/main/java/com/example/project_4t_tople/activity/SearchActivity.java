package com.example.project_4t_tople.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.project_4t_tople.R;
import com.example.project_4t_tople.helper.GifLoading;
import com.example.project_4t_tople.model.Mypage;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.helper.log.Logger;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity
        implements View.OnClickListener {
    Button buttonHome, buttonSearch, buttonSet;
    String user_id = "";
    String URL_find = "http://175.198.87.149:8080/moim.4t.spring/testMoim.tople";
    String URL_join = "http://175.198.87.149:8080/moim.4t.spring/insertMem.tople";

    Intent intent;

    HttpResponse response;
    HttpResponse2 response2;

    AsyncHttpClient client;

    String moimcode;
    String moimname;
    String prod;
    String photo;
    String loca;
    int count;

    //UI영역
    EditText editText_insertCode;
    Button button_find, button_join;
    ImageView imageView_moim;

    TextView textView_count_legion;
    TextView textView_moimname;
    TextView textView_moimProd;

    LinearLayout layout_search, layout_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        client = new AsyncHttpClient();
        response = new HttpResponse(this);
        response2 = new HttpResponse2(this);

        //하단 버튼 바
        buttonHome = findViewById(R.id.buttonHome);
        buttonSearch= findViewById(R.id.buttonSearch);
        buttonSet= findViewById(R.id.buttonSet);

        buttonHome.setOnClickListener(this);
        buttonSearch.setOnClickListener(this);
        buttonSet.setOnClickListener(this);

        //레이아웃 서치
        layout_search = findViewById(R.id.layout_search);
        editText_insertCode=findViewById(R.id.editText_insertCode);
        button_find=findViewById(R.id.button_find);

        button_find.setOnClickListener(this);

        //레이아웃 뷰
        layout_view=findViewById(R.id.layout_view);
        imageView_moim=findViewById(R.id.imageView_moim);
        textView_count_legion=findViewById(R.id.textView_count_legion);
        textView_moimname=findViewById(R.id.textView_moimname);
        textView_moimProd=findViewById(R.id.textView_moimProd);
        button_join=findViewById(R.id.button_join);

        button_join.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        requestMe();
        layout_search.setVisibility(View.VISIBLE);
        layout_view.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonHome:
                intent = new Intent(this, MypageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                break;

            case R.id.buttonSearch:
                onResume();
                break;

            case R.id.buttonSet:
                intent = new Intent(this, SetActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.button_find:
                moimcode = editText_insertCode.getText().toString();
                getMoimData();
                break;

            case R.id.button_join:
                moimJoin();
                break;
        }
    }

    private void requestMe() {
        //카카오서버에 기존 요청 정보외에 추가 정보(커스텀 파라미터등)을 요청하기 위한 키값
        final List<String> keys = new ArrayList<>();

        UserManagement.getInstance().me(keys, new MeV2ResponseCallback() {
            //v1에서는 세션종료에러, 비회원에러, 그외 에러가 있었으나 기존 v1이 종료되고 v2가 적용됨에 따라 비회원 에러는 사라짐
            //의도치 않은 세션 종료로 인한 에러
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Toast.makeText(SearchActivity.this, "세션 종료. 어플을 재실행해주세요!", Toast.LENGTH_SHORT);
                finish();
            }

            //세션 종료로 인한 에러를 제외한 모든 에러
            @Override
            public void onFailure(ErrorResult errorResult) {
                String error_message = "SessionClosed외의 에러 발생" + errorResult;
                Logger.d(error_message);
            }

            @Override
            public void onSuccess(MeV2Response response) {
                Long id = response.getId();
                user_id = Long.toString(id); //유저고유 아이디값
            }
        });
    }

    class HttpResponse extends AsyncHttpResponseHandler {

        Activity activity;
        GifLoading mProgressDialog;

        public HttpResponse(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onStart() {
            if (mProgressDialog == null)
                mProgressDialog = new GifLoading(activity);
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        public void onFinish() {
            mProgressDialog.dismiss(); // 이거 안넣으면 "잠시만 기다려주세요.." 계속뜸
            mProgressDialog = null;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String strJson = new String(responseBody);

            String result = "FAIL";
            try {
                JSONObject jsonObject = new JSONObject(strJson);
                JSONArray item = jsonObject.getJSONArray("items");

                result = jsonObject.getString("result");

                JSONObject temp = (JSONObject) item.get(0);

                moimname = temp.getString("moimname");
                prod = temp.getString("prod");
                count = temp.getInt("count");
                photo = temp.getString("pic");
                loca = temp.getString("loca");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (result.equals("OK")) {
                moimInfoView();
            } else {
                Toast.makeText(SearchActivity.this, "존재하지 않는 모임 입니다.",
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Log.d("[error]", "error:"+error);
        }
    }

    class HttpResponse2 extends AsyncHttpResponseHandler {

        Activity activity;
        GifLoading mProgressDialog;

        public HttpResponse2(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onStart() {
            if (mProgressDialog == null)
                mProgressDialog = new GifLoading(activity);
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        public void onFinish() {
            mProgressDialog.dismiss(); // 이거 안넣으면 "잠시만 기다려주세요.." 계속뜸
            mProgressDialog = null;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                JSONObject temp = new JSONObject(new String(responseBody));
                String result = temp.getString("result");

                if (result.equals("ALREADY")){
                    Toast.makeText(SearchActivity.this,"이미 가입된 모임입니다!"
                            , Toast.LENGTH_SHORT).show();
                }else {
                    goList();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        }
    }

    private void getMoimData(){
        RequestParams params = new RequestParams();
        params.put("moimcode", moimcode);
        client.post(URL_find, params, response);
    }

    private void moimInfoView() {
        editText_insertCode.getText().clear();

        Glide.with(this)
            .load(photo)
            .apply(RequestOptions.circleCropTransform())
            .into(imageView_moim);

        textView_count_legion.setText(loca+" 지역에서 "+count+"명이 참여중입니다!");
        textView_moimname.setText(moimname);
        textView_moimProd.setText(prod);
        layout_view.setVisibility(View.VISIBLE);
        layout_search.setVisibility(View.GONE);
    }

    private void moimJoin() {
        RequestParams params =new RequestParams();
        params.put("id", user_id);
        params.put("moimcode", moimcode);
        client.post(URL_join, params, response2);
    }

    private void goList() {
        Toast.makeText(this, "모임 가입 성공! TOgether+peoPle = TOPLE!",Toast.LENGTH_SHORT).show();
        intent=new Intent(this, Mypage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
    }
}

