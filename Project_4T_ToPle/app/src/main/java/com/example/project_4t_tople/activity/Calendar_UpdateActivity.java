package com.example.project_4t_tople.activity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.project_4t_tople.R;
import com.example.project_4t_tople.helper.DateTimeHelper;
import com.example.project_4t_tople.model.Calendar;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Calendar_UpdateActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    // 객체 선언
    EditText detail_update_name, detail_update_help, detail_update_money;
    Button detail_update_time, detail_update_bt1, detail_update_bt2, detail_update_bt3;
    TextView detail_update_date;
    String str_year, str_month, str_day;
    int HOUR = 0, MINIUTE = 0;

    private GoogleMap map;
    SupportMapFragment mapFragment;
    SearchView searchView;
    Double lat, lot;    // 위도 경도를 알아내기 위해서 사용
    String location;    // 검색한 장소
    String time;
    Calendar calendar_item;

    // 통신
    AsyncHttpClient client;
    String calendar_update_url = "http://175.198.87.149:8080/moim.4t.spring/updateSchedule.tople";
    String calendar_delete_url = "http://175.198.87.149:8080/moim.4t.spring/deleteSchedule.tople";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar__update);

        //모임 코드
        calendar_item =(Calendar) getIntent().getSerializableExtra("calendar_item");
        client=new AsyncHttpClient();

        detail_update_name = findViewById(R.id.detail_update_name);
        detail_update_date = findViewById(R.id.detail_update_date);
        detail_update_time = findViewById(R.id.detail_update_time);
        detail_update_help = findViewById(R.id.detail_update_help);
        detail_update_bt1 = findViewById(R.id.detail_update_bt1);
        detail_update_bt2 = findViewById(R.id.detail_update_bt2);
        detail_update_bt3 = findViewById(R.id.detail_update_bt3);
        detail_update_money = findViewById(R.id.detial_update_money);

        str_year = calendar_item.getSch_year();
        str_month = calendar_item.getSch_month();
        str_day = calendar_item.getSch_day();

        detail_update_name.setText(calendar_item.getSch_title());
        detail_update_time.setText(calendar_item.getSch_time());
        detail_update_money.setText(Integer.toString(calendar_item.getSch_amount()));
        detail_update_help.setText(calendar_item.getSch_sub());
        detail_update_date.setText(str_year + " - " + str_month + " - " + str_day);

        // 지도
        searchView = findViewById(R.id.searchView);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        // Dialog에 출력하기 위해 현재 시스템 시간를 구하여 전역변수에 미리 셋팅
        int[] time = DateTimeHelper.getInstance().getTime();
        HOUR = time[0];
        MINIUTE = time[1];

        // 맵 실행
        MapActivity();
        mapFragment.getMapAsync(this);

        // 이벤트 설정
        detail_update_time.setOnClickListener(this);
        detail_update_bt1.setOnClickListener(this);
        detail_update_bt2.setOnClickListener(this);
        detail_update_bt3.setOnClickListener(this);
    }

    //    맵 실행 코드
    private void MapActivity() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 검색창에서 입력한 값을 location에 저장
                location = searchView.getQuery().toString();
                List<Address> addressesList = null;

                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(Calendar_UpdateActivity.this);
                    try {
                        // 검색으로 위도 경도 값 얻오는 방법
                        addressesList = geocoder.getFromLocationName(location, 1);
                        lat = addressesList.get(0).getLatitude();
                        lot = addressesList.get(0).getLongitude();
                    } catch (IOException e) {
                        // 주소 변환 실패시 실행
                        e.printStackTrace();
                        Toast.makeText(Calendar_UpdateActivity.this, "해당 검색에 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    Address address = addressesList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    map.addMarker(new MarkerOptions().position(latLng).title(location));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
                } else {
                    return false;
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        RequestParams params = null;
        Intent intent = null;
        switch (v.getId()) {
//          시간 Dialog
            case R.id.detail_update_time:
                showTimePickerDialog();
                break;
//                저장 버튼
            case R.id.detail_update_bt1:
                String sch_year = str_year;
                String sch_month = str_month;
                String sch_day = str_day;
                String sch_title = detail_update_name.getText().toString().trim();
                String sch_time = time;
                String sch_sub = detail_update_help.getText().toString().trim();
                String sch_amount = detail_update_money.getText().toString().trim();
                Double sch_lat = lat;
                Double sch_lot = lot;

                // 입력값이 있으면, 서버로 데이터 전송 및 요청
                params = new RequestParams();

                params.put("sch_moimcode", calendar_item.getSch_moimcode());
                params.put("sch_schnum", calendar_item.getSch_schnum());
                params.put("sch_year", sch_year);
                params.put("sch_month", sch_month);
                params.put("sch_day", sch_day);
                params.put("sch_title", sch_title);
                params.put("sch_time", sch_time);
                params.put("sch_sub", sch_sub);
                params.put("sch_amount", sch_amount);
                if((calendar_item.getSch_lot() != sch_lat) && (calendar_item.getSch_lat() != sch_lot)){
                    params.put("sch_lat", sch_lat);
                    params.put("sch_lot", sch_lot);
                }else {
                    params.put("sch_lat", calendar_item.getSch_lat());
                    params.put("sch_lot", calendar_item.getSch_lot());
                }

                client.post(calendar_update_url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String strJson = new String(responseBody);
                        try {
                            JSONObject json = new JSONObject(strJson);
                            String rt  = json.getString("result");
                            if(rt.equals("OK")){
                                Toast.makeText(Calendar_UpdateActivity.this ,"수정성공",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(Calendar_UpdateActivity.this ,"수정실패",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    }
                });
                intent = new Intent(this, Calendar_ReadActivity.class);
                intent.putExtra("calendar_item", calendar_item);
                startActivity(intent);
                break;
//                삭제 버튼
            case R.id.detail_update_bt2:
                params = new RequestParams();
                params.put("sch_schnum", calendar_item.getSch_schnum());
                client.post(calendar_delete_url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String strJson = new String(responseBody);
                        try {
                            JSONObject json = new JSONObject(strJson);
                            String rt  = json.getString("result");
                            if(rt.equals("OK")){
                                Toast.makeText(Calendar_UpdateActivity.this ,"삭제성공",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(Calendar_UpdateActivity.this ,"삭제실패",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    }
                });
                finish();
                break;
//                취소 버튼
            case R.id.detail_update_bt3:
                intent = new Intent(Calendar_UpdateActivity.this, Calendar_ReadActivity.class);
                intent.putExtra("calendar_item", calendar_item);
                startActivity(intent);
                break;
        }
    }

    //    시간 dialog
    private void showTimePickerDialog() {
        // 원본 데이터 백업
        final int temp_hh = HOUR;
        final int temp_nn = MINIUTE;
        // TimePickerDialog 객체 생성
        // TimePickerDialog(Context, 이벤트 핸들러, 시, 분, 24시간제 사용여부)
        // 24시간제 사용여부 : treu=24시간제, false=12시간제
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // 사용자가 선택한 값을 전역변수에 저장
                HOUR = hourOfDay;
                MINIUTE = minute;
                detail_update_time.setText(HOUR + ":" + MINIUTE);
            }
        }, HOUR, MINIUTE, false);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                HOUR = temp_hh;
                MINIUTE = temp_nn;
            }
        });
        dialog.setTitle("시간 선택");
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setMessage("약속시간을 선택하세요.");
        dialog.setCancelable(false);
        dialog.show();
        time = HOUR + ":" + MINIUTE;
    }

    // 기존에 찍은 맵 위치를 그대로 가져옴
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng latLng = new LatLng(calendar_item.getSch_lat(), calendar_item.getSch_lot());
        // 해당 위치로 이동
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        // 마크 찍기
        map.addMarker(new MarkerOptions().position(latLng)).showInfoWindow();
        // 카메라 줌
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }
}
