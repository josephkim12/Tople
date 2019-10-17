package com.example.project_4t_tople.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.project_4t_tople.R;
import com.example.project_4t_tople.adapter.AuthorityAdapter;
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

public class MemberActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, View.OnClickListener {
    Button button;
    AuthorityAdapter adapter;
    List<TopleModel> listUsers;
    ListView listMem;
    TopleModel moimUserManage;
    TopleModel loginUser;
    int moimcode;

    AsyncHttpClient client;

    // 권한 변경
    HttpResponsePermit responsePermit;
    // 유저 추방
    HttpResponseKickOut responseKickOut;

    HttpResponseTotUser responseTotUser;

    // URL
    String URLPermit = "http://175.198.87.149:8080/moim.4t.spring/updateMem.tople";
    String URLKickOut = "http://175.198.87.149:8080/moim.4t.spring/dropUser.tople"; // 파라미터 뭐 보내줘야하는지
    // 모임코드로 모임에 속한 유저들의 정보 얻어오기
    String URLTotUsers = "http://175.198.87.149:8080/moim.4t.spring/testMoimUsets.tople";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        // id/ 바뀐내용, 모임코드
        button = findViewById(R.id.button);
        listUsers = new ArrayList<>();

        Toolbar tb = findViewById(R.id.app_toolbar);
        setSupportActionBar(tb);

        moimcode = getIntent().getIntExtra("moimcode", 0);
        loginUser = (TopleModel) getIntent().getSerializableExtra("loginUser");

        moimUserManage = new TopleModel();

        adapter = new AuthorityAdapter(this, R.layout.admin_item, listUsers);
        listMem = findViewById(R.id.listMem);
        listMem.setAdapter(adapter);

        client = new AsyncHttpClient();
        responsePermit = new HttpResponsePermit(this);
        responseTotUser = new HttpResponseTotUser(this);
        responseKickOut = new HttpResponseKickOut(this);

        // 이벤트 설정
        button.setOnClickListener(this);
        listMem.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter != null) {
            adapter.clear();
        }
        RequestParams paramsUsers = new RequestParams();
        paramsUsers.put("moimcode", moimcode);
        client.post(URLTotUsers, paramsUsers, responseTotUser);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        moimUserManage = adapter.getItem(position);
        showAlertDialog();
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("회원관리");
        builder.setMessage("권환설정 및 회원추방");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);

        // 긍정 버튼 추가 및 이벤트 설정
        builder.setPositiveButton("권한설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permit();
            }
        });

        // 부정 버튼 추가 및 이벤트 설정
        builder.setNegativeButton("회원추방", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                kickOut();
            }
        });

        // 설정한 정보로 Dialog 생성
        AlertDialog dialog = builder.create();

        // Dialog를 화면에 표시
        dialog.show();
    }

    private void permit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("권환주기");
        builder.setMessage(moimUserManage.getName() + "님에게 권한을 주시겠습니까?");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);

        // 긍정 버튼 추가 및 이벤트 설정
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permitSelect();
                Toast.makeText(MemberActivity.this, "권한 레벨을 선택해 주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        // 부정 버튼 추가 및 이벤트 설정
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MemberActivity.this, "권한 설정을 취소합니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 설정한 정보로 Dialog 생성
        AlertDialog dialog = builder.create();

        // Dialog를 화면에 표시
        dialog.show();
    }

    private void permitSelect() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("확인");
        builder.setIcon(android.R.drawable.star_big_on);
        builder.setCancelable(false);

        // 리스트에 출력할 문자열 배열
        final String[] items = {"모임장", "관리자", "일반회원"};

        // 리스트 : 긍정버튼의 역활
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (loginUser.getPermit() > which + 1) {
                    Toast.makeText(MemberActivity.this, "자신보다 높은 권한을 줄 수 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    if (which == 0) {
                        moimUserManage.setPermit(1);
                    } else if (which == 1) {
                        moimUserManage.setPermit(2);
                    } else if (which == 2) {
                        moimUserManage.setPermit(3);
                    }
                    // 권한수정
                    RequestParams paramsPermit = new RequestParams();
                    paramsPermit.put("id", moimUserManage.getId());
                    paramsPermit.put("moimcode", moimcode);
                    paramsPermit.put("fav", moimUserManage.getFav());
                    paramsPermit.put("permit", moimUserManage.getPermit());
                    client.post(URLPermit, paramsPermit, responsePermit);
                }
            }
        });

        // 부정의 의미를 갖는 버튼
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MemberActivity.this, "권한 설정을 취소합니다.", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void kickOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("강퇴하기");
        builder.setMessage(moimUserManage.getName() + "님을 추방하시겠습니까?");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);

        // 긍정 버튼 추가 및 이벤트 설정
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (loginUser.getPermit() > moimUserManage.getPermit()) {
                    Toast.makeText(MemberActivity.this, "자신보다 높은 권한의 사람을 추방할 수 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    RequestParams paramsKickOut = new RequestParams();
                    paramsKickOut.put("id", moimUserManage.getId());
                    paramsKickOut.put("moimcode", moimcode);

                    client.post(URLKickOut, paramsKickOut, responseKickOut);
                }
            }
        });
        // 부정 버튼 추가 및 이벤트 설정
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MemberActivity.this, "추방을 취소합니다.", Toast.LENGTH_SHORT).show();
            }
        });
        // 설정한 정보로 Dialog 생성
        AlertDialog dialog = builder.create();
        // Dialog를 화면에 표시
        dialog.show();
    }

    class HttpResponsePermit extends AsyncHttpResponseHandler {
        Activity activity;
        ProgressDialog dialog;

        public HttpResponsePermit(Activity activity) {
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
            String item = null;
            try {
                JSONObject json = new JSONObject(strJson);
                String result = json.getString("result");
                if (result.equals("OK")) {
                    if(moimUserManage.getPermit() == 1) {item = "모임장";}
                    else if(moimUserManage.getPermit() == 2) {item = "관리자";}
                    else if(moimUserManage.getPermit() == 3) {item = "일반회원";}

                    Toast.makeText(MemberActivity.this, moimUserManage.getId() + "님이" + item + "로 설정되었습니다.", Toast.LENGTH_SHORT).show();
                    MemberActivity.this.onResume();
                } else {
                    Toast.makeText(MemberActivity.this, "권한변경 실패", Toast.LENGTH_SHORT).show();
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

    class HttpResponseKickOut extends AsyncHttpResponseHandler {
        Activity activity;
        ProgressDialog dialog;

        public HttpResponseKickOut(Activity activity) {
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
                    Toast.makeText(MemberActivity.this, moimUserManage.getName() + "님을 추방했습니다.", Toast.LENGTH_SHORT).show();
                    MemberActivity.this.onResume();
                } else {
                    Toast.makeText(MemberActivity.this, "추방 실패", Toast.LENGTH_SHORT).show();
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