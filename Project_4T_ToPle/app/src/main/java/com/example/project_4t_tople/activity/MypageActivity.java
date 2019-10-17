package com.example.project_4t_tople.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.project_4t_tople.R;
import com.example.project_4t_tople.adapter.MypageAdapter;
import com.example.project_4t_tople.helper.FileUtils;
import com.example.project_4t_tople.helper.GifLoading;
import com.example.project_4t_tople.helper.PhotoHelper;
import com.example.project_4t_tople.helper.RegexHelper;
import com.example.project_4t_tople.model.Mypage;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.helper.log.Logger;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class MypageActivity extends AppCompatActivity   implements View.OnClickListener, AdapterView.OnItemClickListener {
    Button buttonHome, buttonSearch, buttonSet;
    Button buttonMake;

    FloatingActionButton fabWrite, floatButton;
    LinearLayout fabLayoutMain, fabLayoutSub;
    LinearLayout linearLayoutMy, linearLayoutMakeMo;
    Animation fab_open, fab_close;
    Boolean isFabOpen = false;
    ImageView imageViewtext;

    GridView gridView;
    ArrayList<Mypage> list;
    MypageAdapter adapter;

    //linearLayoutmakeMo 레이아웃
    Button buttonBackToHome, buttonPhoto, buttonSave;
    EditText editTextName, editTextLoca, editTextIntro;

    // 업로드할 사진파일의 경로
    String filePath = null;

    AsyncHttpClient client; // implementation 'com.loopj.android:android-async-http:1.4.9' in build.gradle
    // 응답처리 객체
    HttpResponse response;

    // list, GridView에 URL
    String URL ="http://175.198.87.149:8080/moim.4t.spring/testMoimItems.tople";

    // insert 용 URL
    String URL2 ="http://175.198.87.149:8080/moim.4t.spring/testCreateMoim.tople";

    String user_id = "";   //멤버 테이블을 불러올때 사용할 유저아이디
    String dialogChk = ""; //다이얼로그 체크 변수
    Intent intent;

    // 지역넣기 버튼
    Button buttonLoca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        fabLayoutMain = (LinearLayout) findViewById(R.id.fabLayoutMain);
        fabLayoutSub = (LinearLayout) findViewById(R.id.fabLayoutSub);
        linearLayoutMy = findViewById(R.id.linearLayoutMy) ;
        linearLayoutMakeMo = findViewById(R.id.linearLayoutMakeMo);
        buttonHome = findViewById(R.id.buttonHome);
        buttonSearch= findViewById(R.id.buttonSearch);
        buttonSet= findViewById(R.id.buttonSet);
        buttonMake= findViewById(R.id.buttonSave);
        buttonBackToHome= findViewById(R.id.buttonBackToHome);
        gridView = findViewById(R.id.gridView);
        imageViewtext = findViewById(R.id.imageViewtext);

        // 지역넣기 버튼
        buttonLoca = findViewById(R.id.buttonLoca);

        list = new ArrayList<>();

        fabWrite = findViewById(R.id.fabWrite);
        floatButton = findViewById(R.id.floatButton);

        adapter = new MypageAdapter(this, R.layout.grid_item,list);

        client = new AsyncHttpClient();
        response = new HttpResponse(this, adapter);

        // 리스트뷰에 어뎁터 연결
        gridView.setAdapter(adapter);

        fabLayoutSub.setVisibility(View.INVISIBLE);
        linearLayoutMy.setVisibility(View.VISIBLE);
        linearLayoutMakeMo.setVisibility(View.GONE);

        buttonBackToHome.setOnClickListener(this);
        fabWrite.setOnClickListener(this);
        floatButton.setOnClickListener(this);
        buttonMake.setOnClickListener(this);
        buttonHome.setOnClickListener(this);
        buttonSearch.setOnClickListener(this);
        buttonSet.setOnClickListener(this);
        gridView.setOnItemClickListener(this);

        //linearLayoutmakeMo 레이아웃
        buttonBackToHome = findViewById(R.id.buttonBackToHome);
        buttonPhoto = findViewById(R.id.buttonPhoto);
        buttonSave = findViewById(R.id.buttonSave);

        editTextName = findViewById(R.id.editTextName);
        editTextLoca = findViewById(R.id.editTextLoca);
        editTextIntro = findViewById(R.id.editTextIntro);

        buttonBackToHome.setOnClickListener(this);
        buttonPhoto.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
        buttonLoca.setOnClickListener(this);


        //로그인 사용자 정보 불러오기
        requestMe();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //다이얼로그 체크값 변수를 확인하여 no이면 다이얼로그 출력후 다이얼로그 체크값 변수를 yes로 변경
                if (dialogChk.equals("no")) {
                    showAddInfoDialog(); //초기정보 입력 다이얼로그 (최초1 회만 출력)
                    dialogChkChange(); //카카오프로필의 다이얼로그 체크 값 변경
                }
            }
        }, 500);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.buttonLoca:
                region_set();
                break;

            case R.id.buttonHome:
                onResume();
                break;

            case R.id.buttonSearch:
                intent = new Intent(this, SearchActivity.class);
                startActivityForResult(intent, 200);
                break;

            case R.id.buttonSet:
                final Intent intent = new Intent(MypageActivity.this, SetActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;

            case R.id.floatButton:
                anim();
                break;

            case R.id.fabWrite:
                anim();
                linearLayoutMy.setVisibility(View.GONE);
                linearLayoutMakeMo.setVisibility(View.VISIBLE);
                break;

            //linearLayoutmakeMo 레이아웃
            case R.id.buttonSave: // 모임 만들면서 홈으로 가기
                String loca = editTextLoca.getText().toString().trim();
                String moimname  = editTextName.getText().toString().trim();
                String prod  = editTextIntro.getText().toString().trim();

                RegexHelper regexHelper = RegexHelper.getInstance();
                String msg = null;
                // 입력값 검사
                if (msg == null && !regexHelper.isValue(moimname)){
                    msg = "모임이름을 입력하세요";
                } else if (msg == null && !regexHelper.isValue(loca)) {
                    msg = "활동영역을 입력하세요";
                } else if (msg == null && !regexHelper.isValue(prod)) {
                    msg = "간단한 모임소개를 입력하세요";
                } else if (msg == null && !regexHelper.isValue(filePath)){
                    msg = "사진을 선택하세요";
                }
                if (msg != null) {
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    return;
                }

                RequestParams params = new RequestParams();
                params.setForceMultipartEntityContentType(true);
                params.put("id" , user_id); // user_id 있는아이디 적용
                params.put("loca" , loca);
                params.put("moimname" , moimname);
                params.put("prod" , prod); // 모임소개
                params.put("color" , "gray"); // 기본값 gray
                try {
                    params.put("pic", new File(filePath));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                client.post(URL2, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            JSONObject temp = new JSONObject(new String(responseBody));
                            JSONObject jsonObject = temp.getJSONObject("item");

                            Mypage mypage = new Mypage();
                            mypage.setLoca(jsonObject.getString("loca"));
                            mypage.setProd(jsonObject.getString("prod"));
                            mypage.setColor(jsonObject.getString("color"));
                            mypage.setMoimname(jsonObject.getString("moimname"));
                            mypage.setCount(jsonObject.getInt("count"));
                            mypage.setPermit(jsonObject.getInt("permit"));
                            mypage.setFav(jsonObject.getString("fav"));
                            mypage.setPic(jsonObject.getString("pic"));
                            mypage.setMoimcode(jsonObject.getInt("moimcode"));

                            MypageActivity.this.onResume();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    }
                });

                linearLayoutMakeMo.setVisibility(View.GONE);
                linearLayoutMy.setVisibility(View.VISIBLE);
                break;

            case R.id.buttonPhoto:  // 사진등록
                showLisDialog();
                break;

            case R.id.buttonBackToHome: // 그냥 뒤로가기
                hideKeypad();
                linearLayoutMakeMo.setVisibility(View.GONE);
                linearLayoutMy.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void region_set() {
        final String[] items = new String[]{"서울", "부산", "대구", "인천", "광주", "대전", "울산", "세종", "경기", "강원", "충북", "충남", "전북", "전남", "경북", "경남", "제주"};
        final int[] selectedIndex = {0};

        android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(MypageActivity.this);
        dialog.setTitle("지역 선택").setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedIndex[0] = which;
            }
        }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editTextLoca.setText(items[selectedIndex[0]]);
            }
        });
        dialog.show();
    }

    private void hideKeypad() {
        View view = getCurrentFocus();
        if(view != null) {
            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Mypage item = adapter.getItem(position);
        Intent intent = new Intent(this, MoimActivity.class);
        intent.putExtra("item", item);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
    }

    public void anim() {
        if (isFabOpen) {
            fabLayoutMain.setVisibility(View.VISIBLE);
            imageViewtext.setVisibility(View.GONE);
            floatButton.animate().rotationBy(60);
            fabWrite.startAnimation(fab_close);
            fabWrite.setClickable(false);
            isFabOpen = false;
        } else {
            fabLayoutSub.setVisibility(View.VISIBLE);
            imageViewtext.setVisibility(View.VISIBLE);
            floatButton.animate().rotationBy(-60);
            fabWrite.startAnimation(fab_open);
            fabWrite.setClickable(true);
            isFabOpen = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();         // 갔다가 돌아와도 화면 갱신이 가능함*

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!adapter.isEmpty()) adapter.clear(); // List 데이터 삭제
                // 통신 요청
                RequestParams params = new RequestParams();
                params.put("id", user_id);
                client.post(URL, params, response);
            }
        }, 100);

        Log.d("[INFO]", "user_id = " + user_id);
    }

    // 통신 결과를 받아서 처리할 이너클래스
    public class HttpResponse extends AsyncHttpResponseHandler {
        // 객체 선언
        Activity activity; // Toast를 표시하는 데 사용하기 위한 Context
        MypageAdapter adapter;  // 데이터를 List에 저장
        GifLoading mProgressDialog;

        public HttpResponse(Activity activity, MypageAdapter adapter) {
            this.activity = activity;
            this.adapter = adapter;
        }

        // 통신 시작시, 자동호출
        @Override
        public void onStart() {
            if (mProgressDialog == null)
                mProgressDialog = new GifLoading(activity);
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
        // 통신 종료
        @Override
        public void onFinish() {
            mProgressDialog.dismiss(); // 이거 안넣으면 "잠시만 기다려주세요.." 계속뜸
            mProgressDialog = null;
        }

        // 통신성공
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String strJson = new String(responseBody); // byte[]을 String으로 변환
            try {
                JSONObject json = new JSONObject(strJson); // 문자열을 Json 갤체로 만들기
                JSONArray items = json.getJSONArray("items");

                for (int i = 0; i < items.length() ; i++) {
                    JSONObject jsonObject = items.getJSONObject(i);

                    Mypage mypage = new Mypage();
                    mypage.setLoca(jsonObject.getString("loca"));
                    mypage.setProd(jsonObject.getString("prod"));
                    mypage.setColor(jsonObject.getString("color"));
                    mypage.setMoimname(jsonObject.getString("moimname"));
                    mypage.setCount(jsonObject.getInt("count"));
                    mypage.setPermit(jsonObject.getInt("permit"));
                    mypage.setFav(jsonObject.getString("fav"));
                    mypage.setPic(jsonObject.getString("pic"));
                    mypage.setMoimcode(jsonObject.getInt("moimcode"));

                    adapter.add(mypage);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 서버로부터 200코드가 아닌 값이 오면 자동 호출
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(MypageActivity.this, "통신실패", Toast.LENGTH_SHORT).show();
        }
    }

    // 목록 선택창을 화면에 표시
    private void showLisDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] items = {"새로 촬영하기", "갤러리에서 가져오기"}; // It should be Array
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // 새로 촬영하기
                        filePath = PhotoHelper.getInstance().getNewPhotoPath();
                        // 카메라 앱 호출출 위한 암묵적 인텐트
                        File file = new File(filePath);
                        Uri uri = null;
                        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            uri = FileProvider.getUriForFile(getApplicationContext(),
                                    getApplicationContext().getPackageName() + ".fileprovider", file);
                            camera_intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            camera_intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } else {
                            uri = Uri.fromFile(file);
                        }
                        camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(camera_intent, 100);
                        Log.d("[INFO]", uri.toString());

                        break;
                    case 1: // 갤러리에서 가져오기
                        Intent galleryIntent = null;
                        if (Build.VERSION.SDK_INT >= 19) {
                            galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                        } else  {
                            galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        }
                        // 임시 파일만 필터링
                        galleryIntent.setType("image/*");
                        galleryIntent.putExtra(Intent.EXTRA_LOCAL_ONLY,true );
                        startActivityForResult(galleryIntent, 101);
                        break;
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case 100: // 카메라 앱
                // 촬영 결과를 갤러리 앱에 등록
                Intent photoIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.parse("file://" + filePath));
                sendBroadcast(photoIntent);
                break;
            case 101: // 갤러리 앱
                if (resultCode == RESULT_OK){ // resultCode
                    // 선택한 파일 경로 얻기
                    filePath = FileUtils.getPath(this, data.getData());
                }
                break;
        }
    }

    private void showAddInfoDialog() {
        android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(this);
        dialog.setTitle("기초 정보 입력");
        dialog.setMessage("지역과 전화번호를 설정해주세요!\n설정하지 않으면 기능사용에 제약이 발생할 수 있습니다! \n"+"(해당 알림은 로그인후 최초 1회만 출력됩니다!)");
        dialog.setPositiveButton("설정 화면으로 이동하기",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MypageActivity.this, SetActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                    }
                });
        //회원정보의 다이얼로그 출력여부 업데이트 영역
        dialog.show();
    }

    private void dialogChkChange() {
        final Map<String, String> properties = new HashMap<String, String>();
        properties.put("DialogShowChk", "yes");
        UserManagement.getInstance().requestUpdateProfile(new ApiResponseCallback<Long>() {
            @Override
            public void onSuccess(Long userId) {
            }
            @Override
            public void onNotSignedUp() {
            }
            @Override
            public void onFailure(ErrorResult errorResult) {
            }
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
            }
        }, properties);
    }

    private void requestMe() {
        //카카오서버에 기존 요청 정보외에 추가 정보(커스텀 파라미터등)을 요청하기 위한 키값
        final List<String> keys = new ArrayList<>();

        UserManagement.getInstance().me(keys, new MeV2ResponseCallback() {
            //v1에서는 세션종료에러, 비회원에러, 그외 에러가 있었으나 기존 v1이 종료되고 v2가 적용됨에 따라 비회원 에러는 사라짐
            //의도치 않은 세션 종료로 인한 에러
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Toast.makeText(MypageActivity.this, "세션 종료. 어플을 재실행해주세요!", Toast.LENGTH_SHORT);
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

                UserAccount account = response.getKakaoAccount(); //액세스토큰을 위한 유저계정 정보
                Map prop = response.getProperties(); //커스텀 프로퍼티값

                dialogChk = (String)prop.get("DialogShowChk"); //다이얼로그 체크 변수
            }
        });
    }
}
