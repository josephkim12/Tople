package com.example.project_4t_tople.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project_4t_tople.R;
import com.example.project_4t_tople.model.Mypage;
import com.example.project_4t_tople.model.TopleModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import cz.msebera.android.httpclient.Header;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {
    Button buttonSendMessage, buttonModifyAllow, buttonMoimSett, buttonMoimDel;
    EditText editText1;
    TopleModel loginUser;

    // 혜민씨한테 받아올 데이터(id, 모임디폴트)
    Mypage moimUSetting;
    String user_id;

    AsyncHttpClient client;

    // 모임삭제와 모임탈퇴를 위한 로그인된 사용자 정보 가져오기
    HttpResponseUser responseUser;
    // 모임 삭제하기
    HttpResponseDelete responseDelete;
    // 모임 탈퇴하기
    HttpResponseWithdraw responseWithdraw;

    // id로 로그인된 사용자 정보 가져오기
    String URLUser = "http://175.198.87.149:8080/moim.4t.spring/selectUserMoimMem.tople";
    // 모임코드로 모임삭제
    String URLDelete = "http://175.198.87.149:8080/moim.4t.spring/deleteMoim.tople";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Toolbar tb = findViewById(R.id.app_toolbar);
        setSupportActionBar(tb);

        buttonSendMessage = findViewById(R.id.buttonSendMessage);
        buttonModifyAllow = findViewById(R.id.buttonModifyAllow);
        buttonMoimSett = findViewById(R.id.buttonMoimSett);
        buttonMoimDel = findViewById(R.id.buttonMoimDel);
        editText1 = findViewById(R.id.editText1);

        loginUser = new TopleModel();
        moimUSetting = (Mypage) getIntent().getSerializableExtra("item");
        user_id = getIntent().getStringExtra("user_id");

        client = new AsyncHttpClient();

        responseUser = new HttpResponseUser(this);
        responseDelete = new HttpResponseDelete(this);
        responseWithdraw = new HttpResponseWithdraw(this);

        buttonSendMessage.setOnClickListener(this);
        buttonModifyAllow.setOnClickListener(this);
        buttonMoimSett.setOnClickListener(this);
        buttonMoimDel.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 로그인한 유저정보
        RequestParams paramsUser = new RequestParams();
        paramsUser.put("id", user_id);
        paramsUser.put("moimcode", moimUSetting.getMoimcode());
        client.post(URLUser, paramsUser, responseUser);
    }

    // 버튼 아래 추가할거 생각하기
    @Override
    public void onClick(View v) {
        Intent intent;
        String tel = null;
        if(v.getId() == R.id.buttonSendMessage) {     // 메세지보내기
            intent = new Intent(this, MessageActivity.class);
            intent.putExtra("moimcode", moimUSetting.getMoimcode());
            startActivity(intent);
        } else if(v.getId() == R.id.buttonModifyAllow) {      // 회원관리
            intent = new Intent(this, MemberActivity.class);
            intent.putExtra("loginUser", (Serializable) loginUser);
            intent.putExtra("moimcode", moimUSetting.getMoimcode());
            startActivity(intent);
        } else if(v.getId() == R.id.buttonMoimSett) {      // 모임설정
            intent = new Intent(this, SettingActivity.class);
            intent.putExtra("moimUSetting",(Serializable) moimUSetting);
            startActivity(intent);
        } else if(v.getId() == R.id.buttonMoimDel) {      // 모임삭제
            if(loginUser.getPermit() == 1) {
                showConfirmDialog();
            } else {
                Toast.makeText(AdminActivity.this, "관리자만 모임을 삭제할 수 있습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private void showConfirmDialog () {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("모임삭제");
        builder.setMessage("모임을 없애시겠습니까?");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);
        // 긍정 버튼 추가 및 이벤트 설정
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 경고 다이얼로그
                warning();
            }
        });
        // 부정 버튼 추가 및 이벤트 설정
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AdminActivity.this, "모임삭제를 취소했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        // 설정한 정보로 Dialog 생성
        AlertDialog dialog = builder.create();
        // Dialog를 화면에 표시
        dialog.show();
    }

    private void warning () {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 제목 설정
        builder.setTitle("경고");
        // 내용 설정
        builder.setMessage("모임에 관한 정보가 전부 지워집니다. 정말 삭제하시겠습니까?");
        // 아이콘 이미지 설정
        builder.setIcon(R.mipmap.ic_launcher);
        // 하드웨어 BackKey가 눌렸을 때, 창이 닫히지 않도록 설정
        builder.setCancelable(false);
        // 확인 버튼의 추가 및 이벤트 설정
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AdminActivity.this, "모임을 삭제했습니다.", Toast.LENGTH_SHORT).show();
                // 모임삭제 재기형한테 전송
                RequestParams requestDelete = new RequestParams();
                requestDelete.put("moimcode", moimUSetting.getMoimcode());
                client.post(URLDelete, requestDelete, responseDelete);
                Intent intent = new Intent(AdminActivity.this, MypageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                finish();
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AdminActivity.this, "모임삭제를 취소했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        // 설정한 정보로 Dialog 생성
        AlertDialog dialog = builder.create();
        // Dialog를 화면에 표시
        dialog.show();
    }
    class HttpResponseUser extends AsyncHttpResponseHandler {
        Activity activity;
        ProgressDialog dialog;

        public HttpResponseUser (Activity activity) {
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

                JSONObject item = json.getJSONObject("item");
                loginUser.setPermit(item.getInt("permit"));
                loginUser.setId(item.getString("id"));
                loginUser.setFav(item.getString("fav"));

                JSONObject kakao_account = item.getJSONObject("kakao_account");
                if (kakao_account.has("birthday")) {
                    loginUser.setBirth(kakao_account.getString("birthday"));
                } else {
                    loginUser.setBirth("생일없음");
                }
                if (kakao_account.has("gender")) {
                    loginUser.setGender(kakao_account.getString("gender"));
                } else {
                    loginUser.setBirth("성별없음");
                }

                JSONObject properties = item.getJSONObject("properties");
                loginUser.setUpdate_prof(properties.getString("profile_image"));
                loginUser.setName(properties.getString("nickname"));
                if (properties.has("update_prof")) {
                    loginUser.setThumb(properties.getString("update_prof"));
                } else {
                    loginUser.setThumb(properties.getString("thumbnail_image"));
                }
                if (properties.has("region")) {
                    loginUser.setLoca(properties.getString("region"));
                } else {
                    loginUser.setLoca("지역없음");
                }
                if(properties.has("phone_num")) {
                    loginUser.setTel(properties.getString("phone_num"));
                } else {
                    loginUser.setTel("번호없음");
                }
                if(loginUser.getPermit() == 1) {
                    editText1.setText("모임코드 : " + moimUSetting.getMoimcode());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(getApplicationContext(), "통신 실패1", Toast.LENGTH_SHORT).show();
        }
    }

    class HttpResponseDelete extends AsyncHttpResponseHandler {
        Activity activity;
        ProgressDialog dialog;

        public HttpResponseDelete (Activity activity) {
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
                String result = json.getString("result");
                if (result.equals("OK")) {
                    Toast.makeText(AdminActivity.this, "모임삭제 성공", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AdminActivity.this, "모임삭제 실패", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(getApplicationContext(), "통신 실패 AdminActivity", Toast.LENGTH_SHORT).show();
        }
    }

    class HttpResponseWithdraw extends AsyncHttpResponseHandler {
        Activity activity;
        ProgressDialog dialog;

        public HttpResponseWithdraw (Activity activity) {
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
                String result = json.getString("result");
                if (result.equals("OK")) {
                    Toast.makeText(AdminActivity.this, "모임탈퇴 성공", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AdminActivity.this, "모임탈퇴 실패", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(getApplicationContext(), "통신 실패 AdminActivity4", Toast.LENGTH_SHORT).show();
        }
    }
}
