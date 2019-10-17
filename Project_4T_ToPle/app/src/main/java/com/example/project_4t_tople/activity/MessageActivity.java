package com.example.project_4t_tople.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.project_4t_tople.R;
import com.example.project_4t_tople.adapter.CallAdapter;
import com.example.project_4t_tople.model.TopleModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    Button button1, button2;
    List<String>telList;

    // 회원 전화번호 담아서 넘겨주기
    CallAdapter adapter;
    List<TopleModel> listUsers;
    ListView listCall;
    int moimcode;

    AsyncHttpClient clientTotUser;
    HttpResponseTotUser responseTotUser;

    // 모임코드로 모임에 속한 유저들의 정보 얻어오기
    String URLTotUsers = "http://175.198.87.149:8080/moim.4t.spring/testMoimUsets.tople";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar tb = findViewById(R.id.app_toolbar);
        setSupportActionBar(tb);

        listUsers = new ArrayList<>();
        moimcode = getIntent().getIntExtra("moimcode", 0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        listCall = findViewById(R.id.listCall);
        adapter = new CallAdapter(this, R.layout.call_item, listUsers);
        telList = new ArrayList<>();

        clientTotUser = new AsyncHttpClient();
        responseTotUser = new HttpResponseTotUser(this);

        listCall.setAdapter(adapter);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        listCall.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        RequestParams paramsUsers = new RequestParams();
        paramsUsers.put("moimcode", moimcode);
        clientTotUser.post(URLTotUsers, paramsUsers, responseTotUser);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        String result = "";
        if (v.getId() == R.id.button1) {
            for(int i =0; i < telList.size(); i++) {
                if(telList.size() >= 1 && telList.size() <= 20) {
                    result += telList.get(i);
                }
            }
            result = result.substring(0, result.lastIndexOf(','));

            intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + result));
            startActivity(intent);
        } else if (v.getId() == R.id.button2) {
            // 정보 없애는 작업
            finish();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TopleModel moimUser = adapter.getItem(position);
        // 모임코드 추가하면 여기에서 작업(리스트에 포함해서)
        if(moimUser.getTel() != "번호없음" && moimUser.getTel() != null) {
            Toast.makeText(this, moimUser.getName() + "님을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
            telList.add(moimUser.getTel() + ",");
        }
    }

    class HttpResponseTotUser extends AsyncHttpResponseHandler {
        Activity activity;
        ProgressDialog dialog;

        public HttpResponseTotUser (Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onStart() {
            dialog = new ProgressDialog(activity);
            dialog.setMessage("잠시만 기다려주세요...");
            dialog.setCancelable(false);
            dialog.show();
        }
        @Override
        public void onFinish() {
            dialog.dismiss();
            dialog = null;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String strJson = new String(responseBody);

            try {
                JSONObject json = new JSONObject(strJson);
                JSONArray users = json.getJSONArray("users");
                for(int i=0; i<users.length(); i++) {
                    JSONObject temp = users.getJSONObject(i);
                    TopleModel topleModel = new TopleModel();
                    topleModel.setPermit(temp.getInt("permit"));
                    topleModel.setId(temp.getString("id"));
                    topleModel.setFav(temp.getString("fav"));
                    JSONObject kakao_account = temp.getJSONObject("kakao_account");
                    if (kakao_account.has("birthday")) {
                        topleModel.setBirth(kakao_account.getString("birthday"));
                    } else {
                        topleModel.setBirth("생일없음");
                    }
                    if (kakao_account.has("gender")) {
                        topleModel.setGender(kakao_account.getString("gender"));
                    } else {
                        topleModel.setBirth("성별없음");
                    }

                    JSONObject properties = temp.getJSONObject("properties");
                    topleModel.setName(properties.getString("nickname"));
                    topleModel.setUpdate_prof(properties.getString("profile_image"));
                    if (properties.has("region")) {
                        topleModel.setLoca(properties.getString("region"));
                    } else {
                        topleModel.setLoca("지역없음");
                    }

                    if (properties.has("update_prof")) {
                        topleModel.setThumb(properties.getString("update_prof"));
                    } else if (properties.has("thumbnail_image")){
                        topleModel.setThumb(properties.getString("thumbnail_image"));
                    } else {
                        topleModel.setThumb("");
                    }

                    if(properties.has("phone_num")) {
                        topleModel.setTel(properties.getString("phone_num"));
                    } else {
                        topleModel.setTel("번호없음");
                    }
                    adapter.add(topleModel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(getApplicationContext(), "통신 실패2", Toast.LENGTH_SHORT).show();
        }
    }
}
