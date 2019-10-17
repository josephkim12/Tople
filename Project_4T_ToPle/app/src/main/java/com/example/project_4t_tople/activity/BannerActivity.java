package com.example.project_4t_tople.activity;

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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.project_4t_tople.R;
import com.example.project_4t_tople.helper.FileUtils;
import com.example.project_4t_tople.helper.PhotoHelper;
import com.example.project_4t_tople.model.Mypage;

import java.io.File;

public class BannerActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imageView10;
    Button buttonChangBanner, buttonBack;
    String filePath;
    Mypage moimUSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        Toolbar tb = findViewById(R.id.app_toolbar);
        setSupportActionBar(tb);

        buttonChangBanner = findViewById(R.id.buttonChangBanner);
        buttonBack = findViewById(R.id.buttonBack);
        imageView10 = findViewById(R.id.imageView10);

        buttonChangBanner.setOnClickListener(this);
        buttonBack.setOnClickListener(this);

        moimUSetting = (Mypage) getIntent().getSerializableExtra("moimUSetting");


    }

    @Override
    protected void onResume() {
        super.onResume();

        Glide
                .with(imageView10)
                .load(moimUSetting.getPic())
                .apply(RequestOptions.circleCropTransform())
                .error(R.drawable.ic_error_w)
                .placeholder(R.drawable.ic_empty_b)
                .into(imageView10);

        Log.d("[INFO]", "moimUSetting.getPic() = " + moimUSetting.getPic());

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonChangBanner) {
            showListDialog();
            onResume();
        } else if(v.getId() == R.id.buttonBack) {
            Intent intent = new Intent(this, SettingActivity.class);
            intent.putExtra("filePath", filePath);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void showListDialog() {
        Intent gIntent = new Intent();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] items = {"새로 촬영하기", "갤러리에서 가져오기"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // 새로 촬영하기
                        filePath = PhotoHelper.getInstance().getNewPhotoPath();
                        // 카메라 앱 호출을 위한 암묵적 인텐트
                        File file = new File(filePath);
                        Uri uri = null;

                        Intent camera_Intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            uri = FileProvider.getUriForFile(getApplicationContext(),
                                    getApplicationContext().getPackageName() + ".fileprovider", file);
                            camera_Intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            camera_Intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } else {
                            uri = Uri.fromFile(file);
                        }
                        Log.d("[INFO]", uri.toString());
                        camera_Intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(camera_Intent, 101);
                        break;
                    case 1: // 갤러리에서 가져오기
                        Intent gIntent = null;
                        if(Build.VERSION.SDK_INT >= 19) {
                            gIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            gIntent.addCategory(Intent.CATEGORY_OPENABLE);
                        } else {
                            gIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        }
                        // 이미지 파일만 필터링
                        gIntent.setType("image/*");
                        gIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                        startActivityForResult(gIntent, 102);
                        break;
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 101:   // 카메라 앱
                // 촬영 결과를 갤러리에  등록
                Intent photoIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.parse("file://" + filePath));
                Glide
                    .with(BannerActivity.this)
                    .load(filePath)
                    .into(imageView10);

                sendBroadcast(photoIntent);
                break;
            case 102:   // 갤러리 앱
                if(resultCode == RESULT_OK) {
                    // 선택한 파일 경로 얻기
                    filePath = FileUtils.getPath(this, data.getData());
                }
                Glide
                    .with(BannerActivity.this)
                    .load(filePath)
                    .into(imageView10);

                break;
        }
    }
}
