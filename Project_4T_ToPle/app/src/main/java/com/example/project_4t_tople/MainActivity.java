package com.example.project_4t_tople;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.project_4t_tople.activity.MypageActivity;
import com.kakao.auth.AccessTokenCallback;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.AuthService;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.auth.authorization.accesstoken.AccessToken;
import com.kakao.auth.network.response.AccessTokenInfoResponse;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    private SessionCallback callback;

    Button button;
    LoginButton btn_kakao;

    String user_id, dialogChk;

    LinearLayout layout1, layout2, layout_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button_login);
        btn_kakao = findViewById(R.id.btn_kakao_login);

        layout1 = findViewById(R.id.layout_login);
        layout2 = findViewById(R.id.layout_load);
        layout_title = findViewById(R.id.layout_title);

        button.setOnClickListener(this);

        layout_title.setVisibility(View.VISIBLE);
        layout1.setVisibility(View.GONE);
        layout2.setVisibility(View.GONE);

        permissionCheck();

        //세션콜
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Session.getCurrentSession().isOpened()) {
                    layout_title.setVisibility(View.GONE);
                    layout1.setVisibility(View.GONE);
                    layout2.setVisibility(View.VISIBLE);
                    requestMe();
                } else {
                    layout_title.setVisibility(View.GONE);
                    layout1.setVisibility(View.VISIBLE);
                    layout2.setVisibility(View.GONE);
                }
            }
        }, 1000);// 1초 정도 딜레이를 준 후 시작
    }

    // 퍼미션 체크 영역
    private void permissionCheck() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                     Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    }, 100);
            }
        }
    }

    //온클릭시 동작할 메소드
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_login:
                btn_kakao.performClick();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //세션상태 변경시 동작
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {
        //세션 오픈 성공
        @Override
        public void onSessionOpened() {
            //카카오톡에서 정보를 받아올 메소드
            requestMe();
        }

        //세션 오픈 실패
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            }
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
                Toast.makeText(MainActivity.this, "세션 종료", Toast.LENGTH_SHORT).show();

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

                requestAccessTokenInfo(); //액세스토큰 유효 검사
                handleScopeError(account); //사용자 정보 수정 토큰 발행
                dialogCheck(); //다이얼로그체크 값 여부 검사

                layout_title.setVisibility(View.GONE);
                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        skipLogin();
                    }
                }, 2500);// 2.5초 정도 딜레이를 준 후 시작
            }
        });
    }

    //dialogChk값을 확인하여 값이 없으면 no로 설정(mypageActivity에서 다이얼로그 출력여부를 검사하기 위함)
    private void dialogCheck() {
        if (dialogChk==null){
            final Map<String, String> properties = new HashMap<String, String>();
            properties.put("DialogShowChk", "no");
            UserManagement.getInstance().requestUpdateProfile(new ApiResponseCallback<Long>() {
                @Override
                public void onSuccess(Long userId) {
                    dialogChk="no";
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
    }

    //SDK환경하에서 액세스토큰의 유효성을 검사하는 메소드
    private void requestAccessTokenInfo() {
        AuthService.getInstance().requestAccessTokenInfo(new ApiResponseCallback<AccessTokenInfoResponse>() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Toast.makeText(MainActivity.this, "세션 종료", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e("failed to get access token info. msg=" + errorResult);
            }
            @Override
            public void onSuccess(AccessTokenInfoResponse accessTokenInfoResponse) {
                long userId = accessTokenInfoResponse.getUserId();
                String userId2 = Long.toString(userId);
                Logger.d("this access token is for userId=" + userId);

                long expiresInMilis = accessTokenInfoResponse.getExpiresInMillis();
                String exTime = Long.toString(expiresInMilis);
                Logger.d("this access token expires after " + expiresInMilis + " milliseconds.");
            }
        });
    }

    //세션이 연결되어 있으면 즉 로그인 정보가 남아 있으면 로그인화면을 자동으로 스킵하는 메소드
    private void skipLogin() {
        //ListActivity
        Intent intent = new Intent(MainActivity.this, MypageActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    //동적 동의 일종의 퍼미션체크 + 액세스토큰과 리프레쉬토큰에 대한 발급을 담당
    private void handleScopeError(UserAccount account) {
        List<String> neededScopes = new ArrayList<>();
        if (account.needsScopeAccountEmail()) {
            neededScopes.add("account_email");
        }
        if (account.needsScopeGender()) {
            neededScopes.add("gender");
        }
        Session.getCurrentSession().updateScopes(this, neededScopes, new
                AccessTokenCallback() {
                    @Override
                    public void onAccessTokenReceived(AccessToken accessToken) {
                        // 유저에게 성공적으로 동의를 받음. 부가정보에 접근 가능한 토큰을 재발급 받게 됨.
                        //Log.d("[token]", "sucess/"+accessToken);
                        String AcessToken = accessToken.getAccessToken();
                        String RefreshToken = accessToken.getRefreshToken();
                    }

                    @Override
                    public void onAccessTokenFailure(ErrorResult errorResult) {
                        // 동의 얻기 실패
                    }
                });
    }
}
