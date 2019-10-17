package com.example.project_4t_tople.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project_4t_tople.R;
import com.example.project_4t_tople.helper.FileUtils;
import com.example.project_4t_tople.helper.PhotoHelper;
import com.example.project_4t_tople.helper.RegexHelper;
import com.example.project_4t_tople.model.Mypage;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

public class BoardUpdateActivity extends AppCompatActivity implements View.OnClickListener {
    EditText bSubject, bCotent;
    ImageView imageView3;
    Button bbntWrite, bbntBack;
    ImageButton imageButton;

    // 업로드할 사진파일의 경로
    String filePathBig = null;

    int listnum;
    Mypage mypage;

    String URL_BoardUpdate = "http://175.198.87.149:8080/moim.4t.spring/boardupdate.tople";

    AsyncHttpClient client;
    HttpResponse_BoardUpdate response_boardUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_update);

        bSubject = findViewById(R.id.bSubject);
        bCotent = findViewById(R.id.bContent);
        imageView3 = findViewById(R.id.imageView3);
        bbntBack = findViewById(R.id.bbntBack);
        bbntWrite = findViewById(R.id.bbntWrite);
        imageButton = findViewById(R.id.imageButton);

        imageButton.setOnClickListener(this);
        bbntBack.setOnClickListener(this);
        bbntWrite.setOnClickListener(this);

        bSubject.setText(getIntent().getStringExtra("subject"));
        bCotent.setText(getIntent().getStringExtra("content"));

        if (getIntent().getStringExtra("filename") != null) {
            // Glide 사용
            Glide.with(imageView3)
                 .load(getIntent().getStringExtra("filename"))
                 .error(R.drawable.ic_error_w)
                 .placeholder(R.drawable.ic_empty_b)
                 .into(imageView3);
        } else imageView3.setVisibility(View.GONE);

        client = new AsyncHttpClient();
        response_boardUpdate = new HttpResponse_BoardUpdate(this);

        mypage = (Mypage) getIntent().getSerializableExtra("item");
        listnum = getIntent().getIntExtra("listnum", listnum);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton:
                showLisDialogThumb();
                break;
            case R.id.bbntBack:
                finish();
                break;
            case R.id.bbntWrite:
                String upsubject = bSubject.getText().toString().trim();
                String upcontent = bCotent.getText().toString().trim();

                RegexHelper regexHelper = RegexHelper.getInstance();
                String msg = null;
                if (msg == null && !regexHelper.isValue(upsubject)) {
                    msg = "제목을 입력하세요.";
                } else if (msg == null && !regexHelper.isValue(upcontent)) {
                    msg = "내용을 입력하세요.";
                }

                //에러가 검출되면 메세지 출력 후 , 실행 종료
                if (msg != null) {
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    return;
                }

                RequestParams params = new RequestParams();
                params.put("listnum", listnum);
                params.put("subject", upsubject);
                params.put("content", upcontent);
                params.put("id", getIntent().getStringExtra("id"));
                params.put("moimcode", mypage.getMoimcode());
                params.put("lev", getIntent().getIntExtra("lev", 0));

                if (filePathBig == null) {
                    params.setForceMultipartEntityContentType(true);
                    imageView3.setVisibility(View.INVISIBLE);
                } else {
                    try {
                        params.put("filename", new File(filePathBig));
                        imageView3.setVisibility(View.VISIBLE);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                client.post(URL_BoardUpdate, params, response_boardUpdate);
                break;
        }
    }

    class HttpResponse_BoardUpdate extends AsyncHttpResponseHandler {
        Activity activity;

        public HttpResponse_BoardUpdate(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String strJson = new String(responseBody);
            try {
                JSONObject json = new JSONObject(strJson);
                String rt = json.getString("result");
                if (rt.equals("OK")) {
                    Toast.makeText(activity, "수정 성공", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(activity, "수정 실패", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "통신실패 BoardUpdateActivity1" + statusCode, Toast.LENGTH_SHORT).show();
        }
    }

    //사진
    private void showLisDialogThumb() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] items = {"새로 촬영하기", "갤러리에서 가져오기"}; // It should be Array
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // 새로 촬영하기
                        filePathBig = PhotoHelper.getInstance().getNewPhotoPath();
                        // 카메라 앱 호출출 위한 암묵적 인텐트
                        File file = new File(filePathBig);
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
                        } else {
                            galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        }
                        // 임시 파일만 필터링
                        galleryIntent.setType("image/*");
                        galleryIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                        startActivityForResult(galleryIntent, 101);

                        break;
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 100: // 카메라 앱
                // 촬영 결과를 갤러리 앱에 등록
                Intent photoIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.parse("file://" + filePathBig));
                if (this != null) {
                    this.sendBroadcast(photoIntent);
                }
                imageView3.setVisibility(View.VISIBLE);
                break;
            case 101: // 갤러리 앱
                if (resultCode == this.RESULT_OK) { // resultCode
                    filePathBig = FileUtils.getPath(this, data.getData());
                }
                imageView3.setVisibility(View.VISIBLE);
                break;
        }

        Glide.with(imageView3)
             .load(filePathBig)
             .error(R.drawable.ic_error_w)
             .placeholder(R.drawable.ic_empty_b)
             .into(imageView3);

        imageView3.setVisibility(View.VISIBLE);
    }
}
